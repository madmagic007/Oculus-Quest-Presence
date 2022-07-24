package me.madmagic.oqrpc.source;

import me.madmagic.oqrpc.api.ApiSender;
import me.madmagic.oqrpc.gui.UpdaterGUI;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.net.URL;

public class UpdateChecker {

    public static final String version = "3.0.0";
    private static final String updateUrl = "https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/update.json";
    private static boolean oG;

    public static String jar = Main.os.contains("win") ? "New version found" : "No new version found";
    public static String apk = "No new apk found";

    public static void check(boolean openGui) {
        oG = openGui;
        if (openGui) {
            apk = "Checking for new apk...";
            UpdaterGUI.open();
        }

        JSONObject rB;
        try {
            rB = ApiSender.ask(updateUrl, "");
        } catch (Exception e) {
            e.printStackTrace();
            apk = "Error checking for updates";
            return;
        }

        //jarVersion
        if (Main.os.contains("win")) {
            if (!openGui) {
                UpdaterGUI.open();
                oG = true;
            }
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

    public static void runInstaller() {
        if (!Main.os.contains("win")) return;
        File path = new File(Config.getUpdater().replace("Util.jar", "OQRPC.msi"));
        try {
            if (!path.exists()) path.createNewFile();
            FileUtils.copyURLToFile(new URL(ApiSender.ask(updateUrl, "").getString("installer")), path);

            Desktop.getDesktop().open(path);

            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
