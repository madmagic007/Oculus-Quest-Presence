package com.madmagic.oqrpc;

import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import java.net.DatagramSocket;
import java.net.InetAddress;


public class MainService extends Service {

    public static boolean isRunning;
    private ApiReceiver receiver;
    public static Service s;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isRunning = true;
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        MainActivity.txtRunning.setText(R.string.running);
        s = this;

        try {
            receiver = new ApiReceiver();
        } catch (Exception ignored) {}

        new ConfigCreator(getFilesDir());
        if (!ConfigCreator.getIp().isEmpty()) ConnectionChecker.run(this);
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        MainActivity.txtRunning.setText(R.string.notRunning);
        receiver.stop();
        new Thread(() -> ApiCaller.call("ended")).start();
    }

    public static void callStart() {
        new Thread(() -> ApiCaller.call("started")).start();
    }

    public static String getIp() {
        String ip = "";
        try(final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception ignored) {}
        return ip;
    }
}