package com.madmagic.oqrpc.main;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjection.Callback;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.madmagic.oqrpc.*;
import com.madmagic.oqrpc.api.ApiSender;
import com.madmagic.oqrpc.recorder.ImageAvailableListener;
import com.madmagic.oqrpc.recorder.StreamingSocket;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static boolean b;

    public static ImageReader reader;
    public static int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    public static int mDisplayWidth;
    public static int mDisplayHeight;
    private boolean mScreenSharing;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = true;

        Config.init(this);
        permission(false);

        Intent service = new Intent(this, MainService.class);
        if (!MainService.isRunning(getApplicationContext(), MainService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(service);
            } else {
                startService(service);
            }
        }

        findViewById(R.id.btnTerminate).setOnClickListener(v -> {
            if (MainService.isRunning(getApplicationContext(), MainService.class)) {
                stopService(service);
                ConnectionChecker.end();
            }
        });

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            if (!MainService.isRunning(getApplicationContext(), MainService.class)) {
                startService(service);
            }
        });

        findViewById(R.id.btnLog).setOnClickListener(v -> new Thread(() -> {
            try {
                File logFile = new File(getExternalFilesDir(null), "log.txt");
                if (!logFile.exists()) logFile.createNewFile();

                String log = Config.config.toString(4) + "\n" +
                        "Trying to request connection: " +
                        ApiSender.send("connect", MainService.getIp(getBaseContext())).toString() +
                        "\ncurrent top: " + Arrays.toString(DeviceInfo.getTopmost(getBaseContext()));

                FileWriter fw = new FileWriter(logFile);
                fw.write(log);
                fw.close();
            } catch (Exception e) {
                Log.d("OQRPC", Log.getStackTraceString(e));
            }
        }).start());

        findViewById(R.id.btnPermissions).setOnClickListener(v -> permission(true));

        TextView moduleState = findViewById(R.id.txtModuleEnabled);

        List<String> list = new ArrayList<>(Module.modules.keySet());
        if (list.isEmpty()) list.add("No modules found");
        else list.add(0, "Modules");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.modulesSpinner);
        spinner.setAdapter(adapter);

        Button btnEnable = findViewById(R.id.btnEnable);
        btnEnable.setOnClickListener(v -> updateModule((String) spinner.getSelectedItem(), true, moduleState));

        Button btnDisable = findViewById(R.id.btnDisable);
        btnDisable.setOnClickListener(v -> updateModule((String) spinner.getSelectedItem(), false, moduleState));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String packageName = (String) spinner.getSelectedItem();

                if (packageName.equals("No modules found") || packageName.equals("Modules")) {
                    btnDisable.setVisibility(View.INVISIBLE);
                    btnEnable.setVisibility(View.INVISIBLE);
                    moduleState.setVisibility(View.INVISIBLE);
                } else {
                    btnDisable.setVisibility(View.VISIBLE);
                    btnEnable.setVisibility(View.VISIBLE);
                    moduleState.setVisibility(View.VISIBLE);

                    boolean enabled = Module.isEnabled(packageName);
                    moduleState.setText(enabled ? "Enabled" : "Disabled");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        findViewById(R.id.btnStartRecord).setOnClickListener(l -> {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mScreenDensity = metrics.densityDpi;
            mDisplayWidth = metrics.widthPixels;
            mDisplayHeight = metrics.heightPixels;

            mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

            shareScreen();
        });

        findViewById(R.id.btnStopRecord).setOnClickListener(l -> stopScreensharing());
    }

    private ImageAvailableListener listener;
    private void shareScreen() {
        if (mScreenSharing) {
            Toast.makeText(getBaseContext(), "Already streaming", Toast.LENGTH_SHORT);
            return;
        }

        if (mMediaProjection == null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(),1);
            return;
        }
        mScreenSharing = true;

        listener = new ImageAvailableListener();

        new Thread(() -> {
            try {
                JSONObject o = new JSONObject()
                        .put("message", "shareInit")
                        .put("data", new JSONObject()
                                .put("width", MainActivity.mDisplayWidth)
                                .put("height", MainActivity.mDisplayHeight));
                ApiSender.send(o);
                listener.socket = new StreamingSocket();
                Log.d("OQRPC", "set the socket");
            } catch (Exception e) {
                Log.d("OQRPC", Log.getStackTraceString(e));
            }
        }).start();

        reader = ImageReader.newInstance(mDisplayWidth, mDisplayHeight, PixelFormat.RGBA_8888, 2);

        mVirtualDisplay = mMediaProjection.createVirtualDisplay("captureDisplay",
                mDisplayWidth, mDisplayHeight, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                reader.getSurface(), null, null);

        reader.setOnImageAvailableListener(listener, null);

        Toast.makeText(getBaseContext(), "Started streaming", Toast.LENGTH_SHORT).show();
    }

    private void stopScreensharing() {
        mScreenSharing = false;
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }

        try {
            listener.socket.end();
        } catch (Exception e) {
            Log.d("OQRPC", Log.getStackTraceString(e));
        }

        Toast.makeText(getBaseContext(), "Stopped streaming", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1) {
            Log.e("OQRPC", "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "User denied screen sharing permission", Toast.LENGTH_SHORT).show();
            return;
        }

        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(new Callback() {
            @Override
            public void onStop() {
                mMediaProjection = null;
                stopScreensharing();
            }
        }, null);

        shareScreen();
    }

    private void permission(boolean force) {
        if (shouldAskUsageStatsPerm(this) || force) {

            //Quest 2 requires permission grant via adb
            if (Build.VERSION.SDK_INT >= 29) {
                new AlertDialog.Builder(this)
                        .setTitle("Quest 2 warning")
                        .setMessage("Because you are using the quest 2, you need to grant the permission using adb. More info found at the repository on github")
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }
            Intent grantPermission = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(grantPermission);
        }
    }

    @SuppressWarnings("deprecation")
    static boolean shouldAskUsageStatsPerm(Context context) {
        boolean granted;
        int mode;
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        if (Build.VERSION.SDK_INT < 29)
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());
        else
            mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        return !granted;
    }

    private static void updateModule(String packageName, boolean enabled, TextView text) {
        Module module = Module.modules.get(packageName);
        module.enabled = enabled;
        Module.modules.put(packageName, module);
        Config.updateModules(Module.modules);
        text.setText(enabled ? "Enabled" : "Disabled");
    }

    @Override
    protected void onDestroy() {
        b = false;
        super.onDestroy();
    }
}
