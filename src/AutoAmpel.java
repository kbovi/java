import java.awt.Color;

import javax.swing.JLabel;


public class AutoAmpel {
	private Lampe rot;
	private Lampe gelb;
	private Lampe gruen;

	public AutoAmpel(JLabel rl, JLabel yl, JLabel gl) {
		this.rot = new Lampe(Color.red, rl);
		this.gelb = new Lampe (Color.yellow, yl);
		this.gruen = new Lampe(Color.green, gl);
		setRot ();
	}

	public void setRot() {
		this.gruen.abschalten();
		this.gelb.abschalten();
		this.rot.anschalten();
	}
	
	public void setGruen() {
		this.gruen.anschalten();
		this.gelb.abschalten();
		this.rot.abschalten();
	}	

	public void setGelb() {
		this.gruen.abschalten();
		this.gelb.anschalten();
		this.rot.abschalten();
	}	
	
	public void setRotGelb() {
		this.gruen.abschalten();
		this.gelb.anschalten();
		this.rot.anschalten();
	}	
}
