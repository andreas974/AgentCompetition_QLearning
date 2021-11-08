package edu.kit.exp.client.gui.screens.quiz;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class QuizItemNumberInput extends QuizItem {

	private JTextField inputField;
	private double answer;
	
	public double getAnswer() {
		return answer;
	}

	public void setAnswer(double answer) {
		this.answer = answer;
	}

	public QuizItemNumberInput(String question, double answer) {
		super(question);
		
		this.answer = answer;
	}

	@Override
	JPanel getAnswerPanel() {
		JPanel returnPanel = basePanel;
		returnPanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		
		JPanel answerListPanel = new JPanel();
		returnPanel.add(answerListPanel);
		answerListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		answerListPanel.setBackground(returnPanel.getBackground());
		answerListPanel.setLayout(new BoxLayout(answerListPanel, BoxLayout.Y_AXIS));
		answerListPanel.setPreferredSize(new Dimension(300, 50));
		answerListPanel.setMinimumSize(answerListPanel.getPreferredSize());
		answerListPanel.setMaximumSize(answerListPanel.getPreferredSize());
		
		inputField = new JTextField();
		inputField.setFont(new Font("Tahoma", Font.PLAIN, 17));	
		inputField.setHorizontalAlignment(JTextField.CENTER);
		answerListPanel.add(inputField);
		
		return returnPanel;
	}

	@Override
	boolean isValid() {
		try {
			Pattern pattern = Pattern.compile("[\\d]+([\\.,][\\d]+)?");
			if (pattern.matcher(inputField.getText()).matches()){
				return Double.valueOf(inputField.getText().replace(",", ".")).equals(this.answer);
			}
		} catch (Exception e) { }
		return false;
	}

}
