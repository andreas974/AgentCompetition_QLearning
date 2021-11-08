package edu.kit.exp.server.gui.treatment;

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

import net.miginfocom.swing.MigLayout;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * The dialog to update treatments.
 * 
 */
public class TreatmentUpdateDialog extends JFrame {

	private static final long serialVersionUID = -8947312817509913541L;
	private TreatmentManagementDialogController guiController = TreatmentManagementDialogController.getInstance();
	private JTextField textFieldName;
	private JTextField textFieldInstitutionKey;
	private JTextField textFieldEnvironmentKey;
	private JEditorPane editorPane;
	private Treatment updatedTreatment;

	public TreatmentUpdateDialog(Treatment treatment, String title) {

		this.updatedTreatment = treatment;
		this.setBounds(50, 50, 450, 250);
		this.setTitle(title);

		JPanel inputPanel = new JPanel();
		getContentPane().add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][][grow]"));

		JLabel lblTreatmentName = new JLabel("Treatment Name:");
		inputPanel.add(lblTreatmentName, "cell 0 0,alignx trailing");

		textFieldName = new JTextField(treatment.getName());
		inputPanel.add(textFieldName, "cell 1 0,growx");
		textFieldName.setColumns(10);

		JLabel lblInstitutionFactoryKey = new JLabel("Institution Factory Key:");
		inputPanel.add(lblInstitutionFactoryKey, "cell 0 1,alignx trailing");

		textFieldInstitutionKey = new JTextField(treatment.getInstitutionFactoryKey());
		inputPanel.add(textFieldInstitutionKey, "cell 1 1,growx");
		textFieldInstitutionKey.setColumns(10);

		JLabel lblEnvironmentFactoryKey = new JLabel("Environment Factory Key:");
		inputPanel.add(lblEnvironmentFactoryKey, "cell 0 2,alignx trailing");

		textFieldEnvironmentKey = new JTextField(treatment.getEnvironmentFactoryKey());
		inputPanel.add(textFieldEnvironmentKey, "cell 1 2,growx");
		textFieldEnvironmentKey.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		inputPanel.add(lblDescription, "cell 0 3");

		editorPane = new JEditorPane();
		editorPane.setText(treatment.getDescription());
		inputPanel.add(editorPane, "cell 1 3,grow");

		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				updatedTreatment.setDescription(editorPane.getText());
				updatedTreatment.setEnvironmentFactoryKey(textFieldEnvironmentKey.getText());
				updatedTreatment.setInstitutionFactoryKey(textFieldInstitutionKey.getText());
				updatedTreatment.setName(textFieldName.getText());

				if (updatedTreatment.getIdTreatment() == null) {

					try {
						guiController.createTreatment(updatedTreatment);
						dispose();
					} catch ( StructureManagementException e) {
						JOptionPane.showMessageDialog(TreatmentManagementDialog.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						updatedTreatment.setIdTreatment(null);
					} catch (DataInputException  e) {
						JOptionPane.showMessageDialog(TreatmentManagementDialog.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						updatedTreatment.setIdTreatment(null);
					}

				} else {

					try {
						guiController.updateTreatment(updatedTreatment);
						dispose();
					} catch ( StructureManagementException e) {
						JOptionPane.showMessageDialog(TreatmentManagementDialog.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (DataInputException e) {
						JOptionPane.showMessageDialog(TreatmentManagementDialog.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}

				}

			}
		});
		buttonPanel.add(btnOK);
	}

}
