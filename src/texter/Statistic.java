package texter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import lehrer.Kollegium;
import tools.Logbuch;
import eltern.Eltern;
import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

/**
 * @author Administrator
 */
public class Statistic {

	private BufferedWriter bwr;
	private FileWriter fw;
	private boolean isOpen;
	private String datname;
	private Kollegium myKollegium;
	private Eltern myEltern;
	private Texter myTexter;

	private boolean E_mitVO;
	private boolean E_mitLU;

	private Logbuch myLogbuch;

	private static Statistic oneStatistic = null;

	public static Statistic getStatistic() {
		if (oneStatistic == null) {
			oneStatistic = new Statistic();
		}
		return oneStatistic;
	}

	private Statistic() {
		this.datname = ESTStatics.STATNAME;
		this.myLogbuch = Logbuch.getLogbuch();
	}

	public void initStatistic(Kollegium k, Eltern e) {
		this.myKollegium = k;
		this.myEltern = e;
		this.open();
		this.myTexter = new Texter(this.myKollegium, this.myEltern);
	}

	public void createStatistic(Kollegium k, Eltern e) {
		initStatistic(k, e);
		this.myKollegium = k;
		this.myEltern = e;
		this.open();

		this.myTexter = new Texter(this.myKollegium, this.myEltern);

		GregorianCalendar cal = new GregorianCalendar();
		String zeit = "";
		zeit += format(cal.get(Calendar.DATE)) + ".";
		zeit += format((cal.get(Calendar.MONTH) + 1)) + ".";
		zeit += cal.get(Calendar.YEAR);

		zeit += " <";
		zeit += format(cal.get(Calendar.HOUR_OF_DAY)) + ":";
		zeit += format(cal.get(Calendar.MINUTE)) + ":";
		zeit += format(cal.get(Calendar.SECOND)) + "> ";

		this.writeTo("Statistik erstellt am " + zeit);

		this.writeTo("Termin: " + this.myTexter.termin());

		E_mitVO = ESTEigenschaften.isMitVoroptimierung();
		E_mitLU = ESTEigenschaften.isMitLuft();

		if (this.E_mitVO) {
			this.writeTo("Algorithmus mit Voroptimierung.");
		} else {
			this.writeTo("Algorithmus ohne Voroptimierung.");
		}

		if (this.E_mitLU) {
			this.writeTo("Algorithmus mit Lufttakten bei Eltern.");
		} else {
			this.writeTo("Algorithmus ohne Lufttakte bei Eltern.");
		}

		this.writeTo("Anzahl W\u00fcnsche insgesamt  = "
				+ this.myEltern.totalAnzahlWunsche());
		this.writeTo("Anzahl erf\u00fcllter W\u00fcnsche = "
				+ this.myEltern.anzahlErfuellterWuensche());

		this.writeTo("-------------");
	}

	public void lehrerStatistik(int lnr) {
		this.writeTo("Lehrer: " + this.myKollegium.getLehrer(lnr).vollerName());
		this.writeTo("Anwesende  Takte   = "
				+ this.myKollegium.getLehrer(lnr).anzahlAnwesendeTakte());
		this.writeTo("Besuchbare Takte   = "
				+ this.myKollegium.getLehrer(lnr).anzahlBesuchbareTakte());
		this.writeTo("Belegbare  Takte   = "
				+ this.myKollegium.getLehrer(lnr).anzahlBelegbareTakte());
		this.writeTo("Belegte    Takte   = "
				+ this.myKollegium.getLehrer(lnr).anzahlBereitsBelegteTakte());

		this.writeTo("Elternw\u00fcnsche = "
				+ this.myEltern.anzahlElternHabenGewaehlt(lnr));

		// das folgende gehört eigentlich in eine anständige Ausgabe!
		{
			ArrayList<String> al = this.myTexter
					.lehrerVonElternErfolgreichGewaehlt(lnr);

			Iterator<String> it = al.iterator();
			while (it.hasNext()) {
				String z = it.next();
				this.writeTo(z);
			}
		}

		{
			ArrayList<String> al = this.myTexter
					.lehrerVonElternErfolglosGewaehlt(lnr);

			Iterator<String> it = al.iterator();
			while (it.hasNext()) {
				String z = it.next();
				this.writeTo(z);
			}
		}
		// bis hier hin!

		this.writeTo("-------------------------------");
	}

	public void elternStatistik(int enr) {

		{
			ArrayList<String> al = this.myTexter.wahlergebniskopf(enr);

			Iterator<String> it = al.iterator();
			while (it.hasNext()) {
				String z = it.next();
				this.writeTo(z);
			}
			;
			// bis hier hin!
			this.writeTo("Termine:");
		}
		// das folgende gehört eigentlich in eine anständige Ausgabe!
		{
			ArrayList<String> al = this.myTexter.wahlergebnis(enr);

			Iterator<String> it = al.iterator();
			while (it.hasNext()) {
				String z = it.next();
				this.writeTo(z);
			}
			;
			// bis hier hin!
			this.writeTo("-------------------------------");
		}
	}

	public void lehrerStatistik() {
		for (int lnr = 0; lnr < this.myKollegium.anzahlLehrer(); lnr++) {
			this.lehrerStatistik(lnr);
		}
	}

	public void elternStatistik() {
		for (int enr = 0; enr < this.myEltern.getAnzahlEltern(); enr++) {
			this.elternStatistik(enr);
		}
	}

	public void raumStatistik(int lnr) {
		this.writeTo("Lehrer: " + this.myKollegium.getLehrer(lnr).vollerName());
		this.writeTo("Raum  : " + this.myKollegium.getLehrer(lnr).getRaum());
		this.writeTo("Belegung:");
		ArrayList<String> termine = this.myTexter.lehrerTermine(lnr);
		Iterator<String> it = termine.iterator();
		while (it.hasNext()) {
			String zeile = it.next();
			this.writeTo(zeile);
		}
		this.writeTo("-------------------------------");
	}

	public void raumStatistik() {
		for (int lnr = 0; lnr < this.myKollegium.anzahlLehrer(); lnr++) {
			raumStatistik(lnr);
		}
	}

	public void oeffneDich() {
		this.close();
		ProcessBuilder proc = new ProcessBuilder(
				ESTEigenschaften.getEditorName(), this.datname);
		try {
			proc.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.myLogbuch.log(e1.toString(), 0);
		}
	}

	public void open() {
		try {
			fw = new FileWriter(datname);
			bwr = new BufferedWriter(fw);
		} catch (IOException e) {
			this.myLogbuch.log(e.toString(), 0);
		}
		this.isOpen = true;
	}

	public void close() {
		if (this.isOpen) {
			try {
				bwr.close();
				fw.close();
			} catch (IOException e) {
				this.myLogbuch.log(e.toString(), 0);
			}
		}
		this.isOpen = false;
	}

	public void writeTo(String s) {
		if (this.isOpen) {
			writeIntern(s);
		}
	}

	private void writeIntern(String s) {
		try {
			bwr.write(s);
			bwr.newLine();
		} catch (IOException e) {
			this.myLogbuch.log(e.toString(), 0);
		}
	}

	private String format(int i) {
		if (i >= 10)
			return "" + i;
		else
			return "0" + i;
	}

	public void KlassenstaerkenStatistik() {
		this.writeTo("Sch\u00fcler pro Klasse");
		this.writeTo("(Geschwisterkinder nur einfach gez\u00e4hlt!)");
		this.writeTo("-----");
		ArrayList<String> ks = this.myTexter.klassenstaerken();
		Iterator<String> it = ks.iterator();
		while (it.hasNext()) {
			String zeile = it.next();
			this.writeTo(zeile);
		}
	}

}
