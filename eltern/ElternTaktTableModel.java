package eltern;

import javax.swing.table.AbstractTableModel;

import lehrer.Kollegium;
import estKonstanten.ESTEigenschaften;

/**
 * @author Administrator
 */
public class ElternTaktTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 3616733655390637367L;

	protected Fall myFall;
	protected Kollegium myKollegium;

	protected String[] columnNames = new String[] { "Zeit", "Taktbelegung" };
	protected Class<?>[] columnClasses = new Class[] { String.class,
			String.class };

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return ESTEigenschaften.getAnzahlTakte();
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
			return ("" + ESTEigenschaften.extendedIntervallText(row));
		case 1:
			if (myFall.isBelegt(row)) {
				int lnr = this.myFall.isBelegtMit(row);
				return ("Gespr\u00e4ch mit " + this.myKollegium.getLehrer(lnr)
						.getKuerzel());
			}
			if (myFall.isGesperrt(row)) {
				return ("gesperrt");
			}
			if (myFall.isFrei(row)) {
				return ("frei");
			}
		default:
			return null;
		}
	}

	public ElternTaktTableModel(Fall theFall, Kollegium k) {
		this.myFall = theFall;
		this.myKollegium = k;
	}
}