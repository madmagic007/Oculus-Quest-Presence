package com.madmagic.oqrpc.gui;

import com.madmagic.oqrpc.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;

public class UpdateChecker {

	private static String version = "2.2.0";
	private static String updateUrl = "https://raw.githubusercontent.com/madmagic007/Oculus-Quest-Presence/master/update.json";
	private static String dlUrl;

	private static JFrame frame;

	public static void check() {
		File old = new File("C:\\Program Files (x86)\\Oculus Quest  Discord RPC");
		if (old.isDirectory()) {
			try {
				Desktop.getDesktop().browse(new URI("https://github.com/madmagic007/Oculus-Quest-Presence/blob/master/OldVersion.md"));
			} catch (Exception ignored) {
			}
		}

		Request r = new Request.Builder()
				.url(updateUrl).build();
		try {
			Response rs = new OkHttpClient().newCall(r).execute();
			JSONObject rB = new JSONObject(rs.body().string());

			if (rB.getString("latest").equals(version)) {
				return;
			}

			dlUrl = rB.getString("url");
			openFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openFrame() {
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

		JButton btnInstall = new JButton("Install");
		btnInstall.setBounds(45, 57, 89, 23);
		frame.getContentPane().add(btnInstall);
		btnInstall.addActionListener(e -> {
			String updater = Config.getUpdater();

			if (updater.isEmpty()) {
				return;
			}
			try {
				String command = "java -jar \"" + updater + "\" \"" + dlUrl + "\"";
				System.out.println("running command " + command);
				Runtime.getRuntime().exec(command);
				frame.dispose();
				System.exit(0);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.setBounds(170, 57, 89, 23);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(e -> frame.dispose());

		frame.setVisible(true);
	}
}
