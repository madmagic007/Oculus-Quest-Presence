package com.madmagic.oqrpc;

import java.awt.EventQueue;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

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
		frame.setBounds(100, 100, 450, 300);
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
		
		JButton validate = new JButton("Validate");
		validate.setBounds(337, 28, 89, 23);
		frame.getContentPane().add(validate);
		
		error = new JLabel();
		error.setBounds(10, 60, 414, 14);
		frame.getContentPane().add(error);
		validate.addActionListener(e -> validate(field.getText()));
	}
	
	public static void validate(String ipv4) {
		try {
			InetAddress address = InetAddress.getByName(ipv4);
			if (!address.isReachable(2000)) {
				error.setText("error finding device " + ipv4);
				return;
			}
			error.setText("found device, but apk is not running on device!");

			ApiSender.ask(Main.getUrl(), new JSONObject().put("address", Main.getip()).put("message", "address"));
			Config.write(new JSONObject().put("address", ipv4));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
