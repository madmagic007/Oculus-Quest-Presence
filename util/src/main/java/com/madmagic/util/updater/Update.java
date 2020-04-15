package com.madmagic.util.updater;

import com.madmagic.util.Config;
import com.madmagic.util.Main;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class Update {

    public static Gui window;
    public static void update(String url) {
        window = new Gui();
        window.frmUpdater.setVisible(true);
        Config.writeLog("opened gui");
        updateError("Starting download");

        File mainJar;
        String thisPath = Config.jarPath();
        File currentThis = new File(thisPath);
        try {
            mainJar = new File(thisPath.replace(currentThis.getName(), "Oculus Quest Discord RPC.jar"));
            mainJar.delete();
            FileUtils.copyURLToFile(new URL(url), mainJar);
            Config.writeLog("downloaded and replaced with recent jar");
        } catch (Exception e) {
            e.printStackTrace();
            Config.writeLog(e.getMessage());
            updateError("Error updating to latest version");
            return;
        }
        try {
            Config.writeLog("finished updating, starting main jar at path: " + thisPath.replace(currentThis.getName(), "Oculus Quest Discord RPC.jar"));
            Main.runMain(thisPath.replace(currentThis.getName(), "Oculus Quest Discord RPC.jar"));
            Config.writeLog("ran the main program");
        } catch (Exception e) {
            Config.writeLog(e.getLocalizedMessage());
            updateError("Error running the program, try starting it manually");
        }
    }

    public static void updateError(String text) {
        new Thread(() -> Gui.lblError.setText(text)).start();
    }
}
