package eltern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * hier kann die menge der kinder in einem fall verwaltet werden. die kinder
 * werden in der reihenfolge der klassen (aufsteigend) eingefuegt. die klasse
 * des kindes mit der kleinsten klasse ist die relevante klasse. sie wird
 * spaeter fuer die verteilung der anschreiben benutzt.
 * 
 * 
 * @author Klaus Bovermann
 * @version 1.0
 */
public class Horde extends TreeSet<Pupil> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4049640104890742837L;

	/**
	 * Liefert die Klasse des jüngsten Kindes.
	 * 
	 * @return Klasse des juengsten Kindes
	 */
	public String getKlasse() {
		if (this.size() == 0) {
			return ("");
		} else {
			return (this.first()).getKlasse();
		}
	}

	/**
	 * Liefert eine ArrayList von Strings aller Vornamen der Kinder
	 * 
	 * @return alle Vornamen
	 */
	public ArrayList<String> getVornamen() {
		ArrayList<String> ret = new ArrayList<String>();
		Iterator<Pupil> it = this.iterator();
		while (it.hasNext()) {
			Pupil p = it.next();
			String vn = p.getVorname();
			ret.add(vn);
		}
		return ret;
	}
}
