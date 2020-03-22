package com.madmagic.oqrpc;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws Exception {

        Config.init();

    	try {
    		if (args[0].equals("boot") && !Config.readBoot()) System.exit(0);
    	} catch (Exception ignored) {}

    	Config.initStartup();
        SystemTrayHandler.systemTray();

        if (Config.getAddress().isEmpty()) {
            InitFrame.open();
        }

        new ApiReceiver();
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
