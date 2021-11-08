package edu.kit.exp.client.gui.screens.quiz;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class QuizItem {
	private String question;
	protected JPanel basePanel;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public QuizItem(String question){
		this.question = question;
		
		basePanel = new JPanel();
		basePanel.setBackground(Color.WHITE);
		basePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	}
	
	abstract JPanel getAnswerPanel();
	
	abstract boolean isValid();
}
