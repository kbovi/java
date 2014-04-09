package tools;

/**
 * Title: TextFileFilter<br>
 * Description: Wird benutzt, um Dateien mit der Endung ".txt" zu filtern<br>
 * Copyright: Copyright (c) 2003 Company: MPG
 * 
 * @author KB
 * 
 * @version 1.0
 * 
 */

public class TextFileFilter extends DateiFilter {

	/**
	 * Konstruiert einen Dateifilter für Dateien mit der Endung <code>txt</code>
	 * , um Textfiles zu spezifizieren.
	 * 
	 */
	public TextFileFilter() {
		super("txt", "Textdatei");
	}
}