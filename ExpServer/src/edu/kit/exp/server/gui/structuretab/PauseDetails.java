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
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Pause;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Container for editing a pause.
 * 
 */
public class PauseDetails extends JPanel {

	private static final long serialVersionUID = -6433621850611921076L;
	
	private StructureTabController guiController = StructureTabController.getInstance();
	private JTextField textFieldTime;
	private JEditorPane editorPane;
	
	private Pause updatedPause;
	private JTextField textFieldSequenceNumber;

	public PauseDetails(Pause pause) {
		
		this.updatedPause = pause;

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][][grow]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(pause.getIdsequenceElement().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");
		
		JLabel lblSequenceNumber = new JLabel("Sequence Number:");
		inputPanel.add(lblSequenceNumber, "cell 0 1,alignx trailing");
		
		textFieldSequenceNumber = new JTextField(pause.getSequenceNumber().toString());
		inputPanel.add(textFieldSequenceNumber, "cell 1 1,growx");
		textFieldSequenceNumber.setColumns(10);

		JLabel lblExperimentName = new JLabel("Time in Seconds:");
		inputPanel.add(lblExperimentName, "cell 0 2,alignx left");

		textFieldTime = new JTextField(Long.valueOf((pause.getTime()/1000)).toString());
		inputPanel.add(textFieldTime, "cell 1 2,growx");
		textFieldTime.setColumns(10);

		JLabel lblDescription = new JLabel("Message:");
		inputPanel.add(lblDescription, "cell 0 3,alignx left");

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setText(pause.getMessage());
		inputPanel.add(editorPane, "cell 1 3,grow");

		JLabel labelHeading = new JLabel("Details of Pause");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				updatedPause.setMessage(editorPane.getText());
								
					try {
						updatedPause.setSequenceNumber(Integer.valueOf(textFieldSequenceNumber.getText()));
						updatedPause.setTime(Long.valueOf(textFieldTime.getText())*1000);
						guiController.updateSequenceElement(updatedPause);
					} catch (StructureManagementException e1 ) {
						JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.ERROR_MESSAGE);
					} catch (DataInputException e1) {
						JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.ERROR_MESSAGE);
					}		
			}
		});
		buttonPanel.add(buttonApplyChanges);

	}
}
