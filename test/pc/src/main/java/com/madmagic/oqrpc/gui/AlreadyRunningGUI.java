package com.madmagic.oqrpc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AlreadyRunningGUI {

    public static void open() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ignored) {
        }
        JFrame frame = new JFrame();
        frame.setType(Window.Type.UTILITY);
        frame.setBounds(100, 100, 294, 152);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("<html>Port 16255 is already in use.<br/>\r\nMake sure the program isn't already running.</html>");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(10, 11, 256, 91);
        frame.getContentPane().add(lblNewLabel);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
 }
