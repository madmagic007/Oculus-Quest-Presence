package com.madmagic.oqrpc;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Timing {

    private static int endTime;
    private static Timer endTimer;

    private static int requestTime;
    private static Timer requestTimer;

    public static void startRequester() {
        requestTime = 10;
        int delay = 1000;
        int period = 1000;
        requestTimer = new Timer();
        requestTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                requester();
            }
        }, delay, period);
    }

    private static void requester() {
        if (requestTime == 1) {
            ApiSender.ask(Main.getUrl(), new JSONObject().put("message", "game"));
            requestTimer.cancel();
            startRequester();
        }
        --requestTime;
    }

    public static void startEnder() {
        endTime = 60;
        int delay = 1000;
        int period = 1000;
        endTimer = new Timer();
        endTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ender();
            }
        }, delay, period);
    }

    private static void ender() {
        if (endTime == 1) {
            requestTimer.cancel();
            Discord.terminate();
        }
        --endTime;
    }

    public static void resetEnder() {
        endTimer.cancel();
        startEnder();
    }

    public static void terminate() {
        try {
            requestTimer.cancel();
        } catch (Exception ignored) {}
        try {
            endTimer.cancel();
        } catch (Exception ignored) {}
    }
}
