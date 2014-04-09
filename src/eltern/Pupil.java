package eltern;

import java.io.Serializable;

/**
 * ein schueler hat hier nur vorname und klasse der comparator benutzt die
 * klasse. dazu muss man sich an die regel halten, dass eine klasse immer eine
 * 3-stellige angelegenheit ist in der ggf. eine fuehrende null auftaucht. bei
 * klassen 11, 12 und 13 reichen auch 2 zeichen!
 * 
 * @author Klaus Bovermann
 * @version 1.0
 */
public class Pupil implements Comparable<Pupil>, Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -2038120923473829484L;

	public Pupil(String vorname, String klasse) {
		this.vorname = vorname;
		this.klasse = klasse;
	}

	public int compareTo(Pupil otherPupil) {
		int gleich = this.klasse.compareTo(otherPupil.getKlasse());
		if (gleich == 0) // zwei Schüler mit gleichem Hausnamen in der gleichen
							// Klasse.
		{
			gleich = this.vorname.compareTo(otherPupil.getVorname());
		}
		return (gleich);
	}

	/**
	 * @uml.property name="vorname"
	 */
	public String getVorname() {
		return (this.vorname);
	}

	/**
	 * @uml.property name="klasse"
	 */
	public String getKlasse() {
		return (this.klasse);
	}

	/**
	 * @uml.property name="vorname"
	 */
	private String vorname;

	/**
	 * @uml.property name="klasse"
	 */
	private String klasse;

}
