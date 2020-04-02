package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiReceiver;
import com.madmagic.oqrpc.api.ApiSender;
import com.madmagic.oqrpc.config.Config;
import com.madmagic.oqrpc.config.ConfigGUI;
import com.madmagic.oqrpc.gui.AlreadyRunningGUI;
import com.madmagic.oqrpc.gui.UpdateChecker;
import mslinks.ShellLink;
import org.json.JSONObject;

import java.io.File;
import java.net.*;

public class Main {

    public static void main(String[] args) {

        Config.init();
    	try {
    		if (args[0].equals("boot") &&!Config.readBoot()) System.exit(0);
    	} catch (Exception ignored) {}

        try {
            new ApiReceiver();
        } catch (Exception e) {
            AlreadyRunningGUI.open();
        }

        UpdateChecker.check();
        bootInit();
        SystemTrayHandler.systemTray();

        if (Config.getAddress().isEmpty()) {
            ConfigGUI.open();
        } else {
            ApiSender.ask(Main.getUrl(), new JSONObject().put("message", "startup").put("address", Main.getIp()));
        }
    }

    private static void bootInit() {
        try {
            File startupFolder = new File(System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\");
            try {
                new File(startupFolder.getAbsolutePath() + "\\oqrpc.bat").delete();
            } catch (Exception ignored) {}

            ShellLink sL = ShellLink.createLink(Config.jarPath());
            sL.setCMDArgs("boot");
            sL.saveTo(startupFolder.getPath() + "\\oqrpc.lnk");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getIp() {
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
