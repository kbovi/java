/**
 * 
 */
package wuerfelPack;

/**
 * @author kbovi
 * @version 1.0 vom 20.12.2010
 *
 */
public class VerflixtBecher extends Wuerfelbecher {

	/**
	 * 
	 */
	public VerflixtBecher() {
		super ();
	}
	
	public int getSumme () {
		return (this.getWurf_1() + this.getWurf_2());
	}
	
	public boolean summeIstSieben () {
		return (this.getSumme() == 7);
	}
}
