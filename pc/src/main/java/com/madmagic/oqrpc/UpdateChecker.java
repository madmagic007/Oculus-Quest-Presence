package com.madmagic.oqrpc;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import javax.swing.*;

public class UpdateChecker {

	private String version = "2.2.0";
	private String updateUrl = "https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/update.json";
	private String dlUrl;

	private JFrame frame;
	private JButton btnInstall;
	private JButton btnNewButton;
	private JLabel lblNotif;

	
	public UpdateChecker() {
		new Thread(() -> {
			Request r = new Request.Builder()
					.url(updateUrl).build();
			try {
				Response rs = new OkHttpClient().newCall(r).execute();
				JSONObject rB = new JSONObject(rs.body().string());

				if (rB.getString("latest").equals(version)) {
					lblNotif.setText("Up to date");
					frame.dispose();
					return;
				}

				dlUrl = rB.getString("url");
				openFrame();
			} catch (Exception e) {
				e.printStackTrace();
				lblNotif.setText("Error checking for updates");
			}
		}).start();
	}
	
	private void openFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		frame = new JFrame();
		frame.setBounds(100, 100, 320, 134);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUpdate = new JLabel("Update found, install now?");
		lblUpdate.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdate.setBounds(10, 11, 284, 14);
		frame.getContentPane().add(lblUpdate);
		
		btnInstall = new JButton("Install");
		btnInstall.setBounds(45, 57, 89, 23);
		frame.getContentPane().add(btnInstall);
		btnInstall.addActionListener(e -> {
			String updater = Config.getUpdater();
			if (updater.isEmpty()) {
				lblNotif.setText("Error running updater");
				return;
			}
			try {
				Runtime.getRuntime().exec("java -jar " + updater + " " + dlUrl);
				System.exit(0);
				frame.dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
				lblNotif.setText("Error running updater");
			}
		});
		
		btnNewButton = new JButton("Cancel");
		btnNewButton.setBounds(170, 57, 89, 23);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(e -> frame.dispose());

		frame.setVisible(true);
	}
}
