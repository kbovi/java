package eltern;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import tools.Logbuch;

/**
 * Write a description of class Hordizer here.
 * 
 * @author igel;Bv August 2007
 * @version 2.2
 */
public class Hordizer {
	private HashMap<String, Fall> dieEltern = new HashMap<String, Fall>();

	private int minKey = 1000;
	private int deltaKey = 7;
	private int numKey = minKey;

	public ArrayList<Eltern> elternArray = new ArrayList<Eltern>();

	/**
	 * Liefert die kleinste (erste) FallNr zurueck
	 */
	// public int getMinKey()
	// {
	// return minKey;
	// }
	/**
	 * Liefert das Inkrement der FallNr zurueck
	 */
	// public int getDeltaKey()
	// {
	// return deltaKey;
	// }
	/**
	 * liefert die groesste (letzte) FallNr zurueck
	 */
	// public int getLastKey()
	// {
	// return (numKey - 7);
	// }

	public Hordizer(String dateiname) {
		String nachname = null;
		String vorname = null;
		String klasse = null;

		Logbuch l = Logbuch.getLogbuch();
		l.log("Hordizer", 10);
		String zeile;

		BufferedReader eingabeDatei;

		try {
			eingabeDatei = new BufferedReader(new InputStreamReader(
					new FileInputStream(dateiname), "UTF-8"));

			// eingabeDatei = new BufferedReader(new FileReader(dateiname));
		}

		catch (IOException e) {
			eingabeDatei = null;
			l.log("Datei nicht gefunden!", 0);
		}

		try {
			zeile = eingabeDatei.readLine().trim();
			zeile = zeile.substring(1); // Wieso????????????????
		} catch (IOException e) {
			zeile = null;
			l.log("Keine Zeile (1)", 0);
		}

		HashMap<String, String> allKeys = new HashMap<String, String>();

		try {
			while (eingabeDatei != null && zeile != null) {
				// zeile = killSpaces(zeile);
				StringTokenizer tokens = new StringTokenizer(zeile, ";");
				/*
				 * Pro Zeile ein Eintrag, mit ";" getrennt: der Nachname der
				 * Vorname Die Klasse Die Adresse, z.B. bestehend aus Str. Nr,
				 * PLZ Ort, Telefon also z.B.: Ach; Florian; 05a; Apfelstr. 111,
				 * 33613 Bielefeld, 123456
				 * 
				 * dabei wird der relevante Key zusammengesetzt aus dem
				 * Nachnamen und der Adresse.
				 * 
				 * Dieser String dient dazu, dass genau solche Schüler mit
				 * identischem Nachnamen und Adresse (was immer dort steht!) als
				 * Geschwister interpretiert werden!
				 */

				try {
					nachname = tokens.nextToken().trim();
					vorname = tokens.nextToken().trim();
					klasse = tokens.nextToken().trim();
				} catch (NoSuchElementException e) {
					l.log("Einlesefehler bei Eltern: " + nachname, 0);
					JOptionPane.showMessageDialog(null,
							"Einlesefehler bei Lehrer: " + nachname, "Achtung",
							JOptionPane.ERROR_MESSAGE);
				}
				if (klasse.startsWith("5") || klasse.startsWith("6")
						|| klasse.startsWith("7") || klasse.startsWith("8")
						|| klasse.startsWith("9")) {
					klasse = "0" + klasse;
				} else {
					// nix!!!!!?????
				}

				String adresse = tokens.nextToken();

				l.log(nachname + "-" + vorname + "-" + klasse + "->" + adresse,
						5);

				String key = nachname + adresse;

				Pupil dieserSchueler = new Pupil(vorname, klasse);

				if (allKeys.get(key) == null) // neuer Schluessel
				{
					l.log("NEUER FALL:" + key + " -> " + numKey, 5);
					allKeys.put(key, String.valueOf(numKey));

					Fall dieserFall = new Fall(nachname, numKey);
					dieserFall.getHorde().add(dieserSchueler);
					dieEltern.put(String.valueOf(numKey), dieserFall);

					numKey += deltaKey;
				} else {
					Fall temp = (Fall) dieEltern.get(String.valueOf(allKeys
							.get(key)));
					temp.getHorde().add(dieserSchueler);
				}
				try {
					zeile = eingabeDatei.readLine();
				} catch (IOException e) {
					zeile = null;
					l.log("Keine Zeile (2)", 0);
				}
			}// while

			eingabeDatei.close();
		} catch (IOException e) {
			l.log("Eingabefehler in Elterndatei " + dateiname, 0);

		}
	}

	public ArrayList<Fall> getEltern() {
		ArrayList<Fall> result = new ArrayList<Fall>();
		Collection<Fall> faelle = dieEltern.values();

		TreeSet<Fall> ts = new TreeSet<Fall>();

		// mache aus den Fällen eine TreeSet!
		{
			Iterator<Fall> it = faelle.iterator();
			while (it.hasNext()) {
				Fall tmpFall = it.next();
				ts.add(tmpFall);
			}
		}

		// mache aus dem TreeSet die ArrayList
		// und ändere die Nummern!
		{
			int newKey = this.minKey;
			Iterator<Fall> it = ts.iterator();
			while (it.hasNext()) {
				Fall tmpFall = it.next();
				tmpFall.setFallNr(newKey);
				newKey = newKey + this.deltaKey;
				result.add(tmpFall);
			}
		}
		return result;
	}
}
