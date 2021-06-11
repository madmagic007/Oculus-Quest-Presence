package com.madmagic.oqrpc.gui;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ScreenShareGUI {

    private static JFrame window;

    public static void init() {
        window = new JFrame("OQRPC ScreenShare");
        window.setBounds(0, 0, 100 ,100);
        window.setVisible(true);
    }

    public static void stop() {
        window.dispose();
    }

    public static void displayFrame(byte[] data) {
        if (window == null) return;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try {
            BufferedImage img = ImageIO.read(bais);
            Rectangle r = window.getBounds();

            window.getGraphics().drawImage(img, 0, 0, r.width, r.height, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
