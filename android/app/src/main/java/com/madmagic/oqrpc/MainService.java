package com.madmagic.oqrpc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;


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
        ConnectionChecker.run(this);
    }

    @Override
    public void onDestroy() {
        isRunning = false;

        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        try {
            new ApiCaller(new JSONObject().put("message", "ended"));
        } catch (Exception ignored) {}
    }

    public void connected() {
        try {
            new ConfigCreator(getFilesDir());
            ActivityGetter.define(getBaseContext());

            new ApiCaller(new JSONObject().put("message", "started"));
            new ApiReceiver();
        } catch (Exception ignored) {}
    }
}