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

import org.joda.time.DateTime;

import edu.kit.exp.common.Constants;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Container for editing sessions.
 * 
 */
public class SessionDetails extends JPanel {

	private static final long serialVersionUID = -6433621850611921076L;
	private StructureTabController guiController = StructureTabController.getInstance();
	private JTextField textFieldName;
	private JTextField textFieldPlannedDate;
	private Session updatedSession;
	private JEditorPane editorPane;
	private JTextField textCohorts;
	private JTextField textSubjectsPerCohort;

	public SessionDetails(Session session) {

		updatedSession = session;

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][grow]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(session.getIdSession().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblSessionName = new JLabel("Session Name:");
		inputPanel.add(lblSessionName, "cell 0 1,alignx left");

		textFieldName = new JTextField(session.getName());
		inputPanel.add(textFieldName, "cell 1 1,growx");
		textFieldName.setColumns(10);

		JLabel lblPlannedDate = new JLabel("Planned Date:");
		inputPanel.add(lblPlannedDate, "cell 0 2,alignx left");

		textFieldPlannedDate = new JTextField();
		inputPanel.add(textFieldPlannedDate, "cell 1 2,growx");

		DateTime date = new DateTime(updatedSession.getPlannedDate());
		textFieldPlannedDate.setColumns(10);
		textFieldPlannedDate.setText(date.toString(Constants.TIME_STAMP_FORMAT));
		
		JLabel lblCohorts = new JLabel("Cohorts:");
		inputPanel.add(lblCohorts, "cell 0 3,alignx left");
		
		String numberOfCohorts = String.valueOf(session.getCohorts().size());
		String numberOfSubjects;
		if(!session.getCohorts().isEmpty()){
			numberOfSubjects = String.valueOf(session.getCohorts().get(0).getSize());
		}else{
			numberOfSubjects = "";
		}
		
		
		textCohorts = new JTextField(numberOfCohorts);
		inputPanel.add(textCohorts, "cell 1 3,growx");
		textCohorts.setColumns(10);
		
		JLabel lblSubjectsPerCohort = new JLabel("Subjects per Cohort:");
		inputPanel.add(lblSubjectsPerCohort, "cell 0 4,alignx trailing");
		
		textSubjectsPerCohort = new JTextField(numberOfSubjects);
				
		inputPanel.add(textSubjectsPerCohort, "cell 1 4,growx");
		textSubjectsPerCohort.setColumns(10);
		
//		JButton btnManageCohorts = new JButton("Manage Cohorts");
//		btnManageCohorts.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				
//				try {
//					guiController.showCohortManagementDialog(updatedSession);
//				} catch (StructureManagementException e) {
//					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//				}
//				
//			}
//		});
//		
//		inputPanel.add(btnManageCohorts, "cell 1 3");

		JLabel lblDescription = new JLabel("Description:");
		inputPanel.add(lblDescription, "cell 0 5");

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setText(session.getDescription());
		inputPanel.add(editorPane, "cell 1 5,grow");

		JLabel labelHeading = new JLabel("Details of Session");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				updatedSession.setDescription(editorPane.getText());
				updatedSession.setName(textFieldName.getText());
								
				
				try {
					updatedSession.setPlannedDate(guiController.parseDateString(textFieldPlannedDate.getText()));
					
						guiController.updateSession(updatedSession, textCohorts.getText(), textSubjectsPerCohort.getText());
					} catch (StructureManagementException e1) {
						JOptionPane.showMessageDialog(StructureTab.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (DataInputException e1) {
						JOptionPane.showMessageDialog(StructureTab.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				

			}
		});
		buttonPanel.add(buttonApplyChanges);

	}
}
