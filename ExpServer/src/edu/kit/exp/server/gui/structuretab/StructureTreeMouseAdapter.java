package edu.kit.exp.server.gui.structuretab;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

/**
 * Experiment tree mouse adapter.
 *
 */
public class StructureTreeMouseAdapter implements MouseListener {

	private JTree experimentTree;
	private StructureTreeBuilder experimentBuilder;
	private DefaultMutableTreeNode node = null;
	private JPopupMenu menu = null;
	private JMenuItem itemAddSession = null;
	private JMenuItem itemAddTreatmentBlock;
	private JMenuItem itemAddPeriod;
	private JMenuItem itemRemove;
	private JMenuItem itemAddQuiz;
	private JMenuItem itemAddPause;
	private JMenuItem itemAddPracticeTreatmentBlock;

	public StructureTreeMouseAdapter(StructureTreeBuilder experimentBuilder) {
		super();

		this.experimentBuilder = experimentBuilder;
		this.experimentTree = experimentBuilder.getTree();

		initMenuItems();
	}

	/**
	 * Initialize menu items and their actions.
	 */
	private void initMenuItems() {

		itemAddSession = new JMenuItem("Add Session");
		itemAddSession.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addSession();

			}
		});

		itemAddTreatmentBlock = new JMenuItem("Add Treatment Block");
		itemAddTreatmentBlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addTreatmentBlock();

			}
		});
		
		itemAddPracticeTreatmentBlock = new JMenuItem("Add Practice Treatment Block");
		itemAddPracticeTreatmentBlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addPracticeTreatmentBlock();

			}
		});

		itemAddPeriod = new JMenuItem("Add Period");
		itemAddPeriod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addPeriods();

			}
		});

		itemRemove = new JMenuItem("Remove");
		itemRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.removeItem();

			}
		});
		
		itemAddQuiz = new JMenuItem("Add Quiz");
		itemAddQuiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addQuiz();

			}
		});
		
		itemAddPause = new JMenuItem("Add Pause");
		itemAddPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				experimentBuilder.addPause();

			}
		});

	}

	@Override
	public void mouseClicked(MouseEvent e) {

//		int selRow = experimentTree.getRowForLocation(e.getX(), e.getY());
//
//		if (selRow != -1) {
//
//			TreePath selPath = experimentTree.getPathForLocation(e.getX(), e.getY());
//
//			experimentTree.setSelectionPath(selPath);
//			experimentTree.scrollPathToVisible(selPath);
//			node = (DefaultMutableTreeNode) experimentTree.getLastSelectedPathComponent();
//
//			if (e.getButton() == MouseEvent.BUTTON3) {
//
//				if (e.isPopupTrigger()) {
//
//					showPopupMenu(node.getUserObject(), e.getComponent(), e.getX(), e.getY());
//
//				}
//			}
//
//			experimentBuilder.refreshDetails(node.getUserObject());
//			experimentBuilder.setSelectedNodeObject(node.getUserObject());
//
//		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

		int selRow = experimentTree.getRowForLocation(e.getX(), e.getY());

		if (selRow != -1) {

			TreePath selPath = experimentTree.getPathForLocation(e.getX(), e.getY());

			experimentTree.setSelectionPath(selPath);
			experimentTree.scrollPathToVisible(selPath);
			node = (DefaultMutableTreeNode) experimentTree.getLastSelectedPathComponent();

			if (e.getButton() == MouseEvent.BUTTON3) {

				if (e.isPopupTrigger()) {

					showPopupMenu(node.getUserObject(), e.getComponent(), e.getX(), e.getY());

				}
			}

			experimentBuilder.refreshDetails(node.getUserObject());
			experimentBuilder.setSelectedNodeObject(node.getUserObject());

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		int selRow = experimentTree.getRowForLocation(e.getX(), e.getY());
		
				if (selRow != -1) {
		
					TreePath selPath = experimentTree.getPathForLocation(e.getX(), e.getY());
		
					experimentTree.setSelectionPath(selPath);
					experimentTree.scrollPathToVisible(selPath);
					node = (DefaultMutableTreeNode) experimentTree.getLastSelectedPathComponent();
		
					if (e.getButton() == MouseEvent.BUTTON3) {
		
						if (e.isPopupTrigger()) {
		
							showPopupMenu(node.getUserObject(), e.getComponent(), e.getX(), e.getY());
		
						}
					}
		
					experimentBuilder.refreshDetails(node.getUserObject());
					experimentBuilder.setSelectedNodeObject(node.getUserObject());
		
				}

//		int selRow = experimentTree.getRowForLocation(e.getX(), e.getY());
//
//		if (selRow != -1) {
//
//			if (e.isPopupTrigger()) {
//
//				showPopupMenu(node.getUserObject(), e.getComponent(), e.getX(), e.getY());
//			}
//
//			experimentBuilder.refreshDetails(node.getUserObject());
//		}

	}

	/**
	 * Shows a context menu for a specific Node.
	 * 
	 * @param userObject
	 * @param component
	 * @param x
	 * @param y
	 */
	private void showPopupMenu(Object userObject, Component component, int x, int y) {

		menu = new JPopupMenu();

		if (userObject.getClass().equals(Experiment.class)) {
			menu.add(itemAddSession);
		}

		if (userObject.getClass().equals(Session.class)) {
			menu.add(itemAddTreatmentBlock);
			menu.add(itemAddPracticeTreatmentBlock);
			menu.add(itemAddQuiz);
			menu.add(itemAddPause);
		}

		if (userObject.getClass().equals(TreatmentBlock.class)) {
			menu.add(itemAddPeriod);
		}

		menu.add(itemRemove);
		menu.show(component, x, y);
	}
	
	

}
