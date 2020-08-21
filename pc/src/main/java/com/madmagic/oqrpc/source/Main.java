package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSocket;
import com.madmagic.oqrpc.api.ApiSender;
import com.madmagic.oqrpc.gui.SettingsGUI;
import com.madmagic.oqrpc.gui.AlreadyRunningGUI;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.*;

public class Main {

    public static String os;

    public static void main(String[] args) {
        os  = System.getProperty("os.name").toLowerCase();
        Config.init();
        bootInit();
        checkUtil();

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
            ApiSender.ask(Main.getUrl(), "startup");
        }
    }

    private static void bootInit() {
        if (!os.contains("win")) return;
        try {
            File startupFolder = new File(System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\");
            try {
                new File(startupFolder.getAbsolutePath() + "\\oqrpc.bat").delete();
            } catch (Exception ignored) {}

            ShellLink sL = ShellLink.createLink(Config.getUpdater());
            sL.setCMDArgs("boot");
            sL.saveTo(startupFolder.getPath() + "\\oqrpc.lnk");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkUtil() {
        File utilJar = new File(Config.getUpdater());
        if (utilJar.exists()) utilJar.delete();
        URL utilUrl = Main.class.getResource("/oqrpc/Util.jar");
        try {
            FileUtils.copyURLToFile(utilUrl, utilJar);
        } catch (Exception e) {
            e.printStackTrace();
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
