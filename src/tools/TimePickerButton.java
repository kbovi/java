package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author KB
 * @version 2.2
 */
public class TimePickerButton extends JButton implements ActionListener {
	/**
     * 
     */
	private static final long serialVersionUID = -4063438938845294335L;
	// Bezugsobjekte
	private JFrame father;

	// Attribute

	// Konstruktor
	public TimePickerButton(JFrame father) {
		super();
		this.father = father;
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) {
		TimePicker t = new TimePicker(Integer.parseInt(this.getName()), father);
		this.setText("" + t.getMinutenNachNullUhr());
	}

	public void setText(String text) {
		this.setName(text);
		int min = Integer.parseInt(text);
		int mins = min % 60;

		if (mins < 10) {
			super.setText("" + (min / 60) + ":0" + (min % 60));
		} else {
			super.setText("" + (min / 60) + ":" + (min % 60));
		}

	}

	// Dienste

}
