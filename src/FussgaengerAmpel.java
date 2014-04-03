import java.awt.Color;

import javax.swing.JLabel;

public class FussgaengerAmpel {

	private Lampe rot;
	private Lampe gruen;

	public FussgaengerAmpel(JLabel rl, JLabel gl) {
		this.rot = new Lampe(Color.red, rl);
		this.gruen = new Lampe(Color.green, gl);
		setRot ();
	}

	public void setRot() {
		this.gruen.abschalten();
		this.rot.anschalten();
	}
	
	public void setGruen() {
		this.gruen.anschalten();
		this.rot.abschalten();
	}	
}
