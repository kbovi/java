package tools;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.lavantech.gui.comp.*;

/**
 * @author Administrator
 */
public class TimePicker extends JDialog implements ActionListener {
	/**
     * 
     */
	private static final long serialVersionUID = -5736695009245848172L;
	TimePanel timePanel;
	/**
	 * @uml.property name="minutenNachNullUhr"
	 */
	private int minutenNachNullUhr;

	public TimePicker(int minPastMidnight, JFrame father) {
		super(father, true);
		super.setLayout(new BorderLayout());

		int hour = minPastMidnight / 60;
		int min = minPastMidnight % 60;
		this.minutenNachNullUhr = minPastMidnight;
		GregorianCalendar c = new GregorianCalendar();

		c.set(Calendar.AM_PM, Calendar.AM);

		c.set(Calendar.MINUTE, min);
		c.set(Calendar.HOUR, hour);
		c.set(Calendar.SECOND, 0);
		timePanel = new TimePanel(c);
		timePanel.addActionListener(this);
		// timePanel.getClockPanel().setHourFormat(LocaleSpecificResources.HOUR_FORMAT_24);
		// Lets try a custom needle
		Polygon polygon = new Polygon();
		polygon.addPoint(5, 5);
		polygon.addPoint(-5, 5);
		polygon.addPoint(-2, 0);
		polygon.addPoint(-5, -50);
		polygon.addPoint(0, -100);
		polygon.addPoint(5, -50);
		polygon.addPoint(2, 0);
		polygon.addPoint(5, 5);
		timePanel.getClockPanel().setHourNeedleShape(polygon);
		timePanel.getClockPanel().setMinNeedleShape(polygon);
		timePanel.getClockPanel().setHourNeedleHeightRatio(0.5); // shorter than
																	// default
		timePanel.getClockPanel().setHourNeedleWidthRatio(0.1); // thicker than
																// default
		timePanel.getClockPanel().setMinNeedleHeightRatio(0.7); // shorter than
																// default
		timePanel.getClockPanel().setMinNeedleWidthRatio(0.1); // thicker than
																// default
		timePanel.getClockPanel().setHourNeedleColor(Color.black);
		timePanel.getClockPanel().setMinNeedleColor(Color.black);

		timePanel.getClockPanel().setSecDisplayed(false);
		timePanel.getClockPanel().setHourFormat(
				LocaleSpecificResources.HOUR_FORMAT_12);

		add(timePanel, BorderLayout.CENTER);
		add(new JLabel("Stellen Sie die Zeiger ein"), BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		String out = timePanel.getCalendar().getTime().toString();
		int p = out.indexOf(":");
		int stunde = Integer.parseInt(out.substring(p - 2, p));
		int minute = Integer.parseInt(out.substring(p + 1, p + 3));

		this.minutenNachNullUhr = stunde * 60 + minute;

	}

	/**
	 * @return
	 * @uml.property name="minutenNachNullUhr"
	 */
	public int getMinutenNachNullUhr() {
		return this.minutenNachNullUhr;
	}
}
