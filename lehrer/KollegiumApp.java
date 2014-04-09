package lehrer;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import eltern.Eltern;

public class KollegiumApp {
	boolean packFrame = true;

	/** Construct the application */
	public KollegiumApp(JFrame owner, Kollegium k, Eltern e, Boolean vorAlgo) {
		KollegiumFrame frame = new KollegiumFrame(owner, k, e, vorAlgo);
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
		frame.setResizable(true);
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}
}
