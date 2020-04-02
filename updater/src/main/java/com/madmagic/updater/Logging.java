package com.madmagic.updater;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Logging {

    private static File log;

    public static void init() {
        try {
            log =  new File(jarPath().replace(new File(jarPath()).getName(), "UpdaterLog.txt"));
            try {
                log.delete();
                log.createNewFile();
            } catch (Exception ignored) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String jarPath() {
        try {
            String path = Logging.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String r =  URLDecoder.decode(path, "UTF-8");

            if (r.startsWith("/")) return r.replaceFirst("/", "");
            return r;
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
