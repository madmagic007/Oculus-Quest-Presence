package com.madmagic.oqrpc.source;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.nio.file.Files;

public class Config {
	
	public static File configFile;
	public static JSONObject config;

	public static void init() {
		try {
			configFile =  new File(jarPath().replace(new File(jarPath()).getName(), "config.json"));
			if (!configFile.exists())configFile.createNewFile();
			config = readFile(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static JSONObject readFile(File file) {
		try {
			return new JSONObject(new String(Files.readAllBytes(file.toPath())));
		} catch (Exception ignored) {}
		return new JSONObject();
	}

	public static void updateConfig(JSONObject o) {
		config = o;
		try {
			FileWriter fw = new FileWriter(configFile);
			fw.write(o.toString(4));
			fw.close();
		} catch (Exception ignored) {}
	}

	public static String getAddress() {
		try {
			return config.getString("address");
		} catch (Exception ignored) {}
		return "";
	}

	public static void setAddress(String ip) {
		updateConfig(config.put("address", ip));
	}

	public static String getApkVersion() {
		try {
			return config.getString("apkVersion");
		} catch (Exception ignored) {}
		return "";
	}

	public static void setApkVersion(String version) {
		updateConfig(config.put("apkVersion", version));
	}

	public static boolean getStartAtBoot() {
		try {
			return config.getBoolean("startBoot");
		} catch (Exception ignored) {}
		return false;
	}

	public static boolean getSleepWake() {
		try {
			return config.getBoolean("sleepWake");
		} catch (Exception ignored) {}
		return true;
	}

	public static int getDelay() {
		try {
			return config.getInt("delay");
		} catch (Exception ignored) {}
		return 3;
	}

	public static boolean getNotifications() {
		try {
			return config.getBoolean("notifications");
		} catch (Exception ignored) {}
		return true;
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

	public static String getUpdater() {
		return jarPath().replace(new File(jarPath()).getName(), "Util.jar");
	}
}
