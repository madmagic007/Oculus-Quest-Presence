package com.madmagic.oqrpc.gui;

import javax.swing.*;
import java.awt.*;

public class OldGUI {

    public static void open() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            JFrame frame = new JFrame();
            frame.setTitle("Old Version Found");
            frame.setType(Window.Type.UTILITY);
            frame.setBounds(100, 100, 372, 109);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().setLayout(null);

            JLabel lbl = new JLabel("<html>Old version found: \"C:\\Program Files (x86)\\Oculus Quest Discord RPC\"<br/>Please delete that folder/run the uninstaller because this version is installed in Appdata folder for the auto updater to work.<html>");
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
            lbl.setVerticalAlignment(SwingConstants.TOP);
            lbl.setBounds(10, 11, 340, 42);
            frame.getContentPane().add(lbl);
            frame.setVisible(true);
        });
    }
}
