package eltern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeSet;

import lehrer.Kollegium;
import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

/**
 * Ein fall ist ein Klasse, in der<br>
 * --> ein nachname,<br>
 * --> eine horde (von geschwisterkindern)<br>
 * --> eine Liste von Zahlen (Nummern von gewuenschten Lehrern)<br>
 * --> eine Liste von Takten verwaltet werden.<br>
 * Die Takte sind belegt (analog zu den Lehrern) mit:<br>
 * -1 : frei<br>
 * -2 : gesperrt<br>
 * irgendwas >= 0 : die Lehrernummer, mit dem in dem Takt gesprochen wird.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Fall implements Serializable, Comparable<Fall> {

	private static final long serialVersionUID = -6713121745818774270L;
	private Horde kinderschar; // hier stehen die Kinder (ggf. mehrere)
	/**
	 * @uml.property name="anzahlLehrerGewollt"
	 */
	private int anzahlLehrerGewollt; // tatsächlich gewünschte
	private int[] lehrerGewollt; // maximal 10 Lehrer!
	private int[] zugTakte; // in welchem Takt wird der Wunsch erfüllt?
	/**
	 * @uml.property name="takte"
	 */
	private int[] takte; // wie bei Lehrern!
	/**
	 * @uml.property name="nachname"
	 */
	private String nachname;
	/**
	 * @uml.property name="fallNr"
	 */
	private int fallNr;

	/**
	 * @param fallNr
	 *            the fallNr to set
	 */
	public void setFallNr(int fallNr) {
		this.fallNr = fallNr;
	}

	public Fall(String nachname, int fallNr) {
		this.nachname = nachname;
		this.fallNr = fallNr;
		this.kinderschar = new Horde();

		// es gibt maximal 10 Wuensche!!!
		this.anzahlLehrerGewollt = 0;
		this.lehrerGewollt = new int[ESTEigenschaften.getMaxWuensche()];
		this.zugTakte = new int[ESTEigenschaften.getMaxWuensche()];

		for (int i = 0; i < ESTEigenschaften.getMaxWuensche(); i++) {
			this.lehrerGewollt[i] = ESTStatics.KEIN_LEHRER;
			this.zugTakte[i] = ESTStatics.KEIN_TAKT;
		}

		// Zum testen!
		if (ESTEigenschaften.getLogLevel() == 11) // testdaten erzeugen!!
		{
			Random r = new Random();
			int zuf = r.nextInt(10); // Maximal 5 Wünsche!
			for (int w = 0; w < zuf; w++) {
				// einen Lehrer zufällig wählen
				int zufLnr = r.nextInt(70); // / es gibt 70 Lehrer?????
				this.addGewollt(zufLnr);
			}
		}

		this.takte = new int[ESTEigenschaften.getAnzahlTakte()];

		for (int i = 0; i < ESTEigenschaften.getAnzahlTakte(); i++) {
			this.takte[i] = ESTStatics.ELTERNFREI;
		}
	}

	// Anzahl der Kinder
	// Anzahl der Wuensche
	// Nummer des iten Wunsches
	// Ist Takt i frei oder gesperrt oder mit welchem Lehrer belegt
	// Setzen: freien Takt mit einem best. Lehrer belegen
	// und einen belegten Takt freigeben

	/**
	 * Wieviele Kinder hat dieser Fall?
	 * 
	 * @return AnzahlKinder
	 */
	public int getKinderzahl() {
		return this.kinderschar.size();
	}

	public boolean alleWuenscheErfuellt() {
		return this.getAnzahlErfuellterWuensche() == this
				.getAnzahlLehrerGewollt();
	}

	public boolean keinWunschErfuellt() {
		return this.getAnzahlErfuellterWuensche() == 0;
	}

	public boolean einigeWuenscheErfuellt() {
		int aew = this.getAnzahlErfuellterWuensche();
		int aw = this.getAnzahlLehrerGewollt();
		return (aew > 0 && aew < aw);
	}

	public int compareTo(Fall otherFall) {
		return (this.getCompareString().compareTo(otherFall.getCompareString()));
	}

	/**
	 * @return
	 * @uml.property name="fallNr"
	 */
	public int getFallNr() {
		return fallNr;
	}

	public void sperre(int tnr) {
		this.takte[tnr] = ESTStatics.ELTERNSPERRE;
	}

	public void sperreNachVorgabe() {
		for (int i = 0; i < ESTEigenschaften.getAnzahlTakte(); i++) {
			befreie(i);
		}

		String sperrTakte = ESTEigenschaften.getElternSperrTakte();
		StringTokenizer st = new StringTokenizer(sperrTakte, ";", false);
		while (st.hasMoreTokens()) {
			String taktString = st.nextToken();
			int tnr = Integer.parseInt(taktString);
			sperre(tnr);
		}
	}

	public void befreieNachVorgabe() {
		for (int i = 0; i < ESTEigenschaften.getAnzahlTakte(); i++) {
			sperre(i);
		}

		String sperrTakte = ESTEigenschaften.getElternSperrTakte();
		StringTokenizer st = new StringTokenizer(sperrTakte, ";", false);
		while (st.hasMoreTokens()) {
			String taktString = st.nextToken();
			int tnr = Integer.parseInt(taktString);
			befreie(tnr);
		}
	}

	public boolean isGesperrt(int tnr) {
		return this.takte[tnr] == ESTStatics.ELTERNSPERRE;
	}

	public void befreie(int tnr) {
		this.takte[tnr] = ESTStatics.ELTERNFREI;
	}

	public boolean isFrei(int tnr) {
		return this.takte[tnr] == ESTStatics.ELTERNFREI;
	}

	public boolean isBelegt(int tnr) {
		return this.takte[tnr] >= 0;
	}

	public int isBelegtMit(int tnr) {
		if (isBelegt(tnr)) {
			return this.takte[tnr];
		} else {
			return -1;
		}
	}

	/**
	 * Liefert alle Vornamen der Kinder
	 * 
	 * @return AlleKinder
	 */
	public ArrayList<String> getVornamen() {
		return this.kinderschar.getVornamen();
	}

	public Horde getHorde() {
		return this.kinderschar;
	}

	/**
	 * Wie heisst die Familie?
	 * 
	 * @return Nachname
	 * @uml.property name="nachname"
	 */
	public String getNachname() {
		return this.nachname;
	}

	/**
	 * Welche Klasse ist anzusprechen?<br>
	 * Dabei wird immer die Klasse des <b>jüngsten</b> Kindes genutzt.
	 * 
	 * @return Klasse des juengsten Kindes
	 */
	public String getKlasse() {
		return this.kinderschar.getKlasse();
	}

	/**
	 * Ein Lehrer wird als gewählt hinzugefügt!
	 * 
	 * @param lehNr
	 */
	public void addGewollt(int lehNr) {
		if (!lehrerWurdeGewollt(lehNr)) {
			int index = this.anzahlLehrerGewollt;
			this.anzahlLehrerGewollt++;
			this.setGewollt(index, lehNr);
		}
	}

	public boolean lehrerWurdeGewollt(int lnr) {
		for (int wnr = 0; wnr < this.getAnzahlLehrerGewollt(); wnr++) {
			if (lnr == this.getGewollt(wnr)) {
				return (true);
			}
		}
		return false;
	}

	public boolean lehrerWurdeErfolgreichGewollt(int lnr) {
		for (int wnr = 0; wnr < this.getAnzahlLehrerGewollt(); wnr++) {
			if (lnr == this.getGewollt(wnr) && this.zugTakte[wnr] >= 0) {
				return (true);
			}
		}
		return false;
	}

	public void clearGewollt() {
		this.anzahlLehrerGewollt = 0;
		for (int i = 0; i < ESTEigenschaften.getMaxWuensche(); i++) {
			this.lehrerGewollt[i] = ESTStatics.KEIN_LEHRER;
		}
	}

	/**
	 * Der index-te gewählte Lehrer wird neu gesetzt.
	 * 
	 * @param index
	 * 
	 * @param lehrerNum
	 */
	public void setGewollt(int index, int lehrerNum) {
		this.lehrerGewollt[index] = lehrerNum;
		// this.anzahlLehrerGewollt++; // ist das schlau?????
	}

	/**
	 * Liefert die Lehrernummer, die als index-ter Lehrer gewählt wurde.
	 * 
	 * @param index
	 * 
	 * @return lehrernummer
	 */
	public int getGewollt(int index) {
		return this.lehrerGewollt[index];
	}

	/**
	 * In dem Takt wird der Wert eingetragen
	 * 
	 * @param taktNr
	 * 
	 * @param wert
	 */
	public void setTakt(int taktNr, int wert) {
		this.takte[taktNr] = wert;
	}

	/**
	 * Liefert die Nummer des Lehrers, der im angegebenen Takt bedient wird.<br>
	 * Falls kein Lehrer hier bedient wird, wird der entspr. Code geliefert.
	 * 
	 * @param taktNr
	 * 
	 * @return Lehrernummer
	 */
	public int getTakt(int taktNr) {
		return this.takte[taktNr];
	}

	/**
	 * In welchem Takt wird der n-te Wunschlehrer "bedient"???<br>
	 * Falls dieser Lehrer nicht bedient wird, wird "-1" geliefert.
	 * 
	 */
	public int getTaktBeiElternwunsch(int wnr) {
		return (this.zugTakte[wnr]);
	}

	public void setTaktBeiElternwunsch(int wnr, int tnr) {
		this.zugTakte[wnr] = tnr;
	}

	/**
	 * @return
	 * @uml.property name="takte"
	 */
	public int[] getTakte() {
		return this.takte;
	}

	public int[] getGewollteKollegen() {
		return this.lehrerGewollt;
	}

	/**
	 * @return Returns the anzahlLehrerGewollt.
	 * @uml.property name="anzahlLehrerGewollt"
	 */
	public int getAnzahlLehrerGewollt() {
		return anzahlLehrerGewollt;
	}

	public int getAnzahlErfuellterWuensche() {
		int ret = 0;
		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			if (this.zugTakte[wnr] != ESTStatics.KEIN_TAKT) {
				ret++;
			}
		}
		return ret;
	}

	public String toString() {
		String ret = this.getNachname() + ":";
		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			int l = this.lehrerGewollt[wnr];
			int t = this.zugTakte[wnr];
			ret += "(" + l + "," + t + ")";
		}
		return ret;
	}

	public String getAlleLehrerNummern() {
		String ret = "";
		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			if (ret != "") {
				ret += " ";
			}
			ret += "" + this.lehrerGewollt[wnr];
		}
		return ret;
	}

	public String getAlleLehrerNummernKorrigiert() {
		String ret = "";
		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			if (ret != "") {
				ret += " ";
			}
			ret += "" + (this.lehrerGewollt[wnr] * 3 + 100);
		}
		return ret;
	}

	public String getAlleLehrer(Kollegium k) {
		String ret = "";
		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			if (ret != "") {
				ret += ", ";
			}
			ret += "" + k.getLehrer(this.lehrerGewollt[wnr]).getName();
		}
		return ret;
	}

	public String[] getAlleLehrerListe(Kollegium k) {
		String[] ret = new String[this.getAnzahlLehrerGewollt()];
		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			String anzeige = k.getLehrer(this.lehrerGewollt[wnr]).getName();
			int imTakt = this.zugTakte[wnr];
			if (imTakt == ESTStatics.KEIN_TAKT) {
				anzeige += " (nicht erfuellt!)";
			} else {
				anzeige += ": ";
				anzeige += ESTEigenschaften.extendedIntervallText(imTakt);
			}
			ret[wnr] = anzeige;
		}
		return ret;
	}

	public String getCompareString() {
		return (this.getKlasse() + "; " + this.nachname + "; " + this
				.getKindernamen());
	}

	public String getKindernamen() {
		Iterator<Pupil> iter = this.getHorde().iterator();
		String kindernamen = "";

		while (iter.hasNext()) {
			if (!kindernamen.equalsIgnoreCase(""))
				kindernamen += ", ";
			Pupil p = iter.next();
			kindernamen += p.getVorname() + " (" + p.getKlasse() + ")";
		}
		return kindernamen;
	}

	public String wahlstring() {
		String s = this.nachname;
		s += ": ";
		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			s += "(L:";
			s += this.lehrerGewollt[wnr];
			s += "; im T:";
			s += this.zugTakte[wnr];
			s += ")";
		}
		return s;
	}

	public ArrayList<String> wahlergebnis() {
		ArrayList<String> zeilen;
		zeilen = new ArrayList<String>();

		TreeSet<Integer> help;
		help = new TreeSet<Integer>();

		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			if (this.zugTakte[wnr] != ESTStatics.KEIN_TAKT) {
				help.add(this.zugTakte[wnr]);
			}
		}

		Iterator<Integer> it = help.iterator();
		while (it.hasNext()) {
			int tnr = it.next();
			boolean gefunden = false;
			int wnr = 0;
			while (!gefunden && wnr < this.anzahlLehrerGewollt) {
				if (this.zugTakte[wnr] == tnr) {
					String s = "";
					s += this.lehrerGewollt[wnr];
					s += ";";
					s += this.zugTakte[wnr];
					zeilen.add(s);
					gefunden = true;
				}
				wnr++;
			}
		}

		// for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++)
		// {
		// if (this.zugTakte [wnr] != ESTStatics.KEIN_TAKT)
		// {
		// String s = "";
		// s += this.lehrerGewollt [wnr];
		// s += ";";
		// s += this.zugTakte[wnr];
		// zeilen.add(s);
		// }
		// }

		for (int wnr = 0; wnr < this.anzahlLehrerGewollt; wnr++) {
			if (this.zugTakte[wnr] == ESTStatics.KEIN_TAKT) {
				String s = "";
				s += this.lehrerGewollt[wnr];
				s += ";";
				s += "-";
				zeilen.add(s);
			}
		}
		return zeilen;

	}
}
