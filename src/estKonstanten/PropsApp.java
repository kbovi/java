package estKonstanten;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

// import javax.swing.JLabel;

/**
 * Starter für ESTPropertiesFrame
 * 
 * @author Administrator
 * 
 */
public class PropsApp {

	boolean packFrame = false;

	/** Construct the application */
	public PropsApp(JFrame owner) { // , JLabel titel) {
		super();
		ESTPropertiesFrame frame = new ESTPropertiesFrame(owner); // , titel);
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
