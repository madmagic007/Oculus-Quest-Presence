package com.madmagic.oqrpc;


import java.net.DatagramSocket;
import java.net.InetAddress;

import com.sun.deploy.util.WinRegistry;

public class Main {

    public static void main(String[] args) throws Exception {
        //create with windows boot
        String value = "\"" + Config.jarPath() + "\"";
        WinRegistry.setStringValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "myJar autorun key", value);
        
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

    public static String getUrl() {
        return "http://" + Config.getAddress() + ":8080";
    }
}
