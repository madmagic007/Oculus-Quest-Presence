package com.madmagic.oqrpc.main;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;
import com.madmagic.oqrpc.*;
import com.madmagic.oqrpc.receivers.ScreenReceiver;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainService extends Service {

    private ApiSocket receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        setText(R.string.running);

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
        setText(R.string.notRunning);
        Log.d("OQRPC", "sTOPPED");

        new Thread(() -> ApiSender.send("offline", this)).start();
        if (receiver.isAlive()) receiver.stop();
    }

    public void callStart() {
        ApiSender.send("online", this);
    }

    private void setText(int text) {
        if (MainActivity.b) {
            MainActivity.txtRunning.setText(text);
        }
    }

    @SuppressWarnings("deprecation")
    public static String getIp(MainService s) {
        String ip = "";
        try {
            WifiManager wm = (WifiManager) s.getSystemService(WIFI_SERVICE);
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