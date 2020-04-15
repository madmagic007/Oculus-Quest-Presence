package com.madmagic.util;

import com.madmagic.util.updater.Update;

import java.util.concurrent.TimeUnit;

public class Main {

	public static String os;

	public static void main(String[] args) {
		os = System.getProperty("os.name").toLowerCase();
		Config.init();
		if (args.length != 1) {
			Config.writeLog("jar started with 0 args");
			System.exit(0);
		}
		if (args[0].equals("boot")) {
			if (!Config.readBoot() || !os.contains("win")) System.exit(0);
			Config.writeLog("util started as boot handler");
			BootHandler.handle();
		} else {
			Config.writeLog("util started as updater");
			Update.update(args[0]);
		}
	}

	public static void runMain(String mainJar) throws Exception {
		Config.writeLog("starting main jar");
		String command;
		if (os.contains("win")) {
			command = "java -jar \"" + mainJar + "\"";
			Runtime.getRuntime().exec(command);
			Update.window.end();
		} else {
			command =  "java -jar " + mainJar.replace(" ", "\\ ");
			Runtime.getRuntime().exec(command);
			Update.updateError("Finished updating.");
		}
		Config.writeLog("command: " + command);

	}
}
