import javax.swing.JLabel;

public class Anlage {

	private FussgaengerAmpel fa;
	private AutoAmpel aa;
	private int state;

	public Anlage(JLabel fr, JLabel fg, JLabel ar, JLabel ay, JLabel ag) {
		this.fa = new FussgaengerAmpel(fr, fg);
		this.aa = new AutoAmpel(ar, ay, ag);
		this.state = 1;
		setAll();
	}

	private void setAll() {
		switch (this.state) {
		case 1:
			this.aa.setRot();
			this.fa.setRot();
			break;
		case 2:
			this.aa.setRotGelb();
			this.fa.setRot();
			break;
		case 3:
			this.aa.setGruen();
			this.fa.setRot();
			break;
		case 4:
			this.aa.setGelb();
			this.fa.setRot();
			break;
		case 5:
			this.aa.setRot();
			this.fa.setRot();
			break;
		case 6:
			this.aa.setRot();
			this.fa.setGruen();
			break;
		}
	}
	
	public void next () {
		this.state = this.state + 1;
		if (this.state == 7) {
			this.state = 1;
		}
		this.setAll();
	}
}
