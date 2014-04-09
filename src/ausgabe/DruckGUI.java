package ausgabe;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tools.Beobachter;

/**
 * Hier kann man waehlen, welche Listen gedruckt werden sollen.
 * 
 * @author Administrator
 */
public class DruckGUI extends JDialog {

	/**
     * 
     */
	private static final long serialVersionUID = 8022209012646492654L;
	private AusgabeGenerator ag;
	private JButton start;
	private String[] elements;
	private boolean vorAlgo;
	private JCheckBox[] myBoxes;

	private Beobachter derBeobachter;

	/**
	 * 
	 * @param ag
	 *            Der ausgabegenerator
	 * 
	 * @param vorAlgo
	 *            um zu wissen, welche Listen gedruckt werden duerfen.
	 */
	public DruckGUI(JFrame owner, AusgabeGenerator ag, boolean vorAlgo) {
		super(owner, "Druckdialog", true);
		this.vorAlgo = vorAlgo;
		this.ag = ag;
		init(this.vorAlgo);
		this.derBeobachter = Beobachter.getBeobachter();
		this.pack();
		// this.setVisible(true);
	}

	private void init(boolean vorAlgo) {
		this.setLayout(new BorderLayout());

		elements = new String[8];
		elements[0] = "LehrerListe";
		elements[1] = "ElternWahl";
		elements[2] = "ElternAntworten";
		elements[3] = "LehrerAntworten";
		elements[4] = "Raumliste";
		elements[5] = "Statistik";
		elements[6] = "LehrerInfo";
		elements[7] = "LehrerInfoFuerEltern";

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(8, 1));

		myBoxes = new JCheckBox[8];

		for (int i = 0; i < elements.length; i++) {
			myBoxes[i] = new JCheckBox();
			myBoxes[i].setName(elements[i]);
			centerPanel.add(myBoxes[i]);
		}

		myBoxes[0].setText("Liste aller Lehrer");
		myBoxes[1].setText("Wahlzettel der Eltern");
		myBoxes[2].setText("Antwortschreiben an die Eltern");
		myBoxes[3].setText("Antwortschreiben an die Lehrer");
		myBoxes[4].setText("Raumlisten");
		myBoxes[5].setText("Balkenstatistik");
		myBoxes[6].setText("LehrerInfo");
		myBoxes[7].setText("LehrerInfoFuerEltern");

		if (vorAlgo) {
			myBoxes[0].setEnabled(true);
			myBoxes[1].setEnabled(true);
			myBoxes[2].setEnabled(false);
			myBoxes[3].setEnabled(false);
			myBoxes[4].setEnabled(false);
			myBoxes[5].setEnabled(true);
			myBoxes[6].setEnabled(true);
			myBoxes[7].setEnabled(true);
		} else {
			myBoxes[0].setEnabled(true);
			myBoxes[1].setEnabled(false);
			myBoxes[2].setEnabled(true);
			myBoxes[3].setEnabled(true);
			myBoxes[4].setEnabled(true);
			myBoxes[5].setEnabled(true);
			myBoxes[6].setEnabled(false);
			myBoxes[7].setEnabled(false);
		}

		this.start = new JButton("Drucken");
		this.add(this.start, "South");
		this.add(new JScrollPane(centerPanel), "Center");

		this.start.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				printMouseClicked(e);
			}

		});

	}

	private void texen(File dir) {
		for (int i = 0; i < this.myBoxes.length; i++) {
			if (this.myBoxes[i].isEnabled() && this.myBoxes[i].isSelected()) {
				String name = this.myBoxes[i].getName();

				if (i == 0 || i == 5 || i == 6 || i == 7) {
					this.ag.lehrerlistenausgabe(name, dir);
				} else if (i == 1 || i == 2) {
					this.ag.elternausgabe(name, dir);
				} else {
					this.ag.lehrerausgabe(name, dir);
				}

			}

		}
	}

	private boolean someAreChecked() {
		for (int i = 0; i < this.myBoxes.length; i++) {
			if (this.myBoxes[i].isSelected()) {
				return true;
			}
		}
		return false;
	}

	private void printMouseClicked(MouseEvent e) {
		if (!this.someAreChecked()) {
			return;
		}
		JFileChooser speichern = new JFileChooser();

		if (this.derBeobachter.getLastOutDir().equals("")) {
			speichern.setCurrentDirectory(new File(System
					.getProperty("user.home")));
		} else {
			speichern.setCurrentDirectory(new File(this.derBeobachter
					.getLastOutDir()));
		}
		// speichern.setCurrentDirectory(new File
		// (System.getProperty("user.home")));
		speichern.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (speichern.showSaveDialog(null) == JFileChooser.CANCEL_OPTION) {
			return;
		}

		File dir = speichern.getSelectedFile();
		this.derBeobachter.setLastOutDir(dir.getPath());

		texen(dir);

		JOptionPane.showMessageDialog(this, "Druck beendet!", "Meldung",
				JOptionPane.INFORMATION_MESSAGE);

		// this.dispose();

	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}
}
