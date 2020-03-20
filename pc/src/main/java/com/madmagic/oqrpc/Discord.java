package com.madmagic.oqrpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class Discord {

    private static DiscordRPC rpc;
    private static DiscordRichPresence presence;

    public static void init() {
        rpc = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        rpc.Discord_Initialize("664525664946356230", handlers, true, "");
        presence = new DiscordRichPresence();

        presence.details = "Just started playing";
        presence.largeImageText = "Oculus Quest";
        presence.largeImageKey = "quest";

        rpc.Discord_UpdatePresence(presence);
    }

    public static void changeGame(String details, String state) {
        presence.details = details;
        presence.state = state;
        rpc.Discord_UpdatePresence(presence);
    }

    public static void terminate() {
        try {
            rpc.Discord_ClearPresence();
        } catch (Exception ignored) {}
    }
}
