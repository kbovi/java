package eltern;

import java.io.Serializable;

import javax.swing.table.AbstractTableModel;

import lehrer.Kollegium;

/**
 * @author Administrator
 */
public class LehrerTableModel extends AbstractTableModel implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3616733755390637367L;
	/**
	 * 
	 */
	protected Kollegium dieKollegen;

	protected String[] columnNames = new String[] { "Lehrernummer",
			"Lehrername" };
	protected Class<?>[] columnClasses = new Class[] { String.class,
			String.class };

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return this.dieKollegen.anzahlLehrer();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Class<?> getColumnClass(int col) {
		return columnClasses[col];
	}

	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return ("" + (3 * row + 100));// (""+this.dieKollegen.getLehrer(row).getNr());
											// //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		case 1:
			return ("" + this.dieKollegen.getLehrer(row).anrede() + " " + this.dieKollegen
					.getLehrer(row).getName());

		default:
			return null;
		}
	}

	public LehrerTableModel(Kollegium k) {
		this.dieKollegen = k;
	}
}