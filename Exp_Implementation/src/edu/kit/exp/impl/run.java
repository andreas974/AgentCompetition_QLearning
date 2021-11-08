package edu.kit.exp.impl;

import edu.kit.exp.client.ClientMain;
import edu.kit.exp.server.ServerMain;

public class run {
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {

		char serverClientChoise = 0;

		if (args.length > 0) {
			String input = args[0].toLowerCase();
			if (input.equals("-s")){
				serverClientChoise = 's';
			}
			else if (input.equals("-c")){
				serverClientChoise = 'c';
			}
			else {
				System.out.println("No expected command line parameters. Use \"-s\" to start a server or \"-c\" to start a client.");
				System.exit(1);
			}
		} else {
			System.out.println("Please enter \"s\" to start a server or \"c\" to start a client.");
			serverClientChoise = ((char) System.in.read());
		}

		switch (serverClientChoise) {
		case 's':
		case 'S':
			System.out.println("Starting server...");
			ServerMain.main(args);
			break;

		case 'c':
		case 'C':
			System.out.println("Starting client...");
			ClientMain.main(args);
			break;

		default:
			System.out.println("No expected command line parameters.");
			System.exit(1);
			break;
		}

	}

}
