package ausgabe;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Starter für DruckGUI
 * 
 * @author Administrator
 * 
 */
public class DruckAPP {
	boolean packFrame = true;

	/**
	 * Construct the application
	 * 
	 * @param ag
	 *            Der ausgabegenerator
	 * 
	 * @param vorAlgo
	 *            um zu wissen, welche Listen gedruckt werden duerfen.
	 */
	public DruckAPP(JFrame owner, AusgabeGenerator ag, boolean vorAlgo) {
		DruckGUI frame = new DruckGUI(owner, ag, vorAlgo);
		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their
		// layout
		if (packFrame) {
			frame.pack();
		} else {
			frame.validate();
		}
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}
}
