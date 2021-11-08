package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Container for editing experiments.
 * 
 */
public class ExperimentDetails extends JPanel {

	private static final long serialVersionUID = -6433621850611921076L;
	
	private StructureTabController guiController = StructureTabController.getInstance();
	private JTextField textFieldName;
	private JEditorPane editorPane;
	
	private Experiment updatedExperiment;

	public ExperimentDetails(Experiment experiment) {
		
		this.updatedExperiment = experiment;

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][grow]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(experiment.getIdExperiment().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblExperimentName = new JLabel("Experiment Name:");
		inputPanel.add(lblExperimentName, "cell 0 1,alignx trailing");

		textFieldName = new JTextField(experiment.getName());
		inputPanel.add(textFieldName, "cell 1 1,growx");
		textFieldName.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		inputPanel.add(lblDescription, "cell 0 2");

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setText(experiment.getDescription());
		inputPanel.add(editorPane, "cell 1 2,grow");

		JLabel labelHeading = new JLabel("Details of Experiment");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				updatedExperiment.setDescription(editorPane.getText());
				updatedExperiment.setName(textFieldName.getText());								
				
					try {
						guiController.updateExperiment(updatedExperiment);
					} catch (StructureManagementException  e1) {
						JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.ERROR_MESSAGE);
					}			
			}
		});
		buttonPanel.add(buttonApplyChanges);

	}
}
