package com.madmagic.oqrpc;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        SystemTrayHandler.systemTray();
        Config.init();

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
}
