package edu.kit.exp.client.gui.screens.quiz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.client.gui.screens.Screen;

public abstract class QuizScreen extends Screen {

	public static class ParamObject extends Screen.ParamObject {
		private static final long serialVersionUID = -1982991126040461821L;

	}

	public static class ResponseObject extends Screen.ResponseObject {
		private static final long serialVersionUID = -2424981455361805781L;

	}

	private static final long serialVersionUID = -7399745553763013231L;

	private ArrayList<QuizItem> quizItems;
	private JLabel titleTextPanel;
	private String titleText;
	private JButton returnButton;
	private JLabel questionText;
	private JPanel answerPanel;
	private int currentQuestion;
	private String preText;
	private String postText;
	private QuizItem currentQuiz;
	private String wrongAnswerText;
	private ImageIcon logo;
	private JLabel logoLabel;

	private long totalQuizTimeStart = 0;
	private long totalQuizTimeEnd = 0;
	private int totalErrorCount = 0;

	public String getWrongAnswerText() {
		return wrongAnswerText;
	}

	public void setWrongAnswerText(String wrongAnswerText) {
		this.wrongAnswerText = wrongAnswerText;
	}

	public String getPreText() {
		return preText;
	}

	public void setPreText(String preText) {
		this.preText = preText;
		this.questionText.setText(preText);
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
		updateTitle();
	}

	public String getButtonText() {
		return returnButton.getText();
	}

	public void setButtonText(String text) {
		returnButton.setText(text);
	}

	public ArrayList<QuizItem> getQuizItems() {
		return quizItems;
	}

	public void setQuizItems(ArrayList<QuizItem> quizItems) {
		this.quizItems = quizItems;
	}

	public ImageIcon getLogo() {
		return logo;
	}

	public void setLogo(ImageIcon logo) {
		this.logo = logo;
		logoLabel.setIcon(logo);
	}

	public QuizScreen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		super(gameId, parameter, screenId, showUpTime);

		quizItems = new ArrayList<>();
		currentQuestion = -1;
		titleText = "Comprehensive Questionnaire";
		preText = "Click the button to start the quiz.";
		postText = "You completed the quiz. Click the button to continue the experiment.";
		wrongAnswerText = "The answer is not correct. Please check instructions again.";
		logo = new ImageIcon(getClass().getResource("/edu/kit/exp/common/resources/kit_logo.png"));

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		// Logo
		JPanel kitLogoPanel = new JPanel();
		add(kitLogoPanel, BorderLayout.NORTH);
		kitLogoPanel.setBackground(Color.WHITE);
		kitLogoPanel.setLayout(new BorderLayout(0, 0));

		logoLabel = new JLabel("");
		logoLabel.setIcon(logo);
		kitLogoPanel.add(logoLabel, BorderLayout.EAST);

		JPanel mainContentPanel = new JPanel();
		add(mainContentPanel, BorderLayout.CENTER);
		mainContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContentPanel.setBackground(Color.WHITE);
		mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

		// Title
		JPanel titlePanel = new JPanel();
		mainContentPanel.add(titlePanel);
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new BorderLayout(0, 0));
		titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		titlePanel.setPreferredSize(new Dimension(1000, 150));
		titlePanel.setMaximumSize(titlePanel.getPreferredSize());
		titlePanel.setMinimumSize(titlePanel.getPreferredSize());
		titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		titleTextPanel = new JLabel(titleText, SwingConstants.LEFT);
		titlePanel.add(titleTextPanel);
		titleTextPanel.setFont(new Font("Tahoma", Font.BOLD, 35));
		titleTextPanel.setForeground(Color.BLACK);

		// Question
		JPanel questionPanel = new JPanel();
		mainContentPanel.add(questionPanel);
		questionPanel.setBackground(Color.WHITE);
		questionPanel.setLayout(new BorderLayout(0, 0));
		questionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		questionPanel.setPreferredSize(new Dimension(1000, 200));
		questionPanel.setMaximumSize(questionPanel.getPreferredSize());
		questionPanel.setMinimumSize(questionPanel.getPreferredSize());
		questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Show "quiz is starting" info
		questionText = new JLabel("<html><center>" + preText + "</center></html>", SwingConstants.CENTER);
		questionPanel.add(questionText);
		questionText.setFont(new Font("Tahoma", Font.BOLD, 17));
		questionText.setForeground(Color.BLACK);

		mainContentPanel.add(Box.createRigidArea(new Dimension(1, 10)));

		// Answers
		answerPanel = new JPanel();
		mainContentPanel.add(answerPanel);
		restAnswerPanel();

		// Button
		JPanel buttonPanel = new JPanel();
		mainContentPanel.add(buttonPanel);
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		returnButton = new JButton("Next");
		buttonPanel.add(returnButton);
		returnButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		returnButton.setBackground(new Color(192, 192, 192));
		returnButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		returnButton.setPreferredSize(new Dimension(200, 50));
		returnButton.setMaximumSize(returnButton.getPreferredSize());
		returnButton.setMinimumSize(returnButton.getPreferredSize());
		returnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showNextQuestion();
			}
		});

		setDefaultButton(returnButton);
	}

	private void showNextQuestion() {
		if (currentQuestion == quizItems.size() - 1 && (currentQuiz == null || currentQuiz.isValid()) && !(mainFrame.getClientId().startsWith("agent")) ) {
			// Show "quiz is over" info
			totalQuizTimeEnd = new Date().getTime();
			questionText.setText("<html><center>" + postText + "</center></html>");
			restAnswerPanel();
			currentQuestion++;
		} else if (currentQuestion >= quizItems.size() || mainFrame.getClientId().startsWith("agent")) {
			// Exit Quiz
			guiController.sendQuizProtocolAndWait(true, totalQuizTimeStart + "," + totalQuizTimeEnd + "," + totalErrorCount);
		} else {
			if (currentQuiz == null || currentQuiz.isValid()) {
				// Receive correct answer and show next (or first) question
				if (currentQuestion == -1) {
					// Start time with first question
					totalQuizTimeStart = new Date().getTime();
				}

				currentQuestion++;
				currentQuiz = quizItems.get(currentQuestion);
				questionText.setText("<html><center>" + currentQuiz.getQuestion() + "</center></html>");
				restAnswerPanel();
				answerPanel.add(currentQuiz.getAnswerPanel());
			} else {
				// Receive wrong answer
				totalErrorCount++;
				JDialog dialog = new JOptionPane(wrongAnswerText, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog("Hinweis");
				dialog.setLocationRelativeTo(this);
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
				dialog.dispose();
			}
		}
		updateTitle();
	}

	protected void addQuizItem(QuizItem item) {
		quizItems.add(item);
	}

	private void updateTitle() {
		titleTextPanel.setText("<html>" + titleText + (currentQuestion > -1 && currentQuestion < quizItems.size() ? " (" + (currentQuestion + 1) + "/" + quizItems.size() + ")" : "") + "</html>");
	}

	private void restAnswerPanel() {
		answerPanel.removeAll();
		answerPanel.setBackground(Color.WHITE);
		answerPanel.setLayout(new BorderLayout(0, 0));
		answerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		answerPanel.setPreferredSize(new Dimension(1000, 400));
		answerPanel.setMaximumSize(answerPanel.getPreferredSize());
		answerPanel.setMinimumSize(answerPanel.getPreferredSize());
		answerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		answerPanel.revalidate();
		answerPanel.repaint();
	}
}
