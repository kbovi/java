package estKonstanten;

public class ESTStatics {

	/**
	 * Der Lehrer ist in der Schule; der Algo kann Termine zuweisen.
	 * 
	 */
	public static final int BELEGBAR = -1;

	/**
	 * Der Lehrer ist nicht in der Schule; der ALGO darf keine Termine setzen.
	 * 
	 */
	public static final int ABWESEND = -2;

	/**
	 * Der Eltern ist nicht in der Schule.<br>
	 * Der ALGO darf keine Termine hier setzten.
	 * 
	 */
	public static final int ELTERNSPERRE = -2;

	/**
	 * Der Eltern ist in der Schule.<br>
	 * Der ALGO darf Termine hier setzten.
	 * 
	 */
	public static final int ELTERNFREI = -1;

	/**
	 * Der Lehrer ist in der Schule. Er organisiert selber Termine oder Eltern
	 * besuchen ihn.<br>
	 * Der ALGO darf keine Termine hier setzten.
	 * 
	 */
	public static final int BESUCHBAR = -3;
	public static final int KEIN_LEHRER = -1; // kein Lehrer hier gewünscht
	public static final int KEIN_TAKT = -1; // zug takt bei Elternwunsch

	public static String PROPNAME = "EST-PROPS.DAT";

	public static String LOGNAME = "EST.LOG";
	public static String STATNAME = "EST.STA";

	public static String SCHULNAME = "Schulname";
	public static String ANZAHL_TAKTE = "AnzahlTakte";
	public static String SCHNITT = "Schnitt";
	public static String TAKTLAENGE = "TaktL\u00E4nge";
	public static String DATUM1 = "DatumErsterTag";
	public static String DATUM2 = "DatumZweiterTag";
	public static String BACKDATE = "R\u00FCckgabedatum";
	public static String FRUEH = "Fr\u00FCherTermin";
	public static String SPAET = "Sp\u00E4terTermin";
	public static String HALBJAHR = "Halbjahr";
	public static String ANFANG1 = "Anfang1";
	public static String ANFANG2 = "Anfang2";
	public static String VOROPTI = "MitVoroptimierung";
	public static String LUFT = "MitLufttakten";
	public static String SHUFFLE = "ElternShuffle";
	public static String PAUSE = "LehrerPause";
	public static String MAX_WUENSCHE = "maxW\u00FCnsche";
	public static String LOGLEVEL = "LogLevel";
	public static String EDITOR = "TextEditor";
	public static String OutPath = "LastOutPath";
	public static String OutDir = "LastOutDir";
	public static String DatenPath = "LastDatenPath";
	public static String DatenDir = "LastDatenDir";
	public static String ELTERNSPERRTAKTE = "Elternsperre";
	public static String ELTERNKNOPF1 = "Elternknopf1";
	public static String ELTERNKNOPF2 = "Elternknopf2";
}
