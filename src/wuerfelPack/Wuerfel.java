package wuerfelPack;

/**
 * @author kbovi
 * @version 1.0 vom 20.12.2010
 *
 */
public class Wuerfel {
	
	private Zufall meinZufall;
	
	public Wuerfel () {
		this.meinZufall = new Zufall ();
	}
	
	public int neuerWurf () {
		return this.meinZufall.neueZufallszahl(1, 6);
	}
}
