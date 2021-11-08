package edu.kit.exp.server.gui.mainframe;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import edu.kit.exp.common.communication.ConnectionException;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.runtab.RunTab;
import edu.kit.exp.server.gui.starttab.ExperimentCreationFrame;
import edu.kit.exp.server.gui.starttab.StartTab;
import edu.kit.exp.server.gui.structuretab.StructureTab;
import edu.kit.exp.server.run.ExistingDataException;
import edu.kit.exp.server.run.SessionDoneException;
import edu.kit.exp.server.run.SessionRunException;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Main Application window
 * 
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1904182956665009961L;
	private MainFrameController guiController = MainFrameController.getInstance();
	private static MainFrame instance;

	private JMenuBar menuBar;
	private JTabbedPane tabbedPane;

	// Tabs
	private StructureTab tabExperimentStructure = StructureTab.getInstance();
	private StartTab tabStart = StartTab.getInstance();
	private RunTab tabRun = RunTab.getInstance();
	private List <String> finalSelectedSensorList = new ArrayList <String> ();
	private boolean bioPluxDone = false;

	public static MainFrame getInstance() {

		if (instance == null) {
			instance = new MainFrame();
		}

		return instance;
	}

	private MainFrame() {

		super();
		this.setSize(new Dimension(1150, 850));
		this.setTitle("Exp Server v.0.1");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		init();

	}

	private void init() {

		/* Menu */
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem itemNewExperiment = new JMenuItem("New Experiment");

		itemNewExperiment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				ExperimentCreationFrame.getInstance().setVisible(true);

			}
		});

		mnFile.add(itemNewExperiment);

		JMenuItem itemOpenExperiment = new JMenuItem("Open Experiment");
		mnFile.add(itemOpenExperiment);

		JMenuItem itemClose = new JMenuItem("Close");
		mnFile.add(itemClose);

		JMenu mnRunSession = new JMenu("Run Session");
		menuBar.add(mnRunSession);

		JMenuItem itemCheckSessionConfig = new JMenuItem("Check Session Configuration");
		itemCheckSessionConfig.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					guiController.checkSessionConfiguration();
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Session successfully checked!");
				} catch (DataInputException e) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (SessionRunException e) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (StructureManagementException e) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		mnRunSession.add(itemCheckSessionConfig);

		JMenuItem itemRunSession = new JMenuItem("Run Session");
		itemRunSession.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					guiController.runSession();
					MainFrameController.getInstance().switchToTab(2);
				} catch (DataInputException ex) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (SessionRunException ex) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (ConnectionException ex) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (ExistingDataException e1) {
					int answer = JOptionPane.showConfirmDialog(StartTab.getInstance(),

					"<html><body>Session was started before but not finished! Do you want to continue the session?<br>Yes = Continue,<br> No = Restart (Data will be lost),<br>Cancel = No action</html></body>");

					if (answer == JOptionPane.YES_OPTION) {

						try {
							guiController.continueSession();
							MainFrameController.getInstance().switchToTab(2);
						} catch (DataInputException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (SessionRunException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (ConnectionException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (ExistingDataException e2) {
							e2.printStackTrace(); //Illegal State
						} catch (StructureManagementException e2) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}

					if (answer == JOptionPane.NO_OPTION) {

						try {
							guiController.resetSession();
							MainFrameController.getInstance().switchToTab(2);
						} catch (DataInputException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (SessionRunException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (ConnectionException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (ExistingDataException e2) {
							e2.printStackTrace(); //Illegal State
						} catch (StructureManagementException e2) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (StructureManagementException e1) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (SessionDoneException e1) {
					int answer = JOptionPane.showConfirmDialog(StartTab.getInstance(), e1.getMessage());

					if (answer == JOptionPane.YES_OPTION) {

						try {
							guiController.resetSession();
							MainFrameController.getInstance().switchToTab(2);
						} catch (DataInputException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (SessionRunException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (ConnectionException ex) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						} catch (ExistingDataException e2) {
							e2.printStackTrace(); //Illegal State
						} catch (StructureManagementException e2) {
							JOptionPane.showMessageDialog(MainFrame.getInstance(), e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}

			}
		});

		mnRunSession.add(itemRunSession);

		JMenuItem itemPayOff = new JMenuItem("Calculate Payoffs");
		itemPayOff.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				guiController.openPayoffDialog();

			}
		});

		mnRunSession.add(itemPayOff);

		/* Tabbed pane */
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.addTab("Start", tabStart);
		tabbedPane.addTab("Experiment Structure", tabExperimentStructure);
		tabbedPane.addTab("Run Session", tabRun);

		getContentPane().add(tabbedPane);
		
		JMenu mnSensor = new JMenu("Sensor");
		menuBar.add(mnSensor);
		
		JMenuItem viewSensorList = new JMenuItem("Configure Sensor List");
		mnSensor.add(viewSensorList);
		
		viewSensorList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String	listData[] =
					{
						"EDA",
						"ECG",
						"Pleth",
						"EEG"
					};

					// Create a new listbox control
					JList<String> listbox = new JList<String>( listData );
					
					Object [] params = {listbox , "Add an additional sensor"};
				    String cusip = JOptionPane.showInputDialog( MainFrame.getInstance(),params, "Input", JOptionPane.PLAIN_MESSAGE );
				    finalSelectedSensorList = listbox.getSelectedValuesList();
				    finalSelectedSensorList.add(cusip);
				    System.out.println(finalSelectedSensorList.toString());
				
			}
		});
		
		/** add query **/
		
		JMenuItem checkConn = new JMenuItem("Check Connection");
		mnSensor.add(checkConn);
		
		/*checkConn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				 // Control checks here
			}
		}); */
		
		/** add checks **/
		JMenuItem initiateRecording = new JMenuItem("Initiate Recording");
		mnSensor.add(initiateRecording);
		initiateRecording.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (finalSelectedSensorList == null  || finalSelectedSensorList.size() == 0) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Sensor List is Empty!", "Error", JOptionPane.ERROR_MESSAGE);
			    }	
				guiController.initiateSensorRecording("",finalSelectedSensorList);
			}
		});

		JMenuItem startRecording = new JMenuItem("Start Recording");
		mnSensor.add(startRecording);
	    startRecording.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (finalSelectedSensorList == null  || finalSelectedSensorList.size() == 0) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Sensor List is Empty!", "Error", JOptionPane.ERROR_MESSAGE);
			    }	
				guiController.startSensorRecording("",finalSelectedSensorList);
			}
		});
	    JMenuItem stopRecording = new JMenuItem("Stop Recording");
		mnSensor.add(stopRecording);
		stopRecording.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (finalSelectedSensorList == null  || finalSelectedSensorList.size() == 0) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Sensor List is Empty!", "Error", JOptionPane.ERROR_MESSAGE);
			    }	
				
				/*PythonInterpreter interpreter = new PythonInterpreter();
				interpreter.exec("from main import closeEvent");
				PyObject someFunc = interpreter.get("closeEvent");
				System.out.println(someFunc);
				PyObject result = someFunc.__call__();
				String realResult = (String) result.__tojava__(String.class);
				System.out.println(realResult);*/
				
				guiController.stopSensorRecording("",finalSelectedSensorList);
			}
		});


	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public boolean isBioPluxDone() {
		return bioPluxDone;
	}

	public void setBioPluxDone(boolean bioPluxDone) {
		this.bioPluxDone = bioPluxDone;
	}

}
