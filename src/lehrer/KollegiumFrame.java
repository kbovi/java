package lehrer;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import takter.Takter;
import tools.Beobachter;
import eltern.Eltern;
import estKonstanten.ESTEigenschaften;

/**
 * @author Administrator
 */
public class KollegiumFrame extends JDialog {
	private static final long serialVersionUID = 3617574920540926007L;
	private Kollegium meinKollegium;
	private Eltern meineEltern;
	private Boolean vorAlgo;

	private Beobachter derBeobachter;

	private JList TaktAusgabeList;

	private JPanel contentPane;

	public static final int WidthOfFrame = 650; // width of frame
	public static final int HeightOfFrame = 400; // height of frame

	private String[] Lehrernamen;

	private JTable myTable;
	private LehrerTaktTableModel actModel;

	private GridBagLayout gbl;
	private GridBagConstraints gbc;

	private JPanel raumeingabePane = new JPanel(); // Panel fuer die linke Seite
													// des Frames

	private JComboBox LehrerDatenLabel;

	private JLabel RaumeingabeLabel = new JLabel();
	private JLabel Hinweis1;
	private JLabel Hinweis2;

	private JLabel SexName = new JLabel();

	private JButton LehrerIncButton = new JButton();
	private JButton LehrerDecButton = new JButton();
	private JButton HakenRaumButton = new JButton();
	private JButton ChangeSexButton = new JButton();

	private JButton Taktfolge1 = new JButton("Taktfolge 1 eintragen");
	private JButton Taktfolge2 = new JButton("Taktfolge 2 eintragen");

	private JButton selektionAbwesend = new JButton();
	private JButton selektionBelegbar = new JButton();
	private JButton selektionBesuchbar = new JButton();

	private JTextField RaumEingabe = new JTextField();

	public KollegiumFrame(JFrame owner, Kollegium k, Eltern el, Boolean vorAlgo) {
		super(owner, "Lehrer bearbeiten", true);
		this.meinKollegium = k;
		this.meineEltern = el;
		this.meinKollegium.reset();

		this.vorAlgo = vorAlgo;

		this.derBeobachter = Beobachter.getBeobachter();

		this.Lehrernamen = new String[this.meinKollegium.anzahlLehrer()];
		for (int i = 0; i < this.meinKollegium.anzahlLehrer(); i++) {
			this.Lehrernamen[i] = this.meinKollegium.getLehrer(i)
					.comboNameWithoutSex();
		}

		LehrerDatenLabel = new JComboBox(this.Lehrernamen);

		this.setResizable(false);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

	}

	/** Component initialization */
	private void jbInit() throws Exception {
		String zusatz;
		if (this.vorAlgo) {
			zusatz = "bearbeiten";
		} else {
			zusatz = "ansehen (nach Zuordnung)";
		}
		this.setTitle("Lehrerdaten " + zusatz);

		gbl = new GridBagLayout();
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(gbl);

		{// Lehrer-Sex-label Zeile 0 Spalten 0
			// SexName.setForeground(Color.gray);
		// SexName.setFont(new java.awt.Font("DialogInput", 1, 14));
			SexName.setHorizontalAlignment(JLabel.RIGHT);
			gbc = makegbc(0, 0, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(SexName, gbc);
			contentPane.add(SexName);
		}

		{// Lehrer-Namen-Auswahl-Button Zeile 0 Spalten 1 2
			LehrerDatenLabel.setForeground(Color.red);
			LehrerDatenLabel.setFont(new java.awt.Font("DialogInput", 1, 14));
			LehrerDatenLabel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					geheZumLehrer(e);
				}
			});
			gbc = makegbc(1, 0, 2, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(LehrerDatenLabel, gbc);
			contentPane.add(LehrerDatenLabel);
		}

		{// Nächster Fall Zeile 1 Spalten 2
			LehrerIncButton.setFont(new java.awt.Font("Dialog", 1, 14));
			LehrerIncButton.setToolTipText("Increase");
			LehrerIncButton.setText("N\u00E4chster Lehrer");
			LehrerIncButton.setBounds(new Rectangle(14, 83, 118, 41));
			LehrerIncButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					LehrerIncButton_mouseClicked(e);
				}
			});
			gbc = makegbc(2, 1, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(LehrerIncButton, gbc);
			contentPane.add(LehrerIncButton);
		}

		{// Change sex Zeile 1 Spalten 0
			ChangeSexButton.setFont(new java.awt.Font("Dialog", 1, 14));
			ChangeSexButton.setToolTipText("Sex \u00e4ndern");
			ChangeSexButton.setText("switch Sex");
			// ChangeSexButton.setBounds(new Rectangle(14, 83, 118, 41));
			ChangeSexButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					ChangeSexButton_mouseClicked(e);
				}
			});
			gbc = makegbc(0, 1, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(ChangeSexButton, gbc);
			contentPane.add(ChangeSexButton);
		}

		{// Voriger Fall Zeile 1 Spalten 1
			LehrerDecButton.setFont(new java.awt.Font("Dialog", 1, 14));
			LehrerDecButton.setToolTipText("Decrease");
			LehrerDecButton.setText("Voriger Lehrer");
			LehrerDecButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					LehrerDecButton_mouseClicked(e);
				}
			});
			gbc = makegbc(1, 1, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(LehrerDecButton, gbc);
			contentPane.add(LehrerDecButton);
		}

		{// Raumeingabe Zeile 2 Spalten 0 1 2
			raumeingabePane.setLayout(new BorderLayout());
			raumeingabePane.setBorder(BorderFactory.createRaisedBevelBorder());
			raumeingabePane.setLayout(new BorderLayout(10, 10));

			HakenRaumButton.setText("Raum zuweisen");
			HakenRaumButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					HakenRaumButton_mouseClicked(e);
				}
			});

			RaumEingabe.setBorder(BorderFactory.createLoweredBevelBorder());

			KeyListener keyListener = new KeyListener() {
				public void keyPressed(KeyEvent keyEvent) {
					if (keyEvent.getKeyCode() == 10) {
						RaumEingabe_actionPerformed(null);
					}
				}

				public void keyReleased(KeyEvent keyEvent) {
				}

				public void keyTyped(KeyEvent keyEvent) {
				}
			};

			RaumEingabe.addKeyListener(keyListener);

			/*
			 * RaumEingabe.addActionListener(new java.awt.event.ActionListener()
			 * { public void actionPerformed(ActionEvent e) {
			 * RaumEingabe_actionPerformed(e); } });
			 */
			RaumeingabeLabel.setText("Raum");
			RaumEingabe.setText(this.meinKollegium.actLehrer().getRaum());

			raumeingabePane.add("West", RaumeingabeLabel);
			raumeingabePane.add("Center", RaumEingabe);
			raumeingabePane.add("East", HakenRaumButton);

			gbc = makegbc(0, 2, 3, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(raumeingabePane, gbc);
			contentPane.add(raumeingabePane);
		}

		{// tabelle mit Takten Zeile 3 Spalten 0 1 2

			Lehrer actTeacher = this.meinKollegium.actLehrer();

			// Einrichten der tablePane:
			actModel = new LehrerTaktTableModel(actTeacher, this.meineEltern);// mach
																				// eine
																				// Tabelle
																				// aus
																				// den
																				// Lehrerdaten
			myTable = new JTable(actModel);
			myTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			JScrollPane kt = new JScrollPane(myTable);
			gbc = makegbc(0, 3, 3, 1);
			gbc.weightx = 100;
			gbc.weighty = 100;
			gbc.fill = GridBagConstraints.BOTH;
			gbl.setConstraints(kt, gbc);
			contentPane.add(kt);
		}

		{// LehrerBelegbarButton Zeile 4 Spalten 0
			selektionBelegbar.setFont(new java.awt.Font("Dialog", 1, 12));
			selektionBelegbar.setToolTipText("Markierte Takte belegbar");
			selektionBelegbar.setText("Markierte Takte von EST org.");
			selektionBelegbar
					.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							selektionBelegbar_mouseClicked(e);
						}
					});
			gbc = makegbc(0, 4, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(selektionBelegbar, gbc);
			contentPane.add(selektionBelegbar);
			selektionBelegbar.setEnabled(this.vorAlgo);

		}

		{// LehrerBesuchbarButton Zeile 4 Spalten 1
			selektionBesuchbar.setFont(new java.awt.Font("Dialog", 1, 12));
			selektionBesuchbar.setToolTipText("Markierte Takte besuchbar");
			selektionBesuchbar.setText("Markierte Takte selbst org.");
			selektionBesuchbar
					.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							selektionBesuchbar_mouseClicked(e);
						}
					});
			gbc = makegbc(1, 4, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(selektionBesuchbar, gbc);
			contentPane.add(selektionBesuchbar);
			selektionBesuchbar.setEnabled(this.vorAlgo);
		}

		{// LehrerSperrenButton Zeile 4 Spalten 2
			selektionAbwesend.setFont(new java.awt.Font("Dialog", 1, 12));
			selektionAbwesend.setToolTipText("Markierte Takte abwesend");
			selektionAbwesend.setText("Markierte Takte sperren");
			selektionAbwesend
					.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							selektionAbwesend_mouseClicked(e);
						}
					});
			gbc = makegbc(2, 4, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(selektionAbwesend, gbc);
			contentPane.add(selektionAbwesend);
			selektionAbwesend.setEnabled(this.vorAlgo);
		}

		{// taktfolge1 Zeile 6 Spalten 0
			String t1Label = ESTEigenschaften.getFrueherTerminLabel();
			Taktfolge1.setText(t1Label);

			Taktfolge1.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					taktfolge1Setzen_mouseClicked(e);
				}
			});
			gbc = makegbc(0, 6, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(Taktfolge1, gbc);
			contentPane.add(Taktfolge1);
			Taktfolge1.setEnabled(this.vorAlgo);
		}

		{
			Hinweis1 = new JLabel("1. Sondertermin eintragen");
			gbc = makegbc(0, 5, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(Hinweis1, gbc);
			contentPane.add(Hinweis1);
		}

		{
			Hinweis2 = new JLabel("2. Sondertermin eintragen");
			gbc = makegbc(2, 5, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(Hinweis2, gbc);
			contentPane.add(Hinweis2);
		}

		{// Taktfolge2 Zeile 6 Spalten 1
			String t2Label = ESTEigenschaften.getSpaeterTerminLabel();
			Taktfolge2.setText(t2Label);

			Taktfolge2.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					taktfolge2Setzen_mouseClicked(e);
				}
			});
			gbc = makegbc(2, 6, 1, 1);
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(Taktfolge2, gbc);
			contentPane.add(Taktfolge2);
			Taktfolge2.setEnabled(this.vorAlgo);
		}

		{// LehrerTaktausgabe Kontrolle Zeile 7 Spalten 0 1 2
			TaktAusgabeList = new JList();
			JScrollPane ta = new JScrollPane(this.TaktAusgabeList);
			gbc = makegbc(0, 7, 3, 1);
			gbc.weightx = 0;
			gbc.weighty = 20;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbl.setConstraints(ta, gbc);
			contentPane.add(ta);
		}

		updateData();

		this.pack();
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			// System.exit(0);
		}
	}

	private void ChangeSexButton_mouseClicked(MouseEvent e) {
		this.meinKollegium.actLehrer().changeSex();

		updateData();

		this.derBeobachter.somethingChanged();
	}

	private void LehrerIncButton_mouseClicked(MouseEvent e) {
		if (this.meinKollegium.actNumberLehrer() < this.meinKollegium
				.anzahlLehrer()) {

			/*
			 * if (!this.RaumEingabe.getText().equals("") &&
			 * JOptionPane.showConfirmDialog( this,
			 * "Soll der angegebene Raum eingetragen werden?", "Raum setzen",
			 * JOptionPane.YES_NO_OPTION) == 0)
			 */
			{
				this.raumSetzen(); // ??????????????????????????????????????????????????????
			}

			this.meinKollegium.inc();

			updateData();

			// LehrerDatenLabel.setText(this.meinKollegium.actLehrer().vollerName()
			// + " Nr. " + this.meinKollegium.actNumberLehrer());
			// this.RaumEingabe.setText(this.meinKollegium.actLehrer().getRaum());
			// myTable.setModel(new
			// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
			// this.LehrerDatenLabel.setSelectedIndex(this.meinKollegium.actNumberLehrer());
		}
	}

	private void LehrerDecButton_mouseClicked(MouseEvent e) {
		if (this.meinKollegium.actNumberLehrer() > 0) {

			this.raumSetzen(); // ??????????????????????????????????????????????????????

			this.meinKollegium.dec();

			updateData();

			// LehrerDatenLabel.setText(this.meinKollegium.actLehrer().vollerName()
			// + " Nr. " + this.meinKollegium.actNumberLehrer());
			// this.RaumEingabe.setText(this.meinKollegium.actLehrer().getRaum());
			// myTable.setModel(new
			// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
			// this.LehrerDatenLabel.setSelectedIndex(this.meinKollegium.actNumberLehrer());
		}
	}

	private void RaumEingabe_actionPerformed(ActionEvent e) {
		this.raumSetzen();

		// und dann ggf. zum nächsten Lehrer!
		this.meinKollegium.inc();
		updateData();
	}

	private void raumSetzen() {
		String RaumEingabeString = this.RaumEingabe.getText();
		// int an = this.meinKollegium.actNumberLehrer();
		this.meinKollegium.actLehrer().setRaum(RaumEingabeString);
		this.derBeobachter.somethingChanged();
	}

	private void selektionBelegbar_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			int[] selektion = myTable.getSelectedRows();

			for (int i = 0; i < selektion.length; i++) {
				this.meinKollegium.actLehrer().setBelegbar(selektion[i]);
			}

			updateData();
			this.derBeobachter.somethingChanged();
		}

		// myTable.setModel(new
		// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
	}

	private void taktfolge1Setzen_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {

			for (int i = 0; i < ESTEigenschaften.getAnzahlTakte(); i++) {
				this.meinKollegium.actLehrer().setAbwesend(i);
			}

			String frueheTakte = ESTEigenschaften.getFrueherTermin();
			StringTokenizer st = new StringTokenizer(frueheTakte, ";", false);
			while (st.hasMoreTokens()) {
				String taktString = st.nextToken();
				int tnr = Integer.parseInt(taktString);
				this.meinKollegium.actLehrer().setBelegbar(tnr);
			}

			updateData();
			this.derBeobachter.somethingChanged();
		}

		// myTable.setModel(new
		// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
	}

	private void taktfolge2Setzen_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			for (int i = 0; i < ESTEigenschaften.getAnzahlTakte(); i++) {
				this.meinKollegium.actLehrer().setAbwesend(i);
			}
			String frueheTakte = ESTEigenschaften.getSpaeterTermin();
			StringTokenizer st = new StringTokenizer(frueheTakte, ";", false);
			while (st.hasMoreTokens()) {
				String taktString = st.nextToken();
				int tnr = Integer.parseInt(taktString);
				this.meinKollegium.actLehrer().setBelegbar(tnr);
			}

			updateData();
			this.derBeobachter.somethingChanged();
		}

		// myTable.setModel(new
		// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
	}

	private void selektionAbwesend_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			int[] selektion = myTable.getSelectedRows();

			for (int i = 0; i < selektion.length; i++) {
				this.meinKollegium.actLehrer().setAbwesend(selektion[i]);
			}

			updateData();
			this.derBeobachter.somethingChanged();
		}

		// myTable.setModel(new
		// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
	}

	private void selektionBesuchbar_mouseClicked(MouseEvent e) {
		if (this.vorAlgo) {
			int[] selektion = myTable.getSelectedRows();

			for (int i = 0; i < selektion.length; i++) {
				this.meinKollegium.actLehrer().setBesuchbar(selektion[i]);
			}

			updateData();
			this.derBeobachter.somethingChanged();
		}

		// myTable.setModel(new
		// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
	}

	private void HakenRaumButton_mouseClicked(MouseEvent e) {
		this.raumSetzen();
	}

	private void geheZumLehrer(ActionEvent e) {
		int lnr = this.LehrerDatenLabel.getSelectedIndex();
		this.meinKollegium.setLehrer(lnr);

		updateData();

		// LehrerDatenLabel.setText(this.meinKollegium.actLehrer().vollerName()
		// + " Nr. " + this.meinKollegium.actNumberLehrer());
		// this.RaumEingabe.setText(this.meinKollegium.actLehrer().getRaum());
		// myTable.setModel(new
		// LehrerTaktTableModel(this.meinKollegium.actLehrer()));
		// this.LehrerDatenLabel.setSelectedIndex(this.meinKollegium.actNumberLehrer());
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

	private void updateData() {
		this.RaumEingabe.setText(this.meinKollegium.actLehrer().getRaum());
		myTable.setModel(new LehrerTaktTableModel(this.meinKollegium
				.actLehrer(), this.meineEltern));
		this.LehrerDatenLabel.setSelectedIndex(this.meinKollegium
				.actNumberLehrer());

		boolean[] freie = this.meinKollegium.actLehrer().freieZeiten();
		boolean[] besuchbare = this.meinKollegium.actLehrer()
				.besuchbareZeiten();

		SexName.setText(this.meinKollegium.actLehrer().anrede());

		Takter t = new Takter();
		String[] zeilen = { "Freie Takte zur Zeit:", "",
				t.takt2StringErsterTag(freie), t.takt2StringZweiterTag(freie),
				"Besuchbare Takte zur Zeit:", "",
				t.takt2StringErsterTag(besuchbare),
				t.takt2StringZweiterTag(besuchbare) };
		this.TaktAusgabeList.setListData(zeilen);
	}

}