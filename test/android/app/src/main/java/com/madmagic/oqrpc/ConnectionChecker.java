package com.madmagic.oqrpc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import com.madmagic.oqrpc.api.ApiSender;
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

        cTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (cMax == 1) {
                    cTimer.cancel();
                }
                --cMax;

                if (isNetworkAvailable(s.getBaseContext())) {
                    cTimer.cancel();
                    runConnecter(s);
                }
            }
        }, 0, 1000);
    }

    private static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));

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

                    JSONObject response = ApiSender.send("connect", MainService.getIp(s.getBaseContext()));
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
