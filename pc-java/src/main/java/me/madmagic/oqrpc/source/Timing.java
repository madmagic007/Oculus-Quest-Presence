package me.madmagic.oqrpc.source;

import me.madmagic.oqrpc.api.ApiSender;
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

        int delay = Config.getDelay() * 1000;
        requestTimer = new Timer();
        requestTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                JSONObject game;
                try {
                    game = ApiSender.ask(Main.getUrl(), "game");
                } catch (Exception ignored) {
                    System.out.println("Failed to request game status, stopping presence");
                    SystemTrayHandler.notif("Quest offline", "RPC service on your Quest has stopped");
                    Discord.terminate();
                    terminate();
                    return;
                }
                if (game.has("message") && game.getString("message").equals("gameResponse")) {
                    startRequester();
                    startEnder();
                    HandleGameReceived.handle(game);
                }
            }
        }, delay, delay);
    }

    private static Timer endTimer;

    public static void startEnder() {
        try {
            endTimer.cancel();
        } catch (Exception ignored) {
        }
        endTimer = new Timer();
        endTimer.schedule(new TimerTask() {
            public void run() {
                terminate();
                Discord.terminate();
            }
        }, 60000);
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
