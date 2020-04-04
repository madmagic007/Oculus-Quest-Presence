package com.madmagic.util.updater;

import java.awt.Window.Type;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class Gui {

	public JFrame frmUpdater;
	public static JLabel lblError;
	
	public Gui() {
		initialize();
	}
	
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frmUpdater = new JFrame();
		frmUpdater.setType(Type.UTILITY);
		frmUpdater.setTitle("Updater");
		frmUpdater.setBounds(100, 100, 322, 132);
		frmUpdater.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUpdater.getContentPane().setLayout(null);
		
		JLabel lblVersion = new JLabel("Installing latest version. Please wait...");
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setBounds(10, 11, 286, 14);
		frmUpdater.getContentPane().add(lblVersion);
		
		lblError = new JLabel("");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setBounds(10, 50, 286, 14);
		frmUpdater.getContentPane().add(lblError);
	}
	
	public void end() {
		frmUpdater.dispose();
	}
}
