package com.madmagic.oqrpc;

import org.json.JSONObject;

import java.net.*;

public class Main {

    public static void main(String[] args) {

        Config.init();

    	try {
    		if (args[0].equals("boot") && !Config.readBoot()) System.exit(0);
    	} catch (Exception ignored) {}

        try {
            new ApiReceiver();
        } catch (Exception e) {
            System.exit(0);
        }

    	Config.initStartup();
        SystemTrayHandler.systemTray();

        if (Config.getAddress().isEmpty()) {
            InitFrame.open();
        } else {
            ApiSender.ask(Main.getUrl(), new JSONObject().put("message", "startup").put("address", Main.getip()));
        }
    }

    public static String getip() {
        String ip = "";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception ignored) {}
        return ip;
    }

    public static String getUrl() {
        return "http://" + Config.getAddress() + ":8080";
    }
}
