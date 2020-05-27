package com.madmagic.oqrpc;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;


public class MainService extends Service {

    public static boolean isRunning = false;
    private ApiReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isRunning = true;
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        setText(R.string.running);

        try {
            receiver = new ApiReceiver(this);
            Config.init(getFilesDir());
            if (!Config.getIp().isEmpty()) {
                ConnectionChecker.run(this);
            }
        } catch (Exception ignored) {}

    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        setText(R.string.notRunning);

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
}