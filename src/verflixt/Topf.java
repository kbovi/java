package verflixt;

public class Topf {
	private int wert;

	public Topf() {
		this.wert = 0;
	}

	public void einzahlen() {
		if (wert < 2) {
			wert++;
		}
	}

	public void auszahlen() {
		if (wert == 2) {
			wert = wert - 2;
		}
	}
}
