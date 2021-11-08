package edu.kit.exp.client.gui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A simple information screen! Can be used i.e. for showing results.
 * 
 */
public class DefaultInfoScreen extends Screen {

	public static class ParamObject extends Screen.ParamObject {
		private static final long serialVersionUID = -7078966659728040885L;
		
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
		public ParamObject(){
			
		}
		
		public ParamObject(String message){
			this.message = message;
		}
	}
	
	private static final long serialVersionUID = -5584915447530697658L;
	private String message;
	private JLabel labelInfo;

	/**
	 * Creates am instance of DefaultInfoScreen.
	 * 
	 * @param gameId
	 * @param parameter
	 *            Screen parameter Index: 0=Info message
	 * @param showUpTime
	 */
	public DefaultInfoScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		this.message = parameter.getMessage();
		setLayout(new BorderLayout(0, 0));
		this.labelInfo = new JLabel(message);
		labelInfo.setFont(new Font("Tahoma", Font.BOLD, 17));

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		setLayout(new BorderLayout(0, 0));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(labelInfo);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(getClass().getResource("/edu/kit/exp/common/resources/kit_logo.png")));
		panel.add(lblLogo, BorderLayout.EAST);
	}

	// public void setCustomScreenParameter(ArrayList<Object> parameters) throws
	// RemoteException {
	//
	// //welcome message text from server
	// this.message= (String) parameters.get(0);
	//
	//
	// }

}
