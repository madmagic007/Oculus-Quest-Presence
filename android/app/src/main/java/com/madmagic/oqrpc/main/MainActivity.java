package com.madmagic.oqrpc.main;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.madmagic.oqrpc.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = true;

        Config.init(this);
        permission(false);

        Intent service = new Intent(this, MainService.class);
        if (!MainService.isRunning(getApplicationContext(), MainService.class)) {
            startForegroundService(service);
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
    }

    private void permission(boolean force) {
        if (shouldAskUsageStatsPerm(this) || force) {
            Intent grantPermission = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            grantPermission.setData(Uri.fromParts("package", getPackageName(), null));
            startActivity(grantPermission);
        }
    }

    static boolean shouldAskUsageStatsPerm(Context context) {
        boolean granted;
        int mode;
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

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
