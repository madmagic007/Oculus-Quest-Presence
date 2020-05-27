package com.madmagic.util;

import java.io.File;

public class BootHandler {

    public static void handle() {
        String thisPath = Config.jarPath();
        File mainJar = new File(thisPath.replace(new File(thisPath).getName(), "Oculus Quest Discord RPC.jar"));
        Config.writeLog("supposed jar path: " + mainJar.getAbsolutePath());

        if (!mainJar.exists()) {
            Config.writeLog("main jar not found, deleting remains");
            try {
                new File(System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\oqrpc.lnk").delete();
                new File(thisPath.replace(new File(thisPath).getName(), "config.json")).delete();
                new File(thisPath.replace(new File(thisPath).getName(), "mapping.json")).delete();
                Config.writeLog("deleted startup shortcut");
            } catch (Exception ignored) {
                Config.writeLog("error deleting files");
            }

            Config.writeLog("attempting to delete self...");
            try {
                File util = new File(Config.jarPath());
                String cmd = "cmd /c ping localhost -n 6 > nul && del " + util.getName();
                Config.writeLog("running command: " + cmd);
                Runtime.getRuntime().exec(cmd);
                System.exit(0);
            } catch (Exception e) {
                Config.writeLog("error deleting self: " + e.getMessage());
            }
            System.exit(0);
        }

        try {
            Config.writeLog("finished updating, starting main jar");
            Main.runMain(mainJar.getAbsolutePath());
            Config.writeLog("ran the main program");
        } catch (Exception e) {
            Config.writeLog(e.getLocalizedMessage());
        }
    }
}
