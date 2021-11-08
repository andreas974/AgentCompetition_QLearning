package edu.kit.exp.server.gui.structuretab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Pause;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Quiz;
import edu.kit.exp.server.jpa.entity.SequenceElement;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * This is the GUI element to create the structure of an experiment and to
 * define its elements.
 * 
 */
public class StructureTreeBuilder {

	private static final int DIVIDER_LOCATION = 600;

	private StructureTabController guiController = StructureTabController.getInstance();
	private JLabel labelInfo;
	private JSplitPane splitPane;
	private JScrollPane leftScrollPane;
	private JScrollPane rightScrollPane;
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private JTree experimentTree;
	private Experiment experiment;
	private JPanel detailsPanel;
	private JPanel rightPanel;
	private JPanel experimentBuilderPanel;
	private ArrayList<Object> expandedNodes;
	private HashMap<Object, TreeNode[]> pathMap = new HashMap<Object, TreeNode[]>();
	private Object selectedNodeObject;
	private static StructureTreeBuilder instance = new StructureTreeBuilder();

	private StructureTreeBuilder() {
		experimentBuilderPanel = new JPanel(new GridLayout(1, 0));
		experimentBuilderPanel.setBackground(Color.WHITE);
	}

	public static StructureTreeBuilder getInstance() {
		return instance;
	}

	/**
	 * Creates the editable experiment tree.
	 * 
	 * @param experiment
	 * @return
	 */
	public JPanel createExperimentBuilder(Experiment experiment) {

		boolean reload = false;

		if (experimentTree != null) {
			reload = true;
			saveStatus();

		}

		this.experiment = experiment;
		experimentBuilderPanel.removeAll();

		initLeft();
		initRight();

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightPanel);
		splitPane.setDividerLocation(DIVIDER_LOCATION);
		splitPane.revalidate();

		experimentBuilderPanel.add(splitPane);

		if (reload) {
			for (Object se : expandedNodes) {

				TreeNode[] tn = pathMap.get(se);

				if (tn != null) {
					experimentTree.expandPath(new TreePath(tn));
				}
			}
			reselectPath(rootNode);
			refreshDetails(selectedNodeObject);
		}

		experimentBuilderPanel.revalidate();
		return experimentBuilderPanel;

	}

	private void initRight() {

		rightPanel = new JPanel(new GridLayout(1, 0));
		rightPanel.setBackground(Color.WHITE);

		labelInfo = new JLabel("Select a experiment component to see it´s details!");

		detailsPanel = new JPanel();
		detailsPanel.setBackground(Color.WHITE);
		detailsPanel.setLayout(new GridLayout(1, 0));
		detailsPanel.add(labelInfo);
		rightScrollPane = new JScrollPane(detailsPanel);

		Dimension size = rightScrollPane.getSize();
		rightPanel.setLayout(new GridLayout(0, 1, 0, 0));
		rightScrollPane.setMaximumSize(new Dimension(150, (int) size.getHeight()));
		rightPanel.add(rightScrollPane);

	}

	/**
	 * Initializes the experiment tree
	 */
	private synchronized void initLeft() {

		rootNode = new DefaultMutableTreeNode(experiment.getName());
		rootNode.setUserObject(experiment);

		// Copy data to independent vectors to avoid concurrent access
		// exceptions
		Vector<Session> vectorSession = new Vector<Session>(experiment.getSessions());
		Collections.sort(vectorSession); /* Sort Sessions */
		Vector<Vector<SequenceElement>> vectorSequenceElements = new Vector<Vector<SequenceElement>>();
		HashMap<String, Vector<Period>> periodVectorMap = new HashMap<String, Vector<Period>>();

		for (Session session : vectorSession) {
			vectorSequenceElements.add(new Vector<SequenceElement>(session.getSequenceElements()));
		}

		for (int sessionIndex = 0; sessionIndex < vectorSequenceElements.size(); sessionIndex++) {

			Vector<SequenceElement> sequneceElementVector = vectorSequenceElements.get(sessionIndex);
			Collections.sort(sequneceElementVector); // Sort Sequence
														// Elements

			for (int sequenceElementIndex = 0; sequenceElementIndex < sequneceElementVector.size(); sequenceElementIndex++) {

				SequenceElement sequenceElement = sequneceElementVector.get(sequenceElementIndex);

				if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

					TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;
					List<Period> periods = treatmentBlock.getPeriods();
					Vector<Period> pVektor = new Vector<Period>(periods);
					String key = sessionIndex + "," + sequenceElementIndex;
					periodVectorMap.put(key, pVektor);
				}
			}
		}

		// Build Tree
		for (int sessionIndex = 0; sessionIndex < vectorSession.size(); sessionIndex++) {

			Session session = vectorSession.get(sessionIndex);
			DefaultMutableTreeNode sessionNode = new DefaultMutableTreeNode(session);
			rootNode.add(sessionNode);

			pathMap.put(session, sessionNode.getPath());

			List<SequenceElement> sequenceElementsOfSession = vectorSequenceElements.get(sessionIndex);

			for (int treatmentBlockIndex = 0; treatmentBlockIndex < sequenceElementsOfSession.size(); treatmentBlockIndex++) {

				SequenceElement se = sequenceElementsOfSession.get(treatmentBlockIndex);

				if (se.getClass().equals(TreatmentBlock.class)) {

					TreatmentBlock treatmentBlock = (TreatmentBlock) se;
					DefaultMutableTreeNode treatmentBlockNode = new DefaultMutableTreeNode(treatmentBlock);
					pathMap.put(treatmentBlock, treatmentBlockNode.getPath());
					sessionNode.add(treatmentBlockNode);

					String key = sessionIndex + "," + treatmentBlockIndex;
					Vector<Period> periods = periodVectorMap.get(key);

					for (int periodIndex = 0; periodIndex < periods.size(); periodIndex++) {

						Period period = periods.get(periodIndex);
						DefaultMutableTreeNode periodNode = new DefaultMutableTreeNode(period);
						pathMap.put(period, periodNode.getPath());
						treatmentBlockNode.add(periodNode);
					}
				} else {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(se);
					sessionNode.add(node);
				}

			}
		}

		treeModel = new DefaultTreeModel(rootNode);
		experimentTree = new JTree(treeModel);

		MouseListener mouseListener = new StructureTreeMouseAdapter(this);
		experimentTree.addMouseListener(mouseListener);
		experimentTree.setEditable(true);
		experimentTree.setCellRenderer(new StructureTreeCellRenderer());
		experimentTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		experimentTree.setShowsRootHandles(true);

		leftScrollPane = new JScrollPane(experimentTree);
		leftScrollPane.setMinimumSize(leftScrollPane.getSize());

	}

	/**
	 * Returns the selected session.
	 * 
	 * @return
	 * @throws DataInputException
	 */
	public Session getSelectedSession() throws DataInputException {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		Object userObject;

		try {

			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
			userObject = parentNode.getUserObject();
		} catch (NullPointerException e) {
			throw new DataInputException("Please select a session!");
		}

		if (userObject.getClass().equals(Session.class)) {
			return (Session) userObject;
		} else {
			throw new DataInputException("Please select a session!");
		}
	}

	/**
	 * Returns the selected treatmentBlock
	 * 
	 * @return
	 * @throws DataInputException
	 */
	public TreatmentBlock getSelectedTreatmentBlock() throws DataInputException {

		Object obj = getSelectedNodeObject();

		if (obj.getClass().equals(TreatmentBlock.class)) {
			return (TreatmentBlock) obj;
		} else {
			throw new DataInputException("Please select a treatment block!");
		}
	}

	/**
	 * Add a session to the experiment tree.
	 */
	public void addSession() {

		try {
			guiController.createNewSession();
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Add a treatment block to the experiment tree.
	 */
	public void addTreatmentBlock() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		// experimentTree.expandPath(parentPath.getLastPathComponent());

		try {
			guiController.createNewTreatmentBlock(session, false);
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Add a treatment block to the experiment tree.
	 */
	public void addPracticeTreatmentBlock() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		// experimentTree.expandPath(parentPath.getLastPathComponent());

		try {
			guiController.createNewTreatmentBlock(session, true);
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Add periods to the experiment tree.
	 */
	public void addPeriods() {

		String input = JOptionPane.showInputDialog(experimentBuilderPanel, "How many periods should be created?", "Period Creation", JOptionPane.QUESTION_MESSAGE);

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		TreatmentBlock tb = (TreatmentBlock) parentNode.getUserObject();

		try {
			guiController.createNewPeriods(tb, Integer.parseInt(input));
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Add a quiz to the experiment tree.
	 */
	public void addQuiz() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		// experimentTree.expandPath(parentPath);

		try {
			guiController.createNewQuiz(session);
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Add a pause to the experiment tree.
	 */
	public void addPause() {

		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = experimentTree.getSelectionPath();

		parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		Session session = (Session) parentNode.getUserObject();

		try {
			guiController.createNewPause(session);
		} catch (StructureManagementException e) {
			JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Removes the selected item from the tree.
	 */
	public void removeItem() {

		TreePath currentSelection = experimentTree.getSelectionPath();

		if (currentSelection != null) {

			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
			Object userObject = currentNode.getUserObject();

			if (userObject.getClass().equals(Session.class)) {
				try {
					guiController.removeSession((Session) userObject);
				} catch (StructureManagementException e) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			if (userObject.getClass().equals(Period.class)) {
				try {
					guiController.removePeriod((Period) userObject);
				} catch (StructureManagementException e) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			if (userObject.getClass().equals(TreatmentBlock.class) || userObject.getClass().equals(Quiz.class) || userObject.getClass().equals(Pause.class)) {
				try {
					guiController.removeSequenceElement((SequenceElement) userObject);
				} catch (StructureManagementException e) {
					JOptionPane.showMessageDialog(StructureTab.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		}
	}

	/**
	 * Refreshes the details panel.
	 * 
	 * @param userObject
	 */
	public void refreshDetails(Object userObject) {

		if (userObject.getClass().equals(Experiment.class)) {

			Experiment exp = (Experiment) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new ExperimentDetails(exp));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(TreatmentBlock.class)) {

			TreatmentBlock block = (TreatmentBlock) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new TreatmentBlockDetails(block));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Session.class)) {

			Session block = (Session) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new SessionDetails(block));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Pause.class)) {

			Pause pause = (Pause) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new PauseDetails(pause));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Quiz.class)) {

			Quiz quiz = (Quiz) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new QuizDetails(quiz));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}

		if (userObject.getClass().equals(Period.class)) {

			Period quiz = (Period) userObject;

			detailsPanel.removeAll();
			detailsPanel.add(new PeriodDetails(quiz));

			detailsPanel.revalidate();
			rightPanel.revalidate();

		}
	}

	public JTree getTree() {
		return experimentTree;
	}

	public void setTree(JTree tree) {
		this.experimentTree = tree;
	}

	/**
	 * Save status of tree for reconstruction.
	 */
	private void saveStatus() {

		expandedNodes = new ArrayList<Object>();
		getTreeStatus(treeModel.getRoot());

	}

	/**
	 * Get status of tree for reconstruction.
	 */
	private void getTreeStatus(final Object anyNode) {

		int cc = treeModel.getChildCount(anyNode);

		for (int i = 0; i < cc; i++) {

			DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeModel.getChild(anyNode, i);
			TreeNode[] path = child.getPath();
			// if (!treeModel.isLeaf(child) && experimentTree.isExpanded(new
			// TreePath(ar))) {
			if (experimentTree.isExpanded(new TreePath(path))) {
				expandedNodes.add(child.getUserObject());
				getTreeStatus(child);
			}
		}
	}

	private void reselectPath(DefaultMutableTreeNode anyNode) {

		int cc = treeModel.getChildCount(anyNode);

		for (int i = 0; i < cc; i++) {

			DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeModel.getChild(anyNode, i);
			TreeNode[] path = child.getPath();

			if (child.getUserObject().equals(selectedNodeObject)) {
				experimentTree.setSelectionPath(new TreePath(path));
				experimentTree.scrollPathToVisible(new TreePath(path));
				return;
			} else {
				getTreeStatus(child);
			}
		}

	}

	public Object getSelectedNodeObject() {
		return selectedNodeObject;
	}

	public void setSelectedNodeObject(Object selectedNodeObject) {
		this.selectedNodeObject = selectedNodeObject;
	}

}
