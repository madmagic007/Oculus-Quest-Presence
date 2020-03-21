package com.madmagic.oqrpc;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ConfigCreator {

    static File cFile;

    public ConfigCreator(File dir) {
        cFile = new File(dir.getPath() + "/config.json");
        try {
            if (!cFile.exists()) cFile.createNewFile();
        } catch (Exception ignored) {}
    }

    public static String getIp() {
        String ip = "";
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(cFile));
            BufferedReader br = new BufferedReader(is);

            String r = "";
            StringBuilder sb = new StringBuilder();

            while ((r = br.readLine()) != null) {
                sb.append("\n").append(r);
            }
            is.close();

            JSONObject main = new JSONObject(sb.toString());
            ip = main.getString("address");
            return ip;
        } catch (Exception e) {
            Log.d("APICALLER", "error " + e.getMessage());
            write(new JSONObject());
            return ip;
        }
    }

    public static void write(JSONObject obj) {
        try {
            FileWriter fw = new FileWriter(cFile);
            fw.write(obj.toString(4));
            fw.close();
        } catch (Exception e) {
            Log.d("APICALLER", "error writing " + e.getMessage());
        }
    }
}

