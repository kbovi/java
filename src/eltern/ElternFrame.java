package eltern;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import lehrer.Kollegium;
import takter.Takter;
import tools.Beobachter;
import estKonstanten.ESTEigenschaften;
import estKonstanten.ESTStatics;

/**
 * @author Administrator
 */
public class ElternFrame extends JDialog {

	private static final long serialVersionUID = 3617574920540926007L;

	private Eltern meineEltern;
	private Kollegium meinKollegium;
	private Boolean vorAlgo;

	private Beobachter derBeobachter;

	private JPanel contentPane;

	private String Namen[];

	private JTable kollegenTable;
	private JTable taktTable;

	private LehrerTableModel lehrerModel;
	private ElternTaktTableModel taktModel;

	private GridBagLayout gbl;
	private GridBagConstraints gbc;

	private JLabel TerminLabel;
	private JLabel LehrerWahlLabel;
	private JLabel Hinweis;

	private JComboBox fallDatenCombo;

	private JButton FallIncButton = new JButton();
	private JButton FallDecButton = new JButton();
	private JButton taktAdditivBefreienButton;
	private JButton taktAdditivSperrenButton;
	// private JButton showElternButton;

	private JButton nachVorgabeSperrenButton;
	private JButton nachVorgabeBefreienButton;

	private JButton kollegenUebernehmenButton;

	private JTextField KollegenEingabe;
	private JList TaktAusgabeList;
	private JList KollegenAusgabeList;

	public ElternFrame(JFrame owner, Kollegium k, Eltern eltern, Boolean vorAlgo) {
		super(owner, "Eltern bearbeiten", false); // sollte modal sein!!!
													// also besser true!!!
		this.meineEltern = eltern;
		this.meinKollegium = k;
		this.vorAlgo = vorAlgo;

		this.derBeobachter = Beobachter.getBeobachter();

		this.setResizable(false);

		this.Namen = new String[this.meineEltern.getAnzahlEltern()];
		for (int i = 0; i < this.meineEltern.getAnzahlEltern(); i++) {
			this.Namen[i] = "";
			this.Namen[i] += this.meineEltern.getFall(i).getFallNr();
			this.Namen[i] += ": ";
			this.Namen[i] += this.meineEltern.getFall(i).getNachname();
			Fall dieserFall = this.meineEltern.get(i);
			String kn = dieserFall.getKindernamen();
			this.Namen[i] += " (" + kn + ")";
		}

		fallDatenCombo = new JComboBox(this.Namen);

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Component initialization */
	private void jbInit() throws Exception {
		String zusatz;
		if (this.vorAlgo) {
			zusatz = "bearbeiten";
		} else {
			zusatz = "ansehen (nach Zuordnung)";
		}
		this.setTitle("Elterndaten " + zusatz);

		contentPane = (JPanel) this.getContentPane();

		gbl = new GridBagLayout();
		contentPane.setLayout(gbl);

		{// Eltern-Namen-Auswahl-Button Zeile 0 Spalten 0 1 2
			fallDatenCombo.setForeground(Color.red);
			fallDatenCombo.setFont(new java.awt.Font("DialogInput", 1, 14));
			fallDatenCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					geheZumFall(e);
				}
			});
			gbc = makegbc(0, 0, 3, 1); // evtl. 0,0,4,1
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(fallDatenCombo, gbc);
			contentPane.add(fallDatenCombo);
		}

		{// Label ‹berschrift links Zeile 1 Spalten 0 1
			TerminLabel = new JLabel("Termine (Liste zum Markieren)");
			gbc = makegbc(0, 1, 2, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(TerminLabel, gbc);
			contentPane.add(TerminLabel);
		}

		{// Label ‹berschrift rechts Zeile 1 Spalten 2 3
			LehrerWahlLabel = new JLabel(
					"anwesende Lehrer (Liste nur zum Lesen)");
			gbc = makegbc(2, 1, 2, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(LehrerWahlLabel, gbc);
			contentPane.add(LehrerWahlLabel);
		}

		{// tabelle mit Takten Zeile 2 Spalten 2 3
			lehrerModel = new LehrerTableModel(meinKollegium);// mach eine
																// Tabelle aus
																// den
																// Lehrerdaten
			kollegenTable = new JTable(lehrerModel);
			kollegenTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			// kollegenTable.setPreferredScrollableViewportSize(new
			// Dimension(180, 220));
			JScrollPane kt = new JScrollPane(kollegenTable);
			gbc = makegbc(2, 2, 2, 1);
			gbc.weightx = 100;
			gbc.weighty = 100;
			gbc.fill = GridBagConstraints.BOTH;
			gbl.setConstraints(kt, gbc);
			contentPane.add(kt);
		}

		{// tabelle mit Lehrern Zeile 2 Spalten 0 1
			Fall actFall = this.meineEltern.getActFall();
			taktModel = new ElternTaktTableModel(actFall, this.meinKollegium); // mach
																				// eine
																				// Tabelle
																				// aus
																				// den
																				// Takten
			taktTable = new JTable(taktModel);
			taktTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			// taktTable.setPreferredScrollableViewportSize(new Dimension(150,
			// 220));
			JScrollPane tt = new JScrollPane(taktTable);
			gbc = makegbc(0, 2, 2, 1);
			gbc.weightx = 100;
			gbc.weighty = 100;
			gbc.fill = GridBagConstraints.BOTH;
			gbl.setConstraints(tt, gbc);
			contentPane.add(tt);
		}

		{// LehrerHinweis Zeile 3 Spalten 3
			Hinweis = new JLabel("Bitte Lehrernummern eingeben!");
			// JScrollPane kesp = new JScrollPane (KollegenEingabe);
			gbc = makegbc(3, 3, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(Hinweis, gbc);
			contentPane.add(Hinweis);
		}

		{// LehrerEingabeFeld Zeile 3 Spalten 2 3
			KeyListener keyListener = new KeyListener() {
				public void keyPressed(KeyEvent keyEvent) {
					if (keyEvent.getKeyCode() == 10) {
						kollegenUebernehmenButton_mouseClicked(null);
					}
				}

				public void keyReleased(KeyEvent keyEvent) {
				}

				public void keyTyped(KeyEvent keyEvent) {
				}
			};

			KollegenEingabe = new JTextField();
			KollegenEingabe.addKeyListener(keyListener);
			// JScrollPane kesp = new JScrollPane (KollegenEingabe);
			gbc = makegbc(2, 3, 1, 1);
			gbc.weightx = 50;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(KollegenEingabe, gbc);
			contentPane.add(KollegenEingabe);
			KollegenEingabe.setEnabled(this.vorAlgo);
		}

		// {// ShowElternButton Zeile 0 Spalten 3
		// showElternButton = new JButton("Zeigen!");
		// gbc = makegbc(3, 0, 1, 1);
		// gbc.weightx = 0;
		// gbc.weighty = 0;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbl.setConstraints(showElternButton, gbc);
		// contentPane.add(showElternButton);
		// showElternButton.setEnabled(true);
		// }

		{// ElternBefreienButton Zeile 3 Spalten 0
			taktAdditivBefreienButton = new JButton("markierte Takte befreien");
			gbc = makegbc(0, 3, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(taktAdditivBefreienButton, gbc);
			contentPane.add(taktAdditivBefreienButton);
			taktAdditivBefreienButton.setEnabled(this.vorAlgo);
		}

		{// elternSperrenButton Zeile 3 Spalten 1
			taktAdditivSperrenButton = new JButton("markierte Takte sperren");
			gbc = makegbc(1, 3, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(taktAdditivSperrenButton, gbc);
			contentPane.add(taktAdditivSperrenButton);
			taktAdditivSperrenButton.setEnabled(this.vorAlgo);
		}

		{// elternBefreienNachVorgaben Zeile 4 Spalten 0
			nachVorgabeBefreienButton = new JButton(
					ESTEigenschaften.zweiterElternButton());
			gbc = makegbc(0, 4, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(nachVorgabeBefreienButton, gbc);
			contentPane.add(nachVorgabeBefreienButton);
			nachVorgabeBefreienButton.setEnabled(this.vorAlgo);
		}

		{// elternSperrenNachVorgaben Zeile 4 Spalten 1
			nachVorgabeSperrenButton = new JButton(
					ESTEigenschaften.ersterElternButton());
			gbc = makegbc(1, 4, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(nachVorgabeSperrenButton, gbc);
			contentPane.add(nachVorgabeSperrenButton);
			nachVorgabeSperrenButton.setEnabled(this.vorAlgo);
		}

		{// Lehrer eintragen Zeile 4 Spalten 2 3
			kollegenUebernehmenButton = new JButton("Kollegen neu eintragen");
			gbc = makegbc(2, 4, 2, 1);
			gbc.weightx = 100;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(kollegenUebernehmenButton, gbc);
			contentPane.add(kollegenUebernehmenButton);
			kollegenUebernehmenButton.setEnabled(this.vorAlgo);
		}

		{// LehrerAusgabeFeld Zeile 5 Spalten 2 3
			this.KollegenAusgabeList = new JList();
			JScrollPane kasp = new JScrollPane(this.KollegenAusgabeList);
			gbc = makegbc(2, 5, 2, 1);
			gbc.weightx = 0;
			gbc.weighty = 50;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbl.setConstraints(kasp, gbc);
			contentPane.add(kasp);
		}

		{// ElternTaktausgabe Kontrolle Zeile 5 Spalten 0 1
			TaktAusgabeList = new JList();
			JScrollPane ta = new JScrollPane(this.TaktAusgabeList);
			gbc = makegbc(0, 5, 2, 1);
			gbc.weightx = 0;
			gbc.weighty = 50;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbl.setConstraints(ta, gbc);
			contentPane.add(ta);
		}

		{// N‰chster Fall Zeile 6 Spalten 2 3
			FallIncButton.setFont(new java.awt.Font("Dialog", 1, 14));
			FallIncButton.setToolTipText("Increase");
			FallIncButton.setText("N\u00E4chster Fall");
			gbc = makegbc(2, 6, 2, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(FallIncButton, gbc);
			contentPane.add(FallIncButton);
		}

		{// Voriger Fall Zeile 6 Spalten 0 1
			FallDecButton.setFont(new java.awt.Font("Dialog", 1, 14));
			FallDecButton.setToolTipText("Decrease");
			FallDecButton.setText("Voriger Fall");
			gbc = makegbc(0, 6, 2, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(FallDecButton, gbc);
			contentPane.add(FallDecButton);
		}

		updateData();

		taktTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tabellenEreignis_mouseClicked(e);
			}
		});

		FallDecButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				FallDecButton_mouseClicked(e);
			}
		});

		FallIncButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				FallIncButton_mouseClicked(e);
			}
		});

		// this.showElternButton.addMouseListener(new
		// java.awt.event.MouseAdapter()
		// {
		// public void mouseClicked(MouseEvent e)
		// {
		// showElternButton_mouseClicked(e);
		// }
		// });

		this.taktAdditivBefreienButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						taktAdditivBefreienButton_mouseClicked(e);
					}
				});

		this.taktAdditivSperrenButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						taktAdditivSperrenButton_mouseClicked(e);
					}
				});

		this.nachVorgabeSperrenButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						nachVorgabeSperrenButton_mouseClicked(e);
					}
				});

		this.nachVorgabeBefreienButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						nachVorgabeBefreienButton_mouseClicked(e);
					}
				});

		this.kollegenUebernehmenButton
				.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						kollegenUebernehmenButton_mouseClicked(e);
					}
				});

		this.pack();
	}

	private void tabellenEreignis_mouseClicked(MouseEvent e) {
		// wieso muss man das haben???
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
		}
	}

	private void FallIncButton_mouseClicked(MouseEvent e) {
		this.meineEltern.inc();
		updateData();
		// this.taktTable.setModel(new
		// ElternTaktTableModel(this.meineEltern.getActFall()));
	}

	private void FallDecButton_mouseClicked(MouseEvent e) {
		this.meineEltern.dec();
		updateData();
		// this.taktTable.setModel(new
		// ElternTaktTableModel(this.meineEltern.getActFall()));
	}

	private void updateData() {
		String out;
		out = this.meineEltern.getFall(this.meineEltern.getActNumberEltern())
				.getAlleLehrerNummernKorrigiert();
		this.KollegenEingabe.setText(out);

		String[] lehrerZeilen = this.meineEltern.getFall(
				this.meineEltern.getActNumberEltern()).getAlleLehrerListe(
				this.meinKollegium);
		this.KollegenAusgabeList.setListData(lehrerZeilen);

		int[] taktFeld = this.meineEltern.get(
				this.meineEltern.getActNumberEltern()).getTakte();

		boolean[] boolTakte = new boolean[taktFeld.length];

		for (int i = 0; i < boolTakte.length; i++) {
			if (taktFeld[i] == -1) {
				boolTakte[i] = true;
			} else {
				boolTakte[i] = false;
			}
		}
		Takter t = new Takter();
		String[] zeilen = { "Freie Takte zur Zeit:", "",
				t.takt2StringErsterTag(boolTakte),
				t.takt2StringZweiterTag(boolTakte) };
		this.TaktAusgabeList.setListData(zeilen);
		this.fallDatenCombo.setSelectedIndex(this.meineEltern
				.getActNumberEltern());
		this.taktTable.setModel(new ElternTaktTableModel(this.meineEltern
				.getActFall(), this.meinKollegium));
	}

	// private void showElternButton_mouseClicked (MouseEvent e)
	// {
	// new ElternKontrollApp(this.meineEltern);
	// }

	private void taktAdditivBefreienButton_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			int[] selektion = taktTable.getSelectedRows();

			for (int i = 0; i < selektion.length; i++) {
				this.meineEltern.get(this.meineEltern.getActNumberEltern())
						.setTakt(selektion[i], ESTStatics.ELTERNFREI);
			}
			this.updateData();
			this.derBeobachter.somethingChanged();
		}
		// this.taktTable.setModel(new
		// ElternTaktTableModel(this.meineEltern.getActFall()));
	}

	private void taktAdditivSperrenButton_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			int[] selektion = taktTable.getSelectedRows();

			for (int i = 0; i < selektion.length; i++) {
				this.meineEltern.get(this.meineEltern.getActNumberEltern())
						.setTakt(selektion[i], ESTStatics.ELTERNSPERRE);
			}
			this.updateData();
			this.derBeobachter.somethingChanged();
		}
		// this.taktTable.setModel(new
		// ElternTaktTableModel(this.meineEltern.getActFall()));
	}

	private void nachVorgabeSperrenButton_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			this.meineEltern.get(this.meineEltern.getActNumberEltern())
					.sperreNachVorgabe();
			this.updateData();
			this.derBeobachter.somethingChanged();
		}
		// this.taktTable.setModel(new
		// ElternTaktTableModel(this.meineEltern.getActFall()));
	}

	private void nachVorgabeBefreienButton_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			this.meineEltern.get(this.meineEltern.getActNumberEltern())
					.befreieNachVorgabe();
			this.updateData();
			this.derBeobachter.somethingChanged();
		}
		// this.taktTable.setModel(new
		// ElternTaktTableModel(this.meineEltern.getActFall()));
	}

	private void kollegenUebernehmenButton_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			kollegenUebernehmenButton_mouseClicked(e, true);
			this.derBeobachter.somethingChanged();
		}
	}

	private void kollegenUebernehmenButton_mouseClicked(MouseEvent e,
			boolean mitClear) {
		String kString = this.KollegenEingabe.getText();

		StringTokenizer s = new StringTokenizer(kString, ", ", false);

		if (mitClear) {
			this.meineEltern.getFall(this.meineEltern.getActNumberEltern())
					.clearGewollt();
		}
		while (s.hasMoreTokens()) {
			String part = s.nextToken();
			int leTransformed = Integer.parseInt(part);
			int le = (leTransformed - 100) / 3;
			if (le >= 0 && le < this.meinKollegium.anzahlLehrer()
					&& ((leTransformed - 100) % 3) == 0) // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			{
				this.meineEltern.getFall(this.meineEltern.getActNumberEltern())
						.addGewollt(le);
			} else {
				JOptionPane.showMessageDialog(null, "Falsche Eingabe: "
						+ leTransformed, "Achtung", JOptionPane.ERROR_MESSAGE);
			}
		}

		String[] out;
		out = this.meineEltern.getFall(this.meineEltern.getActNumberEltern())
				.getAlleLehrerListe(this.meinKollegium);

		this.KollegenAusgabeList.setListData(out);

	}

	private void geheZumFall(ActionEvent e) {
		int index = this.fallDatenCombo.getSelectedIndex();
		this.meineEltern.setEltern(index);
		updateData();
		// this.taktTable.setModel(new
		// ElternTaktTableModel(this.meineEltern.getActFall()));
	}

	private GridBagConstraints makegbc(int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(1, 1, 1, 1);
		return gbc;
	}

}
