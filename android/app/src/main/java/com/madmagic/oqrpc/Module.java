package com.madmagic.oqrpc;

import android.util.Log;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class Module implements Serializable {

    public static Map<String, Module> modules = new HashMap<>();

    public String name, appId;
    public int port;
    public boolean enabled = true;

    public Module(String name, int port, String appId) {
        this.name = name;
        this.port = port;
        this.appId = appId;
    }

    public static void init() {
        try {
            Map<String, Module> stored = Config.readModules();
            modules = new HashMap<>();

            if (Config.moduleFolder.listFiles() == null) return;
            for (File file : Config.moduleFolder.listFiles()) {
                if (file.isDirectory() || !isValid(file)) continue;
                JSONObject obj = getJsonObject(file);

                Module module = new Module(obj.getString("packageName"), obj.getInt("port"),
                        obj.has("appId") ? obj.getString("appId") : "");
                try {
                    module.enabled = stored.get(module.name).enabled;
                } catch (Exception ignored) {}
                modules.put(obj.getString("packageName"), module);
            }
            Config.updateModules(modules);
        } catch (Exception ignored) {} //android JSON is stupid, unnecessary exception catching pff
    }

    public static boolean isEnabled(String packageName) {
        if (!modules.containsKey(packageName)) return false;
        return modules.get(packageName).enabled;
    }

    public static int getPort(String packageName) {
        return modules.get(packageName).port;
    }

    public static String getAppKey(String packageName) {
        return modules.get(packageName).appId;
    }

    private static boolean isValid(File file) {
        try {
            JSONObject main = getJsonObject(file);
            main.getString("packageName");
            main.getInt("port");
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    private static JSONObject getJsonObject(File file) {
        try {
            return new JSONObject(new String(Config.read(file)));
        } catch (Exception ignored) {}
        return new JSONObject();
    }
}
