package com.madmagic.oqrpc.gui;

import java.awt.*;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.madmagic.oqrpc.source.Config;
import com.madmagic.oqrpc.source.Discord;
import com.madmagic.oqrpc.source.Main;
import com.madmagic.oqrpc.api.ApiSender;
import org.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.io.File;
import java.io.FileWriter;

public class ConfigGUI {

	private JFrame frame;
	private static JTextField field;
	public static JLabel error;
	private static JCheckBox startBoot;
	
	public static void open() {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				ConfigGUI window = new ConfigGUI();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public ConfigGUI() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame("Oculus Quest Discord RPC");
		frame.setBounds(100, 100, 450, 155);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("IP Address (IPv4) of your Oculus Quest:");
		lblNewLabel.setBounds(10, 11, 403, 14);
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

		if (Main.os.contains("win")) {
			startBoot = new JCheckBox("Start with Windows");
			startBoot.setBounds(10, 85, 127, 23);
			frame.getContentPane().add(startBoot);
			startBoot.setSelected(Config.readBoot());
		}

		JButton saveLog = new JButton("Save log");
		saveLog.setBounds(137, 85, 89, 23);
		frame.getContentPane().add(saveLog);
		saveLog.addActionListener(e -> {
			try {
				File log = new File(Config.jarPath().replace(new File(Config.jarPath()).getName(), "log.txt"));
				FileWriter fw = new FileWriter(log);
				fw.write(Discord.sb.toString());
				fw.close();
				Desktop.getDesktop().edit(log);
			} catch (Exception ignored) {}
		});

		JButton btnSaveSettings = new JButton("Save");
		btnSaveSettings.setBounds(337, 85, 89, 23);
		frame.getContentPane().add(btnSaveSettings);
		btnSaveSettings.addActionListener(e -> {
			Config.setBootSetting(startBoot.isSelected());
			Config.setAddress(field.getText());
		});
	}
	
	private static void setCurrent() {
		String ip = Config.getAddress();
		if (ip.isEmpty()) field.setText("Not set");
		else field.setText(ip);

	}
	
	public static void validate(String ipv4) {
		try {
			Config.setAddress(ipv4);
			InetAddress address = InetAddress.getByName(ipv4);
			if (!address.isReachable(2000)) {
				error.setText("Cannot connect to Quest @ " + ipv4);
				return;
			}
			error.setText("Found Quest, however application is not running on device!");

			JSONObject validate = ApiSender.ask(Main.getUrl("validate"));
			if (validate.has("valid")) {
				error.setText("device found and running apk. Ready to go!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
