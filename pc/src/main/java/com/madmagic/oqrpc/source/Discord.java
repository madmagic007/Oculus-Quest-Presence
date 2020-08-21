package com.madmagic.oqrpc.source;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.JSONObject;

public class Discord {

    private static DiscordRPC rpc;
    private static DiscordRichPresence presence;
    public static String appId = "664525664946356230";
    public static String lastUsed = appId;

    public static void init(String appId) {
        rpc = DiscordRPC.INSTANCE;

        if (!lastUsed.equals(appId)) terminate();
        rpc.Discord_Initialize(appId, null, true, null);
        Discord.lastUsed = appId;

        presence = new DiscordRichPresence();
        if (presence.details == null) {
            presence.details = "Just started playing";
            presence.largeImageText = "Oculus Quest";
            presence.largeImageKey = "quest";
        }
        rpc.Discord_UpdatePresence(presence);
    }

    private static long current = 0;
    public static void updatePresence(JSONObject o) {
        if (!lastUsed.equals(o.getString("appId"))) init(o.getString("appId"));

        presence = new DiscordRichPresence();
        presence.details = o.has("details") ? o.getString("details") : "";
        presence.state = o.has("state") ? o.getString("state") : "";
        if (o.has("largeImageKey")) presence.largeImageKey = o.getString("largeImageKey");
        if (o.has("smallImageKey")) presence.smallImageKey = o.getString("smallImageKey");
        if (o.has("largeImageText")) presence.largeImageText = o.getString("largeImageText");
        if (o.has("smallImageText")) presence.smallImageText = o.getString("smallImageText");
        if (o.has("remaining")) presence.endTimestamp =  System.currentTimeMillis() / 1000 + o.getInt("remaining");
        if (o.has("elapsed")) {
            if (o.getBoolean("elapsed")) {
                if (current == 0) current = System.currentTimeMillis() / 1000;
                presence.startTimestamp = current;
            } else current = 0;
        } else current = 0;
        rpc.Discord_UpdatePresence(presence);
    }

    public static void terminate() {
        try {
            System.out.println("terminating presence");
            rpc.Discord_ClearPresence();
            rpc.Discord_Shutdown();
        } catch (Exception ignored) {}
    }
}
