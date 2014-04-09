package verflixt;

import wuerfelPack.VerflixtBecher;

public class Spieler {
	private boolean aktiv;
	private int geld;
	private int anzahlWuerfe;
	private int augenzahl;
	private VerflixtBecher derBecher;
	private Topf derTopf;
	
	public Spieler (int startGeld, VerflixtBecher derBecher, Topf derTopf) {
		this.aktiv = false;
		this.geld = startGeld;
		this.anzahlWuerfe = 0;
		this.augenzahl = 0;
		this.derBecher = derBecher;
		this.derTopf = derTopf;
	}
	
	public void aktiviere () {
		this.aktiv = true;
	}
	
	public void deaktiviere () {
		this.aktiv = false;
	}
	
	public boolean istAktiv () {
		return this.aktiv;
	}
	
	public void einsetzen () {
		this.geld --;
		this.derTopf.einzahlen();
	}
	
	public void wuerfeln () {
		if (this.istAktiv()) {
			this.derBecher.werfen();
			this.anzahlWuerfe ++;
			if (derBecher.summeIstSieben()) {
				this.augenzahl= this.augenzahl - 7;
				this.deaktiviere();
			}
			else {
				this.augenzahl = this.augenzahl+ this.derBecher.getSumme();
			}
		}
	}
}
