package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSender;
import org.json.JSONObject;

public class HandleGameReceived {

    private static JSONObject gitObj = new JSONObject();

    public static void handle(JSONObject o) {
        System.out.println("handling " + o.getString("packageName"));
        boolean own = true;
        try {
            own = o.getString("appId").equals(Discord.appId);
        } catch (Exception ignored) {
            o.put("appId", Discord.appId);
        }

        if (own) {
            String packageName = o.getString("packageName");
            String name = o.getString("name");
            if (getGitObj().has(packageName) && !o.getBoolean("detailed")) {
                JSONObject game = getGitObj().getJSONObject(packageName);
                if (game.has("details")) o.put("details", game.getString("details"));
                if (game.has("state")) o.put("state", game.getString("state"));
                if (!o.has("largeImageKey") && game.has("key")) o.put("largeImageKey", game.getString("key"));
            }
            if (!o.has("details") && !o.has("state")) o.put("details", "Currently Playing:").put("state", name.isEmpty() ? name.split("\\.")[name.split("\\.").length - 1] : name);

            if (!o.has("largeImageKey")) {
                o.put("largeImageKey", "quest");
            } else if (!o.has("smallImageKey")) o.put("smallImageKey", "quest");

            if (o.has("largeImageKey") && o.getString("largeImageKey").equals("quest"))
                o.put("largeImageText", "OQRPC v." + UpdateChecker.version + " by MadMagic");
            else if (o.has("smallImageKey") && o.getString("smallImageKey").equals("quest"))
                o.put("smallImageText", "OQRPC v." + UpdateChecker.version + " by MadMagic");
        }

        Discord.updatePresence(o);
    }

    public static JSONObject getGitObj() {
        if (gitObj.isEmpty()) {
            try {
                gitObj = ApiSender.ask("https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/lang.json", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } return gitObj;
    }
}
