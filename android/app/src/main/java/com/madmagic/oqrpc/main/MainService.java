package com.madmagic.oqrpc.main;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
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
        Log.d("OQRPC", "service starting");

        try {
            receiver = new ApiSocket(this);

            if (MainActivity.shouldAskUsageStatsPerm(this) && !MainActivity.b) {
                stopSelf();
                System.exit(0);
            }

            Config.init(this);
            if (!Config.getAddress().isEmpty()) {
                ConnectionChecker.run(this);
            }
        } catch (Exception e) {
            Log.d("OQRPC", "e: " + Log.getStackTraceString(e));
        }

        ScreenReceiver.register(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();

        new Thread(() -> ApiSender.send("offline", getIp(getBaseContext()))).start();
        if (receiver.isAlive()) receiver.stop();
    }

    public void callStart() {
        Log.d("OQRPC", "sending online message");
        ApiSender.send("online", getIp(getBaseContext()));
    }


    public static String getIp(Context context) {
        String ip = "";
        try {
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        String name = getString(R.string.app_name);
        Notification notif = new NotificationCompat.Builder(this, name)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        startForeground(1, notif);
        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = getString(R.string.app_name);
        NotificationChannel c = new NotificationChannel(name, name, NotificationManager.IMPORTANCE_LOW);
        NotificationManager nMgr = getSystemService(NotificationManager.class);
        nMgr.createNotificationChannel(c);
    }
}