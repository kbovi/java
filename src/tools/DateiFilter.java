package tools;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Title: DateiFilter<br>
 * Description: Wird benutzt, um Dateien mit einer vorgegebenen Endung zu
 * filtern<br>
 * Copyright: Copyright (c) 2006 Company: MPG
 * 
 * @author KB
 * 
 * @version 1.0
 * 
 */

public class DateiFilter extends FileFilter {

	String endung;
	String characteristic;

	/**
	 * Dateien mit der als Parameter angegebenen Endung werden gefiltert.
	 * 
	 * @param endung
	 */
	public DateiFilter(String endung, String characteristic) {
		this.characteristic = characteristic;
		this.endung = endung;
	}

	public DateiFilter() {
		super();
	}

	public boolean accept(File f) {
		boolean ok = f.isDirectory();
		if (!ok) {
			String suffix = getSuffix(f);
			if (suffix != null)
				ok = suffix.equals(endung);
		}
		return (ok);
	}

	public String getDescription() {
		return (this.characteristic + " (*." + endung + ")");
	}

	private String getSuffix(File f) {
		String s = f.getPath();
		String suff = null;
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1)
			suff = s.substring(i + 1).toLowerCase();
		return suff;
	}
}