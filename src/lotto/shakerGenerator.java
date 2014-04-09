package lotto;

import java.util.ArrayList;
import java.util.Random;

import estKonstanten.ESTEigenschaften;

/**
 * Diese Klasse verwaltet eine Menge von natürlichen Zahlen, typischerweise
 * (0,1,2,...,n-1), jedoch auch durch ArrayList von Integern vorgegebene Zahlen,
 * gibt auf Anforderung eine zufällige dieser Zahlen aus, die dann aus der Menge
 * gestrichen wird. Man kann fragen, ob es noch nicht-gezogene Zahlen gibt.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class shakerGenerator {
	private ArrayList<Integer> vorrat;
	private Random zuf;

	/**
	 * Constructor
	 */
	public shakerGenerator() {
		this.vorrat = new ArrayList<Integer>();
		zuf = new Random();
	}

	/**
	 * Constructor
	 */
	public shakerGenerator(ArrayList<Integer> vorrat) {
		this.vorrat = vorrat;
		zuf = new Random();
	}

	/**
	 * Constructor for objects of class shakerGenerator
	 */
	public shakerGenerator(int n) {
		ESTEigenschaften.init();
		vorrat = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			vorrat.add(i);
		}
		zuf = new Random();
	}

	/**
	 * Fragt, ob es noch Zahlen im Vorrat gib.
	 * 
	 * @return gibt es noch Zahlen
	 */
	public boolean nochZahlenDa() {
		return !this.vorrat.isEmpty();
	}

	/**
	 * Gibt eine zufällige Zahl des Vorrats und löscht sie.
	 * 
	 * @return eine Zahl des Vorrats
	 */
	public int gibEineZahl() {
		int index;
		if (ESTEigenschaften.isMitElternShuffle()) {
			index = zuf.nextInt(this.vorrat.size());
		} else {
			index = 0;
		}
		int ret = vorrat.get(index);
		vorrat.remove(index);
		return ret;
	}
}
