package lehrer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

/**
 * @author Klaus Bovermann
 * @version 1.1 Ein Lehrer hat in dem Takt-Array Einträge: -1 (Belegbar) : freie
 *          Zeit für organisierte Gespräche -2 (Abwesend) : Abwesend! -3
 *          (Besuchbar) : anwesend, jedoch nicht frei für organisierte
 *          Gespräche! 0 oder positiv : hat ein Elterngespräch mit der entspr.
 *          Fall-Nr.
 */
public class Lehrer implements Serializable, Comparable<Lehrer> {
	/**
	 * @uml.property name="name"
	 */
	private String name;
	/**
	 * @uml.property name="kuerzel"
	 */
	private String kuerzel;
	/**
	 * @uml.property name="raum"
	 */
	private String raum;
	/**
	 * @uml.property name="sex"
	 */
	private boolean sex;
	private int[] enr;

	/**
     *
     */
	private static final long serialVersionUID = 3832901057159706675L;

	/**
	 * Alle Takte sind gesperrt!
	 * 
	 * @param name
	 * @param sex
	 * @param raum
	 */
	public Lehrer(String name, String kuerzel, boolean sex, String raum) {
		this.name = name;
		this.kuerzel = kuerzel;
		this.raum = raum;
		this.sex = sex;
		this.enr = new int[ESTEigenschaften.getAnzahlTakte()];
		if (ESTEigenschaften.getLogLevel() == 11) // testdaten erzeugen!!
		{
			Random r = new Random();
			int zuf = r.nextInt(2);
			String takte;
			for (int i = 0; i < ESTEigenschaften.getAnzahlTakte(); i++) {
				enr[i] = ESTStatics.ABWESEND; // alle sind erstmal gesperrt!!
			}
			if (zuf == 0) {
				takte = ESTEigenschaften.getFrueherTermin();
			} else {
				takte = ESTEigenschaften.getSpaeterTermin();
			}
			StringTokenizer st = new StringTokenizer(takte, ";", false);
			while (st.hasMoreTokens()) {
				String taktString = st.nextToken();
				int tnr = Integer.parseInt(taktString);
				this.setBelegbar(tnr);
			}
			this.setRaum("" + (100 + r.nextInt(900)));

		} else {
			for (int i = 0; i < ESTEigenschaften.getAnzahlTakte(); i++) {
				enr[i] = ESTStatics.ABWESEND; // alle sind erstmal gesperrt!!
			}
		}
	}

	/**
	 * Der Name des Kollegen wird ausgeliefert
	 * 
	 * @return Name des Kollegen
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	public int compareTo(Lehrer otherTeacher) {
		return this.compareString().compareTo(otherTeacher.compareString());
	}

	/**
	 * Den Namen des Kollegen (neu) setzen (Wozu braucht man das?).
	 * 
	 * @param name
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Der Raum, in dem der Kollege sitzt, wird ausgeliefert
	 * 
	 * @return Raum, in dem der Kollege sitzt.
	 * @uml.property name="raum"
	 */
	public String getRaum() {
		return raum;
	}

	/**
	 * Den Raum (neu) zuweisen
	 * 
	 * @param raum
	 * @uml.property name="raum"
	 */
	public void setRaum(String raum) {
		this.raum = raum;
	}

	/**
	 * Das Geschlecht des Kollegen wird ausgeliefert. (true == männlich)
	 * 
	 * @return boolean Geschlecht des Kollegen
	 * @uml.property name="sex"
	 */
	public boolean getSex() {
		return sex;
	}

	public void changeSex() {
		this.sex = !this.sex;
	}

	/**
	 * Die Anrede in der Form "Herr" oder "Frau"
	 * 
	 * @return Herr oder Frau
	 */

	public String anrede() {
		if (this.istMann()) {
			return ("Herr");
		} else {
			return ("Frau");
		}
	}

	public boolean istMann() {
		return this.sex;
	}

	/**
	 * Der volle Name des Kollegen in der Form <i>Anrede Blank Nachname</i>
	 * 
	 * @return name in Form Anrede Blank Nachname
	 * 
	 */
	public String vollerName() {
		return (this.anrede() + " " + this.getName() + " (" + this.getKuerzel() + ")");
	}

	public String ausgabeName() {
		return (this.anrede() + " " + this.getName());
	}

	public String comboName() {
		return (this.kuerzel + "; " + this.anrede() + " " + this.getName());
	}

	public String comboNameWithoutSex() {
		return (this.kuerzel + " (" + this.getName() + ")");
	}

	public String compareString() {
		return this.name + "; " + this.kuerzel;
	}

	/**
	 * Das Geschlecht des Kollegen (neu) setzen (true == männlich)
	 * (Geschlechtsumwandlung!)
	 * 
	 * @param sex
	 * @uml.property name="sex"
	 */
	public void setSex(boolean sex) {
		this.sex = sex;
	}

	/**
	 * Mit dem Aufruf belege(tnr, -1) wird der lehrer in diesem takt frei!<br>
	 * Mit dem Aufruf belege(tnr, -2) wird der lehrer in diesem takt gesperrt!<br>
	 * Mit dem Aufruf belege(tnr, eNr), wobei eNr einen gültigen elternwert
	 * haben sollte, wird der lehrer in diesem takt mit dem entspr. eltern (eNr)
	 * belegt, ist also nicht mehr frei!
	 * 
	 * <b>Achtung:</b> Es wird immer gnadenlos ohne Test(!) überschrieben!
	 * 
	 */
	public void belege(int tnr, int elternnummer) {
		this.enr[tnr] = elternnummer;
	}

	/**
	 * Es wird geprüft, ob der Kollege in dem angegebenen Takt ein Gespräch hat
	 * 
	 * @param tnr
	 * @return hat der Kollege zu der Taktnummer ein Gespräch?
	 */
	public boolean istBelegt(int tnr) {
		return (this.enr[tnr] >= 0);
	}

	/**
	 * Falls der Kollege zu der angegebenen Taktnummer ein Gespräch hat, wird
	 * die entspr. Elternnummer ausgegeben, sonst wird <-1> zurückgeliefert.
	 * 
	 * @param tnr
	 * 
	 * @return Elternnummer oder -1
	 * 
	 */
	public int istBelegtMit(int tnr) {
		if (istBelegt(tnr)) {
			return this.enr[tnr];
		} else {
			return (-1); // dummy Rückgabe!!!
		}
	}

	/**
	 * Es wird geprüft, ob der Kollege in dem angegebenen Takt abwesend ist.
	 * 
	 * @param tnr
	 * 
	 * @return ist der Kollege zu dem Takt gesperrt?
	 * 
	 */
	public boolean istAbwesend(int tnr) {
		return (this.enr[tnr] == ESTStatics.ABWESEND);
	}

	/**
	 * Es wird geprüft, ob der Kollege in dem angegebenen Takt in der Schule
	 * ist,<br>
	 * jedoch nicht algorithmisch belegbar ist.
	 * 
	 * @param tnr
	 * 
	 * @return ist der Kollege zu dem Takt gesperrt?
	 * 
	 */
	public boolean istBesuchbar(int tnr) {
		return (this.enr[tnr] == ESTStatics.BESUCHBAR);
	}

	/**
	 * Es wird geprüft, ob der Kollege in dem angegebenen Takt frei ist,<br>
	 * also ob er dort ein Gespräch kriegen kann.
	 * 
	 * @param tnr
	 * 
	 * @return ist der Kollege zu dem Takt frei für ein Elterngespräch?
	 * 
	 */
	public boolean istBelegbar(int tnr) {
		if (!ESTEigenschaften.isMitLehrerPause()) {
			return (this.enr[tnr] == ESTStatics.BELEGBAR);
		} else {
			boolean taktIstFrei = (this.enr[tnr] == ESTStatics.BELEGBAR);
			int anzahlVorheriger = 0;
			int testnummer = tnr - 1;
			while (testnummer >= 0 && this.istBelegt(testnummer)) {
				anzahlVorheriger++;
				testnummer--;
			}
			boolean genugVorigeSindFrei = (anzahlVorheriger <= ESTEigenschaften
					.getAnzahlOhnePP());
			return (taktIstFrei && genugVorigeSindFrei);
		}
	}

	private boolean istBelegbarIntern(int tnr) {
		return (this.enr[tnr] == ESTStatics.BELEGBAR);
	}

	/**
	 * Sperre den Kollegen in dem angegebenen Takt. Hier kann ab jetzt kein
	 * Gespräch mehr stattfinden.<br>
	 * Das sollte sicher nicht während der Zuordnung passieren.!
	 * 
	 * @param tnr
	 */
	public void setAbwesend(int tnr) {
		this.enr[tnr] = ESTStatics.ABWESEND;
	}

	/**
	 * Sperre den Kollegen in dem angegebenen Takt. Hier kann ab jetzt kein
	 * Gespräch mehr stattfinden.<br>
	 * Das sollte sicher nicht während der Zuordnung passieren.!
	 * 
	 * @param tnr
	 */
	public void setBesuchbar(int tnr) {
		this.enr[tnr] = ESTStatics.BESUCHBAR;
	}

	/**
	 * Der übergebene Takt wird frei (steht also für ein Elterngespräch zur
	 * Verfügung)!
	 * 
	 * @param tnr
	 */
	public void setBelegbar(int tnr) {
		this.enr[tnr] = ESTStatics.BELEGBAR;
	}

	/**
	 * @return the kuerzel
	 * @uml.property name="kuerzel"
	 */
	public String getKuerzel() {
		return kuerzel;
	}

	public int anzahlBelegbareTakte() {
		int anz = 0;
		for (int tnr = 0; tnr < ESTEigenschaften.getAnzahlTakte(); tnr++) {
			int inhaltHier = this.enr[tnr];
			if (inhaltHier == ESTStatics.BELEGBAR) {
				anz++;
			}
		}
		return anz;
	}

	public int anzahlAnwesendeTakte() {
		int anz = 0;
		for (int tnr = 0; tnr < ESTEigenschaften.getAnzahlTakte(); tnr++) {
			int inhaltHier = this.enr[tnr];
			if (inhaltHier == ESTStatics.ABWESEND) {
				anz++;
			}
		}
		return ESTEigenschaften.getAnzahlTakte() - anz;
	}

	public int anzahlBesuchbareTakte() {
		int anz = 0;
		for (int tnr = 0; tnr < ESTEigenschaften.getAnzahlTakte(); tnr++) {
			int inhaltHier = this.enr[tnr];
			if (inhaltHier == ESTStatics.BESUCHBAR) {
				anz++;
			}
		}
		return anz;
	}

	public int anzahlBereitsBelegteTakte() {
		int anz = 0;
		for (int tnr = 0; tnr < ESTEigenschaften.getAnzahlTakte(); tnr++) {
			if (this.enr[tnr] >= 0) {
				anz++;
			}
		}
		return anz;
	}

	public ArrayList<String> getTerminListe() {
		return getTerminListe(0, ESTEigenschaften.getAnzahlTakte() - 1);
	}

	public ArrayList<String> getTerminListe(int von, int bis) {
		ArrayList<String> ret = new ArrayList<String>();
		for (int tnr = von; tnr <= bis; tnr++) {
			String s = "";
			s += tnr;
			s += ";";
			if (this.istAbwesend(tnr)) {
				s += "x";
			} else if (this.istBesuchbar(tnr)) {
				s += ".";
			} else if (this.istBelegt(tnr)) {
				s += this.istBelegtMit(tnr);
			} else if (this.istBelegbarIntern(tnr)) {
				s += "-";
			}
			ret.add(s);
		}
		return ret;
	}

	/**
	 * Gibt ein Feld zurück, in dem alle Zeiten angegeben sind, in dem der
	 * Lehrer (noch) frei belegbar ist.
	 * 
	 * @return ein boolesches Feld
	 * 
	 */
	public boolean[] freieZeiten() {
		boolean[] belegt = new boolean[ESTEigenschaften.getAnzahlTakte()];
		for (int i = 0; i < belegt.length; i++) {
			belegt[i] = this.istBelegbar(i);
		}
		return belegt;
	}

	/**
	 * Gibt ein Feld zurück, in dem alle Zeiten angegeben sind, in dem der
	 * Lehrer besuchbar ist.
	 * 
	 * @return ein boolesches Feld
	 * 
	 */
	public boolean[] besuchbareZeiten() {
		boolean[] belegt = new boolean[ESTEigenschaften.getAnzahlTakte()];
		for (int i = 0; i < belegt.length; i++) {
			belegt[i] = this.istBesuchbar(i);
		}
		return belegt;
	}

	/**
	 * Gibt ein Feld zurück, in dem alle Zeiten angegeben sind, in dem der
	 * Lehrer vom System belegbar ist.
	 * 
	 * @return ein boolesches Feld
	 * 
	 */
	public boolean[] belegbareZeiten() {
		boolean[] belegt = new boolean[ESTEigenschaften.getAnzahlTakte()];
		for (int i = 0; i < belegt.length; i++) {
			belegt[i] = this.istBelegbar(i);
		}
		return belegt;
	}

	/**
	 * Gibt ein Feld zurück, in dem alle Zeiten angegeben sind, in dem der
	 * Lehrer anwesend ist:<br>
	 * Frei oder bereits belegt oder besuchbar.
	 * 
	 * @return ein boolesches Feld
	 */
	public boolean[] anwesendeZeiten() {
		boolean[] belegt = new boolean[ESTEigenschaften.getAnzahlTakte()];
		for (int i = 0; i < belegt.length; i++) {
			boolean typ = this.istBelegbar(i) || this.istBesuchbar(i)
					|| this.istBelegt(i);
			belegt[i] = typ;
		}
		return belegt;
	}
}
