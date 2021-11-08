package edu.kit.exp.server.gui.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * The payoff dialog frame.
 * 
 */
public class PayoffDialog extends JFrame {

	private static final long serialVersionUID = 8482341470946680664L;

	private MainFrameController guiController = MainFrameController.getInstance();
	private JTextField textFieldFactor;

	private JPanel textPanel;

	private JTextArea textArea;

	public PayoffDialog() {

		getContentPane().setBackground(Color.WHITE);
		this.setBounds(200, 200, 500, 400);
		setTitle("Subject Payoffs");

		textPanel = new JPanel();
		textPanel.setBackground(Color.WHITE);
		getContentPane().add(textPanel, BorderLayout.CENTER);
		textPanel.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();

		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel.add(scrollPane);

		JPanel northPanel = new JPanel();
		getContentPane().add(northPanel, BorderLayout.NORTH);

		JLabel lblInfo = new JLabel("Enter a factor to calculate subject payoffs.\r\n");
		northPanel.add(lblInfo);

		textFieldFactor = new JTextField("1");
		northPanel.add(textFieldFactor);
		textFieldFactor.setColumns(10);

		JButton btnOK = new JButton("Calculate");
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				calc();

			}

		});
		northPanel.add(btnOK);
	}

	public void calc() {
		List<Subject> list = null;
		try {
			list = guiController.getSubjects();
		} catch (DataInputException e) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		for (Subject subject : list) {
			String line = "";
			Double fac = Double.valueOf(textFieldFactor.getText());
			Double payOff = subject.getPayoff();
			Double product = payOff * fac;
			line += subject.getIdClient() + ", (" + payOff + ") : " + product.toString() + " Euro" + System.lineSeparator();
			textArea.append(line);

		}

	}
}
