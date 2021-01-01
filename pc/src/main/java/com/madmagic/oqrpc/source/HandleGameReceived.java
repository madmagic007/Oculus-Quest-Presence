package com.madmagic.oqrpc.source;

import org.json.JSONObject;

public class HandleGameReceived {

    public static void handle(JSONObject o) {
        if (!o.has("appId")) o.put("appId", Discord.appId);

        if (o.has("largeImageKey") && !o.has("smallImageKey"))
            o.put("smallImageKey", "quest").put("smallImageText", "OQRPC v." + UpdateChecker.version + " by MadMagic");
        else
            o.put("largeImageKey", "quest").put("largeImageText", "OQRPC v." + UpdateChecker.version + " by MadMagic");

        if (!o.getBoolean("detailed"))
            o.put("details", "Currently Playing:").put("state", o.getString("name"));

        Discord.updatePresence(o);
    }
}
