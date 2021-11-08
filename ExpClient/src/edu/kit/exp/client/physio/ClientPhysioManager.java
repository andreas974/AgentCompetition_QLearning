package edu.kit.exp.client.physio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import edu.kit.exp.client.comunication.ClientCommunicationManager;
import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.common.PhysioControlCommand;

public class ClientPhysioManager {

	private static ClientPhysioManager instance;
	List<String> commandList = new ArrayList<String> ();
	List <Integer> processIds = new ArrayList<Integer> ();
	List <Process> processThreads = new ArrayList <Process>();
	Process pluxPr = null;
	Process eegPr = null;
	 InputStream stderr = null;
     InputStream stdout = null;               
     BufferedReader reader = null;
     OutputStream stdin = null;
     BufferedWriter writer = null;
     String item;
    
	private ClientPhysioManager() {
	}

	public static ClientPhysioManager getInstance() {

		if (instance == null) {
			instance = new ClientPhysioManager();
		}
		return instance;
	}
	
	public void sendPhysioDataToServer(HashMap<String, Double> physioData) {
		ClientCommunicationManager.getInstance().getMessageSender().sendPhysioData(System.currentTimeMillis(), physioData);
	}
	
	public void processCommand(PhysioControlCommand command , List<String> sensorList){
		if (command ==PhysioControlCommand.INITIATE ) {
			System.out.println("Initiate");
			System.out.println(sensorList);
			for (String item : sensorList) {
				System.out.println(item);
				if (item.equals("ECG") || item.equals("EDA")
						|| item.equals("Pleth") ) {
					final JFileChooser fc = new JFileChooser();
					// In response to a button click:
					int returnVal = fc.showDialog(ClientGuiController.getInstance().getMainFrame(),
							"Please choose the batch file for Bioplux recording");
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						// This is where a real application would open the
						// file.
						String filePath = fc.getSelectedFile()
								.getAbsolutePath();
						System.out.println(filePath);
						String escaped = filePath.replace(new String("\\"), new String("\\\\")) ;
						System.out.println(escaped);
						commandList.add(escaped);
					}
				}  else if (item.equals("EEG")) {
					/* String command ="cmd.exe /c python" + myscript.py */
					final JFileChooser fc = new JFileChooser();

					// In response to a button click:
					int returnVal = fc.showDialog(ClientGuiController.getInstance().getMainFrame(),
							"Please choose the py file to start EEG Brain Products");
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						// This is where a real application would open the
						// file.
						String filePath = fc.getSelectedFile()
								.getAbsolutePath();
						System.out.println(filePath);
						String escaped = filePath.replace(new String("\\"),
								new String("\\\\"));
						System.out.println(escaped);
						// start py script
						commandList.add(escaped);
					}
				}
			}
			System.out.println("Sensor recording setup initiated");
		} else  if (command ==PhysioControlCommand.START_RECORDING ) {
			System.out.println("Sensor recording starting..." );
			for (final String item : commandList) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if (item.endsWith("bat")) {
								String clientId = InetAddress.getLocalHost()
										.getHostName();
								ProcessBuilder pb = new ProcessBuilder(item,
										clientId);
								pluxPr = pb.start();
								System.out.println(item + " started for "
										+ clientId);
								InputStream stdout = pluxPr.getInputStream();
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(stdout));
								String line;
								while ((line = reader.readLine()) != null) {
									System.out.println("Stdout: " + line);

								}
								try {
									pluxPr.waitFor();

								} catch (InterruptedException e2) {
									e2.printStackTrace();
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			    
		} else  if (command == PhysioControlCommand.STOP_RECORDING ) {
			System.out.println("Tries to stop");				
			for (String item:commandList) {
				if (item.endsWith("bat")) {
					System.out.println("Tries to stop " + item);		       
					pluxPr.destroy();						
				}			
			}
		}	
	}
	}
