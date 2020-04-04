package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.Config;
import com.madmagic.oqrpc.gui.ConfigGUI;
import com.madmagic.oqrpc.source.Discord;
import com.madmagic.oqrpc.source.Main;
import com.madmagic.oqrpc.source.SystemTrayHandler;
import com.madmagic.oqrpc.source.Timing;
import org.json.JSONObject;

public class ResponseHandler {

    public static void handle(JSONObject response) {
        if (!response.has("message")) return;
        String message = response.getString("message");

        if (message.equals("valid")) ConfigGUI.error.setText("device found and running apk. Ready to go!");
        if (message.equals("started")) {
            System.out.println("received device online message");
            SystemTrayHandler.notif("Quest online", "Your Quest is online");
            Discord.init();
            Timing.startRequester();
            Timing.startEnder();
        }

        if (message.equals("ended")) {
            System.out.println("device offline");
            SystemTrayHandler.notif("Quest offline", "RPC service on your Quest has stopped");
            Discord.terminate();
            Timing.terminate();
        }

        if (message.equals("game")) {
            Timing.startEnder();
            nameHandle(
                    ApiSender.ask("https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/lang.json",
                    new JSONObject()), response.getString("game"));
        }

        if (message.equals("note")) {
            String note = response.getString("note");
            SystemTrayHandler.notif("Message from your Quest", note);
        }

        if (message.equals("connect")) {
            ApiSender.ask(Main.getUrl(), new JSONObject().put("message", "valid"));
        }

        if (response.has("apkVersion")) {
            Config.setApk(response.getString("apkVersion"));
        }
    }
    
    public static StringBuilder sb = new StringBuilder();

    public static void nameHandle(JSONObject gitObj, String name) {
        String details = "Currently playing:";
        String state = "";
        String largeImageKey = "quest";

        if (!gitObj.has(name)) {
            if (!sb.toString().contains(name)) sb.append(name).append("\n");
            state = name.split("\\.")[name.split("\\.").length-1];
        } else {
            JSONObject game = gitObj.getJSONObject(name);
            if (game.has("details")) details = game.getString("details");
            if (game.has("state")) state = game.getString("state");
            if (game.has("key")) largeImageKey = game.getString("key");
        }
        Discord.changeGame(details, state, largeImageKey);
    }
}
