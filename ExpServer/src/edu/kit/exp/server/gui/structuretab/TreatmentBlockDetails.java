package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import edu.kit.exp.server.Constants;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.gui.treatment.TreatmentManagementDialogController;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Container for editing TreatmentBlocks.
 * 
 */
public class TreatmentBlockDetails extends JPanel implements Observer {

	private static final long serialVersionUID = -6433621850611921076L;
	private JTextField textFieldName;
	private JTextField textFieldSequenceNumber;
	private StructureTabController guiController = StructureTabController.getInstance();
	private TreatmentManagementDialogController treatmentDialogControler = TreatmentManagementDialogController.getInstance();
	private TreatmentBlock updatedTreatmentBlock;
	private JEditorPane editorPane;
	private List<Treatment> teatmentList;
	private JPanel comboBoxPanel;
	private JCheckBox chckbxPractice;
	private JCheckBox checkBoxCR;
	private JCheckBox checkBoxFactorial;

	public TreatmentBlockDetails(TreatmentBlock treatmentBlock) {

		updatedTreatmentBlock = treatmentBlock;
		treatmentDialogControler.addObserver(this);

		try {
			setTeatmentList(treatmentDialogControler.getAllTreatments());
		} catch (StructureManagementException e1) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		setBorder(new EmptyBorder(10, 3, 10, 3));
		setBackground(UIManager.getColor("Button.background"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setBackground(UIManager.getColor("Button.background"));
		inputPanel.setLayout(new MigLayout("", "[][grow][]", "[][][][][][][][grow][][]"));

		JLabel labelId = new JLabel("ID:");
		inputPanel.add(labelId, "cell 0 0");

		JLabel labelIdInfo = new JLabel(treatmentBlock.getIdsequenceElement().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblExperimentName = new JLabel("Name:");
		inputPanel.add(lblExperimentName, "cell 0 1,alignx left");

		textFieldName = new JTextField(treatmentBlock.getName());
		inputPanel.add(textFieldName, "cell 1 1,growx");
		textFieldName.setColumns(10);

		JLabel labelSequenceNumber = new JLabel("Sequence Number:");
		inputPanel.add(labelSequenceNumber, "cell 0 2,alignx left");

		System.out.println(treatmentBlock.getSequenceNumber());

		textFieldSequenceNumber = new JTextField(treatmentBlock.getSequenceNumber().toString());
		inputPanel.add(textFieldSequenceNumber, "cell 1 2,growx");
		textFieldSequenceNumber.setColumns(10);

		JLabel lblPractice = new JLabel("Practice:");
		inputPanel.add(lblPractice, "cell 0 3");

		chckbxPractice = new JCheckBox("Mark as practice treatment block.");
		chckbxPractice.setSelected(treatmentBlock.getPractice());
		inputPanel.add(chckbxPractice, "cell 1 3");

		JLabel labelTreatments = new JLabel("Treatments:");
		inputPanel.add(labelTreatments, "cell 0 4,alignx left");

		comboBoxPanel = new JPanel();
		JButton btnNewButton = new JButton("Add / Remove");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				guiController.showTreatmentDialog();

			}
		});

		JLabel lblTreatmentNames = new JLabel();
		String treatmentNames = "<html><body>";

		for (Treatment treatment : treatmentBlock.getTreatments()) {
			treatmentNames += treatment.getName() + "<br>";
		}

		treatmentNames += "</html></body>";
		lblTreatmentNames.setText(treatmentNames);

		inputPanel.add(lblTreatmentNames, "cell 1 4");
		inputPanel.add(btnNewButton, "cell 2 4");

		checkBoxCR = new JCheckBox("Match completely random");
		if (treatmentBlock.getRandomization() != null && treatmentBlock.getRandomization().equals(Constants.DRAW_WITH_REPLACEMENT)) {
			checkBoxCR.setSelected(true);

		}
		inputPanel.add(checkBoxCR, "cell 1 5");

		checkBoxFactorial = new JCheckBox("Match factorial");
		if (treatmentBlock.getRandomization() != null && treatmentBlock.getRandomization().equals(Constants.DRAW_WITHOUT_REPLACEMENT)) {
			checkBoxFactorial.setSelected(true);

		}
		inputPanel.add(checkBoxFactorial, "cell 1 6");

		JLabel lblDescription = new JLabel("Description:");
		inputPanel.add(lblDescription, "cell 0 7");

		editorPane = new JEditorPane();
		editorPane.setBackground(Color.WHITE);
		editorPane.setText(treatmentBlock.getDescription());
		inputPanel.add(editorPane, "cell 1 7 2 3,grow");

		JLabel labelHeading = new JLabel("Details of Treatment Block");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel ButtonPanel = new JPanel();
		panel.add(ButtonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				updatedTreatmentBlock.setDescription(editorPane.getText());
				updatedTreatmentBlock.setName(textFieldName.getText());
				updatedTreatmentBlock.setSequenceNumber(Integer.valueOf(textFieldSequenceNumber.getText()));

				if (checkBoxCR.isSelected()) {
					updatedTreatmentBlock.setRandomization(Constants.DRAW_WITH_REPLACEMENT);
				} else {

					if (checkBoxFactorial.isSelected()) {
						updatedTreatmentBlock.setRandomization(Constants.DRAW_WITHOUT_REPLACEMENT);
					} else {
						updatedTreatmentBlock.setRandomization(null);
					}

				}

				updatedTreatmentBlock.setPractice(chckbxPractice.isSelected());

				try {
					guiController.updateSequenceElement(updatedTreatmentBlock);
				} catch (StructureManagementException e1) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (DataInputException e1) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		ButtonPanel.add(buttonApplyChanges);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {

		comboBoxPanel.removeAll();
		setTeatmentList((List<Treatment>) arg);
		comboBoxPanel.revalidate();

	}

	public void setTeatmentList(List<Treatment> teatmentList) {
		this.teatmentList = teatmentList;
	}

	public List<Treatment> getTeatmentList() {
		return teatmentList;
	}
}
