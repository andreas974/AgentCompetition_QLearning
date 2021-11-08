package edu.kit.exp.server.gui.starttab;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Frame to Create an new Experiment
 * 
 */
public class ExperimentCreationFrame extends JFrame {

	private static final long serialVersionUID = 3826698217592532154L;
	private static ExperimentCreationFrame instance;
	private JTextField textFieldExperimentName;
	private JEditorPane editorPane;
	private StartTabController guiController = StartTabController.getInstance();

	public static ExperimentCreationFrame getInstance() {

		if (instance == null) {
			instance = new ExperimentCreationFrame();
		}

		return instance;
	}

	private ExperimentCreationFrame() {

		setTitle("New Experiment");
		// setSize(new Dimension(450,350));
		this.setBounds(150, 150, 450, 350);

		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panelInput = new JPanel();
		panelInput.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panelInput, BorderLayout.CENTER);
		panelInput.setLayout(new MigLayout("", "[][grow]", "[][grow]"));

		JLabel lblExperimentName = new JLabel("Experiment Name:");
		panelInput.add(lblExperimentName, "cell 0 0,alignx trailing");

		textFieldExperimentName = new JTextField();
		panelInput.add(textFieldExperimentName, "cell 1 0,growx");
		textFieldExperimentName.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		panelInput.add(lblDescription, "cell 0 1");

		editorPane = new JEditorPane();
		panelInput.add(editorPane, "cell 1 1,grow");

		JPanel panelButton = new JPanel();
		getContentPane().add(panelButton, BorderLayout.SOUTH);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String name = textFieldExperimentName.getText();
				String description = editorPane.getText();
				try {
					guiController.createNewExperiment(name, description);
					ExperimentCreationFrame.getInstance().dispose();
				} catch (DataInputException e) {
					JOptionPane.showMessageDialog(ExperimentCreationFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (StructureManagementException e) {
					JOptionPane.showMessageDialog(ExperimentCreationFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelButton.add(btnSave);
	}
}
