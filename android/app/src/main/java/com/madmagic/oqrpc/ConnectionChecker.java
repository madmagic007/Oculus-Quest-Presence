package com.madmagic.oqrpc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ConnectionChecker {

    public static void run(MainActivity ac) {
        int delay = 1000, period = 1000;
        Timer timer = new Timer();

        WifiManager manager = (WifiManager) ac.getSystemService(Context.WIFI_SERVICE);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (manager.isWifiEnabled() && manager.getConnectionInfo().getNetworkId() != -1) {
                    timer.cancel();
                    ac.init();
                }
            }
        }, delay, period);
    }
}
