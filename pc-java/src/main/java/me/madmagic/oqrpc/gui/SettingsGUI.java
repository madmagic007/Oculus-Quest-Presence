package me.madmagic.oqrpc.gui;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.*;

import me.madmagic.oqrpc.source.Config;
import me.madmagic.oqrpc.source.Main;
import me.madmagic.oqrpc.api.ApiSender;
import me.madmagic.oqrpc.source.SystemTrayHandler;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class SettingsGUI {

	private static JFrame frame;

	public static void open() {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception ignored) {}

			frame = new JFrame("Oculus Quest Discord RPC");
			frame.setBounds(100, 100, 450, 271);
			frame.getContentPane().setLayout(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setResizable(false);

			JLabel lblNewLabel = new JLabel("IP address (IPv4) of your Oculus Quest:");
			lblNewLabel.setBounds(10, 11, 414, 14);
			frame.getContentPane().add(lblNewLabel);

			JTextField fieldAddress = new JTextField(Config.getAddress().isEmpty() ? "Not set" : Config.getAddress());
			fieldAddress.setBounds(10, 29, 317, 20);
			frame.getContentPane().add(fieldAddress);
			fieldAddress.setColumns(10);

			JLabel error = new JLabel();
			error.setBounds(10, 60, 414, 14);
			frame.getContentPane().add(error);

			JButton validate = new JButton("Validate");
			validate.setBounds(337, 28, 89, 23);
			frame.getContentPane().add(validate);
			validate.addActionListener(e -> error.setText(validate(fieldAddress.getText())));


			JLabel lblPresenceUpdateDelay = new JLabel("Presence update delay in seconds: (Default is 3 seconds)");
			lblPresenceUpdateDelay.setBounds(10, 85, 317, 14);
			frame.getContentPane().add(lblPresenceUpdateDelay);

			JLabel lblLowerValueMeans = new JLabel("Lower value means more updates, might decrease general quest performance");
			lblLowerValueMeans.setBounds(10, 110, 414, 14);
			frame.getContentPane().add(lblLowerValueMeans);

			JSpinner spinnerDelay = new JSpinner();
			spinnerDelay.setModel(new SpinnerNumberModel(10, 1, 60, 1));
			spinnerDelay.setBounds(337, 82, 87, 23);
			frame.getContentPane().add(spinnerDelay);
			spinnerDelay.setValue(Config.getDelay());

			JCheckBox startBoot = new JCheckBox("Start with Windows");
			startBoot.setBounds(10, 202, 127, 23);
			frame.getContentPane().add(startBoot);
			startBoot.setSelected(Config.getStartAtBoot());
			startBoot.setVisible(false);
			if (Main.os.contains("win")) {
				startBoot.setVisible(true);
			}

			JCheckBox notifications = new JCheckBox("Get notified when quest is online/offline");
			notifications.setBounds(10, 174, 222, 23);
			frame.getContentPane().add(notifications);
			notifications.setSelected(Config.getNotifications());

			JCheckBox sleepWake = new JCheckBox("Stop/start presence when quest goes to sleep/wakes up");
			sleepWake.setBounds(10, 148, 416, 23);
			frame.getContentPane().add(sleepWake);
			sleepWake.setSelected(Config.getSleepWake());

			JButton btnSaveSettings = new JButton("Save");
			btnSaveSettings.setBounds(335, 202, 89, 23);
			frame.getContentPane().add(btnSaveSettings);
			btnSaveSettings.addActionListener(e -> {
				JSONObject o = new JSONObject()
						.put("startBoot", startBoot.isSelected())
						.put("address", fieldAddress.getText())
						.put("sleepWake", sleepWake.isSelected())
						.put("delay", (int) spinnerDelay.getValue())
						.put("notifications", notifications.isSelected());
				Config.updateConfig(o);
				frame.dispose();
			});

			JButton btnDebug = new JButton("Log/Debug");
			btnDebug.setBounds(238, 202, 89, 23);
			frame.getContentPane().add(btnDebug);
			btnDebug.addActionListener(l -> {
				new Thread(() -> {
					try {
						File logFile = new File(new File(Config.jarPath()).getParentFile(), "debugLog.txt");
						if (logFile.exists()) logFile.delete();
						logFile.createNewFile();

						StringBuilder sb = new StringBuilder(Config.config.toString(4))
								.append("\nTrying to request connection to quest: ")
								.append(validate(fieldAddress.getText()))
								.append("\nSending notification: ")
								.append(SystemTrayHandler.notif("Test", "Notification test") ? "Successful" : "Unsuccessful")
								.append("\nRequesting game status: \n")
								.append(ApiSender.ask(Main.getUrl(), "game"));

						FileWriter fw = new FileWriter(logFile);
						fw.write(sb.toString());
						fw.close();

						Desktop.getDesktop().edit(logFile);
					} catch (Exception ignored) {
					}
				}).start();
			});

			frame.setVisible(true);
		});
	}
	
	public static String validate(String ipv4) {
		try {
			InetAddress address = InetAddress.getByName(ipv4);
			if (!address.isReachable(2000))
				return "Cannot connect to Quest @ " + ipv4;

			if (ApiSender.ask("http://" + ipv4 + ":16255", "validate").has("valid")) {
				Config.setAddress(ipv4);
				return "Found Quest and the service responded";
			}
		} catch (IOException ignored) {
			return "Found Quest, however the service didn't respond";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "An error has occurred validating";
	}
}
