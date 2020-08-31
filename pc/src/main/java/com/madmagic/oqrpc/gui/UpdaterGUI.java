package com.madmagic.oqrpc.gui;

import com.madmagic.oqrpc.source.UpdateChecker;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.net.URL;

public class UpdaterGUI {

    public static JFrame frame;
    public static JLabel lblNotif;
    public static JLabel lblApk;
    public static JButton btnInstall;
    public static JButton btnDownload;

    public static String apkUrl;

    public static void open() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ignored) {
        }
        frame = new JFrame();
        frame.setTitle("Updater");
        frame.setBounds(100, 100, 263, 143);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        lblNotif = new JLabel("Checking for updates");
        lblNotif.setBounds(10, 11, 132, 14);
        frame.getContentPane().add(lblNotif);

        btnInstall = new JButton("Install");
        btnInstall.setBounds(152, 7, 89, 23);
        frame.getContentPane().add(btnInstall);
        btnInstall.setVisible(false);
        btnInstall.addActionListener(e -> UpdateChecker.openUtil());

        lblApk = new JLabel("Checking for updates");
        lblApk.setBounds(10, 45, 138, 14);
        frame.getContentPane().add(lblApk);

        btnDownload = new JButton("Download");
        btnDownload.setBounds(152, 41, 89, 23);
        frame.getContentPane().add(btnDownload);
        btnDownload.setVisible(false);
        btnDownload.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("select directory");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".apk", "apk");
            chooser.setFileFilter(filter);
            chooser.setSelectedFile(new File("oqrpc.apk"));
            chooser.setApproveButtonText("select");
            chooser.setApproveButtonToolTipText("select this file");
            chooser.setMultiSelectionEnabled(true);

            int returnValue = chooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    FileUtils.copyURLToFile(new URL(apkUrl), chooser.getSelectedFile());
                    lblApk.setText("Finished downloading apk");
                } catch (Exception ignored) {
                    lblApk.setText("Error downloading apk");
                }
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(152, 75, 89, 23);
        frame.getContentPane().add(btnCancel);
        btnCancel.addActionListener(e -> frame.dispose());
        frame.setVisible(true);
        UpdateChecker.updateLbl();
    }
}
