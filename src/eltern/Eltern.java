package eltern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import tools.Logbuch;
import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

public class Eltern extends ArrayList<Fall> implements Serializable {

	private static final long serialVersionUID = 4049070523558409016L;
	private int actNumber;

	public Eltern(String dateiname) {

		Logbuch l = Logbuch.getLogbuch();
		l.log("elternContructor", 10);

		// try
		// {
		// FileInputStream fs = new FileInputStream(dateiname);
		// ObjectInputStream is = new ObjectInputStream (fs);
		//
		// Eltern eltern = (Eltern)is.readObject();
		// is.close();
		//
		// this.addAll(eltern);
		//
		// l.log("Eltern Eingelesen!", 0);
		// }
		// catch (ClassNotFoundException e)
		// {
		// l.log(e.toString(), 0);
		// }
		// catch (IOException e)
		// {
		// l.log(e.toString(), 0);
		// }

		{
			Hordizer myHordizer = new Hordizer(dateiname);
			ArrayList<Fall> myList = myHordizer.getEltern();

			TreeSet<Fall> ts = new TreeSet<Fall>();
			Iterator<Fall> it = myList.iterator();
			while (it.hasNext()) {
				Fall f = it.next();
				ts.add(f);
			}

			it = ts.iterator();
			while (it.hasNext()) {
				Fall f = it.next();
				this.add(f);
			}
		}
		this.actNumber = 0;
	}

	public void loescheLeereEltern() {
		Logbuch l = Logbuch.getLogbuch();
		l.log("Der L\u00f6scher!", 10);

		Iterator<Fall> it = this.iterator();
		while (it.hasNext()) {
			Fall f = it.next();
			if (f.getAnzahlLehrerGewollt() == 0) {
				String n = f.getNachname();
				l.log("gel\u00f6scht: " + n, 10);
				it.remove();
			}
		}
		this.reset();
	}

	public void reset() {
		this.actNumber = 0;
	}

	public int getElternNummer(String nachname) {
		for (int i = 0; i < this.getAnzahlEltern(); i++) {
			if (this.getNachname(i).equals(nachname)) {
				return i;
			}
		}
		return -1;

	}

	public Fall getFall(int i) {
		return this.get(i);
	}

	public void setWunsch(int elternNr, int wunschNr, int lehrerNr) {
		this.getFall(elternNr).setGewollt(wunschNr, lehrerNr);
	}

	/**
	 * Trägt den Lehrer, der als "wunschNr-ter" gewählt wurde, in dem Takt ein.
	 * 
	 * @param elternNr
	 * 
	 * @param wunschNr
	 * 
	 * @param taktNr
	 */
	public void erfuelleWunsch(int elternNr, int wunschNr, int taktNr) {
		int lehNr = this.getFall(elternNr).getGewollt(wunschNr);
		this.getFall(elternNr).setTakt(taktNr, lehNr);
		this.getFall(elternNr).setTaktBeiElternwunsch(wunschNr, taktNr);
	}

	public void macheWunscherfuellungRueckgaengig(int enr, int wnr) {
		int tnr = this.getFall(enr).getTaktBeiElternwunsch(wnr);
		this.getFall(enr).befreie(tnr);
		this.getFall(enr).setTaktBeiElternwunsch(wnr, ESTStatics.KEIN_TAKT);
	}

	public boolean elternWunschWurdeErfuellt(int enr, int wnr) {
		return (this.getFall(enr).getTaktBeiElternwunsch(wnr) != ESTStatics.KEIN_TAKT);
	}

	public boolean isGesperrt(int enr, int tnr) {
		return (this.getFall(enr).isGesperrt(tnr));
	}

	public boolean hatZeit(int enr, int tnr) {
		Fall f = this.getFall(enr);
		boolean mitLuft = ESTEigenschaften.isMitLuft();

		if (!mitLuft) {
			return f.isFrei(tnr);
		} else {
			boolean freiHier = f.isFrei(tnr);
			int tnrL = tnr - 1;
			int tnrR = tnr + 1;
			boolean freiLeft;
			if ((tnr == 0) || (tnr == ESTEigenschaften.getSchnitt())) {
				freiLeft = true;
			} else {
				freiLeft = f.isFrei(tnrL);
			}

			boolean freiRight;
			if ((tnr == ESTEigenschaften.getSchnitt() - 1)
					|| (tnr == ESTEigenschaften.getAnzahlTakte() - 1)) {
				freiRight = true;
			} else {
				freiRight = f.isFrei(tnrR);
			}
			return (freiHier && freiLeft && freiRight);
		}
	}

	public void setTakt(int enr, int tnr, int wert) {
		this.getFall(enr).setTakt(tnr, wert);
	}

	public void sperre(int enr, int tnr) {
		this.getFall(enr).sperre(tnr);
	}

	public void befreie(int enr, int tnr) {
		this.getFall(enr).befreie(tnr);
	}

	/**
	 * Welcher Lehrer wurde als "wunschNr-ter" gewählt?
	 * 
	 * @param elternNr
	 * 
	 * @param wunschNr
	 * 
	 * @return lehrernummer
	 */
	public int getWunsch(int elternNr, int wunschNr) {
		return this.getFall(elternNr).getGewollt(wunschNr);
	}

	public int getTakt(int enr, int tnr) {
		return this.getFall(enr).getTakt(tnr);
	}

	public int getTaktBeiElternwunsch(int enr, int wnr) {
		return (this.getFall(enr).getTaktBeiElternwunsch(wnr));
	}

	public void setTaktBeiElternwunsch(int enr, int wnr, int tnr) {
		this.getFall(enr).setTaktBeiElternwunsch(wnr, tnr);
	}

	public void setEltern(int enr) {
		if (enr >= 0 && enr < this.getAnzahlEltern()) {
			this.actNumber = enr;
		}
	}

	public int getActNumberEltern() {
		return this.actNumber;
	}

	public Fall getActFall() {
		return this.getFall(this.getActNumberEltern());
	}

	public void inc() {
		if (this.actNumber < this.size() - 1) {
			this.actNumber = this.actNumber + 1;
		}
	}

	public void dec() {
		if (this.actNumber > 0) {
			this.actNumber = this.actNumber - 1;
		}
	}

	/**
	 * Wieviele Eltern werden momentan verwaltet?
	 * 
	 * @return anzahlEltern
	 */
	public int getAnzahlEltern() {
		return this.size();
	}

	/**
	 * Wieviele Lehrer hat man gewählt?
	 * 
	 * @param enr
	 * 
	 * @return anzahlGewuenschterEltern
	 */
	public int getAnzahlLehrerGewollt(int enr) {
		return (this.getFall(enr)).getAnzahlLehrerGewollt();
	}

	/**
	 * Wieviele Kinder hat die Familie?
	 * 
	 * @param enr
	 * 
	 * @return anzahlKinder
	 */
	public int getKinderzahl(int enr) {
		return this.getFall(enr).getKinderzahl();
	}

	/**
	 * Liefert die Lehrernummer des Lehrers, der als index-ter Lehrer gewünscht
	 * wurde.
	 * 
	 * @param enr
	 * 
	 * @param index
	 * 
	 * @return der index-te gewünschte Lehrer
	 */
	public int getGewollt(int enr, int index) {
		return this.getFall(enr).getGewollt(index);
	}

	/**
	 * Wie heisst die Familie?
	 * 
	 * @param enr
	 * 
	 * @return Nachname
	 */
	public String getNachname(int enr) {
		return this.getFall(enr).getNachname();
	}

	public ArrayList<String> getVornamen(int enr) {
		return this.getFall(enr).getVornamen();
	}

	public String wahlstring(int enr) {
		return this.getFall(enr).wahlstring();
	}

	public int anzahlElternHabenGewaehlt(int lnr) {
		int anz = 0;
		for (int enr = 0; enr < this.getAnzahlEltern(); enr++) {
			if (this.getFall(enr).lehrerWurdeGewollt(lnr)) {
				anz++;
			}
		}
		return anz;
	}

	public int totalAnzahlWunsche() {
		int ret = 0;
		for (int enr = 0; enr < this.getAnzahlEltern(); enr++) {
			ret += this.getFall(enr).getAnzahlLehrerGewollt();
		}
		return ret;
	}

	public int anzahlErfuellterWuensche() {
		int ret = 0;
		for (int enr = 0; enr < this.getAnzahlEltern(); enr++) {
			ret += this.getFall(enr).getAnzahlErfuellterWuensche();
		}
		return ret;

	}

	public int getAnzahlErfuellterWuensche(int enr) {
		return this.getFall(enr).getAnzahlErfuellterWuensche();
	}

	public boolean alleWuenscheErfuellt(int enr) {
		return this.getFall(enr).alleWuenscheErfuellt();
	}

	public boolean keinWunschErfuellt(int enr) {
		return this.getFall(enr).keinWunschErfuellt();
	}

	public boolean einigeWuenscheErfuellt(int enr) {
		return this.getFall(enr).einigeWuenscheErfuellt();
	}

	public ArrayList<String> alleElternNamen(String anfang) {
		ArrayList<String> ret = new ArrayList<String>();
		for (int enr = 0; enr < this.getAnzahlEltern(); enr++) {
			String name = this.getNachname(enr).toLowerCase();
			String patt = anfang.toLowerCase();
			if (name.startsWith(patt)) {
				String zeile = "";
				zeile += enr;
				zeile += ": ";
				zeile += this.getNachname(enr);
				zeile += " (";
				zeile += this.getFall(enr).getKindernamen();
				zeile += ")";
				ret.add(zeile);
			}
		}
		return ret;
	}
}
