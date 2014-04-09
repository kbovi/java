package tools;

import estKonstanten.ESTEigenschaften;

/**
 * Title: SerialFilter<br>
 * Description: Wird benutzt, um Dateien mit der entsprechenden Endung aus der
 * "*.cfg"-Datei zu filtern.<br>
 * Copyright: Copyright (c) 2006 Company: MPG
 * 
 * @author KB
 * 
 * @version 1.0
 * 
 */

public class SerialLehrerFilter extends DateiFilter {

	/**
	 * Konstruiert einen Dateifilter für Dateien mit der entsprechenden Endung
	 * (steht im ConfigFile!), um Dateien mit serialisierten Daten zu
	 * spezifizieren.
	 * 
	 */
	public SerialLehrerFilter() {
		super(ESTEigenschaften.lehrerDateiKennung(),
				"serialisierte Lehrer-Daten");
	}
}