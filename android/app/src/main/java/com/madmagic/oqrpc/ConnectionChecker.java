package com.madmagic.oqrpc;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectionChecker {

    public static void run(MainService service) {

        int delay = 1000, period = 1000;
        Timer timer = new Timer();

        WifiManager manager = (WifiManager) service.getSystemService(Context.WIFI_SERVICE);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (manager.isWifiEnabled() && manager.getConnectionInfo().getNetworkId() != -1) {
                    timer.cancel();
                    service.connected();
                }
            }
        }, delay, period);
    }
}
