package wuerfelPack;

/**
 * @author kbovi
 * @version 1.0 vom 20.12.2010
 *
 */
public class Wuerfelbecher {
	
	private Wuerfel w1;
	private Wuerfel w2;
	
	private int wurf1;
	private int wurf2;
	

	public Wuerfelbecher() {
		super();
		w1 = new Wuerfel ();
		w2 = new Wuerfel ();
		this.werfen();
	}
	
	public void werfen () {
		this.wurf1 = this.w1.neuerWurf();
		this.wurf2 = this.w2.neuerWurf();
	}

	/**
	 * @return the wurf1
	 */
	public int getWurf1() {
		return wurf1;
	}

	/**
	 * @return the wurf2
	 */
	public int getWurf2() {
		return wurf2;
	}
}
