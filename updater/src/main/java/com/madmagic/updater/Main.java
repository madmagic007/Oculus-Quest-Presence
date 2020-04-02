package com.madmagic.updater;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.io.FileUtils;

public class Main {

	public static void main(String[] args) {
		if (args.length != 1) return;
		String url = args[0];
		Logging.init();
		System.out.println("url");
		Logging.writeLog("Updating from url " + url);

		Gui window = new Gui();
		window.frmUpdater.setVisible(true);
		Logging.writeLog("opened gui");
		updateError("Starting download");

		File mainJar;
		try {
			String thisPath = getJarDir();
			File currentThis = new File(thisPath);
			
			mainJar = new File(thisPath.replace(currentThis.getName(), "Oculus Quest Discord RPC.jar"));
			FileUtils.copyURLToFile(new URL(url), mainJar);
			Logging.writeLog("downloaded and replaced with recent jar");
		} catch (Exception e) {
			e.printStackTrace();
			Logging.writeLog(e.getMessage());
			updateError("Error updating to latest version");
			return;
		}
		
		try {
			updateError("Finished updating, restarting program");
			Logging.writeLog("Finished updating, restarting program");
			Runtime.getRuntime().exec("java -jar \"" + mainJar.getPath() + "\"");
			Logging.writeLog("ran the main program");
			window.end();
		} catch (Exception e) {
			e.printStackTrace();
			Logging.writeLog(e.getLocalizedMessage());
			updateError("Error running the program, try starting it manually");
		}
	}
	
	private static void updateError(String text) {
		new Thread(() -> Gui.lblError.setText(text)).start();
	}
	
	private static String getJarDir() {
		try {
			String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String r =  URLDecoder.decode(path, "UTF-8");

			if (r.startsWith("/")) return r.replaceFirst("/", "");
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
