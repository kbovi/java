package texter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import lehrer.Kollegium;
import eltern.Eltern;
import eltern.Fall;
import estKonstanten.ESTEigenschaften;

/**
 * @author Administrator
 */
public class Texter {

	private Kollegium kollegen;
	private Eltern eltern;

	// private TreeSet <Fall> sortEltern;
	// private ArrayList <Fall> sortierteFaelle;

	public Texter(Kollegium k, Eltern e) {
		this.kollegen = k;
		this.eltern = e;
	}

	// public String nameZeitRaum (int lehrerNummer, int taktNummer) {
	// Lehrer l = this.kollegen.getLehrer(lehrerNummer);
	// if (l != null && taktNummer >= 0 && taktNummer <
	// ESTEigenschaften.getAnzahlTakte())
	// {
	// String fullName = l.vollerName();
	// String raum = l.getRaum();
	// String zeit = ESTEigenschaften.anfangsZeit(taktNummer);
	// return fullName + " um " + zeit + " Uhr in Raum "+ raum;
	// }
	// else
	// {
	// return "";
	// }
	// }

	public ArrayList<String> lehrerTermineErsterTag(int lnr, boolean mitTag) {
		return lehrerTermine(lnr, 0, ESTEigenschaften.getSchnitt() - 1, mitTag);
	}

	public ArrayList<String> lehrerTermineZweiterTag(int lnr, boolean mitTag) {
		return lehrerTermine(lnr, ESTEigenschaften.getSchnitt(),
				ESTEigenschaften.getAnzahlTakte() - 1, mitTag);
	}

	public ArrayList<String> lehrerTermine(int lnr) {
		return lehrerTermine(lnr, 0, ESTEigenschaften.getAnzahlTakte() - 1,
				true);
	}

	public ArrayList<String> lehrerTermine(int lnr, int von, int bis,
			boolean mitTermin) {
		ArrayList<String> ret = new ArrayList<String>();
		ArrayList<String> roh = this.kollegen.getTermine(lnr, von, bis);

		Iterator<String> it = roh.iterator();
		while (it.hasNext()) {
			String rohzeile = it.next();
			StringTokenizer st = new StringTokenizer(rohzeile, ";", false);
			String tnrs = st.nextToken();
			String enrs = st.nextToken();
			int tnr = Integer.parseInt(tnrs);
			String neueZeile = "";
			if (mitTermin) {
				neueZeile += ESTEigenschaften.wochentag(tnr) + ",";
				neueZeile += ESTEigenschaften.anfangsZeit(tnr);
				neueZeile += ": ";
			} else {
				neueZeile += "" + tnr + ": ";
			}
			if (enrs.equals("x")) {
				neueZeile += "nicht anwesend.";
			} else if (enrs.equals("-")) // noch frei
			{
				neueZeile += "-";
			} else if (enrs.equals(".")) // noch frei (selber organisiert)
			{
				neueZeile += ".";
			} else {
				int enr = Integer.parseInt(enrs);
				neueZeile += this.getVolleId(enr);
			}
			ret.add(neueZeile);
		}

		return ret;
	}

	public String getVolleId(int enr) {
		return (this.eltern.getFall(enr).getNachname() + "; " + this.eltern
				.getFall(enr).getKindernamen());
	}

	public ArrayList<String> lehrerVonElternErfolgreichGewaehlt(int lnr) {
		ArrayList<String> ret = new ArrayList<String>();

		for (int tnr = 0; tnr < ESTEigenschaften.getAnzahlTakte(); tnr++) {
			if (this.kollegen.getLehrer(lnr).istBelegt(tnr)) {
				int enr = this.kollegen.getLehrer(lnr).istBelegtMit(tnr);
				String zeile = "";
				zeile += ESTEigenschaften.wochentag(tnr) + ",";
				zeile += ESTEigenschaften.anfangsZeit(tnr);
				zeile += ": ";
				zeile += this.getVolleId(enr);
				ret.add(zeile);
			} else if (this.kollegen.getLehrer(lnr).istBelegbar(tnr)
					|| this.kollegen.getLehrer(lnr).istBesuchbar(tnr)) {
				String zeile = "";
				zeile += ESTEigenschaften.wochentag(tnr) + ",";
				zeile += ESTEigenschaften.anfangsZeit(tnr);
				zeile += ": (noch frei)";
				ret.add(zeile);
			}
		}
		return ret;
	}

	public ArrayList<String> lehrerVonElternErfolglosGewaehlt(int lnr) {
		ArrayList<String> ret = new ArrayList<String>();

		for (int enr = 0; enr < this.eltern.getAnzahlEltern(); enr++) {
			if (this.eltern.getFall(enr).lehrerWurdeGewollt(lnr)
					&& !this.eltern.getFall(enr).lehrerWurdeErfolgreichGewollt(
							lnr)) {
				String zeile = "Erfolglos: ";
				zeile += this.eltern.getFall(enr).getNachname();
				zeile += "; ";
				zeile += this.eltern.getFall(enr).getKindernamen();
				ret.add(zeile);
			}
		}
		return ret;
	}

	public String termin() {
		String ret = "";

		if (ESTEigenschaften.findetAnEinemTagStatt()) {
			ret += ESTEigenschaften.getWochentag(1) + ", "
					+ ESTEigenschaften.getDatumErsterTag();
		} else {
			ret += ESTEigenschaften.getWochentag(1) + ", "
					+ ESTEigenschaften.getDatumErsterTag();
			ret += "; ";
			ret += ESTEigenschaften.getWochentag(2) + ", "
					+ ESTEigenschaften.getDatumZweiterTag();
		}

		return ret;
	}

	public ArrayList<String> wahlergebniskopf(int enr) {
		ArrayList<String> ret = new ArrayList<String>();

		// Fall act = this.sortierteFaelle.get(nummer);
		Fall act = this.eltern.getFall(enr);

		ret.add("Eltern  : " + act.getNachname());
		ret.add("Kinder  : " + act.getKindernamen());
		ret.add("Klasse  : " + act.getKlasse());
		ret.add("W\u00fcnsche : " + act.getAnzahlLehrerGewollt());
		ret.add("Erf\u00fcllt: " + act.getAnzahlErfuellterWuensche());

		if (act.alleWuenscheErfuellt()) {
			ret.add("Alle W\u00fcnsche erf\u00fcllt!");
		} else if (act.einigeWuenscheErfuellt()) {
			ret.add("Einige W\u00fcnsche erfuellt!");
		} else if (act.keinWunschErfuellt()) {
			ret.add("Kein Wunsch erfüllt!");
		} else {
			ret.add("Programmfehler!");
		}
		return ret;
	}

	public ArrayList<String> wahlergebniskopfAusgabe(int enr) {
		ArrayList<String> ret = new ArrayList<String>();

		// Fall act = this.sortierteFaelle.get(nummer);
		Fall act = this.eltern.getFall(enr);
		// String nn = this.eltern.getNachname(enr);

		String kinder = act.getKindernamen();
		StringTokenizer st = new StringTokenizer(kinder, ",", false);
		while (st.hasMoreTokens()) {
			// ret.add(nn + ", " + st.nextToken().trim());
			ret.add(st.nextToken().trim());
		}
		return ret;
	}

	public String elternname(int enr) {
		return this.eltern.getFall(enr).getNachname();

	}

	public String fallnummer(int enr) {
		return "" + this.eltern.getFall(enr).getFallNr();
	}

	public ArrayList<String> wahlergebnis(int enr) {
		ArrayList<String> zeilen = new ArrayList<String>();
		ArrayList<String> rohdaten = this.eltern.getFall(enr).wahlergebnis();
		// ArrayList <String> rohdaten =
		// this.sortierteFaelle.get(nummer).wahlergebnis();
		Iterator<String> it = rohdaten.iterator();
		while (it.hasNext()) {
			String zeile = it.next();
			StringTokenizer st = new StringTokenizer(zeile, ";", false);
			int lnr = Integer.parseInt(st.nextToken());
			String tnrt = st.nextToken();
			String s = "";
			if (tnrt.equals("-")) {
				s = "" + this.kollegen.getLehrer(lnr).vollerName()
						+ ": leider kein Termin";
			} else {
				int tnr = Integer.parseInt(tnrt);
				s = "" + this.kollegen.getLehrer(lnr).vollerName() + " am "
						+ ESTEigenschaften.wochentag(tnr) + " um "
						+ ESTEigenschaften.anfangsZeit(tnr) + " Raum : "
						+ this.kollegen.getLehrer(lnr).getRaum();
			}
			zeilen.add(s);
		}

		return zeilen;
	}

	public ArrayList<String> wahlergebnisAusgabe(int enr) {
		ArrayList<String> zeilen = new ArrayList<String>();
		ArrayList<String> rohdaten = this.eltern.getFall(enr).wahlergebnis();
		// ArrayList <String> rohdaten =
		// this.sortierteFaelle.get(nummer).wahlergebnis();
		Iterator<String> it = rohdaten.iterator();
		while (it.hasNext()) {
			String zeile = it.next();
			StringTokenizer st = new StringTokenizer(zeile, ";", false);
			int lnr = Integer.parseInt(st.nextToken());
			String tnrt = st.nextToken();
			String s = "";
			if (tnrt.equals("-")) {
				s = this.kollegen.getLehrer(lnr).ausgabeName()
						+ ";leider kein Termin";
			} else {
				int tnr = Integer.parseInt(tnrt);
				s = this.kollegen.getLehrer(lnr).ausgabeName() + ";"
						+ ESTEigenschaften.wochentag(tnr) + ";"
						+ ESTEigenschaften.anfangsZeit(tnr) + ";"
						+ this.kollegen.getLehrer(lnr).getRaum();
			}
			zeilen.add(s);
		}

		return zeilen;
	}

	public ArrayList<String> alleLehrer(boolean anwesend) {
		ArrayList<String> ret = new ArrayList<String>();
		for (int lnr = 0; lnr < this.kollegen.anzahlLehrer(); lnr++) {
			String raum = this.kollegen.getLehrer(lnr).getRaum();
			raum = raum.trim();
			if (!raum.equals("")) {
				raum = "; Raum " + raum;
			} else {
				raum = "; Raum ??";
			}
			String az;
			if (anwesend) {
				az = this.kollegen.anwesendeZeiten(lnr);
			} else {
				az = this.kollegen.belegbareZeiten(lnr);
			}
			if (az.equals("")) {
				az = "-";
			}
			if (anwesend) {
				ret.add(this.kollegen.getLehrer(lnr).ausgabeName() + "; " + az
						+ raum);
			} else {
				ret.add(this.kollegen.getLehrer(lnr).ausgabeName() + "; " + az);
			}
		}
		return ret;
	}

	public ArrayList<String> alleLehrerInfo() {
		ArrayList<String> ret = new ArrayList<String>();
		for (int lnr = 0; lnr < this.kollegen.anzahlLehrer(); lnr++) {
			String ausgabe = this.kollegen.getLehrer(lnr).ausgabeName();
			ausgabe += "; ";
			String blz = this.kollegen.belegbareZeiten(lnr);
			if (blz.equals("")) {
				blz = "-";
			}
			ausgabe += blz;
			ausgabe += "; ";
			String bsz = this.kollegen.besuchbareZeiten(lnr);
			if (bsz.equals("")) {
				bsz = "-";
			}
			ausgabe += bsz;

			ret.add(ausgabe);
		}
		return ret;
	}

	public ArrayList<int[]> alleLehrerStatistik() {
		ArrayList<int[]> ret;
		ret = new ArrayList<int[]>();
		int[] inhalt;
		for (int i = 0; i < this.kollegen.anzahlLehrer(); i++) {
			inhalt = new int[3];
			inhalt[0] = i;
			inhalt[1] = this.kollegen.getLehrer(i).anzahlBelegbareTakte()
					+ this.kollegen.getLehrer(i).anzahlBereitsBelegteTakte();
			inhalt[2] = this.eltern.anzahlElternHabenGewaehlt(i);
			ret.add(inhalt);
		}
		return ret;
	}

	public ArrayList<String> datumAusgabe() {
		ArrayList<String> ret = new ArrayList<String>();
		if (ESTEigenschaften.findetAnEinemTagStatt()) {
			String zeile = ESTEigenschaften.getWochentag(1);
			zeile += ", " + ESTEigenschaften.getDatumErsterTag();
			zeile += ", " + ESTEigenschaften.anfang1() + " Uhr";
			zeile += " bis " + ESTEigenschaften.end2() + " Uhr";
			ret.add(zeile);
		} else {
			String zeile1 = ESTEigenschaften.getWochentag(1);
			zeile1 += ", " + ESTEigenschaften.getDatumErsterTag();
			zeile1 += ", " + ESTEigenschaften.anfang1() + " Uhr";
			zeile1 += " bis " + ESTEigenschaften.end1() + " Uhr";
			ret.add(zeile1);

			String zeile2 = ESTEigenschaften.getWochentag(2);
			zeile2 += ", " + ESTEigenschaften.getDatumZweiterTag();
			zeile2 += ", " + ESTEigenschaften.anfang2() + " Uhr";
			zeile2 += " bis " + ESTEigenschaften.end2() + " Uhr";
			ret.add(zeile2);
		}
		return ret;
	}

	public ArrayList<String> klassenstaerken() {
		ArrayList<String> ret = new ArrayList<String>();

		HashMap<String, Integer> zahlen;
		zahlen = new HashMap<String, Integer>();

		for (int enr = 0; enr < this.eltern.getAnzahlEltern(); enr++) {
			String kla = this.eltern.getFall(enr).getKlasse();
			if (zahlen.containsKey(kla)) {
				Integer anz = zahlen.get(kla);
				zahlen.put(kla, anz + 1);
			} else {
				zahlen.put(kla, 1);
			}
		}

		Iterator<String> it = zahlen.keySet().iterator();

		// Hier werden die Einträge hoffentlich sortiert!!
		TreeSet<String> ts = new TreeSet<String>();

		while (it.hasNext()) {
			String kla = it.next();
			Integer anz = zahlen.get(kla);
			ts.add("Klasse " + kla + ": " + anz);
		}

		it = ts.iterator();
		while (it.hasNext()) {
			ret.add(it.next());
		}

		return ret;
	}
}
