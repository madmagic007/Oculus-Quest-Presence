package com.madmagic.oqrpc;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class SystemTrayHandler {

    private static TrayIcon icon;

    public static void systemTray() {
        if (!SystemTray.isSupported()) return;

        try {
            PopupMenu menu = new PopupMenu();

            MenuItem item = new MenuItem("exit");
            item.addActionListener(e -> System.exit(0));
            menu.add(item);

            MenuItem send = new MenuItem("manual send");
            send.addActionListener(e -> {
                     ApiSender.ask(Main.getUrl(), new JSONObject().put("message", "game").put("address", Main.getip()));
            });
            menu.add(send);

            MenuItem ip = new MenuItem("set ip");
            ip.addActionListener(e -> InitFrame.open());
            menu.add(ip);

            Image image = ImageIO.read(SystemTrayHandler.class.getResource("/quest.png"));
            int trayIconWidth = new TrayIcon(image).getSize().width;
            SystemTray tray = SystemTray.getSystemTray();
            Image ic = image.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH);
            icon = new TrayIcon(ic, "Oculus Quest RPC", menu);
            tray.add(icon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void notif() {
        icon.displayMessage("Oculus Quest Online", "Your quest is online", TrayIcon.MessageType.INFO);
    }
}
