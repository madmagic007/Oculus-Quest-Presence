package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSender;
import com.madmagic.oqrpc.gui.UpdaterGUI;

import org.json.JSONObject;

import java.io.File;

public class UpdateChecker {

    public static final String version = "2.7.0";
    private static final String updateUrl = "https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/update.json";
    private static String jarUrl;
    private static boolean oG;

    public static String jar = "No new jar found";
    public static String apk = "No new apk found";

    public static void check(boolean openGui) {
        oG = openGui;
        if (openGui) {
            jar = "Checking for new jar...";
            apk = "Checking for new apk...";
            UpdaterGUI.open();
        }

        JSONObject rB;
        try {
            rB = ApiSender.ask(updateUrl, "");
        } catch (Exception e) {
            e.printStackTrace();
            jar = "Error checking for updates";
            apk = "Error checking for updates";
            return;
        }

        //jarVersion
        if (!rB.has("latest") || rB.getString("latest").equals(version)) {
            jar = "Already latest jar version";
        } else {
            jarUrl = rB.getString("url");
            if (!openGui) {
                UpdaterGUI.open();
                oG = true;
            }
            jar = "New jar found";
            UpdaterGUI.btnInstall.setVisible(true);
        }

        //apkVersion
        String currentApkVersion = Config.getApkVersion();
        if (currentApkVersion.isEmpty()) return;
        if (!rB.getString("apkVersion").equals(currentApkVersion)) {
            if (!openGui && !oG) {
                UpdaterGUI.open();
                oG = true;
            }
            UpdaterGUI.btnDownload.setVisible(true);
            apk = "New apk version found";
            UpdaterGUI.apkUrl = rB.getString("apkUrl");
        } else apk = "Already latest apk version";
        updateLbl();
    }

    public static void updateLbl() {
        new Thread(() -> {
            if (oG) {
                UpdaterGUI.lblNotif.setText(jar);
                UpdaterGUI.lblApk.setText(apk);
            }
        }).start();
    }

    public static void openUtil() {
        File utilJar = new File(Config.getUpdater());
        if (!utilJar.exists()) {
            jar = "Error updating: Util.jar not found";
            updateLbl();
        }

        try {
            String command;
            if (Main.os.contains("win")) {
                command = "java -jar \"" + utilJar.getAbsolutePath() + "\" \"" + jarUrl + "\"";
            } else {
                command = "java -jar " + Config.getUpdater().replace(" ", "\\ ") + " " + jarUrl;
            }

            System.out.println("running command " + command);
            Runtime.getRuntime().exec(command);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            jar = "Error updating: Util.jar not found";
            updateLbl();
        }
    }
}
