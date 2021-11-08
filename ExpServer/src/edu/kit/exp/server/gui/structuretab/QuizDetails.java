package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Quiz;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Container for editing quizzes.
 * 
 */
public class QuizDetails extends JPanel {

	private static final long serialVersionUID = -6433621850611921076L;

	private StructureTabController guiController = StructureTabController.getInstance();
	private JTextField textFieldFactoryKey;

	private Quiz updatedQuiz;
	private JTextField textFieldSequenceNumber;

	public QuizDetails(Quiz quiz) {

		this.updatedQuiz = quiz;

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

		JLabel labelIdInfo = new JLabel(quiz.getIdsequenceElement().toString());
		inputPanel.add(labelIdInfo, "cell 1 0");

		JLabel lblSequenceNumber = new JLabel("Sequence Number:");
		inputPanel.add(lblSequenceNumber, "cell 0 1,alignx trailing");

		textFieldSequenceNumber = new JTextField(quiz.getSequenceNumber().toString());
		inputPanel.add(textFieldSequenceNumber, "cell 1 1,growx");
		textFieldSequenceNumber.setColumns(10);

		JLabel lblFactoryKey = new JLabel("Screen ID:");
		inputPanel.add(lblFactoryKey, "cell 0 2,alignx left");

		textFieldFactoryKey = new JTextField(quiz.getQuizFactoryKey());
		inputPanel.add(textFieldFactoryKey, "cell 1 2,growx");
		textFieldFactoryKey.setColumns(10);

		JLabel labelHeading = new JLabel("Details of Quiz");
		panel.add(labelHeading, BorderLayout.NORTH);
		labelHeading.setHorizontalAlignment(SwingConstants.LEFT);
		labelHeading.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JButton buttonApplyChanges = new JButton("Apply Changes");
		buttonApplyChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				updatedQuiz.setQuizFactoryKey(textFieldFactoryKey.getText());

				try {
					updatedQuiz.setSequenceNumber(Integer.valueOf(textFieldSequenceNumber.getText()));
					guiController.updateSequenceElement(updatedQuiz);
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
