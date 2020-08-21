package com.madmagic.oqrpc.gui;

import java.awt.*;
import java.net.InetAddress;

import javax.swing.*;

import com.madmagic.oqrpc.source.Config;
import com.madmagic.oqrpc.source.HandleGameReceived;
import com.madmagic.oqrpc.source.Main;
import com.madmagic.oqrpc.api.ApiSender;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class SettingsGUI {

	private static JFrame frame;
	private static JTextField field;
	public static JLabel error;

	public static void open() {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ignored) {}

			frame = new JFrame("Oculus Quest Discord RPC");
			frame.setBounds(100, 100, 450, 271);
			frame.getContentPane().setLayout(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			JLabel lblNewLabel = new JLabel("IP Address (IPv4) of your Oculus Quest:");
			lblNewLabel.setBounds(10, 11, 414, 14);
			frame.getContentPane().add(lblNewLabel);

			field = new JTextField();
			field.setBounds(10, 29, 317, 20);
			frame.getContentPane().add(field);
			field.setColumns(10);
			setCurrent();

			JButton validate = new JButton("Validate");
			validate.setBounds(337, 28, 89, 23);
			frame.getContentPane().add(validate);
			validate.addActionListener(e -> validate(field.getText()));

			error = new JLabel();
			error.setBounds(10, 60, 414, 14);
			frame.getContentPane().add(error);

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

			JButton saveLog = new JButton("Save log");
			saveLog.setBounds(238, 202, 89, 23);
			frame.getContentPane().add(saveLog);
			saveLog.addActionListener(e -> {
				try {
					File log = new File(Config.jarPath().replace(new File(Config.jarPath()).getName(), "log.txt"));
					FileWriter fw = new FileWriter(log);
					fw.write(HandleGameReceived.sb.toString());
					fw.close();
					Desktop.getDesktop().edit(log);
				} catch (Exception ignored) {}
			});

			JButton btnSaveSettings = new JButton("Save");
			btnSaveSettings.setBounds(335, 202, 89, 23);
			frame.getContentPane().add(btnSaveSettings);
			btnSaveSettings.addActionListener(e -> {
				JSONObject o = new JSONObject()
						.put("startBoot", startBoot.isSelected())
						.put("address", field.getText())
						.put("sleepWake", sleepWake.isSelected())
						.put("delay", (int) spinnerDelay.getValue())
						.put("notifications", notifications.isSelected());
				Config.updateConfig(o);
				frame.dispose();
			});

			frame.setVisible(true);
		});
	}
	
	private static void setCurrent() {
		String ip = Config.getAddress();
		field.setText(ip.isEmpty() ? "Not set" : ip);
	}
	
	public static void validate(String ipv4) {
		try {
			InetAddress address = InetAddress.getByName(ipv4);
			if (!address.isReachable(2000)) {
				error.setText("Cannot connect to Quest @ " + ipv4);
				return;
			}
			error.setText("Found Quest, however the service is not running.");

			if (ApiSender.ask("http://" + ipv4 + ":16255", "validate").has("valid")) {
				error.setText("Found Quest and the service is running");
			}
			Config.setAddress(ipv4);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
