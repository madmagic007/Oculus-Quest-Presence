package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSender;
import org.json.JSONObject;

public class HandleGameReceived {

    public static StringBuilder sb = new StringBuilder();
    private static JSONObject gitObj = new JSONObject();

    public static JSONObject getGitObj() {
        if (gitObj.isEmpty()) {
            gitObj = ApiSender.ask("https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/lang.json", "");
        } return gitObj;
    }

    public static void handle(JSONObject o) {
        String name = o.getString("name");
        boolean ownApp;
        try {
            Long.parseLong(o.getString("appId"));
            ownApp = true;
        } catch (Exception ignored) {
            ownApp = false;
            o.put("appId", Discord.appId);
        }

        if (!o.has("largeImageKey")) o.put("largeImageKey", "quest");
        if (!o.has("largeImageText")) o.put("largeImageText", "OQRPC v." + UpdateChecker.version + " by MadMagic");

        if (!getGitObj().has(name)) {
            if (!sb.toString().contains(name)) sb.append(name).append("\n");
            o.put("state", name.split("\\.")[name.split("\\.").length-1]);
        } else {
            JSONObject game = gitObj.getJSONObject(name);

            if (game.has("key") && !ownApp) {
                o.put("largeImageKey", game.getString("key"))
                        .put("smallImageKey", "quest");
            }

            if (!o.getBoolean("detailed")) {
                o.put("details", game.getString("details"))
                        .put("state", game.getString("state"));

                JSONObject personal = Config.getMapping();
                if (personal.has(name)) {
                    JSONObject personalGame = personal.getJSONObject(name);

                    if (personalGame.has("details")) o.put("details", personalGame.getString("details"));
                    if (personalGame.has("state")) o.put("state", personalGame.getString("state"));
                    if (personalGame.has("key") && !ownApp) {
                        o.put("largeImageKey", personalGame.getString("key"))
                                .put("smallImageKey", "quest");
                    }
                    if (personalGame.has("largeImageText")) o.put("largeImageText", personalGame.getString("largeImageText"));
                    if (personalGame.has("smallImageText")) o.put("smallImageText", personalGame.getString("smallImageText"));
                }
            }
        }
        Discord.updatePresence(o);
    }
}
