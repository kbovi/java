package algorithmus;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lehrer.Kollegium;
import lotto.shakerGenerator;
import texter.Statistic;
import tools.Logbuch;
import eltern.Eltern;
import eltern.Fall;
import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

/**
 * @author Administrator
 */
public class Algorithmus {

	// private Kollegium tempKollegen;
	// private Eltern tempEltern;
	private Kollegium meineKollegen;
	private Eltern meineEltern;
	private Logbuch myLogbuch;

	private boolean E_mitVO;
	private boolean mitBevorzugung;

	// boolean E_shuff;
	private int E_maxW;
	private int E_anzT;
	private int E_schn;

	private JFrame gui;

	public Algorithmus(Kollegium k, Eltern e, JFrame gui) {
		myLogbuch = Logbuch.getLogbuch();
		myLogbuch.log("Algorithmus", 5);
		ESTEigenschaften.init();
		ESTEigenschaften.logPrefs(10);
		this.gui = gui;

		E_mitVO = ESTEigenschaften.isMitVoroptimierung();
		E_maxW = ESTEigenschaften.getMaxWuensche();
		E_anzT = ESTEigenschaften.getAnzahlTakte();
		E_schn = ESTEigenschaften.getSchnitt();

		this.meineKollegen = k;
		this.meineEltern = e;

		if (this.E_mitVO) {
			this.myLogbuch.log("Voroptimierung beginnt.", 0);
			this.voroptiAlleEltern();
		}

		mitBevorzugung = JOptionPane.showConfirmDialog(gui,
				"Sollen Kinderreiche bevorzugt werden?", "Problem:",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

		zuordnung();
		zweitage();
		konzentrierung();
		zweitage();
		testausgabe(meineEltern);
		Statistic s = Statistic.getStatistic();
		s.createStatistic(meineKollegen, meineEltern);
		s.KlassenstaerkenStatistik();
		s.lehrerStatistik();
		s.raumStatistik();
		s.elternStatistik();
		s.close();

		// s.oeffneDich(); // nur zum Testen!
	}

	private void voroptiAlleEltern() {

		for (int f = 0; f < meineEltern.size(); f++) {
			voropti(f);
		}
	}

	private void voropti(int en) {
		this.myLogbuch.log("Voropti von " + en, 10);
		int aw = this.meineEltern.getAnzahlLehrerGewollt(en);
		int[] links = new int[this.E_maxW];
		int[] lehr = new int[this.E_maxW];

		for (int m = 0; m < aw; m++) {
			lehr[m] = this.meineEltern.getGewollt(en, m);
			if (lehr[m] == 0) {
				links[m] = 0;
			} else {
				int n = 0;
				while (n < this.E_anzT
						&& !(beideHabenZeit(en,
								this.meineEltern.getGewollt(en, m), n))) {
					n++;
				}
				if (n >= this.E_anzT) {
					links[m] = -1000;
				} else {
					links[m] = n;
				}
			}
		}
		// sortiere der groesse nach:
		for (int i = 0; i < aw - 1; i++) { // i FROM 1 UPTO aw - 1 REP
			for (int j = i + 1; j < aw; j++) { // FOR j FROM i+1 UPTO aw REP
				if (links[i] < links[j]) { // IF lg [i].linke grenze <
											// lg[j].linke grenze
					// tausche;
					int h = links[i]; // INT VAR h :: lg [i].linke grenze;
					links[i] = links[j]; // lg [i].linke grenze := lg [j].linke
											// grenze ;
					links[j] = h; // lg [j].linke grenze := h;
					h = lehr[i]; // h := lg [i].lehrer ;
					lehr[i] = lehr[j]; // lg [i].lehrer := lg [j].lehrer;
					lehr[j] = h; // lg [j].lehrer := h.
				}
			}
		}

		// neu eintragen.

		for (int i = 0; i <= aw - 1; i++) { // FOR i FROM 1 UPTO aw REP
			this.meineEltern.setWunsch(en, i, lehr[i]); // eltern wunsch (en, i,
														// lg [i].lehrer);
														// eltern set takt (en,
														// i, max (0, lg
														// [i].linke grenze))
			// //PER (* min stand hier vorher!!! *)
			// END PROC voropti;

		}
	}

	/*
	 * (* -------------- EMULATION DER ALTEN VERSION ----------------- *)
	 * 
	 * BOOL PROC eltern zeit (INT enr, tnr): IF tnr <= 0 THEN FALSE ELSE eltern
	 * hat zeit (enr, tnr) FI END PROC eltern zeit;
	 */

	private int elternTakt(int enr, int wnr) {
		return (this.meineEltern.getTaktBeiElternwunsch(enr, wnr)); // takt bei
																	// eltern
																	// wunsch
																	// (enr,
																	// wnr)
	}

	private void elternSetTakt(int enr, int wnr, int tnr) {
		this.meineEltern.setTaktBeiElternwunsch(enr, wnr, tnr);
	}

	private void zuordnung() {

		int ret;
		int opt, neu;
		int mw = E_maxW;
		int ae = this.meineEltern.getAnzahlEltern();
		int[] altw = new int[mw];

		Random zuf = new Random();

		for (int w = 0; w < mw; w++) {

			// ordne den wten wunsch aller eltern zu;
			this.myLogbuch.log("" + w + "-ter Wunsch wird zugeordnet", 10);

			// ---------------------------------------------

			// zwei Listen generieren für Eltern mit mehr als 2 Kindern (viel)
			// und solchen mit 1 oder 2 Kindern (wenig)

			ArrayList<Integer> wenig = new ArrayList<Integer>();
			ArrayList<Integer> viel = new ArrayList<Integer>();

			for (int enr = 0; enr < ae; enr++) {
				Fall dieserFall = this.meineEltern.getFall(enr);
				if (dieserFall.getKinderzahl() >= 3) {
					viel.add(enr);
				} else {
					wenig.add(enr);
				}
			}

			// System.out.println(viel);

			/*
			 * for (int i = 0; i < viel.size(); i++) {
			 * System.out.print(this.meineEltern.get(viel.get(i)).getNachname()
			 * + "(" + this.meineEltern.get(viel.get(i)).getKlasse() + "); "); }
			 * System.out.println();
			 */

			// ---------------------------------------------

			shakerGenerator skwenig = new shakerGenerator(wenig);
			shakerGenerator skviel = new shakerGenerator(viel);

			if (!mitBevorzugung) {
				skwenig = new shakerGenerator(ae);
				skviel = new shakerGenerator();
			}

			// shakerGenerator sk = new shakerGenerator (ae);
			while (skwenig.nochZahlenDa() || skviel.nochZahlenDa()) {
				int en;

				// suche aus der passenden Menge jemanden aus!

				if (!skviel.nochZahlenDa()) // es gibt nur noch skwenig!
				{
					en = skwenig.gibEineZahl();
				} else {
					if (!skwenig.nochZahlenDa()) // es gibt nur noch skviel!
					{
						en = skviel.gibEineZahl();
					} else // es gibt beides!!
					{
						int zahl = zuf.nextInt(100);
						if (zahl > -1) // hier zu Testzwecken IMMER aus
										// skviel!!!
						{
							en = skviel.gibEineZahl();
						} else {
							en = skwenig.gibEineZahl();
						}
					}
				}
				this.myLogbuch.log("" + w + "-ter Wunsch von " + en
						+ " wird zugeordnet", 10);
				// System.out.println ("" + w + "-ter Wunsch von " + en +
				// " wird zugeordnet");
				int ln = this.meineEltern.getWunsch(en, w);
				if (ln >= 0 && this.elternTakt(en, w) != -1000) // ??????? -1000
																// ?????
				{
					this.myLogbuch.log("ENR " + en + "; WNR " + w + "; LNR "
							+ ln, 10);
					ordneDenWunschZu(en, w, ln);
				}
			}
		}

		this.myLogbuch.log("Optimierung beginnt", 10);
		for (int f = 0; f < ae; f++) {
			// optimiere;
			int b;
			do {
				b = bandbreite(f);
				schiebeLinkeGrenzeNachRechts(f);
			} while (bandbreite(f) != b);

			do {
				b = bandbreite(f);
				schiebeRechteGrenzeNachLinks(f);
			} while (bandbreite(f) != b);

			// neuzuordnung das erste mal!;
			for (int w = 0; w < mw; w++) {
				if (this.elternTakt(f, w) == ESTStatics.ELTERNFREI) {
					int n = this.meineEltern.getWunsch(f, w);
					this.myLogbuch.log("Zuordnung von " + f + ", " + w + " -> "
							+ n, 10);
					ordneDenWunschZu(f, w, n);
				}
			}
		}

		do {
			this.myLogbuch.log("Neue Optimierung", 10);
			opt = 0;
			neu = 0;
			for (int f = 0; f < ae; f++) {
				for (int w = 0; w < mw; w++) {
					altw[w] = this.elternTakt(f, w);
				}
				// optimiere again;
				int b;
				do {
					b = bandbreite(f);
					schiebeLinkeGrenzeNachRechts(f);
				} while (bandbreite(f) != b);

				do {
					b = bandbreite(f);
					schiebeRechteGrenzeNachLinks(f);
				} while (bandbreite(f) != b);

				for (int w = 0; w < mw; w++) {
					if (this.elternTakt(f, w) != altw[w]) {
						opt++;
					}
				}
				// neuzuordnung again;
				for (int w = 0; w < mw; w++) {
					if (this.elternTakt(f, w) == -1) {
						int n = this.meineEltern.getWunsch(f, w);
						this.myLogbuch.log("Neuzuordnung von " + f + ", " + w
								+ " -> " + n, 10);
						ordneDenWunschZu(f, w, this.meineEltern.getWunsch(f, w));
						if (this.meineEltern.getTakt(f, w) != -1) { // ???????
																	// -1 ?????
							neu++;
						}
					}
				}
			}
			String question = "";
			question += "\u00c4nderungen durch Optimierung: \t" + opt;
			question += "\n";
			question += "\u00c4nderungen durch Neuzuordnung: \t" + neu;
			question += "\n";

			// ergebnis
			int daneben = 0;
			int gesbreite = 0;
			for (int f = 1; f < ae; f++) {
				gesbreite = gesbreite + bandbreite(f);
				for (int w = 0; w < mw; w++) {
					if (this.elternTakt(f, w) == -1) {
						daneben++;
					}
				}
			}

			question += "Daneben: " + daneben;
			question += "\n";
			question += "Summe der Bandbreiten: " + gesbreite;
			question += "\n \n";
			question += "Also noch weiter optimieren?";
			ret = JOptionPane.showConfirmDialog(gui, question,
					"Erneut optimieren?", JOptionPane.YES_NO_OPTION);
		} while (ret == JOptionPane.YES_OPTION); // UNTIL w no
													// ("Noch einmal optimieren")
													// PER. ???????????
	}

	private void schiebeLinkeGrenzeNachRechts(int f) {
		this.myLogbuch.log("Schiebe l -> r von " + f, 10);
		int wn, t;
		wn = minwunsch(f);
		t = this.elternTakt(f, maxwunsch(f));
		while (t > this.elternTakt(f, wn)) {
			if (beideHabenZeit(f, this.meineEltern.getWunsch(f, wn), t)) {
				trageAus(f, wn);
				trageEin(f, wn, t);
			} else {
				t--;
			}
		}
	}

	private void schiebeRechteGrenzeNachLinks(int f) {
		this.myLogbuch.log("Schiebe r -> l von " + f, 10);
		int wn, t;
		boolean cont = true;
		wn = maxwunsch(f);
		int mWunsch = minwunsch(f);
		this.myLogbuch.log("MaxWunsch von " + f + " ist " + wn, 10);
		this.myLogbuch.log("MinWunsch von " + f + " ist " + mWunsch, 10);
		t = this.elternTakt(f, mWunsch);
		this.myLogbuch.log("t vorher ist " + t, 10);
		while (cont && t < this.elternTakt(f, wn)) {
			this.myLogbuch.log("f  ist " + f, 10);
			this.myLogbuch.log("t  ist " + t, 10);
			this.myLogbuch.log("wn ist " + wn, 10);
			if (beideHabenZeit(f, this.meineEltern.getWunsch(f, wn), t)) {
				trageAus(f, wn);
				trageEin(f, wn, t);
				cont = false;
			} else {
				t++;
			}
		}
	}

	private int maxwunsch(int f) {
		int wn = 0;
		for (int i = 1; i < E_maxW; i++) {
			if (this.elternTakt(f, i) <= E_anzT
					&& this.elternTakt(f, i) > this.elternTakt(f, wn)) {
				wn = 1;
			}
		}
		return (wn);
	}

	private int minwunsch(int f) {
		int wn = 0;
		for (int i = 1; i < E_maxW; i++) {
			if (this.elternTakt(f, i) > 0
					&& this.elternTakt(f, i) < this.elternTakt(f, wn)) {
				wn = i;
			}
		}
		return (wn);
	}

	private void ordneDenWunschZu(int enr, int wnr, int lnr) {
		int takt = besterTakt(enr, wnr, lnr);
		this.myLogbuch.log("bester Takt: " + takt, 10);
		if (takt >= 0) {
			trageEin(enr, wnr, takt);
		} else {
			this.elternSetTakt(enr, wnr, takt); // d.h.: dieser Wunsch ist nicht
												// erfüllbar!
		}
	}

	private void trageEin(int en, int wn, int takt) {
		erfuelleElternWunsch(en, wn, takt);
	}

	private void trageAus(int en, int wn) {
		macheWunscherfuellungRueckgaengig(en, wn);
	}

	private boolean beideHabenZeit(int elter, int leh, int tn) {
		if (tn == 0) {
			this.myLogbuch.log("BHZ-Aufruf E:" + elter + " L:" + leh + " T: "
					+ tn, 10);
		}
		boolean lhz = leh >= 0 && tn >= 0
				&& this.meineKollegen.getLehrer(leh).istBelegbar(tn);
		if (!lhz && tn == 0) {
			this.myLogbuch.log("L hat keine Zeit!", 10);
		}
		boolean ehz = leh >= 0 && tn >= 0
				&& this.meineEltern.hatZeit(elter, tn);
		if (!ehz & tn == 0) {
			this.myLogbuch.log("E hat keine Zeit!", 10);
		}
		boolean ret = (leh >= 0 && tn >= 0 && lhz && ehz);

		if (tn == 0 && ret) {
			this.myLogbuch.log("BHZ im Takt 0", 10);
		}
		return (ret);
	}

	private void erfuelleElternWunsch(int enr, int wnr, int tnr) {
		this.meineEltern.erfuelleWunsch(enr, wnr, tnr);
		int lnr = this.meineEltern.getWunsch(enr, wnr);
		this.meineKollegen.trageFallEin(lnr, tnr, enr);
		this.myLogbuch.log("Erf\u00fclle: ENR=" + enr + " WNR=" + wnr + " LNR="
				+ lnr + " TNR=" + tnr, 10);
		int taktein = this.meineEltern.getTaktBeiElternwunsch(enr, wnr);
		this.myLogbuch.log("Eingetragener Takt bei ENR " + enr + " WNR " + wnr
				+ " ist " + taktein, 10);
	}

	private void macheWunscherfuellungRueckgaengig(int enr, int wnr) {
		this.myLogbuch.log("MWR f\u00fcr ENR " + enr + ", WNR " + wnr, 10);
		int tnr = this.meineEltern.getTaktBeiElternwunsch(enr, wnr);
		int lnr = this.meineEltern.getWunsch(enr, wnr);

		this.meineEltern.macheWunscherfuellungRueckgaengig(enr, wnr);
		this.myLogbuch.log("LNR: " + lnr, 10);
		this.myLogbuch.log("TNR: " + tnr, 10);
		this.meineKollegen.trageFallAus(lnr, tnr);
	}

	private int besterTakt(int elternnummer, int wunschnummer, int lehrernummer) {
		boolean blind = true;
		for (int w = 0; w <= wunschnummer; w++) { // ??????? oder <
			if (this.elternTakt(elternnummer, w) >= 0) { // > 0 ????? !!!!!
			// if (this.elternTakt (elternnummer,w) > 0) { // > 0 ????? !!!!!
				blind = false;
			}
		}

		if (blind) {
			// ordne blind zu
			for (int t = 0; t < E_anzT; t++) {
				if (beideHabenZeit(elternnummer, lehrernummer, t)) {
					return (t); // LEAVE ordne blind zu WITH t
				}
			}
			return -1;
		} else {
			// ordne eng zu
			int engsterTakt = this.elternTakt(elternnummer, 1);
			int diffVorher = E_anzT;

			for (int i = 0; i <= wunschnummer - 1; i++) { // / oder < ?????
				int test = engsteVerbindung(this.elternTakt(elternnummer, i),
						elternnummer, lehrernummer);
				if (test > 0
						&& diffVorher > (Math.abs(test
								- this.elternTakt(elternnummer, i)))) {
					engsterTakt = test;
					diffVorher = Math.abs(test
							- this.elternTakt(elternnummer, i));
				}
			}

			if (diffVorher == E_anzT) {
				return (-1);
			} else {
				return (engsterTakt);
			}
		}
	}

	private void ordneNeuEin(int f, int wn, int tmin, int tmax) {
		boolean ok;
		int t, diff = 1000;
		for (int w = 0; w < E_maxW; w++) {
			t = this.elternTakt(f, w);
			if (w != wn && t >= tmin && t <= tmax
					&& this.meineEltern.getWunsch(f, w) >= 0) {
				t = t + 2;
				ok = false;
				while (t <= tmax && !ok) {
					if (beideHabenZeit(f, this.meineEltern.getWunsch(f, wn), t)
							&& Math.abs(t - this.elternTakt(f, w)) < diff) {
						diff = Math.abs(t - this.elternTakt(f, w));
						trageAus(f, wn);
						trageEin(f, wn, t);
						ok = true;
					}
					t++; // (* ACHTUNG !!!!! ???????? *)
				}
				t = this.elternTakt(f, w) - 2;
				ok = false;
				while (t >= tmin && !ok) {
					if (beideHabenZeit(f, this.meineEltern.getWunsch(f, wn), t)
							&& Math.abs(t - this.elternTakt(f, w)) < diff) {
						diff = Math.abs(t - this.elternTakt(f, w));
						trageAus(f, wn);
						trageEin(f, wn, t);
						ok = true;
					}
					t--;
				}
			}
		}
	}

	private int engsteVerbindung(int ab, int en, int ln) {
		int tl = ab, tr = ab + 1, s = ab;
		int schnitt = E_schn;
		while (erlaubt(tl) || erlaubt(tr)) {
			if (erlaubt(tl) && beideHabenZeit(en, ln, tl)) {
				return (tl);
			} // THEN LEAVE engste verbindung WITH tl
			else if (erlaubt(tr) && beideHabenZeit(en, ln, tr)) {
				return (tr); //
			} // THEN LEAVE engste verbindung WITH tr
			else {
				// vergroessere intervall
				if (s <= schnitt) // s liegt am ersten tag
				{
					if (tr < schnitt // tr liegt sicher am ersten tag
							|| !erlaubt(tl)) {
						tr++;
					}
					tl--;
				} else if (tl > schnitt // tl liegt sicher am zweiten tag
						|| !erlaubt(tr)) {
					tl--;
				}
				tr++;
			}
		}
		return (-1);
	}

	private int bandbreite(int f) {
		int min = E_anzT, max = 0, t;
		for (int w = 0; w < E_maxW; w++) {
			t = this.elternTakt(f, w);
			if (t >= 0 && t < min) {// ??? < min ????
				min = this.elternTakt(f, w);
			}
			if (t > max) {
				max = this.elternTakt(f, w);
			}
		}
		if (min < E_schn && max >= E_schn) {
			return (max - min + 1000);
		} else {
			return (max - min);
		}
	}

	private boolean erlaubt(int t) {
		return (t >= 0 && t < E_anzT);
	}

	/*
	 * private int anzahlIntervalle (int elternnummer, int lehrernummer) { int
	 * anzahl = 0; boolean neu = true;
	 * 
	 * for (int counter = 0; counter < this.konstanten.getAnzahlTakte(); counter
	 * ++) { if (beideHabenZeit(elternnummer,lehrernummer,counter)) { if (neu) {
	 * anzahl++; neu = false; } else { neu = true; } } } return (anzahl); }
	 */

	private void konzentrierung() {
		this.myLogbuch.log("Konzentrierung", 10);
		int vt;
		for (int f = 0; f < this.meineEltern.getAnzahlEltern(); f++) {
			this.myLogbuch.log("Konzentrierung von " + f, 10);
			if (fallAnBeidenTagenDa(f)) {
				vt = vollererTag(f);
				if (moeglichNach(f, vt)) {
					this.myLogbuch.log("Schiebe " + f + " nach " + vt, 10);
					schiebeNach(f, vt);
				} else if (moeglichNach(f, 3 - vt)) {
					this.myLogbuch
							.log("Schiebe " + f + " nach " + (3 - vt), 10);
					schiebeNach(f, 3 - vt);
				}
			}
		}
	}

	private void zweitage() {
		int s = 0;
		for (int f = 0; f < this.meineEltern.getAnzahlEltern(); f++) {
			if (fallAnBeidenTagenDa(f)) {
				s++;
			}
		}
		this.myLogbuch
				.log("Eltern die an beiden Tagen kommen sollen: " + s, 10);
	}

	private boolean fallAnBeidenTagenDa(int f) {
		boolean tag1 = false, tag2 = false;
		for (int w = 0; w < E_maxW; w++) {
			if (tag(f, w) == 1) {
				tag1 = true;
			} else if (tag(f, w) == 2) {
				tag2 = true;
			}
		}
		return (tag1 && tag2);
	}

	private int tag(int f, int w) {
		if (this.elternTakt(f, w) > 0 && this.elternTakt(f, w) < E_schn + 1) {
			return (1);
		} else if (elternTakt(f, w) > E_schn && elternTakt(f, w) < E_anzT + 1) {
			return (2);
		} else {
			return (0);
		}
	}

	private int vollererTag(int f) {
		int tag1 = 0, tag2 = 0;
		for (int w = 0; w < E_maxW; w++) {
			if (tag(f, w) == 1) {
				tag1++;
			} else if (tag(f, w) == 2) {
				tag2++;
			}
		}
		if (tag1 > tag2) {
			return 1;
		} else {
			return 2;
		}
	}

	private boolean moeglichNach(int f, int zt) { // (* zt: Zieltag *)
		int tmin = 0, tmax = 0, st = 3 - zt; // (* st: Starttag *)
		boolean moeglGes = true, moeglW;
		int schnitt = E_schn, anzahlTakte = E_anzT;
		if (zt == 0) {
			this.myLogbuch.log("Tag 0 in <PROC moeglich nach>", 0);
		} else if (zt == 1) {
			tmin = 0; // /// oder 1 ?????
			tmax = schnitt - 1;
		} else if (zt == 2) {
			tmin = schnitt;
			tmax = anzahlTakte - 1;
		}

		for (int w = 0; w < E_maxW; w++) {
			if (tag(f, w) == st) {
				moeglW = false;
				for (int t = tmin; t <= tmax; t++) { // FOR t FROM tmin UPTO
														// tmax REP
					if (beideHabenZeit(f, this.meineEltern.getWunsch(f, w), t)) {
						moeglW = true;
					}
				}
				moeglGes = (moeglGes && moeglW);
			}
		}
		return moeglGes;
	}

	private void schiebeNach(int f, int zt) {
		int st = 3 - zt;
		int tmin = 0, tmax = 0;
		int schnitt = E_schn, anzahlTakte = E_anzT;
		if (zt == 0) {
			this.myLogbuch.log("Tag 0 in <PROC schiebe nach>", 0);
		} else if (zt == 1) {
			tmin = 0;
			tmax = schnitt - 1;
		} else if (zt == 2) {
			tmin = schnitt;
			tmax = anzahlTakte - 1;
		}

		for (int w = 0; w < E_maxW; w++) {
			if (tag(f, w) == st) {
				ordneNeuEin(f, w, tmin, tmax);
			}
		}
	}

	private void testausgabe(Eltern e) {
		Logbuch l = Logbuch.getLogbuch();
		l.log("Ausgabe nach Algo", 5);
		for (int en = 0; en < e.getAnzahlEltern(); en++) {
			String s = e.wahlstring(en);
			l.log(s, 5);
		}
		l.log("Ausgabe beendet von Algo", 5);
	}
}