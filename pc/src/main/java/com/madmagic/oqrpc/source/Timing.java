package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSender;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Timing {

    public static Timer requestTimer;

    public static void startRequester() {
        try {
            requestTimer.cancel();
        } catch (Exception ignored) {
        }

        requestTimer = new Timer();
        requestTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                JSONObject game = ApiSender.ask(Main.getUrl(), "game");
                if (game.has("game")) {
                    startEnder();
                    startRequester();
                    Discord.changeGame(game.getString("game"));
                }
            }
        }, 10000, 10000);
    }

    private static Timer endTimer;

    public static void startEnder() {
        try {
            endTimer.cancel();
        } catch (Exception ignored) {
        }
        endTimer = new Timer();
        endTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                terminate();
                Discord.terminate();
            }
        }, 60000, 1000);
    }

    public static void terminate() {
        try {
            requestTimer.cancel();
        } catch (Exception ignored) {
        }
        try {
            endTimer.cancel();
        } catch (Exception ignored) {
        }
    }
}
