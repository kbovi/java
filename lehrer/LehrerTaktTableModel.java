package lehrer;

import javax.swing.table.AbstractTableModel;

import eltern.Eltern;
import estKonstanten.ESTEigenschaften;

/**
 * @author Administrator
 */
public class LehrerTaktTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 3616733755390637367L;

	protected Lehrer myTeacher;
	protected Eltern myEltern;

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
			if (myTeacher.istBelegt(row)) {
				int index = myTeacher.istBelegtMit(row);
				String ausgabe;
				if (this.myEltern == null) {
					ausgabe = "belegt mit E.Index "
							+ myTeacher.istBelegtMit(row);
				} else {
					int elNummer = this.myEltern.getFall(index).getFallNr();
					String name = this.myEltern.getFall(index).getNachname();
					ausgabe = "belegt mit " + name + " (" + elNummer + ")";
				}
				return (ausgabe);
			}
			if (myTeacher.istAbwesend(row)) {
				return ("abwesend");
			}
			if (myTeacher.istBelegbar(row)) {
				return ("frei (von EST organisiert)");
			}
			if (myTeacher.istBesuchbar(row)) {
				return ("frei (selber organisiert)");
			}
		default:
			return "Falscher Eintrag"; // null;
		}
	}

	public LehrerTaktTableModel(Lehrer theTeacher, Eltern el) {
		this.myTeacher = theTeacher;
		this.myEltern = el;
	}
}