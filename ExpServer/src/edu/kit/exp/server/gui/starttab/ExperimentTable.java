package edu.kit.exp.server.gui.starttab;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents the JTable for Experiments.
 *
 */
public class ExperimentTable extends JTable{
	
	private static final long serialVersionUID = -8296076701639918834L;

	public ExperimentTable(DefaultTableModel tableModel) {
		super(tableModel);
	}

	@Override
	public boolean isCellEditable(int x, int y) {

		if (y == 0) {
			return false;
		}

		return true;
	}

}
