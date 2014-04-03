import java.awt.Color;

import javax.swing.JLabel;

public class Lampe {

	private boolean an;
	private Color farbe;
	private JLabel label;

	public Lampe(Color c, JLabel label) {
		this.farbe = c;
		this.label = label;
		this.abschalten();
	}

	public void anschalten() {
		this.an = true;
		this.label.setBackground(this.farbe);
	}

	public void abschalten() {
		this.an = false;
		this.label.setBackground(Color.gray);
	}

	public boolean isAn() {
		return an;
	}
}
