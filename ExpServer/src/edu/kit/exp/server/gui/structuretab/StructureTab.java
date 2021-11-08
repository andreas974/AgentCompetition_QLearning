package edu.kit.exp.server.gui.structuretab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.server.jpa.entity.Experiment;

/**
 * The tab for editing the experiment structure.
 * 
 */
public class StructureTab extends JPanel implements Observer {

	private static final long serialVersionUID = -4150123067575255277L;

	private static StructureTab instance;

	/* Panels */
	private JPanel infoPanel;
	private JPanel treePanel;
	private JPanel buttonPanel;

	/* Label */
	private JLabel jLabelInformation;

	private StructureTabController guiController = StructureTabController.getInstance();
	private StructureTreeBuilder experimentBuilder = StructureTreeBuilder.getInstance();

	private StructureTab() {
		super();

		guiController.addObserver(this);
		setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setPreferredSize(new Dimension(1091, 743));
		init();
	}

	public static StructureTab getInstance() {

		if (instance == null) {
			instance = new StructureTab();
		}

		return instance;
	}

	private void init() {

		// Tree Panel
		this.setLayout(new BorderLayout(0, 0));
		treePanel = new JPanel();
		treePanel.setBackground(Color.WHITE);
		treePanel.setLayout(new GridLayout(1, 0, 0, 0));
		add(treePanel);

		// Button Panel
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		add(buttonPanel, BorderLayout.SOUTH);

		// Info Panel
		infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
		infoPanel.setLayout(new GridLayout(0, 1, 0, 0));
		add(infoPanel, BorderLayout.NORTH);

		jLabelInformation = new JLabel("Please choose an experiment from the list below.");
		infoPanel.add(jLabelInformation);

		Experiment currentExperiment = guiController.getCurrentExperiment();

		if (currentExperiment != null) {
			initExperimentBuilder(guiController.getCurrentExperiment());
		}

	}

	public void initExperimentBuilder(Experiment experiment) {

		treePanel.removeAll();

		treePanel.add(experimentBuilder.createExperimentBuilder(experiment));

		treePanel.revalidate();
		revalidate();

	}

	@Override
	public void update(Observable o, Object arg) {

		initExperimentBuilder((Experiment) arg);

	}

}
