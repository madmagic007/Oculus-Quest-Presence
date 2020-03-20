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

        try {
            //Request permission for usage monitor
            requestUsageStatsPermission();

            //Define the context in activityGetter
            ActivityGetter.define(getBaseContext());

            //create config
            new ConfigCreator(getFilesDir());

            //start the apiReceiver and send started to pc
            new ApiReceiver();
            new ApiCaller(new JSONObject().put("message", "started"));

            //button
            Button b = findViewById(R.id.button);
            b.setOnClickListener(e -> {
                finish();
                System.exit(0);
            });

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
