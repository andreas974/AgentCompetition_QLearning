package edu.kit.exp.client.gui.screens.quiz;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class QuizItemMultipleChoice extends QuizItem {

	private ArrayList<String> answerText;
	private ArrayList<Boolean> answerValid;
	private ArrayList<AbstractButton> answerOption;
	private boolean selectMultiple;
	
	public boolean isSelectMultiple() {
		return selectMultiple;
	}

	public void setSelectMultiple(boolean selectMultiple) {
		this.selectMultiple = selectMultiple;
	}

	public QuizItemMultipleChoice(String question) {
		super(question);
	
		answerText = new ArrayList<>();
		answerValid = new ArrayList<>();
		selectMultiple = false;
	}

	public void addAnswer(String text) {
		addAnswer(text, false);
	}
	
	public void addAnswer(String text, boolean isCorrect){
		answerText.add(text);
		answerValid.add(isCorrect);
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
		answerListPanel.setPreferredSize(new Dimension(300, 200));
		
		answerOption = new ArrayList<>();
		AbstractButton answer;
		ButtonGroup answerGroup = new ButtonGroup();
		
		int validAnswerCount = 0;
		for (int i = 0; i < answerValid.size(); i++) {
			if (answerValid.get(i)){
				validAnswerCount++;
			}
		}
		boolean multipleCorrectAnswers = validAnswerCount > 1;
		
		for (int i = 0; i < answerText.size(); i++) {
			if (multipleCorrectAnswers || selectMultiple){
				answer = new JCheckBox(answerText.get(i), false);
			}
			else {
				answer = new JRadioButton(answerText.get(i), false);
				answerGroup.add(answer);
			}
			answer.setFont(new Font("Tahoma", Font.PLAIN, 17));	
			answerOption.add(answer);
			
			answer.setBackground(returnPanel.getBackground());
			answerListPanel.add(answer);
		}
		
		return returnPanel;
	}

	@Override
	boolean isValid() {
		for (int i = 0; i < answerOption.size(); i++) {
			if (answerOption.get(i).isSelected() != answerValid.get(i)){
				return false;
			}
		}
		return true;
	}
}
