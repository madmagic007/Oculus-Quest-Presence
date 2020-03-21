package com.madmagic.oqrpc;

import org.json.JSONObject;

public class ResponseHandler {

    public static void handle(JSONObject response) {
        if (!response.has("message")) return;
        String message = response.getString("message");

        if (message.equals("valid")) InitFrame.error.setText("device found and running apk. Ready to go!");
        if (message.equals("started")) {
            System.out.println("device online");
            Timing.terminate();
            SystemTrayHandler.notif("Quest online", "Your Quest is online");
            Discord.init();
            Timing.startRequester();
            Timing.startEnder();
        }

        if (message.equals("ended")) {
            SystemTrayHandler.notif("Quest offline", "RPC service on your Quest has stopped");
            Discord.terminate();
            Timing.terminate();
        }

        if (message.equals("game")) {
            Timing.resetEnder();
            nameHandle(ApiSender.ask("https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/lang.json",
                    new JSONObject()), response.getString("game"));
        }

        if (message.equals("note")) {
            String note = response.getString("note");
            SystemTrayHandler.notif("Message from your Quest", note);
        }
    }

    public static void nameHandle(JSONObject gitObj, String name) {
        if (!gitObj.has(name)) {
            System.out.println(name);
            String toSet = name.split("\\.")[name.split("\\.").length-1];
            Discord.changeGame("Currently playing:", toSet);
            return;
        }
        JSONObject game = gitObj.getJSONObject(name);
        Discord.changeGame(game.getString("details"), game.getString("state"));
    }
}
