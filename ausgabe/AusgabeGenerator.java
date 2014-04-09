package ausgabe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import lehrer.Kollegium;
import lehrer.Lehrer;
import lizenz.ESTReadLicence;
import texter.Texter;
import tools.Logbuch;
import eltern.Eltern;
import estKonstanten.ESTEigenschaften;

/**
 * @author Administrator
 */
public class AusgabeGenerator {
	Eltern myEltern;
	Kollegium myKollegium;
	Texter myTexter;
	Logbuch myLogbuch;
	String schulname;

	public AusgabeGenerator(Eltern e, Kollegium k) {
		this.myEltern = e;
		this.myKollegium = k;
		this.myTexter = new Texter(k, e);
		ESTEigenschaften.init();

		ESTReadLicence erl = new ESTReadLicence();
		String lizenz = erl.getLicence();
		this.schulname = lizenz;

		myLogbuch = Logbuch.getLogbuch();
	}

	public String getSchulname() {
		return this.schulname;
	}

	public void parsen(String dateinameIn, String dateinameOut, boolean neu,
			int enr) {
		String zeile;
		BufferedReader eingabeDatei;
		boolean ignore = false;
		try {
			eingabeDatei = new BufferedReader(new FileReader(dateinameIn));
		} catch (IOException e) {
			eingabeDatei = null;
			JOptionPane.showMessageDialog(null, "Einagbe-Datei " + dateinameIn
					+ " nicht gefunden!", "Achtung", JOptionPane.ERROR_MESSAGE);
			this.myLogbuch.log("Einagbe-Datei nicht gefunden!", 0);
		}

		BufferedWriter ausgabeDatei;
		try {
			ausgabeDatei = new BufferedWriter(new FileWriter(dateinameOut, neu));
		} catch (IOException e) {
			ausgabeDatei = null;
			this.myLogbuch.log(e.toString(), 0);
		}

		try {
			zeile = eingabeDatei.readLine();
		} catch (IOException e) {
			zeile = null;
			this.myLogbuch.log("Keine Zeile (1)", 0);
		}

		try {

			while (eingabeDatei != null && zeile != null) {

				String[] teile = zeile.split("[#]");
				if (teile.length == 1) {
					if (!ignore) {
						ausgabeDatei.write(teile[0]);
						ausgabeDatei.newLine();
					}
				} else {
					for (int i = 0; i < teile.length; i++) {
						String current = teile[i];
						if (i % 2 == 0) {
							if (!ignore) {
								ausgabeDatei.write(current);
							}
						} else {
							// Hier ist ein SCHLÜSSELWORT
							if (current.startsWith("if")) {
								ignore = !erfuellt(current, enr);
							} else if (current.equals("end")) {
								ignore = false;
							} else {
								ausgabeDatei.write(replace(current, enr));
							}
						}
					}
					ausgabeDatei.newLine();
				}
				zeile = eingabeDatei.readLine();
			}
		} catch (IOException e) {
			this.myLogbuch.log(e.toString(), 0);
		}

		try {
			ausgabeDatei.close();
			eingabeDatei.close();
		} catch (IOException e) {
			this.myLogbuch.log(e.toString(), 0);
		}
	}

	public boolean erfuellt(String bedingung, int enr) {
		if (bedingung.equals("ifeinige")) {

			return this.myEltern.einigeWuenscheErfuellt(enr);
		} else if (bedingung.equals("ifeinkind")) {
			return this.myEltern.getFall(enr).getKinderzahl() == 1;
		} else if (bedingung.equals("ifmehrkinder")) {
			return this.myEltern.getFall(enr).getKinderzahl() > 1;
		} else if (bedingung.equals("ifzweikinder")) {
			return this.myEltern.getFall(enr).getKinderzahl() == 2;
		} else if (bedingung.equals("ifvielekinder")) {
			return this.myEltern.getFall(enr).getKinderzahl() > 2;
		} else if (bedingung.equals("ifeintermin")) {
			return this.myEltern.getFall(enr).getAnzahlLehrerGewollt() == 1;
		} else if (bedingung.equals("ifmehrtermine")) {
			return this.myEltern.getFall(enr).getAnzahlLehrerGewollt() > 1;
		} else if (bedingung.equals("ifkeine")) {
			return this.myEltern.keinWunschErfuellt(enr);
		} else if (bedingung.equals("iferfolglos")) {
			return !this.myTexter.lehrerVonElternErfolglosGewaehlt(enr)
					.isEmpty();
		} else {
			this.myLogbuch.log("Falsche  Bedingung : " + bedingung, 0);
			return false;
		}
	}

	public void elternausgabe(String mustername, File dir) {
		ausgabe(mustername, dir, this.myEltern.getAnzahlEltern());
	}

	public void lehrerausgabe(String mustername, File dir) {
		ausgabe(mustername, dir, this.myKollegium.anzahlLehrer());
	}

	public void lehrerlistenausgabe(String mustername, File dir) {
		ausgabe(mustername, dir, 1);
	}

	public void ausgabe(String mustername, File dir, int anzahl) {
		String DMpfad = System.getProperty("user.dir") + "//" + "Druckmuster"
				+ "//" + mustername;

		String AGpfad = dir.getPath() + "//" + mustername;
		(new File(AGpfad)).mkdir();

		String eingabe = DMpfad + "//kopf.txt";
		String ausgabe = AGpfad + "//ausgabe.tex";

		this.myLogbuch.log("Eingabe von  " + eingabe, 5);
		this.myLogbuch.log("Ausgabe nach " + ausgabe, 5);
		this.myLogbuch.log("Vor Kopfparsen", 5);
		parsen(eingabe, ausgabe, false, -1); // -1 ist dummy!

		this.myLogbuch.log("Eingabe von  " + eingabe, 5);
		this.myLogbuch.log("Ausgabe nach " + ausgabe, 5);
		this.myLogbuch.log("Vor Seiteparsen", 5);
		this.myLogbuch.log("Anzahl: " + anzahl, 5);

		for (int i = 0; i < anzahl; i++) {
			eingabe = DMpfad + "//seite.txt";
			parsen(eingabe, ausgabe, true, i);
		}
		eingabe = DMpfad + "//fuss.txt";
		this.myLogbuch.log("Eingabe von  " + eingabe, 5);
		this.myLogbuch.log("Ausgabe nach " + ausgabe, 5);
		this.myLogbuch.log("Vor Fussparsen", 5);
		parsen(eingabe, ausgabe, true, -1); // -1 ist dummy
	}

	public String replace(String schluesselwort, int nr) {
		if (schluesselwort.equals("datum")) {
			return arrayList2LaTeXItemize(this.myTexter.datumAusgabe());
		}
		if (schluesselwort.equals("dat1")) {
			return ESTEigenschaften.getDatumErsterTag();
		}
		if (schluesselwort.equals("Wochentag1")) {
			return ESTEigenschaften.getWochentag(1) + ", "
					+ ESTEigenschaften.getDatumErsterTag();
		}
		if (schluesselwort.equals("Wochentag2")) {
			return ESTEigenschaften.getWochentag(2) + ", "
					+ ESTEigenschaften.getDatumZweiterTag();
		}
		if (schluesselwort.equals("backdate")) {
			return ESTEigenschaften.getBackDatum();
		} else if (schluesselwort.equals("elternnummer")) {
			return this.myTexter.elternname(nr) + " ("
					+ this.myTexter.fallnummer(nr) + ")";
		} else if (schluesselwort.equals("elternkopf")) {
			String ret = this.arrayList2LaTeXItemize(this.myTexter
					.wahlergebniskopfAusgabe(nr));

			return ret;
		} else if (schluesselwort.equals("termine")) {
			String ret = this.arrayList2LaTeXTable(
					this.myTexter.wahlergebnisAusgabe(nr), 4);

			return ret;
		} else if (schluesselwort.equals("anrede")) {
			String ret = "Liebe";
			Lehrer l = this.myKollegium.getLehrer(nr);
			if (l.getSex()) {
				ret += "r";
			}

			ret += " " + l.ausgabeName() + ", ";

			return ret;
		} else if (schluesselwort.equals("lehrername")) {
			return this.myKollegium.getLehrer(nr).ausgabeName();

		} else if (schluesselwort.equals("raumname")) {
			return this.myKollegium.getLehrer(nr).getRaum();

		} else if (schluesselwort.startsWith("allelehrer")) {
			StringTokenizer st = new StringTokenizer(schluesselwort, "!", false);
			/* String sw = */st.nextToken(); // das eigentliche schluesselwort
			String ue = st.nextToken(); // die gewuenschte ueberschrift
			String sp = st.nextToken(); // die Spaltenbeschreibung

			// String ret =
			// this.arrayList2LaTeXEnumerate(this.myTexter.alleLehrer());
			String ret = this.arrayList2LaTeXLongTable(
					this.myTexter.alleLehrer(true), ue, // "Nr&Name&Zeit&Raum",
					sp // "l|l|l|"
					);

			return ret;
		} else if (schluesselwort.startsWith("fuerelternallelehrer")) {
			StringTokenizer st = new StringTokenizer(schluesselwort, "!", false);
			/* String sw = */st.nextToken(); // das eigentliche schluesselwort
			String ue = st.nextToken(); // die gewuenschte ueberschrift
			String sp = st.nextToken(); // die Spaltenbeschreibung

			// String ret =
			// this.arrayList2LaTeXEnumerate(this.myTexter.alleLehrer());
			String ret = this.arrayList2LaTeXLongTable(
					this.myTexter.alleLehrer(false), ue, // "Nr&Name&Zeit",
					sp // "l|l|l|"
					);

			return ret;
		} else if (schluesselwort.startsWith("infoallelehrer")) {
			StringTokenizer st = new StringTokenizer(schluesselwort, "!", false);
			/* String sw = */st.nextToken(); // das eigentliche schluesselwort;
												// nur zum Überspringen hier
												// gelesen!
			String ue = st.nextToken(); // die gewuenschte ueberschrift
			String sp = st.nextToken(); // die Spaltenbeschreibung

			// System.out.println (this.myTexter.alleLehrer());
			// String ret =
			// this.arrayList2LaTeXEnumerate(this.myTexter.alleLehrer());
			String ret = this.arrayList2LaTeXLongTable(
					this.myTexter.alleLehrerInfo(), ue, // "Nr&Name&Von EST organisiert&Selber organisiert",
					sp // "p{3cm}|p{5cm}|p{5cm}|"
					);

			return ret;
		} else if (schluesselwort.equals("alletermine1")) {
			String ret = this.arrayList2LaTeXEnumerate(this.myTexter
					.lehrerTermineErsterTag(nr, false));

			return ret;
		} else if (schluesselwort.equals("alletermine2")) {
			String ret = this.arrayList2LaTeXEnumerate(this.myTexter
					.lehrerTermineZweiterTag(nr, false));

			return ret;
		} else if (schluesselwort.equals("alleterminetabelle1")) {
			String ret = this.arrayList2LaTeXSmallTable(this.myTexter
					.lehrerTermineErsterTag(nr, false));

			return ret;
		} else if (schluesselwort.equals("alleterminetabelle2")) {
			String ret = this.arrayList2LaTeXSmallTable(this.myTexter
					.lehrerTermineZweiterTag(nr, false));

			return ret;
		} else if (schluesselwort.equals("alleterminedescription1")) {
			String ret = this.arrayList2LaTeXDescription(this.myTexter
					.lehrerTermineErsterTag(nr, false));

			return ret;
		} else if (schluesselwort.equals("alleterminedescription2")) {
			String ret = this.arrayList2LaTeXDescription(this.myTexter
					.lehrerTermineZweiterTag(nr, false));

			return ret;
		} else if (schluesselwort.equals("statistikallelehrer")) {
			String ret = this
					.statistik2TeX(this.myTexter.alleLehrerStatistik());

			return ret;
		} else if (schluesselwort.equals("lehrererfolgreich")) {
			String ret = this.arrayList2LaTeXItemize(this.myTexter
					.lehrerVonElternErfolgreichGewaehlt(nr));

			return ret;
		} else if (schluesselwort.equals("lehrererfolglos")) {
			String ret = this.arrayList2LaTeXItemize(this.myTexter
					.lehrerVonElternErfolglosGewaehlt(nr));

			return ret;
		} else {
			return schluesselwort;
		}
	}

	public String arrayList2LaTeXItemize(ArrayList<String> l) {
		if (l.isEmpty()) {
			return "";
		}
		String ret = '\\' + "begin{itemize}";
		for (String s : l) {
			ret += '\\' + "item ";
			ret += s;
		}
		ret += '\\' + "end{itemize}";

		return ret;
	}

	public String arrayList2LaTeXSmallTable(ArrayList<String> l) {
		if (l.isEmpty()) {
			return "";
		}
		String ret = '\\' + "begin{tabular}{|l|p{6cm}|}";
		// ret += '\\';
		// ret += '\\';
		ret += '\\' + "hline ";

		for (String s : l) {
			int posOfPoint = s.indexOf(':');
			String ts = s.substring(0, posOfPoint);
			int tn = Integer.parseInt(ts);
			String termin = ESTEigenschaften.anfangsZeit(tn);
			String ss = termin + "&" + s.substring(posOfPoint + 1);
			ret += ss;
			ret += '\\';
			ret += '\\';
			ret += '\\' + "hline ";
		}
		ret += '\\' + "end{tabular}";

		return ret;

	}

	public String arrayList2LaTeXEnumerate(ArrayList<String> l) {
		if (l.isEmpty()) {
			return "";
		}
		String ret = '\\' + "begin{enumerate} ";
		ret += '\\' + "addtocounter{enumi}{-1} "; // damit es bei 0 anfängt!

		for (String s : l) {
			int posOfPoint = s.indexOf(':');
			String ts = s.substring(0, posOfPoint);
			int tn = Integer.parseInt(ts);
			String termin = ESTEigenschaften.anfangsZeit(tn);
			String ss = termin + ": " + s.substring(posOfPoint + 1);

			ret += '\\' + "item ";
			ret += ss;
		}
		ret += '\\' + "end{enumerate}";

		return ret;
	}

	public String arrayList2LaTeXDescription(ArrayList<String> l) {
		if (l.isEmpty()) {
			return "";
		}
		String ret = '\\' + "begin{description} ";

		for (String s : l) {
			int posOfPoint = s.indexOf(':');
			String ts = s.substring(0, posOfPoint);
			int tn = Integer.parseInt(ts);
			String termin = ESTEigenschaften.anfangsZeit(tn);
			String eintrag = s.substring(posOfPoint + 1);

			ret += '\\' + "item[" + termin + "]";
			ret += eintrag;
		}
		ret += '\\' + "end{description}";

		return ret;
	}

	public String arrayList2LaTeXTable(ArrayList<String> l, int spalten) {
		if (l.isEmpty()) {
			return "";
		}
		String ret = '\\' + "begin{tabular} ";
		ret += "{*{" + spalten + "}{l}} ";
		ret += "Lehrer&Tag&Zeit&Raum ";
		ret += '\\';
		ret += '\\';
		ret += '\\' + "hline ";
		Iterator<String> it = l.iterator();
		while (it.hasNext()) {
			String s = it.next();
			StringTokenizer st = new StringTokenizer(s, ";", false);
			ret += st.nextToken();
			while (st.hasMoreTokens()) {
				String dieser = st.nextToken();
				if (dieser.startsWith("leider")) {
					ret += "&";
					ret += '\\' + "multicolumn{3}{l}{leider kein Termin}";
				} else {
					ret += "&" + dieser;
				}
			}
			if (it.hasNext()) {
				ret += '\\';
				ret += '\\';
			}
		}
		ret += '\\' + "end{tabular} ";

		return ret;
	}

	public String arrayList2LaTeXLongTable(ArrayList<String> l, String header,
			String rowDescription) {
		if (l.isEmpty()) {
			return "";
		}
		String ret = '\\' + "begin{longtable} ";
		ret += "{|";
		ret += "l|";
		ret += rowDescription;
		// for (int i = 0; i < spalten; i++)
		// {
		// ret += "l|";
		// }
		ret += "} ";
		ret += '\\' + "hline ";
		ret += header;
		ret += '\\';
		ret += '\\';
		ret += '\\' + "hline ";
		ret += '\\' + "hline ";
		Iterator<String> it = l.iterator();
		int nr = -1; // damit es bei 0 anfängt!
		while (it.hasNext()) {
			nr++;
			String s = it.next();
			StringTokenizer st = new StringTokenizer(s, ";", false);
			ret += (nr * 3 + 100); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			ret += "&";
			ret += st.nextToken();
			while (st.hasMoreTokens()) {
				String dieser = st.nextToken();
				ret += "&" + dieser;
			}
			// if (it.hasNext())
			{
				ret += '\\';
				ret += '\\';
				ret += '\\' + "hline ";
			}
		}
		ret += '\\' + "end{longtable} ";

		return ret;
	}

	public String statistik2TeX(ArrayList<int[]> werte) {
		String ret = '\\' + "begin{longtable} ";
		ret += "{*{" + 4 + "}{l}} ";
		ret += "Lehrer& &von EST belegbare Takte&Elternwuensche";
		ret += '\\';
		ret += '\\';
		ret += '\\' + "hline ";
		for (int[] dieser : werte) {
			int frei = dieser[1];
			int bel = dieser[2];
			ret += this.myKollegium.getLehrer(dieser[0]).ausgabeName();
			ret += "&";
			double diff = (bel - frei) / 3.0;
			if (diff >= 0) {
				ret += '\\';
				ret += "rule{" + diff + "ex}{5pt}";
			} else {
				ret += '\\';
				ret += "rule{" + (-diff) + "ex}{1pt}";
			}

			ret += "&" + frei + "&" + bel;
			ret += '\\';
			ret += '\\';

		}
		ret += "&&&";
		ret += '\\';
		ret += "end{longtable}";
		return ret;
	}

}
