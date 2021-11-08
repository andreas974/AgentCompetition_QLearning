package edu.kit.exp.client.gui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A simple welcome screen!
 * 
 * Parameters: Index=0: welcome message
 * 
 */
public class DefaultWelcomeScreen extends Screen {

	private static final long serialVersionUID = 3873228381802962074L;
	private String welcomeMessage;
	private JLabel labelWelcomeMessage;

	/**
	 * Constructor
	 * 
	 * @param gameId
	 *            gameId
	 * @param parameter
	 *            Index 0 = welcome message
	 * @param screenId
	 *            screenId
	 * @param showUpTime
	 *            in milliseconds
	 */
	public DefaultWelcomeScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		this.welcomeMessage = "<html><body><h1>Welcome to the experiment!</h1><br> Please wait... </html></body>";
		setLayout(new BorderLayout(0, 0));
		this.labelWelcomeMessage = new JLabel(welcomeMessage);
		labelWelcomeMessage.setFont(new Font("Tahoma", Font.BOLD, 17));

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		setLayout(new BorderLayout(0, 0));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(labelWelcomeMessage);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/edu/kit/exp/common/resources/kit_logo.png"))));
		panel.add(lblLogo, BorderLayout.WEST);
	}

}
