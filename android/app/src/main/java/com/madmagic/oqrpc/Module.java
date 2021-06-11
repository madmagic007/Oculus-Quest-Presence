package com.madmagic.oqrpc;

import android.util.Log;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

                JSONObject obj = getJsonObject(file);
                Log.d("OQRPC", "MODULE");
                if (obj == null || !obj.has("packageName") || !obj.has("port")){
                    Log.d("OQRPC", "BAD MODULE " + (obj == null));
                    continue;
                }

                Module module = new Module(obj.getString("packageName"), obj.getInt("port"),
                        obj.has("appId") ? obj.getString("appId") : "");
                try {
                    module.enabled = stored.get(module.name).enabled;
                } catch (Exception ignored) {}
                modules.put(obj.getString("packageName"), module);
                Log.d("OQRPC", "put module " + modules.size());
            }
            Config.updateModules(modules);
        } catch (Exception ignored) {}
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


    private static JSONObject getJsonObject(File file) {
        try {
            byte[] bytes = new byte[(int) file.length()];
            DataInputStream is = new DataInputStream(new FileInputStream(file));
            is.readFully(bytes);
            is.close();
            return new JSONObject(new String(bytes));
        } catch (Exception ignored) {}
        return null;
    }
}
