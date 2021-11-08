package edu.kit.exp.server.gui.starttab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * The start tab class.
 * 
 */
public class StartTab extends JPanel implements Observer {

	private static final long serialVersionUID = -4150123067575255277L;

	private static StartTab instance = new StartTab();

	/* Panels */
	private JPanel infoPanel;
	private JPanel tablePanel;
	private JPanel buttonPanel;

	/* ScollPane */
	private JScrollPane jScrollPaneTable;

	/* Table */
	private DefaultTableModel tableModel;
	private JTable table;

	/* Label */
	private JLabel jLabelInformation;

	/* Button */
	private JButton jButtonOpenExperiment;
	private JButton jButtonNewExperiment;

	private StartTabController guiController = StartTabController.getInstance();
	private Object[][] tableData;
	private String[] tableHeadings = { "ID", "Name", "Description" };
	private JButton btnDeleteExperiment;

	private StartTab() {
		super();
		setBorder(new EmptyBorder(20, 20, 20, 20));
		guiController.addObserver(this);
		init();
	}

	public static StartTab getInstance() {

		return instance;
	}

	private void init() {

		// Table Pane
		List<Experiment> list = null;

		try {
			list = guiController.getAllExperiments();
		} catch (DataManagementException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		tableData = transFormTableData(list);

		this.setLayout(new BorderLayout(0, 0));

		tablePanel = new JPanel();
		add(tablePanel);

		createExperimentTable();

		// Button Panel
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		add(buttonPanel, BorderLayout.SOUTH);

		jButtonNewExperiment = new JButton("New Experiment");
		jButtonNewExperiment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ExperimentCreationFrame.getInstance().setVisible(true);

			}
		});

		jButtonOpenExperiment = new JButton("Open Experiment");
		jButtonOpenExperiment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int selectedRow = table.getSelectedRow();
				Integer idExperiment = null;

				if (selectedRow >= 0) {
					Vector<?> rowVector = (Vector<?>) tableModel.getDataVector().elementAt(selectedRow);
					idExperiment = Integer.valueOf(rowVector.elementAt(0).toString());
				}

				try {
					guiController.openExperiment(idExperiment);
				} catch (StructureManagementException e1) {
					JOptionPane.showMessageDialog(StartTab.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (DataInputException e1) {
					JOptionPane.showMessageDialog(StartTab.getInstance(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		buttonPanel.add(jButtonNewExperiment);
		buttonPanel.add(jButtonOpenExperiment);

		btnDeleteExperiment = new JButton("Delete Experiment");
		btnDeleteExperiment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//
				int answer = JOptionPane.showConfirmDialog(StartTab.getInstance(), "Do you want to delete this experiment? All date linked to this experiment will be lost.");

				if (answer == JOptionPane.YES_OPTION) {

					int selectedRow = table.getSelectedRow();
					Integer idExperiment = null;

					if (selectedRow >= 0) {
						Vector<?> rowVector = (Vector<?>) tableModel.getDataVector().elementAt(selectedRow);
						idExperiment = Integer.valueOf(rowVector.elementAt(0).toString());
					}
					try {
						guiController.deleteExperiment(idExperiment);
					} catch (DataInputException e) {
						JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (StructureManagementException e) {
						JOptionPane.showMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		buttonPanel.add(btnDeleteExperiment);

		// Info Panel
		infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
		infoPanel.setLayout(new GridLayout(0, 1, 0, 0));
		add(infoPanel, BorderLayout.NORTH);

		jLabelInformation = new JLabel("Please choose an experiment from the list below.");
		infoPanel.add(jLabelInformation);

	}

	private Object[][] transFormTableData(List<Experiment> list) {

		int rows = list.size();
		int collumns = 3;

		Object[][] result = new Object[rows][collumns];

		Experiment experiment;

		for (int i = 0; i < list.size(); i++) {

			experiment = list.get(i);
			result[i][0] = experiment.getIdExperiment();
			result[i][1] = experiment.getName();
			result[i][2] = experiment.getDescription();

		}

		return result;

	}

	private void updateTable(List<Experiment> e) {

		tableData = transFormTableData(e);
		tablePanel.removeAll();
		createExperimentTable();
		tablePanel.revalidate();

	}

	private void createExperimentTable() {

		tablePanel.setBackground(Color.WHITE);
		tablePanel.setLayout(new GridLayout(1, 0, 0, 0));

		tableModel = new DefaultTableModel(tableData, tableHeadings);
		table = new ExperimentTable(tableModel);

		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBackground(Color.WHITE);
		jScrollPaneTable = new JScrollPane(table);
		tablePanel.add(jScrollPaneTable);

		table.getColumnModel().getColumn(0).setPreferredWidth(0);
		table.getColumnModel().getColumn(1).setPreferredWidth(90);
		table.getColumnModel().getColumn(2).setPreferredWidth(411);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {

		updateTable((List<Experiment>) arg);

	}

}
