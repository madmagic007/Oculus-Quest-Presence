package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSender;
import com.madmagic.oqrpc.gui.SettingsGUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public class SystemTrayHandler {

    private static TrayIcon icon;

    public static void systemTray() {
        if (!SystemTray.isSupported()) return;

        try {
            PopupMenu menu = new PopupMenu();

            MenuItem send = new MenuItem("Request presence to start");
            send.addActionListener(e -> Main.askStartup());
            menu.add(send);

            MenuItem ip = new MenuItem("Open settings");
            ip.addActionListener(e -> SettingsGUI.open());
            menu.add(ip);

            MenuItem update = new MenuItem("Check for updates");
            update.addActionListener(e -> UpdateChecker.check(true));
            menu.add(update);

            MenuItem item = new MenuItem("Exit");
            item.addActionListener(e -> System.exit(0));
            menu.add(item);

            Image image = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("oqrpc/quest.png")));
            int trayIconWidth = new TrayIcon(image).getSize().width;
            SystemTray tray = SystemTray.getSystemTray();
            Image ic = image.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH);
            icon = new TrayIcon(ic, "Oculus Quest RPC", menu);
            tray.add(icon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean notif(String caption, String text) {
        try {
            if (!Config.getNotifications()) return false;
            icon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
            return true;
        } catch (Exception ignored) {
            System.out.println("error sending notification");
            return false;
        }
    }
}
