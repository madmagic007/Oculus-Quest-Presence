package com.madmagic.oqrpc;

import android.os.Environment;

import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config {

    static File cFile;
    private static JSONObject config;

    static File mFile;
    static File moduleFolder;

    public static void init(File dir) {
        cFile = new File(dir.getPath() + "/config.json");
        mFile = new File(dir.getPath(), "moduleConfig.modules");
        moduleFolder = new File(Environment.getExternalStorageDirectory(), "OQRPC Modules");
        try {
            if (!cFile.exists()) cFile.createNewFile();
            config = getConfig();

            if (!mFile.exists()) mFile.createNewFile();
            if (!moduleFolder.isDirectory()) moduleFolder.mkdir();
        } catch (Exception ignored) {}
    }

    public static JSONObject updateConfig(JSONObject o) {
        try {
            write(cFile, o.toString(4).getBytes());
        } catch (Exception ignored) {}
        return getConfig();
    }

    private static JSONObject getConfig() {
        try {
            return new JSONObject(new String(read(cFile)));
        } catch (Exception ignored) {}
        return updateConfig(new JSONObject());
    }

    public static String getAddress() {
        try {
            return config.getString("address");
        } catch (Exception ignore) {}
        return "";
    }

    public static void setAddress(String address) {
        try {
            updateConfig(config.put("address", address));
        } catch (Exception ignored) {}
    }

    public static boolean getSleepWake() {
        try {
            return config.getBoolean("sleepWake");
        } catch (Exception ignore) {}
        return false;
    }

    public static void setSleepWake(boolean value) {
        try {
            updateConfig(config.put("sleepWake", value));
        } catch (Exception ignored) {}
    }

    private static void write(File file, byte[] data) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (Exception ignored) {}
    }

    public static byte[] read(File file) throws Exception {
        byte[] bytes = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(bytes);
        dis.close();
        return bytes;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Module> readModules() {
        try {
            return (Map<String, Module>) toObject(read(mFile));
        } catch (Exception ignored) {}
        return new HashMap<>();
    }

    public static void updateModules(Map<String, Module> map) {
        try {
            write(mFile, toByteArr(map));
        } catch (Exception ignored) {}
    }

    private static Object toObject(byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis);
        return in.readObject();
    }

    private static byte[] toByteArr(Object o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        return baos.toByteArray();
    }

}

