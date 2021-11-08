package edu.kit.exp.server;

import java.rmi.RemoteException;

import edu.kit.exp.server.gui.mainframe.MainFrameController;

/**
 * Main class of the server application.
 *
 */
public class ServerMain {    
	
    public static void main (String[] args) throws RemoteException {
       	MainFrameController guiController = MainFrameController.getInstance();
    	guiController.initApplication();
    }
}