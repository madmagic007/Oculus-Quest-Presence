 package com.madmagic.oqrpc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.Timer;

import kotlin.annotation.Target;

 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("APICALLER", "starting app");

        try {

            new ApiReceiver();
            requestUsageStatsPermission();
            ActivityGetter.define(getBaseContext());

            new ConfigCreator(getFilesDir());
            if (!ConfigCreator.getIp().isEmpty()) {
                Log.d("APICALLER", "connection checker");
                ConnectionChecker.run(this);
            }

            //button
            Button button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    System.exit(0);
                }
            });

        }catch (Exception ignored) {}
    }


    public void init() {
        try {
            Log.d("APICALLER", "sending online");
            new ApiCaller(new JSONObject().put("message", "started"));
        } catch (Exception ignored) {}
    }

     void requestUsageStatsPermission() {
         if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                 && !hasUsageStatsPermission(this)) {
             startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
         }
     }


     boolean hasUsageStatsPermission(Context context) {
         AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
         int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                 android.os.Process.myUid(), context.getPackageName());
         boolean granted = mode == AppOpsManager.MODE_ALLOWED;
         return granted;
     }
}
