package edu.kit.exp.client.gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import edu.kit.exp.common.Constants;
import edu.kit.exp.server.jpa.PersistenceUtil;

/**
 * The login screen.
 * 
 */
public class LoginScreen extends JFrame {

	private static final long serialVersionUID = -2162644827002542056L;
	private static LoginScreen instance;

	// text fields
	private JTextField textFieldClientId;
	private JTextField textFieldServer;

	// JPanel
	private JPanel inputPanel;
	private JPanel buttonPanel;

	// JControls
	private JButton buttonLogin;

	private LoginScreen() throws HeadlessException {
		this.setBounds(50, 50, 299, 177);
		this.setTitle("Login Screen");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		init();
	}

	public static LoginScreen getInstance() {

		if (instance == null) {
			instance = new LoginScreen();
		}

		return instance;
	}

	private void init() {

		// text fields
		textFieldClientId = new JTextField(25);
		try {
			textFieldClientId.setText(InetAddress.getLocalHost().getHostName());
		} catch (Exception ex) {
			textFieldClientId.setText("Client001");
		}

		// input panel
		inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		inputPanel.setLayout(new MigLayout());

		inputPanel.add(new JLabel("Client ID:"));
		inputPanel.add(textFieldClientId, "span, grow, wrap");

		inputPanel.add(new JLabel("Server:"));
		textFieldServer = new JTextField();
		textFieldServer.setText(Constants.getServerName());
		inputPanel.add(textFieldServer, "span, grow, wrap");

		// button
		buttonLogin = new JButton("Login");
		getRootPane().setDefaultButton(buttonLogin);
		buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		buttonPanel.add(buttonLogin, BorderLayout.CENTER);
		buttonLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryLogin();
			}
		});

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(inputPanel, BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	}

	public void tryLogin() {
		buttonLogin.setEnabled(false);

		try {
			PersistenceUtil.setDatabaseIP(textFieldServer.getText());
			ClientGuiController.getInstance().login(textFieldClientId.getText(), textFieldServer.getText());
            MainFrame.getInstance().setClientId(textFieldClientId.getText());
			MainFrame.getInstance().setVisible(true);
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(LoginScreen.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		buttonLogin.setEnabled(true);
	}

	public void setClientId(String name) {
		textFieldClientId.setText(name);
	}

	public void setServerIp(String ip) {
		textFieldServer.setText(ip);
	}
}
