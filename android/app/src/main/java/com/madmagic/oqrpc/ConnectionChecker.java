package com.madmagic.oqrpc;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectionChecker {

    private static Timer cTimer;
    private static int cMax = 180;

    public static void run(MainService s) {
        try {
            cTimer.cancel();
        } catch (Exception ignored) {}
        cTimer = new Timer();

        WifiManager manager = (WifiManager) s.getSystemService(Context.WIFI_SERVICE);


        cTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (cMax == 1) {
                    cTimer.cancel();
                }
                --cMax;

                if (manager.isWifiEnabled() && manager.getConnectionInfo().getNetworkId() != -1) {
                    cTimer.cancel();
                    runConnecter();
                }
            }
        }, 0, 1000);
    }

    private static Timer fTimer;
    private static int max = 120;

    private static void runConnecter() {
        try {
            fTimer.cancel();
        } catch (Exception ignored) {}
        fTimer = new Timer();
        fTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    if (max == 1) {
                        fTimer.cancel();
                    } --max;

                    JSONObject response = ApiCaller.call("connect");
                    if (response.has("connected")) {
                        fTimer.cancel();
                        MainService.callStart();
                    }
                } catch (Exception ignored) {}
            }
        }, 0, 3000);
    }

    public static void end() {
        try {
            cTimer.cancel();
        } catch (Exception ignored) {}
        try {
            fTimer.cancel();
        } catch (Exception ignored) {}
    }
}
