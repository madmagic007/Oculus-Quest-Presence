package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.gui.UpdaterGUI;
import com.sun.deploy.util.UpdateCheck;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.File;

public class UpdateChecker {

    private static final String version = "2.4.0";
    private static final String updateUrl = "https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/update.json";
    private static String jarUrl;
    private static boolean oG;

    public static String jar = "Checking for new Jar";
    public static String apk = "Checking for new Apk";

    public static void check(boolean openGui) {
        oG = openGui;
        if (openGui) UpdaterGUI.open();
        Request r = new Request.Builder()
                .url(updateUrl).build();

        JSONObject rB;
        try {
            Response rs = new OkHttpClient().newCall(r).execute();
            rB = new JSONObject(rs.body().string());
        } catch (Exception e) {
            e.printStackTrace();
            jar = "Error checking for updates";
            updateLbl();
            return;
        }

        //jarVersion
        if (rB.getString("latest").equals(version)) {
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
        updateLbl();

        //apkVersion
        String currentApkVersion = Config.getApkVersion();
        if (currentApkVersion.isEmpty()) return;
        if (!rB.getString("apkVersion").equals(currentApkVersion)) {
            if (!openGui) {
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
