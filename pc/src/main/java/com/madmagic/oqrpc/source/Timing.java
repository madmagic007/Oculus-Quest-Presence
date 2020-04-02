package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSender;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Timing {

    private static Timer requestTimer;

    public static void startRequester() {
        try  {
            requestTimer.cancel();
            System.out.println("stopped requestTimer");
        } catch (Exception ignored) {}
        int delay = 1000;
        int period = 10000;
        requestTimer = new Timer();
        requestTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ApiSender.ask(Main.getUrl(), new JSONObject().put("message", "game").put("address", Main.getIp()));
            }
        }, delay, period);
    }

    private static Timer endTimer;

    public static void startEnder() {
        try {
            endTimer.cancel();
        } catch (Exception ignored) {}
        int delay = 60000;
        int period = 1000;
        endTimer = new Timer();
        endTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                terminate();
                Discord.terminate();
            }
        }, delay, period);
    }

    public static void terminate() {
        try  {
            requestTimer.cancel();
        } catch (Exception ignored) {}
        try {
            endTimer.cancel();
        } catch (Exception ignored) {}
    }
}
