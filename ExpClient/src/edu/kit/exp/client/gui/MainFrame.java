package edu.kit.exp.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.Constants;

/**
 * Class of the main Application window.
 * 
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 8780788346280812429L;
	private static MainFrame instance = null;
	private Dimension screenSize;
	private JPanel contentPanel;
	private static Screen currentScreen;
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

	public Dimension getScreenSize() {
		return screenSize;
	}

	public static MainFrame getInstance() {

		if (instance == null) {
			instance = new MainFrame();
		}

		return instance;
	}

	private MainFrame() {
		super();
		getContentPane().setBackground(Color.LIGHT_GRAY);

		currentScreen = null;

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.LIGHT_GRAY);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setSize(this.getContentPane().getSize());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));

		// Fullscreen
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// this.setSize(screenSize);
		this.setTitle("Exp Client v.0.1");

		if (Constants.isSystemDebugMode()) {
			this.setSize(new Dimension(1280, 1024));
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
		} else {
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			this.setAlwaysOnTop(true);
			this.setUndecorated(true);
			this.setExtendedState(MAXIMIZED_BOTH);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (JOptionPane.showConfirmDialog(e.getWindow(), "Really exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						e.getWindow().dispose();
						System.exit(0);
					}
				}
			});
		}
	}

	public void showScreen(Screen screen) {

		Long clientTimeStamp = 0l;

		if (currentScreen != null) {
			currentScreen.extiScreen();

			if (currentScreen.getLogDisplayEvent() == true && currentScreen.getGameId() != null) {
				clientTimeStamp = new Date().getTime();
				ClientGuiController.getInstance().sendTrialLogMessage(currentScreen.getGameId(), currentScreen.getLogTextExit(), currentScreen.getScreenId(), currentScreen.getLogValueExit(), clientTimeStamp);
			}
		}
		currentScreen = screen;

		contentPanel.removeAll();
		if (screen != null) {
			contentPanel.add(screen, BorderLayout.CENTER);
		}
		contentPanel.revalidate();
		contentPanel.repaint();

		if (screen != null) {
			clientTimeStamp = new Date().getTime();
			if (screen.getLogDisplayEvent() == true && screen.getGameId() != null) {
				ClientGuiController.getInstance().sendTrialLogMessage(screen.getGameId(), screen.getLogTextEnter(), screen.getScreenId(), screen.getLogValueExit(), clientTimeStamp);
			}
			screen.getParameter().setLogTimeEnter(clientTimeStamp);
		}

	}
}
