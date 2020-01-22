import club.minnced.discord.rpc.*;


public class Main {
	
	public static DiscordRPC lib;
	public static DiscordRichPresence presence;
	
    public static void main(String[] args) {
        lib = DiscordRPC.INSTANCE;
        String applicationId = "664525664946356230";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(applicationId, handlers, true, "");

        presence = new DiscordRichPresence();
        presence.startTimestamp = 0;
        presence.details = "Browsing Oculus Home";
        presence.largeImageText = "Oculus Quest";
        presence.largeImageKey = "quest";
        lib.Discord_UpdatePresence(presence);
        
        Frame.open();
    }
    
    public static void edit(String details, String state) {
    	presence.details = details;
    	presence.state = state;
    	lib.Discord_UpdatePresence(presence);
    }
}