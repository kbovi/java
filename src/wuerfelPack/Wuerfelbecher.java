package wuerfelPack;

/**
 * @author kbovi
 * @version 1.0 vom 20.12.2010
 *
 */
public class Wuerfelbecher {
	
	private Wuerfel wuerfel_1;
	private Wuerfel wuerfel_2;
	
	private int wurf_1;
	private int wurf_2;
	

	public Wuerfelbecher() {
		super();
		wuerfel_1 = new Wuerfel ();
		wuerfel_2 = new Wuerfel ();
		this.werfen();
	}
	
	public void werfen () {
		this.wurf_1 = this.wuerfel_1.neuerWurf();
		this.wurf_2 = this.wuerfel_2.neuerWurf();
	}

	/**
	 * @return the wurf1
	 */
	public int getWurf_1() {
		return wurf_1;
	}

	/**
	 * @return the wurf2
	 */
	public int getWurf_2() {
		return wurf_2;
	}
}
