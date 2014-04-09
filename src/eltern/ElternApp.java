package eltern;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import lehrer.Kollegium;

public class ElternApp {
	// boolean packFrame = false;

	/** Construct the application */
	public ElternApp(JFrame owner, Kollegium k, Eltern eltern, Boolean vorAlgo) {
		ElternFrame frame = new ElternFrame(owner, k, eltern, vorAlgo);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		frame.setResizable(true);
		frame.setLocation((screenSize.width - frameSize.width) / 4,
				(screenSize.height - frameSize.height) / 2);
		frame.pack();
		frame.validate();
		frame.setVisible(true);
	}

}
