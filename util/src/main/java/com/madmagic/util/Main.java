package com.madmagic.util;

import com.madmagic.util.updater.Update;

public class Main {

	public static void main(String[] args) {
		Config.init();
		if (args.length != 1) {
			Config.writeLog("jar started with 0 args");
			System.exit(0);
		}
		if (args[0].equals("boot")) {
			if (!Config.readBoot()) System.exit(0);
			Config.writeLog("util started as boot handler");
			BootHandler.handle();
		} else {
			Config.writeLog("util started as updater");
			Update.update(args[0]);
		}
	}

	public static void runMain(String mainJar) throws Exception {
		Config.writeLog("starting main jar");
		Runtime.getRuntime().exec("java -jar \"" + mainJar + "\"");
	}
}
