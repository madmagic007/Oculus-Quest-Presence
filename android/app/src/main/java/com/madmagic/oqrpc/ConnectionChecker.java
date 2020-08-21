package com.madmagic.oqrpc;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.madmagic.oqrpc.main.MainService;
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
                    runConnecter(s);
                }
            }
        }, 0, 1000);
    }

    private static Timer fTimer;
    private static int max = 60;

    private static void runConnecter(MainService s) {
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

                    JSONObject response = ApiSender.send("connect", s);
                    if (response.has("connected")) {
                        end();
                        s.callStart();
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
