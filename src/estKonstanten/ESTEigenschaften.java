package estKonstanten;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import takter.Takter;
import tools.Logbuch;

/**
 * @author Administrator
 */
public class ESTEigenschaften {

	/**
	 * @uml.property name="props"
	 */
	private static Properties props = null;

	/**
	 * @return
	 * @uml.property name="props"
	 */
	public static Properties getProps() {
		init();
		return props;
	}

	public static void logPrefs(int level) {
		init();
		Logbuch l = Logbuch.getLogbuch();
		l.log("LogPrefsAusgabe", level);
		l.log("Eigenschaften:", level);
		Set<Object> keys = props.keySet();
		for (Object key : keys) {
			l.log(key + ": " + props.getProperty((String) key), level);
		}
	}

	public static void init() {
		if (props == null) {
			props = new Properties();
			loadProps();
		}
	}

	public static void saveProps() {
		try {
			FileOutputStream output = new FileOutputStream(ESTStatics.PROPNAME);
			props.store(output, "EST-Eigenschaften");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadProps() {
		try {
			FileInputStream input = new FileInputStream(ESTStatics.PROPNAME);
			props.load(input);
			input.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Keine Properties ladbar!",
					"Achtung", JOptionPane.ERROR_MESSAGE);

			// System.out.println ("Leider keine Eigenschaften geladen!");
			// e.printStackTrace();
		}
	}

	private static String zeitMitNull(int angabe) {
		String STD;
		if (angabe == 0) {
			STD = "00";
		} else if (angabe < 10) {
			STD = "0" + angabe;
		} else {
			STD = "" + angabe;
		}

		return STD;
	}

	private static String zeitString(int Std, int Min) {
		String STD = zeitMitNull(Std);
		String MIN = zeitMitNull(Min);
		return STD + ":" + MIN;
	}

	/**
	 * Diese Methode liefert die Zeit, die zu einem Takt gehört.<br>
	 * Genauer: Es wird der ZeitPUNKT erzeugt, der zu dem Anfang(!!!) des
	 * angegebenen Taktes gehört.<br>
	 * Also wird z.B. für den Takt 17 die Zeit 10:20 erzeugt, wobei der Takt 17
	 * um 10:20 anfängt und um 10:30 endet.
	 * 
	 */
	public static String anfangsZeit(int tnr) {
		init();
		int nr;
		int schnitt = Integer.parseInt(props.getProperty(ESTStatics.SCHNITT));
		int anfang1 = Integer.parseInt(props.getProperty(ESTStatics.ANFANG1));
		int anfang2 = Integer.parseInt(props.getProperty(ESTStatics.ANFANG2));
		int taktLaenge = Integer.parseInt(props
				.getProperty(ESTStatics.TAKTLAENGE));
		if (tnr < schnitt) {
			nr = anfang1 + (tnr) * taktLaenge;
		} else {
			nr = anfang2 + (tnr - schnitt) * taktLaenge;
		}
		int anfStd = nr / 60;
		int anfMin = nr % 60;

		return zeitString(anfStd, anfMin);
	}

	/**
	 * Diese Methode liefert die Zeit, die zu einem Takt gehört.<br>
	 * Genauer: Es wird der ZeitPUNKT erzeugt, der zu dem Ende(!!!) des
	 * angegebenen Taktes gehört.<br>
	 * Also wird z.B. für den Takt 17 die Zeit 10:30 erzeugt, wobei der Takt 17
	 * um 10:20 anfängt und um 10:30 endet.
	 * 
	 */
	public static String endZeit(int tnr) {
		init();
		int nr;
		int schnitt = Integer.parseInt(props.getProperty(ESTStatics.SCHNITT));
		int anfang1 = Integer.parseInt(props.getProperty(ESTStatics.ANFANG1));
		int anfang2 = Integer.parseInt(props.getProperty(ESTStatics.ANFANG2));
		int taktLaenge = Integer.parseInt(props
				.getProperty(ESTStatics.TAKTLAENGE));
		if (tnr < schnitt) {
			nr = anfang1 + (tnr + 1) * taktLaenge;
		} else {
			nr = anfang2 + (tnr + 1 - schnitt) * taktLaenge;
		}
		int anfStd = nr / 60;
		int anfMin = nr % 60;

		return zeitString(anfStd, anfMin);
	}

	public static String extendedIntervallText(int tnr) {
		init();
		return (wochentag(tnr) + ", " + anfangsZeit(tnr) + " - " + endZeit(tnr));
	}

	public static String schuleDateiKennung() {
		init();
		return (props.getProperty(ESTStatics.HALBJAHR) + "-schule");
	}

	public static String elternDateiKennung() {
		init();
		return (props.getProperty(ESTStatics.HALBJAHR) + "-eltern");
	}

	public static String lehrerDateiKennung() {
		init();
		return (props.getProperty(ESTStatics.HALBJAHR) + "-lehrer");
	}

	public static int getAnzahlTakte() {
		init();
		String at = props.getProperty(ESTStatics.ANZAHL_TAKTE);
		return Integer.parseInt(at);
	}

	public static int getSchnitt() {
		init();
		String s = props.getProperty(ESTStatics.SCHNITT);
		return Integer.parseInt(s);
	}

	public static String getDatumErsterTag() {
		init();
		return props.getProperty(ESTStatics.DATUM1);
	}

	public static String getDatumZweiterTag() {
		init();
		return props.getProperty(ESTStatics.DATUM2);
	}

	public static String getBackDatum() {
		init();
		return props.getProperty(ESTStatics.BACKDATE);
	}

	public static String anfang1() {
		return anfangsZeit(0);
	}

	public static String anfang2() {
		return anfangsZeit(getSchnitt());
	}

	public static String end1() {
		return endZeit(getSchnitt() - 1);
	}

	public static String end2() {
		return endZeit(getAnzahlTakte() - 1);
	}

	public static boolean findetAnEinemTagStatt() {
		return getDatumErsterTag().equals(getDatumZweiterTag());
	}

	public static String getWochentag(int tagNr) {
		String et;
		if (tagNr == 1) {
			et = getDatumErsterTag();
		} else {
			et = getDatumZweiterTag();
		}

		int ret = -1;
		try {
			Date da = DateFormat.getDateInstance(DateFormat.DEFAULT,
					Locale.GERMAN).parse(et);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(da);
			ret = cal.get(Calendar.DAY_OF_WEEK);

		} catch (ParseException e) {
			// System.out.println (e);
			// e.printStackTrace();
		}
		return (bezeichner(ret));
	}

	public static String wochentag(int taktnr) {
		if (taktnr < ESTEigenschaften.getSchnitt()) {
			return getWochentag(1);
		} else {
			return getWochentag(2);
		}
	}

	private static String bezeichner(int nr) {
		switch (nr) {
		case Calendar.SUNDAY:
			return "Sonntag";
		case Calendar.MONDAY:
			return "Montag";
		case Calendar.TUESDAY:
			return "Dienstag";
		case Calendar.WEDNESDAY:
			return "Mittwoch";
		case Calendar.THURSDAY:
			return "Donnerstag";
		case Calendar.FRIDAY:
			return "Freitag";
		case Calendar.SATURDAY:
			return "Samstag";
		default:
			return "???";
		}
	}

	public static String getFrueherTermin() {
		init();
		return props.getProperty(ESTStatics.FRUEH);
	}

	public static String getSpaeterTermin() {
		init();
		return props.getProperty(ESTStatics.SPAET);
	}

	public static String getElternSperrTakte() {
		init();
		return props.getProperty(ESTStatics.ELTERNSPERRTAKTE);
	}

	public static String getFrueherTerminLabel() {
		init();
		return terminLabel(props.getProperty(ESTStatics.FRUEH));
	}

	public static String getSpaeterTerminLabel() {
		init();
		return terminLabel(props.getProperty(ESTStatics.SPAET));
	}

	private static String terminLabel(String takte) {
		int az = getAnzahlTakte();
		boolean[] t = new boolean[az];
		for (int i = 0; i < az; i++) {
			t[i] = false;
		}
		StringTokenizer st = new StringTokenizer(takte, ";", false);
		while (st.hasMoreTokens()) {
			int takt = Integer.parseInt(st.nextToken());
			t[takt] = true;
		}
		Takter ta = new Takter();
		String ret = ta.takt2String(t);
		return ret;
	}

	public static int getAnfang1() {
		init();
		String a1 = props.getProperty(ESTStatics.ANFANG1);
		return Integer.parseInt(a1);
	}

	public static int getAnfang2() {
		init();
		String a2 = props.getProperty(ESTStatics.ANFANG2);
		return Integer.parseInt(a2);
	}

	public static int getTaktLaenge() {
		init();
		String tl = props.getProperty(ESTStatics.TAKTLAENGE);
		return Integer.parseInt(tl);
	}

	public static int getAnzahlOhnePP() {
		int tl = getTaktLaenge();
		return 120 / tl;
	}

	public static boolean isMitLuft() {
		init();
		String ml = props.getProperty(ESTStatics.LUFT);
		return (ml.equals("Ja"));
	}

	public static boolean isMitVoroptimierung() {
		init();
		String vo = props.getProperty(ESTStatics.VOROPTI);
		return (vo.equals("Ja"));
	}

	public static boolean isMitElternShuffle() {
		init();
		String vo = props.getProperty(ESTStatics.SHUFFLE);
		return (vo.equals("Ja"));
	}

	public static boolean isMitLehrerPause() {
		init();
		String vo = props.getProperty(ESTStatics.PAUSE);
		return (vo.equals("Ja"));
	}

	public static int getMaxWuensche() {
		init();
		String mw = props.getProperty(ESTStatics.MAX_WUENSCHE);
		return Integer.parseInt(mw);
	}

	public static int getLogLevel() {
		init();
		String mw = props.getProperty(ESTStatics.LOGLEVEL);
		return Integer.parseInt(mw);
	}

	public static String getSchulname() {
		init();
		return props.getProperty(ESTStatics.SCHULNAME);
	}

	public static String ersterElternButton() {
		init();
		return props.getProperty(ESTStatics.ELTERNKNOPF1);
	}

	public static String zweiterElternButton() {
		init();
		return props.getProperty(ESTStatics.ELTERNKNOPF2);
	}

	public static String getLastOutPath() {
		init();
		return props.getProperty(ESTStatics.OutPath);
	}

	public static String getLastOutDir() {
		init();
		return props.getProperty(ESTStatics.OutDir);
	}

	public static String getLastDatenPath() {
		init();
		return props.getProperty(ESTStatics.DatenPath);
	}

	public static String getLastDatenDir() {
		init();
		return props.getProperty(ESTStatics.DatenDir);
	}

	public static String getEditorName() {
		init();
		return props.getProperty(ESTStatics.EDITOR);
	}
}