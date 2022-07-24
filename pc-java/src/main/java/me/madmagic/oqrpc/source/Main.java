package me.madmagic.oqrpc.source;

import me.madmagic.oqrpc.api.ApiSocket;
import me.madmagic.oqrpc.api.ApiSender;
import me.madmagic.oqrpc.gui.SettingsGUI;
import me.madmagic.oqrpc.gui.AlreadyRunningGUI;

import java.net.*;

public class Main {

    public static String os;

    public static void main(String[] args) {
        os  = System.getProperty("os.name").toLowerCase();
        Config.init();

        try {
            new ApiSocket();
        } catch (Exception e) {
            AlreadyRunningGUI.open();
        }

        UpdateChecker.check(false);
        SystemTrayHandler.systemTray();

        if (Config.getAddress().isEmpty()) {
            SettingsGUI.open();
        } else {
            askStartup();
        }
    }

    public static void askStartup() {
        try {
            String url = Main.getUrl();
            System.out.println("asking startup at: " + url);
            ApiSender.ask(url, "startup");
        } catch (Exception e) {
            System.out.println("Failed to request service on the quest to start");
        }
    }

    public static String getIp() {
        String ip = "";
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            ip = String.valueOf(socket.getLocalAddress());
        } catch (Exception ignored) {}
        return ip.replaceFirst("/", "");
    }

    public static String getUrl() {
        return "http://" + Config.getAddress() + ":16255";
    }
}
