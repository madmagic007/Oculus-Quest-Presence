package com.madmagic.oqrpc.source;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.madmagic.oqrpc.api.ApiSender;
import org.json.JSONObject;

public class Discord {

    private static DiscordRPC rpc;
    private static DiscordRichPresence presence;

    public static void init() {
        rpc = DiscordRPC.INSTANCE;

        rpc.Discord_Initialize("664525664946356230", new DiscordEventHandlers(), true, "");
        presence = new DiscordRichPresence();

        presence.details = "Just started playing";
        presence.largeImageText = "Oculus Quest";
        presence.largeImageKey = "quest";

        rpc.Discord_UpdatePresence(presence);
    }

    public static StringBuilder sb = new StringBuilder();
    private static JSONObject gitObj = new JSONObject();

    public static void changeGame(String name) {
        String details = "Currently playing:";
        String state = "";
        String largeImageKey = "quest";
        String smallImageKey = "";

        if (gitObj.isEmpty()) {
            gitObj = ApiSender.ask("https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/lang.json", "");
        }
        if (!gitObj.has(name)) {
            if (!sb.toString().contains(name)) sb.append(name).append("\n");
            state = name.split("\\.")[name.split("\\.").length-1];
        } else {
            JSONObject game = gitObj.getJSONObject(name);
            if (game.has("details")) details = game.getString("details");
            if (game.has("state")) state = game.getString("state");
            if (game.has("key")) {
                largeImageKey = game.getString("key");
                smallImageKey = "quest";
            }
        }

        //personal mapping
        JSONObject personal = Config.readMapping();
        if (personal.has(name)) {
            System.out.println("personal found");
            JSONObject pGame = personal.getJSONObject(name);
            if (pGame.has("details")) details = pGame.getString("details");
            if (pGame.has("state")) state = pGame.getString("state");
            if (pGame.has("key")) {
                largeImageKey = pGame.getString("key");
                smallImageKey = "quest";
            }
        }

        presence.details = details;
        presence.state = state;
        presence.largeImageKey = largeImageKey;
        presence.smallImageKey = smallImageKey;
        rpc.Discord_UpdatePresence(presence);
    }

    public static void terminate() {
        try {
            rpc.Discord_ClearPresence();
        } catch (Exception ignored) {}
    }
}
