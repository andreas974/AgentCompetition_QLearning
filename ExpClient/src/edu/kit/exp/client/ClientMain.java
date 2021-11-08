package edu.kit.exp.client;

import java.rmi.RemoteException;

import edu.kit.exp.client.gui.LoginScreen;
import edu.kit.exp.common.Constants;

/**
 * Main class of the client
 * 
 */
public class ClientMain {

	static public void main(String[] args) throws RemoteException {
		// check command line args for server ip and client id
		String serverName = null;
		String clientId = null;
		boolean autologin = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].toLowerCase().equals("-server") && (i + 1) < args.length) {
				serverName = args[i + 1];
			} else if (args[i].toLowerCase().equals("-clientid") && (i + 1) < args.length) {
				clientId = args[i + 1];
			} else if (args[i].toLowerCase().equals("-autologin")) {
				autologin = true;
			} else if (args[i].toLowerCase().equals("-properties") && (i + 1) < args.length) {
				Constants.addSystemProperties(new String[] { args[i + 1] });
			}
		}

		//Set command line parameters if provided
		LoginScreen loginScreen = LoginScreen.getInstance();
		if (serverName != null)
			loginScreen.setServerIp(serverName);
		if (clientId != null)
			loginScreen.setClientId(clientId);
		loginScreen.setVisible(true);

		if (autologin) {
			loginScreen.tryLogin();
		}
	}
}
