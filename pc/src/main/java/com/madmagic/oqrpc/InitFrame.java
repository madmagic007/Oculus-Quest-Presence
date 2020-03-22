package com.madmagic.oqrpc;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.awt.event.ActionEvent;

public class InitFrame {

	private JFrame frame;
	private static JTextField field;
	public static JLabel error;
	
	public static void open() {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				InitFrame window = new InitFrame();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public InitFrame() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame("Oculus Quest Ipv4");
		frame.setBounds(100, 100, 450, 155);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Ipv4 of your oculus quest:");
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
		
		JCheckBox startBoot = new JCheckBox("Start with windows boot");
		startBoot.setBounds(10, 85, 141, 23);
		frame.getContentPane().add(startBoot);
		startBoot.setSelected(Config.readBoot());
		
		JButton saveLog = new JButton("Save log file");
		saveLog.setBounds(157, 85, 107, 23);
		frame.getContentPane().add(saveLog);
		saveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File map = new File(System.getenv("APPDATA") + "/oqrpc");
					File log = new File(map.getAbsolutePath() + "/log.txt");
					FileWriter fw = new FileWriter(log);
					fw.write(ResponseHandler.sb.toString());
					fw.close();
					Desktop.getDesktop().edit(log);
				} catch (Exception ignored) {}
			}
		});
		
		JButton btnSaveSettings = new JButton("Save");
		btnSaveSettings.setBounds(337, 85, 89, 23);
		frame.getContentPane().add(btnSaveSettings);
		btnSaveSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Config.setBootSetting(startBoot.isSelected());
				Config.setAddress(field.getText());
			}
		});
	}
	
	private static void setCurrent() {
		String ip = Config.getAddress();
		if (ip.isEmpty()) field.setText("Not set");
		else field.setText(ip);

	}
	
	public static void validate(String ipv4) {
		try {
			InetAddress address = InetAddress.getByName(ipv4);
			if (!address.isReachable(2000)) {
				error.setText("error finding device " + ipv4);
				return;
			}
			error.setText("found device, but apk is not running on device!");

			ApiSender.ask("http://" + ipv4 + ":8080", new JSONObject().put("address", Main.getip()).put("message", "address"));
			Config.setAddress(ipv4);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
