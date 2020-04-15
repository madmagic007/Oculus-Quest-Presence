package com.madmagic.util;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    private static File log;
    private static File configFile;

    public static void init() {
        try {
            log =  new File(jarPath().replace(new File(jarPath()).getName(), "UpdaterLog.txt"));
            try {
                log.delete();
                log.createNewFile();
            } catch (Exception ignored) {}
            configFile =  new File(jarPath().replace(new File(jarPath()).getName(), "config.json"));
            if (!configFile.exists())configFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeConfig(JSONObject obj) {
        try {
            if (!configFile.exists()) configFile.createNewFile();
            FileWriter fw = new FileWriter(configFile);
            fw.write(obj.toString(4));
            fw.close();
        } catch (Exception ignored) {}
    }

    public static boolean readBoot() {
        JSONObject main = read();
        if (main.has("startBoot")) return main.getBoolean("startBoot");
        writeConfig(main.put("startBoot", true));
        return true;
    }

    private static JSONObject read() {
        try {
            String text = new String(Files.readAllBytes(Paths.get(configFile.getAbsolutePath())));
            if (text.equals("")) {
                return new JSONObject();
            } else return new JSONObject(text);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static String jarPath() {
        String r;
        try {
            String path = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            r =  URLDecoder.decode(path, "UTF-8");
            if (r.startsWith("/")) r = r.replaceFirst("/", "");
            if (Main.os.contains("win")) {
                return r;
            } else {
                return "/" + r;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void writeLog(String logText) {
        try {
            if (!log.exists()) log.createNewFile();
            String oldText = new String(Files.readAllBytes(Paths.get(log.getAbsolutePath())));
            FileWriter fw = new FileWriter(log);
            fw.write(oldText + logText + "\n");
            fw.close();
        } catch (Exception ignored) {}
    }
}
