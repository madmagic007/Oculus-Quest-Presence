package com.madmagic.oqrpc.main;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.madmagic.oqrpc.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static  boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = true;

        if (!hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        Intent service = new Intent(MainActivity.this, MainService.class);
        if (!MainService.isRunning(getApplicationContext(), MainService.class))
            startService(service);

        Button b = findViewById(R.id.btnTerminate);
        b.setOnClickListener(v -> {
            if (MainService.isRunning(getApplicationContext(), MainService.class)) {
                stopService(service);
                ConnectionChecker.end();
            }
        });

        Button start = findViewById(R.id.btnStart);
        start.setOnClickListener(v -> {
            if (!MainService.isRunning(getApplicationContext(), MainService.class)) {
                startService(service);
            }
        });

        TextView moduleState = findViewById(R.id.txtModuleEnabled);

        Config.init(getFilesDir());
        Module.init();
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
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button btnDebugLog = findViewById(R.id.btnLog);
        btnDebugLog.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    File logFile = new File(Config.moduleFolder, "debugLog.txt");
                    if (logFile.exists()) logFile.delete();
                    logFile.createNewFile();

                    StringBuilder log = new StringBuilder(Config.config.toString(4) + "\n")
                            .append("Trying to request connection: ")
                            .append(ApiSender.send("connect", MainService.getIp(getBaseContext())).toString())
                            .append("\ncurrent top: ").append(DeviceInfo.getTopmost(getBaseContext()));

                    FileWriter fw = new FileWriter(logFile);
                    fw.write(log.toString());
                    fw.close();
                } catch (Exception ignored) {
                }
            }).start();
        });
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

    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
