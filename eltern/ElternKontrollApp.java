package eltern;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ElternKontrollApp {
	// boolean packFrame = false;

	/** Construct the application */
	public ElternKontrollApp(Eltern eltern) {
		ElternKontrollFrame frame = new ElternKontrollFrame(eltern);

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		// frame.setSize(640, 400);
		frame.setResizable(true);
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their
		// layout
		// if (packFrame)
		// {
		frame.pack();
		// }
		// else
		// {
		frame.validate();
		// }
		frame.setVisible(true);
	}
}
