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
import androidx.core.app.NotificationCompat;
import com.madmagic.oqrpc.*;
import com.madmagic.oqrpc.api.ApiSender;
import com.madmagic.oqrpc.api.ApiSocket;
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
        //ApiSender.send("online", getIp(getBaseContext()));
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

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(getString(R.string.textTitle))
                .setContentText(getString(R.string.textDescription))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setNotificationSilent()
                .build();
        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    getString(R.string.app_name),
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}