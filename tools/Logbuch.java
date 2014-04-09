package tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

/**
 * @author Administrator
 */
public class Logbuch {
	private BufferedWriter bwr;
	private FileWriter fw;
	private String datname;
	// private int level;
	private boolean isOpen;

	private static Logbuch oneLogbuch = null;

	public static Logbuch getLogbuch() {
		if (oneLogbuch == null) {
			oneLogbuch = new Logbuch();
		}
		return oneLogbuch;
	}

	private Logbuch() {
		this.datname = ESTStatics.LOGNAME;
		// this.level = ESTEigenschaften.getLogLevel();
		this.isOpen = false;
	}

	public void oeffneDich() {
		this.close("Schliessen zum EDITieren");
		ProcessBuilder proc = new ProcessBuilder(
				ESTEigenschaften.getEditorName(), this.datname);
		try {
			proc.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			this.open("\u00d6ffnen nach EDITieren");
		}
	}

	public void open(String id) {
		try {
			fw = new FileWriter(datname, true);
			// fw = new FileWriter (datname);
			bwr = new BufferedWriter(fw);
		} catch (IOException e) {
			// System.out.println (e);
		}
		this.isOpen = true;
		logintern("<<" + id + ">> " + "Logbuch ge\u00f6ffnet mit Level "
				+ ESTEigenschaften.getLogLevel());
	}

	public void close(String id) {
		if (this.isOpen) {
			logintern("<<" + id + ">> " + "Logbuch geschlossen");
			try {
				bwr.close();
				fw.close();
			} catch (IOException e) {
				// System.out.println (e);
			}
		}
		this.isOpen = false;
	}

	public void log(String s, int level) {
		if (this.isOpen && ESTEigenschaften.getLogLevel() >= level) {
			logintern(s);
		}
	}

	private void logintern(String s) {
		GregorianCalendar cal = new GregorianCalendar();
		String zeit = "";
		zeit += format(cal.get(Calendar.DATE)) + ".";
		zeit += format((cal.get(Calendar.MONTH) + 1)) + ".";
		zeit += cal.get(Calendar.YEAR);

		zeit += " <";
		zeit += format(cal.get(Calendar.HOUR_OF_DAY)) + ":";
		zeit += format(cal.get(Calendar.MINUTE)) + ":";
		zeit += format(cal.get(Calendar.SECOND)) + "> ";
		// zeit += cal.get(Calendar.MILLISECOND) + " ms) > ";

		try {
			bwr.write(zeit + s);
			bwr.newLine();
		} catch (IOException e) {
			// System.out.println (e);
		}
	}

	private String format(int i) {
		if (i >= 10)
			return "" + i;
		else
			return "0" + i;
	}
}
