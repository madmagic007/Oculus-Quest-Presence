package com.madmagic.oqrpc;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectionChecker {

    public static MainService service;
    private static int limit;
    private static Timer cTimer;

    public static void run(MainService s) {
        service = s;
        int delay = 1000, period = 1000;
        limit = 180;
        cTimer = new Timer();

        WifiManager manager = (WifiManager) service.getSystemService(Context.WIFI_SERVICE);

        cTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                limit();
                if (manager.isWifiEnabled() && manager.getConnectionInfo().getNetworkId() != -1) {
                    cTimer.cancel();
                    runConnecter();
                }
            }
        }, delay, period);
    }

    private static void limit() {
        if (limit == 1) {
            cTimer.cancel();
        }
        --limit;
    }

    private static Timer fTimer;
    private static int max;

    private static void runConnecter() {
        int delay = 3000, period = 1000;
        max = 120;
        fTimer = new Timer();
        fTimer.scheduleAtFixedRate(new TimerTask () {
            public void run() {
                try {
                    max();
                    new ApiCaller(new JSONObject().put("message", "connect"));
                } catch (Exception ignored) {}
            }
        }, delay, period);
    }

    public static void finish() {
        fTimer.cancel();
        service.connected();
    }

    private static void max() {
        if (max == 1) {
            fTimer.cancel();
        }
        --max;
    }
}
