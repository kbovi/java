package takter;

import java.io.Serializable;

import tools.Logbuch;
import estKonstanten.ESTEigenschaften;

/**
 * @author Administrator
 */
public class Takter implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 6628652396680235637L;

	// Wann gehts denn los?!
	private int startzeit;

	// Taktlaenge in Minuten
	private int taktlaenge;

	private int anzahlTakte;

	// TaktArray, dessen ,,Groesse'' von Spannweite und taktlaenge abhaengt.
	/**
	 * @uml.property name="takt"
	 */
	private boolean[] takt = null;

	private Logbuch myLogbuch;

	public Takter() {
		this.myLogbuch = Logbuch.getLogbuch();
		this.startzeit = ESTEigenschaften.getAnfang1();
		this.taktlaenge = ESTEigenschaften.getTaktLaenge();
		this.anzahlTakte = ESTEigenschaften.getAnzahlTakte();

		this.takt = new boolean[this.anzahlTakte];

		for (int i = 0; i < this.anzahlTakte; i++) {
			this.takt[i] = false;
		}
	}

	public String takt2StringErsterTag(boolean[] taktFeld) {
		String termine = t2s(taktFeld, 0, ESTEigenschaften.getSchnitt());
		termine = termine.trim();
		if (termine.equals("")) {
			return "";
		} else {
			return (ESTEigenschaften.getDatumErsterTag() + "," + termine);
		}
	}

	public String takt2StringZweiterTag(boolean[] taktFeld) {
		String termine = t2s(taktFeld, ESTEigenschaften.getSchnitt(),
				ESTEigenschaften.getAnzahlTakte());
		termine = termine.trim();
		if (termine.equals("")) {
			return "";
		} else {
			if (ESTEigenschaften.findetAnEinemTagStatt()) {
				return (termine);
			} else {
				return (ESTEigenschaften.getDatumZweiterTag() + "," + termine);
			}

		}
	}

	public String takt2String(boolean[] taktFeld) {
		String first = takt2StringErsterTag(taktFeld).trim();
		String second = takt2StringZweiterTag(taktFeld).trim();

		if ((first.equals("")) && (second.equals(""))) {
			return "";
		} else if (first.equals("")) {
			return second;
		} else if (second.equals("")) {
			return first;
		} else {
			return (takt2StringErsterTag(taktFeld) + ", " + "\n" + takt2StringZweiterTag(taktFeld));
		}
	}

	private String t2s(boolean[] taktFeld, int von, int bis) {
		String theString = "";

		for (int i = von; i < bis; i++) {

			if (!taktFeld[i]) {
				// i++; // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			} else {
				if (theString != "") {
					theString += ", ";
				}
				theString += ESTEigenschaften.anfangsZeit(i) + "-";
				// i++;
				while (i < bis && taktFeld[i]) {
					i++;
				}
				theString += ESTEigenschaften.endZeit(i - 1);
			}
		}
		return theString;
	}

	/**
	 * @return
	 * @uml.property name="takt"
	 */
	public boolean[] getTakt() {
		return this.takt;
	}

	public boolean getTakt(int i) {
		return this.takt[i];
	}

	private String killspaces(String theString) // private Hilfsmethode um
												// Leerzeichen zu ignorieren
	{
		java.util.StringTokenizer spaceTokenizer = new java.util.StringTokenizer(
				theString, " ");

		String result = "";

		while (spaceTokenizer.hasMoreTokens()) {
			result += spaceTokenizer.nextToken();
		}

		return (result);
	}

	// Syntax von taktkette; "(h)h:mm-hh:mm,hh:mm-hh:mm,..."
	public void aufspalten(String taktkette) {
		taktkette = this.killspaces(taktkette);

		java.util.StringTokenizer fensterTokenizer = new java.util.StringTokenizer(
				taktkette, ",");
		int numUhrzeiten = fensterTokenizer.countTokens() * 2; // Wie viele
																// Uhrzeiten es?

		int[] grenzen = new int[numUhrzeiten]; // Grenzen der Zeitfenster;
		int grenzZaehler = 0;

		java.util.StringTokenizer vonBisTokenizer = null;
		java.util.StringTokenizer stundenMinutenTokenizer = null;

		String einFenster = "";
		String von = "";
		String bis = "";
		String stunden = "";
		String minuten = "";

		while (fensterTokenizer.hasMoreTokens()) {

			einFenster = fensterTokenizer.nextToken(); // Separiere alle durch ,
														// getrennten
														// Zeitintervalle
			vonBisTokenizer = new java.util.StringTokenizer(einFenster, "-");

			while (vonBisTokenizer.hasMoreTokens()) // Trenne die Zeitintervalle
													// in von und bis
			{
				von = vonBisTokenizer.nextToken();

				if (!vonBisTokenizer.hasMoreTokens()) // Es gibt kein bis!
				{

					this.myLogbuch.log("SYNTAX ERROR: _BIS_ fehlt", 0);
				} else {
					bis = vonBisTokenizer.nextToken();
				}

				stundenMinutenTokenizer = new java.util.StringTokenizer(von,
						":");

				while (stundenMinutenTokenizer.hasMoreTokens()) // Trenne von
																// und bis in
																// minuten und
																// stunden
				{
					stunden = stundenMinutenTokenizer.nextToken();

					if (!stundenMinutenTokenizer.hasMoreTokens()) // Es gibt
																	// keine
																	// Minuten!
					{
						this.myLogbuch.log("SYNTAX ERROR: _MINUTEN_ fehlt", 0);
					} else {
						minuten = stundenMinutenTokenizer.nextToken();
					}

					grenzen[grenzZaehler++] = Integer.parseInt(stunden) * 60
							+ Integer.parseInt(minuten);

				}

				stundenMinutenTokenizer = new java.util.StringTokenizer(bis,
						":");

				while (stundenMinutenTokenizer.hasMoreTokens()) {
					stunden = stundenMinutenTokenizer.nextToken();

					if (!stundenMinutenTokenizer.hasMoreTokens()) // Es gibt
																	// keine
																	// Minuten!
					{
						this.myLogbuch.log("SYNTAX ERROR: _MINUTEN_ fehlt", 0);
					} else {
						minuten = stundenMinutenTokenizer.nextToken();
					}

					grenzen[grenzZaehler++] = Integer.parseInt(stunden) * 60
							+ Integer.parseInt(minuten);
				}

			}
		}

		int taktnummer = 0; // Takte vergeben
		for (int i = 0; i <= (numUhrzeiten / 2); i += 2) {
			for (int j = grenzen[i]; j <= grenzen[i + 1]; j += taktlaenge) {
				taktnummer = ((j - this.startzeit) / this.taktlaenge);
				this.myLogbuch.log("Takt-Nr. " + taktnummer + " -> belegt!", 5);
				if ((taktnummer >= 0) && (taktnummer < this.anzahlTakte)) {
					this.takt[taktnummer] = true;
				} else {
					this.myLogbuch
							.log("OVER- / UNDERFLOW-ERROR: Zeitfenter liegt ausserhalb der Anwesenheit!",
									0);
				}
			}
		}
	}

}
