
public class Stromquelle {

	private boolean obAkku;
	private int maxLadung;
	private int ladung;
	/**
	 * @param obAkku
	 * @param maxLadung
	 * @param ladung
	 */
	public Stromquelle(boolean obAkku, int maxLadung, int ladung) {
		super();
		this.obAkku = obAkku;
		this.maxLadung = maxLadung;
		this.ladung = ladung;
	}
	/**
	 * @return the obAkku
	 */
	public boolean isObAkku() {
		return obAkku;
	}
	/**
	 * @return the maxLadung
	 */
	public int getMaxLadung() {
		return maxLadung;
	}
	/**
	 * @return the ladung
	 */
	public int getLadung() {
		return ladung;
	}
	
	
}
