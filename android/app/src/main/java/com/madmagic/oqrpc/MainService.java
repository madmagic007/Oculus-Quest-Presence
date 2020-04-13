package com.madmagic.oqrpc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


public class MainService extends Service {

    public static boolean isRunning = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isRunning = true;
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();

        try {
            new ApiReceiver();
        } catch (Exception ignored) {}

        new ConfigCreator(getFilesDir());
        if (!ConfigCreator.getIp().isEmpty()) ConnectionChecker.run(this);
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();

        try {
            new ApiCaller(new JSONObject().put("message", "ended"));
        } catch (Exception ignored) {}
    }

    private static boolean accept = true;

    private static String version = "1.0";
    public void connected() {
        if (!accept) return;
        accept = false;
        try {
            ActivityGetter.define(getBaseContext());
            new ApiCaller(new JSONObject().put("message", "started").put("apkVersion", version));
        } catch (Exception ignored) {}
    }
}