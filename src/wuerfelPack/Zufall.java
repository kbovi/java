package wuerfelPack;

import java.util.Random;

/**
 * @author kbovi
 * @version 1.0 vom 20.12.2010
 *
 */
public class Zufall {
	
	private Random myRandom;
	
	public Zufall () {
		this.myRandom = new Random ();
	}
	
	public int neueZufallszahl (int von, int bis) {
		return (this.myRandom.nextInt(bis - von + 1) +von);
	}
}
