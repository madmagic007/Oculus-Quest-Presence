package com.madmagic.oqrpc.main;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.Toast;
import com.madmagic.oqrpc.*;
import com.madmagic.oqrpc.receivers.ScreenReceiver;


public class MainService extends Service {

    private ApiSocket receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();

        try {
            receiver = new ApiSocket(this);
            Config.init(getFilesDir());
            Module.init();
            if (!Config.getAddress().isEmpty()) {
                ConnectionChecker.run(this);
            }
        } catch (Exception ignored) {}

        ScreenReceiver.register(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();

        new Thread(() -> ApiSender.send("offline", getIp(getBaseContext()))).start();
        if (receiver.isAlive()) receiver.stop();
    }

    public void callStart() {
        ApiSender.send("online", getIp(getBaseContext()));
    }

    @SuppressWarnings("deprecation")
    public static String getIp(Context context) {
        String ip = "";
        try {
            WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        } catch (Exception ignored) {}
        return ip;
    }

    public static boolean isRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}