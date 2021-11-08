package edu.kit.exp.server.gui.structuretab;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.kit.exp.server.jpa.entity.Pause;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Quiz;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

/**
 * Renderer for tree elements
 *
 */
class StructureTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 6942963121233876244L;

	private static URL treatmentBlockIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/server/resources/treatmentBlock.gif");
	private static URL quizIconUrl = StructureTreeCellRenderer.class.getResource("/edu/kit/server/resources/quiz.gif");
	private static URL pauseIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/server/resources/pause.gif");
	private static URL sessionIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/server/resources/session.gif");
	private static URL practiceTBIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/server/resources/practiceTreatmentBlock.gif");
	private static URL practicePeriodIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/server/resources/practicePeriod.gif");
	private static URL periodIconURL = StructureTreeCellRenderer.class.getResource("/edu/kit/server/resources/period.gif");

	public StructureTreeCellRenderer() {

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value.getClass().equals(DefaultMutableTreeNode.class)) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object obj = node.getUserObject();

			if (obj.getClass().equals(TreatmentBlock.class)) {

				TreatmentBlock tb = (TreatmentBlock) obj;

				if (tb.getPractice()) {
					setIcon(new ImageIcon(practiceTBIconURL));
				} else {
					setIcon(new ImageIcon(treatmentBlockIconURL));
				}
			}
			if (obj.getClass().equals(Quiz.class)) {
				setIcon(new ImageIcon(quizIconUrl));
			}
			if (obj.getClass().equals(Pause.class)) {
				setIcon(new ImageIcon(pauseIconURL));
			}
			if (obj.getClass().equals(Period.class)) {

				Period p = (Period) obj;

				if (p.getPractice()) {
					setIcon(new ImageIcon(practicePeriodIconURL));
				} else {
					setIcon(new ImageIcon(periodIconURL));
				}

			}
			if (obj.getClass().equals(Session.class)) {
				setIcon(new ImageIcon(sessionIconURL));
			}
		}

		return this;

	}
}