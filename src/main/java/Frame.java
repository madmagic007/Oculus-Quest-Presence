import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frame {

	private JFrame frame;
	static JTextField textField;
	static DiscordRPC lib = Main.lib;
	static DiscordRichPresence presence = Main.presence;
	
	public static void open() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Frame window = new Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Frame() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Oculus Quest Presence");
		frame.setBounds(100, 100, 410, 195);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCurrentlyPlaying = new JLabel("Currently playing:");
		lblCurrentlyPlaying.setBounds(10, 11, 414, 14);
		frame.getContentPane().add(lblCurrentlyPlaying);
		
		textField = new JTextField();
		textField.setBounds(10, 36, 244, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnSetPresence = new JButton("Set presence");
		btnSetPresence.setBounds(264, 36, 117, 21);
		frame.getContentPane().add(btnSetPresence);
		btnSetPresence.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.edit("Currently playing:", textField.getText());
			}
		});
		
		JLabel lblOtherOptions = new JLabel("Other options:");
		lblOtherOptions.setBounds(10, 67, 414, 14);
		frame.getContentPane().add(lblOtherOptions);
		
		JButton btnOculusHome = new JButton("Oculus Home");
		btnOculusHome.setBounds(10, 92, 117, 20);
		frame.getContentPane().add(btnOculusHome);
		btnOculusHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.edit("Chilling in Oculus Home", null);
			}
		});
		
		JButton btnBrowsingLibrary = new JButton("Browsing library");
		btnBrowsingLibrary.setBounds(137, 92, 117, 20);
		frame.getContentPane().add(btnBrowsingLibrary);
		btnBrowsingLibrary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.edit("Browsing library", null);
			}
		});
		
		JButton btnNewButton = new JButton("Browsing store");
		btnNewButton.setBounds(264, 91, 117, 21);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.edit("Browsing store", null);
			}
		});
		
		JButton btnOculusWeb = new JButton("Oculus Web");
		btnOculusWeb.setBounds(10, 123, 117, 20);
		frame.getContentPane().add(btnOculusWeb);
		btnOculusWeb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.edit("Browsing Oculus Web", null);
			}
		});
		
		JButton btnInstallingGames = new JButton("Installing games");
		btnInstallingGames.setBounds(137, 123, 117, 20);
		frame.getContentPane().add(btnInstallingGames);
		btnInstallingGames.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.edit("Installing games", null);
			}
		});
		
		JButton btnBeingBored = new JButton("Being bored");
		btnBeingBored.setBounds(264, 123, 117, 20);
		frame.getContentPane().add(btnBeingBored);
		btnBeingBored.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.edit("Being bored", null);
			}
		});
	}
}
