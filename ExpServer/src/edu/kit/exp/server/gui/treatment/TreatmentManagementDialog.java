package edu.kit.exp.server.gui.treatment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.structuretab.StructureTab;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Dialog for the management of treatments
 *
 */
public class TreatmentManagementDialog extends JFrame implements Observer {

	private static final long serialVersionUID = 8722129063752300961L;
	private TreatmentManagementDialogController guiController = TreatmentManagementDialogController.getInstance();
	private static TreatmentManagementDialog instance;
	private List<Treatment> treatmentList;

	public static TreatmentManagementDialog getInstance() {

		if (instance == null) {
			instance = new TreatmentManagementDialog();
		}

		return instance;
	}

	/* Filds */
	private JPanel treatmentListPanel;
	private JPanel contentPanel;
	private JList<Treatment> list;

	public TreatmentManagementDialog() {

		guiController.addObserver(this);
		setTitle("Treatment Management Dialog");
		this.setBounds(200, 200, 800, 450);

		contentPanel = new JPanel();
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		// LIST
		try {
			treatmentList = guiController.getAllTreatments();
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		treatmentListPanel = new JPanel();
		treatmentListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPanel.add(treatmentListPanel, BorderLayout.CENTER);
		treatmentListPanel.setLayout(new BorderLayout(0, 0));

		list = new JList<Treatment> (treatmentList.toArray(new Treatment[treatmentList.size()]));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));

		treatmentListPanel.add(listScroller);

		// INfo
		JLabel lblInfo = new JLabel("Select a treatment from the list to change it.");
		treatmentListPanel.add(lblInfo, BorderLayout.NORTH);
		lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));

		JPanel panelButtons = new JPanel();
		contentPanel.add(panelButtons, BorderLayout.SOUTH);

		JButton btnNewTreatment = new JButton("New Treatment");
		btnNewTreatment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				guiController.showTreatmentCreationDialog();

			}
		});
		panelButtons.add(btnNewTreatment);

		JButton btnChange = new JButton("Update Treatment");
		btnChange.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Treatment t = list.getSelectedValue();

				try {
					guiController.showTreatmentUpdateDialog(t);
				} catch (DataInputException e) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		panelButtons.add(btnChange);

		JButton btnDeleteTreatment = new JButton("Delete Treatment");
		btnDeleteTreatment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Treatment t = list.getSelectedValue();
				try {
					guiController.deleteTreatment(t);
				} catch ( StructureManagementException ex) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}catch (DataInputException ex){
					JOptionPane.showMessageDialog(StructureTab.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				

			}

		});

		panelButtons.add(btnDeleteTreatment);
		
		JButton btnAddTreatmentToTreatmentBlock = new JButton("Add to TreatmentBlock");
		btnAddTreatmentToTreatmentBlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Treatment t = list.getSelectedValue();
				try {
					guiController.addTreatmentToTreatmentBlock(t);
				} catch ( StructureManagementException ex) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (DataInputException ex) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

			}

		});

		panelButtons.add(btnAddTreatmentToTreatmentBlock);
		
		
		JButton btnRemove = new JButton("Remove from TreatmentBlock");
		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Treatment t = list.getSelectedValue();
				try {
					guiController.removeTreatmentFromTreatmentBlock(t);
				} catch (StructureManagementException ex) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (DataInputException ex) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

			}

		});

		panelButtons.add(btnRemove);
		
	}

	private void initTreatmentList() {

		treatmentListPanel.removeAll();

		list = new JList<Treatment> (treatmentList.toArray(new Treatment[treatmentList.size()]));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(10);
		list.setSelectedIndex(treatmentList.size() - 1);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));

		treatmentListPanel.add(listScroller);

		treatmentListPanel.revalidate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {

		treatmentList = (List<Treatment>) arg;

		initTreatmentList();
		// list.setSelectedIndex(treatmentList.size() - 1);

	}

	public void close() {
		this.dispose();

	}

	// private Treatment[] convertListData()

}
