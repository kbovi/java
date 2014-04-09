package anwendung;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import lehrer.Kollegium;
import lehrer.KollegiumApp;
import lizenz.ESTReadLicence;
import schule.Schule;
import texter.Statistic;
import tools.Beobachter;
import tools.Logbuch;
import tools.SerialSchuleFilter;
import tools.TextFileFilter;
import algorithmus.Algorithmus;
import ausgabe.AusgabeGenerator;
import ausgabe.DruckAPP;
import eltern.Eltern;
import eltern.ElternApp;
import eltern.ElternKontrollApp;
import estKonstanten.ESTEigenschaften;
import estKonstanten.PropsApp;

/**
 * Version 5.1
 * <p>
 * April 2013
 */
public class ESTApp extends javax.swing.JFrame {

	/**
     *
     */
	private static final long serialVersionUID = -7016100318000307895L;

	private static final String version = "Version 5.3 ( Bv / Ig )\n November 2013";

	private int state;

	private JMenuBar menuBar;

	private JMenu prefsMenu;
	private JMenuItem prefsMenuItem;
	private JSeparator toolsSeparator;
	private JMenuItem exitMenuItem;

	private JMenu loadMenu;
	private JMenuItem loadMenuItem;
	private JSeparator loadSeparator1;
	private JMenuItem loadLehrerMenuItemTxt;
	private JMenuItem loadElternMenuItemTxt;

	private JMenu lehrerMenu;
	private JMenuItem editLehrerMenuItem;
	private JMenuItem freeAllTeachersItem;

	private JMenu elternMenu;
	private JMenuItem showElternMenuItem;
	private JMenuItem editElternMenuItem;
	private JMenuItem crunchElternMenuItem;
	private JMenuItem klassenDetailsMenuItem;

	private JMenu saveMenu;
	private JMenuItem saveMenuItem;

	private JMenu toolsMenu;
	private JMenuItem algoMenuItem;
	private JMenuItem showLogMenuItem;
	private JMenuItem showStatMenuItem;
	private JMenuItem printMenuItem;

	private JMenu helpMenu;
	private JMenuItem helpMenuItem;
	private JMenuItem strukturItem;

	private JLabel TitelZeile;
	private JLabel TitelBild;

	private Logbuch myLogbuch;
	private Schule mySchule;

	private Kollegium myKollegium;
	private Eltern myEltern;
	private Boolean vorAlgo;

	private Boolean hasLicence;

	private Beobachter derBeobachter;

	private JFileChooser dateiElternAuswahlTxtIn; // für das Einlesen der
													// ElternTXT
	private JFileChooser dateiLehrerAuswahlTxtIn; // für das Einlesen der
													// LehrerTXT
	private JFileChooser dateiSchuleAuswahlSerIn; // für das Einlesen der Schule
	private JFileChooser dateiSchuleAuswahlSerOut; // für das Serialisieren der
													// Schule

	private TextFileFilter textFileFilter;
	private SerialSchuleFilter serialSchuleFileFilter;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		ESTApp frame = new ESTApp();

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

	private String getLizenz() {
		ESTReadLicence erl = new ESTReadLicence();
		String lizenz = erl.getLicence();
		if (lizenz.equals("")) {
			this.hasLicence = false;
			lizenz = "keine Lizenz!";
		} else {
			this.hasLicence = true;
		}
		return lizenz;
	}

	private void setzeTitel(String dateiname) {
		setTitle("Elternsprchtag : " + dateiname);
	}

	public ESTApp() {
		super();

		ESTEigenschaften.init();
		this.myLogbuch = Logbuch.getLogbuch();
		this.myLogbuch.open("GUI startet!");

		setzeTitel("leer");
		String lizenztext = getLizenz();
		initGUI();

		{
			TitelZeile = new JLabel();
			TitelZeile.setFont(new java.awt.Font("Dialog", 1, 20));
			TitelZeile.setBorder(BorderFactory.createEtchedBorder());

			// TitelZeile.setText(ESTEigenschaften.getSchulname());

			TitelZeile.setText(lizenztext);

			TitelZeile.setVerticalTextPosition(SwingConstants.TOP);
			TitelZeile.setHorizontalAlignment(JLabel.CENTER);

			getContentPane().add(TitelZeile, BorderLayout.SOUTH);
		}

		{
			TitelBild = new JLabel();
			TitelBild.setBorder(BorderFactory.createEtchedBorder());
			TitelBild.setIcon(new ImageIcon("EST.JPG"));
			TitelBild.setHorizontalAlignment(JLabel.CENTER);

			getContentPane().add(TitelBild, BorderLayout.CENTER);
		}
	}

	private void initGUI() {

		this.derBeobachter = Beobachter.getBeobachter();
		this.derBeobachter.resetSomethingChanged();

		this.state = 0;

		this.vorAlgo = true;

		serialSchuleFileFilter = new SerialSchuleFilter();
		// serialLehrerFileFilter = new SerialLehrerFilter ();
		// serialElternFileFilter = new SerialElternFilter ();
		textFileFilter = new TextFileFilter();
		dateiElternAuswahlTxtIn = new JFileChooser();
		dateiLehrerAuswahlTxtIn = new JFileChooser();

		// dateiElternAuswahlSerIn = new JFileChooser();
		// dateiLehrerAuswahlSerIn = new JFileChooser();

		this.dateiElternAuswahlTxtIn
				.addChoosableFileFilter(this.textFileFilter);
		this.dateiElternAuswahlTxtIn.setFileFilter(this.textFileFilter);

		this.dateiLehrerAuswahlTxtIn
				.addChoosableFileFilter(this.textFileFilter);
		this.dateiLehrerAuswahlTxtIn.setFileFilter(this.textFileFilter);

		// this.dateiElternAuswahlSerIn.addChoosableFileFilter(this.serialElternFileFilter);
		// this.dateiElternAuswahlSerIn.setFileFilter(this.serialElternFileFilter);

		// this.dateiLehrerAuswahlSerIn.addChoosableFileFilter(this.serialLehrerFileFilter);
		// this.dateiLehrerAuswahlSerIn.setFileFilter(this.serialLehrerFileFilter);

		this.dateiSchuleAuswahlSerOut = new JFileChooser();
		this.dateiSchuleAuswahlSerOut
				.addChoosableFileFilter(this.serialSchuleFileFilter);

		this.dateiSchuleAuswahlSerIn = new JFileChooser();
		this.dateiSchuleAuswahlSerIn
				.addChoosableFileFilter(this.serialSchuleFileFilter);

		try {
			setSize(600, 300);
			{
				menuBar = new JMenuBar();
				setJMenuBar(menuBar);
				{
					{
						toolsSeparator = new JSeparator();
						loadSeparator1 = new JSeparator();
						// loadSeparator2 = new JSeparator ();
					}
				}
				{
					prefsMenu = new JMenu();
					menuBar.add(prefsMenu);
					prefsMenu.setText("Programm");
					{
						prefsMenuItem = new JMenuItem();
						prefsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openProps();
							}
						});
						prefsMenu.add(prefsMenuItem);
						prefsMenuItem.setText("Einstellungen");
					}
					prefsMenu.add(toolsSeparator);
					{
						exitMenuItem = new JMenuItem();
						exitMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								beenden();
							}
						});
						prefsMenu.add(exitMenuItem);
						exitMenuItem.setText("beenden");
					}

				}

				loadMenu = new JMenu();
				menuBar.add(loadMenu);
				loadMenu.setText("Laden");
				{
					loadMenuItem = new JMenuItem();
					loadMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							ladeSchule();
						}
					});
					loadMenu.add(loadMenuItem);
					loadMenuItem.setText("Lade Gesamtdaten");
				}
				loadMenu.add(loadSeparator1);
				{
					loadLehrerMenuItemTxt = new JMenuItem();
					loadLehrerMenuItemTxt
							.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									ladeLehrerTXT();
								}
							});
					loadMenu.add(loadLehrerMenuItemTxt);
					loadLehrerMenuItemTxt.setText("Lade Lehrer aus Text-Datei");
				}
				{
					loadElternMenuItemTxt = new JMenuItem();
					loadElternMenuItemTxt
							.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									ladeElternTXT();
								}
							});
					loadMenu.add(loadElternMenuItemTxt);
					loadElternMenuItemTxt.setText("Lade Eltern aus Text-Datei");
				}

				saveMenu = new JMenu();
				menuBar.add(saveMenu);
				saveMenu.setText("Speichern");
				{
					saveMenuItem = new JMenuItem();
					saveMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							saveSchule();
						}
					});
					saveMenu.add(saveMenuItem);
					saveMenuItem.setText("Speichere Gesamtdaten");
					saveMenuItem.setEnabled(false);
				}

				{
					lehrerMenu = new JMenu();
					menuBar.add(lehrerMenu);
					lehrerMenu.setText("Lehrer");
					{
						editLehrerMenuItem = new JMenuItem();
						editLehrerMenuItem
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										editLehrer();
									}
								});

						freeAllTeachersItem = new JMenuItem();
						freeAllTeachersItem
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										freeTeachers();
									}
								});

						lehrerMenu.add(this.freeAllTeachersItem);
						freeAllTeachersItem.setText("Init");
						freeAllTeachersItem.setEnabled(false);

						lehrerMenu.add(editLehrerMenuItem);
						editLehrerMenuItem.setText("Bearbeiten");
						editLehrerMenuItem.setEnabled(false);
					}
				}
				{
					elternMenu = new JMenu();
					menuBar.add(elternMenu);
					elternMenu.setText("Eltern");
					{
						showElternMenuItem = new JMenuItem();
						showElternMenuItem
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										showEltern();
									}
								});
						elternMenu.add(showElternMenuItem);
						showElternMenuItem.setText("Zeigen");
						showElternMenuItem.setEnabled(false);
					}
					{
						editElternMenuItem = new JMenuItem();
						editElternMenuItem
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										editEltern();
									}
								});
						elternMenu.add(editElternMenuItem);
						editElternMenuItem.setText("Bearbeiten");
						editElternMenuItem.setEnabled(false);
					}
					{
						crunchElternMenuItem = new JMenuItem();
						crunchElternMenuItem
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										crunchEltern();
									}
								});
						elternMenu.add(crunchElternMenuItem);
						crunchElternMenuItem.setText("Aufr\u00e4umen");
						crunchElternMenuItem.setEnabled(false);
					}
					{
						klassenDetailsMenuItem = new JMenuItem();
						klassenDetailsMenuItem
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										klassenStatistik();
									}
								});
						elternMenu.add(klassenDetailsMenuItem);
						klassenDetailsMenuItem
								.setText("Sch\u00fcler pro Klasse?");
						klassenDetailsMenuItem.setEnabled(false);
					}
				}

				toolsMenu = new JMenu();
				menuBar.add(toolsMenu);
				toolsMenu.setText("Aktionen");
				{
					algoMenuItem = new JMenuItem();
					algoMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							algoAusfuehren();
						}
					});
					toolsMenu.add(algoMenuItem);
					algoMenuItem.setText("Zuordnung");
					algoMenuItem.setEnabled(false);
				}
				{
					showLogMenuItem = new JMenuItem();
					showLogMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							openLogbuch();
						}
					});
					toolsMenu.add(showLogMenuItem);
					showLogMenuItem.setText("Zeige Logbuch");
					showLogMenuItem.setEnabled(false);
				}
				{
					showStatMenuItem = new JMenuItem();
					showStatMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							zeigeStatistic();
						}
					});
					toolsMenu.add(showStatMenuItem);
					showStatMenuItem.setText("Zeige Statistik");
					showStatMenuItem.setEnabled(false);
				}
				{
					printMenuItem = new JMenuItem();
					printMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							printDialog();
						}
					});
					toolsMenu.add(printMenuItem);
					printMenuItem.setText("Druckdialog");
					printMenuItem.setEnabled(false);
				}
				{
					helpMenu = new JMenu();
					menuBar.add(helpMenu);
					helpMenu.setText("Help");

					{
						helpMenuItem = new JMenuItem();
						helpMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								ueber();
							}
						});
						helpMenu.add(helpMenuItem);
						helpMenuItem.setText("EST-Info");
					}

					{
						strukturItem = new JMenuItem();
						strukturItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								strukturHelp();
							}
						});
						helpMenu.add(strukturItem);
						strukturItem.setText("Hilfe zur Importstruktur");
					}
				}
				updateState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			beenden();
		}
	}

	private void beenden() {
		if (this.derBeobachter.somethingHasChanged()) {
			this.saveTheSchool();
		}
		this.myLogbuch.close("GUI beendet!");
		this.derBeobachter.save();
		System.exit(0);
	}

	private void openProps() {
		new PropsApp(this); // , TitelZeile);
	}

	private void openLogbuch() {
		if (this.showLogMenuItem.isEnabled()) {
			myLogbuch.oeffneDich();
		}
	}

	private void zeigeStatistic() {
		if (this.showStatMenuItem.isEnabled()) {
			Statistic s = Statistic.getStatistic();
			s.oeffneDich();
		}
	}

	private void algoAusfuehren() {
		if (this.derBeobachter.somethingHasChanged()) {
			JOptionPane.showMessageDialog(this, "Bitte vorher Daten sichern!",
					"Achtung", JOptionPane.ERROR_MESSAGE);
			return;
		}

		new Algorithmus(this.myKollegium, this.myEltern, this);
		this.state = 4;
		updateState();
		this.vorAlgo = false;
		this.derBeobachter.somethingChanged();
	}

	private void printDialog() {
		if (!this.hasLicence) {
			JOptionPane.showMessageDialog(this,
					"Kein Drucken möglich, da unlizensierte Version!",
					"Achtung", JOptionPane.ERROR_MESSAGE);
			return;
		} else {

			if (this.printMenuItem.isEnabled()) {
				new DruckAPP(this, new AusgabeGenerator(this.myEltern,
						this.myKollegium), this.algoMenuItem.isEnabled());
			}
		}
	}

	private void ladeSchule() {
		this.myLogbuch.log("LadeSchuleButton", 10);

		dateiSchuleAuswahlSerIn.setDialogTitle("Schuldaten laden");
		if (this.derBeobachter.getLastDatenDir().equals("")) {
			dateiSchuleAuswahlSerIn.setCurrentDirectory(new File(System
					.getProperty("user.dir")));
		} else {
			dateiSchuleAuswahlSerIn.setCurrentDirectory(new File(
					this.derBeobachter.getLastDatenDir()));
		}
		int returnVal = dateiSchuleAuswahlSerIn.showOpenDialog(null);

		if (returnVal == JFileChooser.CANCEL_OPTION) {
			this.myLogbuch.log("Nix gemacht", 10);
		} else {
			File f = dateiSchuleAuswahlSerIn.getSelectedFile();
			String fileName = f.getPath();
			this.derBeobachter.setLastDatenDir(fileName);
			this.myLogbuch.log("SchulDateiname: " + fileName, 5);

			Logbuch l = Logbuch.getLogbuch();
			l.log("SchuleContructor", 10);

			String dateikennung = ESTEigenschaften.schuleDateiKennung();
			if (fileName.endsWith(dateikennung)) {
				try {
					FileInputStream fs = new FileInputStream(fileName);
					ObjectInputStream is = new ObjectInputStream(fs);

					this.mySchule = (Schule) is.readObject();
					is.close();

					this.myEltern = this.mySchule.getMyEltern();
					this.myKollegium = this.mySchule.getMyKollegen();
					this.vorAlgo = this.mySchule.getVorAlgo();

					l.log("Schule Eingelesen!", 0);

					this.setzeTitel(fileName);
				} catch (ClassNotFoundException e) {
					l.log(e.toString(), 0);
				} catch (IOException e) {
					l.log(e.toString(), 0);
				}
			}// then-Zweig
			else {
				l.log("Falscher Dateiname beim Einlesen der Schule!", 0);
			}

			// Schule mySchule = new Schule (fileName);
			// this.myEltern = mySchule.getMyEltern();
			// this.myKollegium = mySchule.getMyKollegen();

			if (this.vorAlgo) {
				this.state = 3;
			} else {
				this.state = 4;
			}
			this.derBeobachter.resetSomethingChanged();
			updateState();
		}

	}

	private void ladeElternTXT() {
		this.myLogbuch.log("LadeElternTxt", 10);
		dateiElternAuswahlTxtIn
				.setDialogTitle("Elterndaten aus Textdatei laden");

		if (this.derBeobachter.getLastDatenDir().equals("")) {
			dateiElternAuswahlTxtIn.setCurrentDirectory(new File(System
					.getProperty("user.dir")));
		} else {
			dateiElternAuswahlTxtIn.setCurrentDirectory(new File(
					this.derBeobachter.getLastDatenDir()));
		}
		int returnVal = dateiElternAuswahlTxtIn.showOpenDialog(null);
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			this.myLogbuch.log("Nix gemacht", 10);
		} else {
			File f = dateiElternAuswahlTxtIn.getSelectedFile();
			String fileName = f.getPath();
			this.derBeobachter.setLastDatenDir(fileName);
			this.myLogbuch.log("TxtElternDateiname: " + fileName, 5);
			this.myEltern = new Eltern(fileName);
			if (this.state == 1) {
				this.state = 3;
			} else {
				this.state = 2;
			}
			updateState();
		}
	}

	private void ladeLehrerTXT() {
		this.myLogbuch.log("ladeLehrerButton", 10);

		dateiLehrerAuswahlTxtIn
				.setDialogTitle("Lehrerdaten aus Textdatei laden");

		if (this.derBeobachter.getLastDatenDir().equals("")) {
			dateiLehrerAuswahlTxtIn.setCurrentDirectory(new File(System
					.getProperty("user.dir")));
		} else {
			dateiLehrerAuswahlTxtIn.setCurrentDirectory(new File(
					this.derBeobachter.getLastDatenDir()));
		}

		int returnVal = dateiLehrerAuswahlTxtIn.showOpenDialog(this);
		if (returnVal == JFileChooser.CANCEL_OPTION) {
			this.myLogbuch.log("Nix gemacht", 5);
		} else {
			File f = dateiLehrerAuswahlTxtIn.getSelectedFile();
			String fileName = f.getPath();
			this.derBeobachter.setLastDatenDir(fileName);
			this.myLogbuch.log("LehrerDateiname: " + fileName, 5);
			this.myKollegium = new Kollegium(fileName);

			if (this.state == 2) {
				this.state = 3;
			} else {
				this.state = 1;
			}
			updateState();
		}
	}

	private void saveSchule() {
		if (this.saveMenuItem.isEnabled()) {
			this.saveTheSchool();
		}
	}

	private void saveTheSchool() {
		if (this.myKollegium != null && this.myEltern != null) {
			if (this.derBeobachter.getLastDatenDir().equals("")) {
				dateiSchuleAuswahlSerOut.setCurrentDirectory(new File(System
						.getProperty("user.dir")));
			} else {
				dateiSchuleAuswahlSerOut.setCurrentDirectory(new File(
						this.derBeobachter.getLastDatenDir()));
			}

			dateiSchuleAuswahlSerOut.setDialogTitle("Schuldaten sichern");
			int ret = dateiSchuleAuswahlSerOut.showSaveDialog(this);

			if (ret == JFileChooser.APPROVE_OPTION) {
				File f = dateiSchuleAuswahlSerOut.getSelectedFile();
				if (f != null && !f.isDirectory()) {
					String fileName = f.getPath();
					this.derBeobachter.setLastDatenDir(fileName);
					// String s = f.getPath();
					String suff = null;
					int i = fileName.lastIndexOf('.');
					if (i > 0 && i < fileName.length() - 1) {
						suff = fileName.substring(i + 1).toLowerCase();
					}
					if (suff == null) {
						fileName = fileName + "."
								+ ESTEigenschaften.schuleDateiKennung();
					}
					this.myLogbuch.log("SchulAusgabe --> " + fileName, 5);
					this.mySchule = new Schule(this.myEltern, this.myKollegium,
							this.vorAlgo);
					this.myLogbuch.log("Schule serialisieren", 5);
					try {
						FileOutputStream fs = new FileOutputStream(fileName);
						ObjectOutputStream os = new ObjectOutputStream(fs);
						os.writeObject(this.mySchule);
						os.close();
						this.derBeobachter.resetSomethingChanged();
						this.setzeTitel(fileName);
					} catch (IOException ioe) {
						this.myLogbuch.log(ioe.toString(), 0);
					}
				}
			} else {
				this.myLogbuch.log("Sicherung abgebrochen", 5);
			}
		}
	}

	private void editLehrer() {
		if (this.editLehrerMenuItem.isEnabled()
				&& this.myKollegium.anzahlLehrer() > 0) {
			new KollegiumApp(this, this.myKollegium, this.myEltern,
					this.vorAlgo);
			// this.derBeobachter.somethingChanged();
		}

	}

	private void freeTeachers() {
		if (this.freeAllTeachersItem.isEnabled()
				&& this.myKollegium.anzahlLehrer() > 0
				&& (JOptionPane
						.showConfirmDialog(
								this,
								"Sollen nun wirklich alle Lehrer\n initialisiert werden?",
								"Lehrer initialisieren",
								JOptionPane.YES_NO_OPTION) == 0)) {
			this.myKollegium.makeAllTeachersAlwaysFree();
			this.derBeobachter.somethingChanged();
		}

	}

	private void editEltern() {
		if (this.editElternMenuItem.isEnabled()
				&& this.myEltern.getAnzahlEltern() > 0) {
			new ElternApp(this, this.myKollegium, this.myEltern, this.vorAlgo);
			// this.derBeobachter.somethingChanged();
		}
	}

	private void showEltern() {
		if (this.editElternMenuItem.isEnabled()
				&& this.myEltern.getAnzahlEltern() > 0) {
			new ElternKontrollApp(this.myEltern);
			// this.derBeobachter.somethingChanged();
		}
	}

	private void crunchEltern() {
		if (this.derBeobachter.somethingHasChanged()) {
			JOptionPane.showMessageDialog(this, "Bitte vorher Daten sichern!",
					"Achtung", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (this.crunchElternMenuItem.isEnabled()
				&& JOptionPane
						.showConfirmDialog(
								this,
								"Sollen nun wirklich alle Eltern entfernt werden,\n die keine Lehrer gew\u00FCnscht haben?",
								"Eltern entfernen", JOptionPane.YES_NO_OPTION) == 0) {
			myEltern.loescheLeereEltern();
			this.derBeobachter.somethingChanged();
		}
	}

	private void updateState() {
		if (!this.hasLicence) {
			prefsMenuItem.setEnabled(true);
			exitMenuItem.setEnabled(false);

			loadMenuItem.setEnabled(false);
			loadLehrerMenuItemTxt.setEnabled(false);
			loadElternMenuItemTxt.setEnabled(false);

			editLehrerMenuItem.setEnabled(false);
			this.freeAllTeachersItem.setEnabled(false);

			editElternMenuItem.setEnabled(false);
			showElternMenuItem.setEnabled(false);
			crunchElternMenuItem.setEnabled(false);
			klassenDetailsMenuItem.setEnabled(false);

			saveMenuItem.setEnabled(false);

			algoMenuItem.setEnabled(false);
			showLogMenuItem.setEnabled(true);
			showStatMenuItem.setEnabled(false);
			printMenuItem.setEnabled(false);

			helpMenuItem.setEnabled(true);
			strukturItem.setEnabled(true);

			return;
		}

		switch (this.state) {
		case 0: // Anfang
		{
			prefsMenuItem.setEnabled(true);
			exitMenuItem.setEnabled(true);

			loadMenuItem.setEnabled(true);
			loadLehrerMenuItemTxt.setEnabled(true);
			loadElternMenuItemTxt.setEnabled(true);

			editLehrerMenuItem.setEnabled(false);
			this.freeAllTeachersItem.setEnabled(false);

			editElternMenuItem.setEnabled(false);
			showElternMenuItem.setEnabled(false);
			crunchElternMenuItem.setEnabled(false);
			klassenDetailsMenuItem.setEnabled(false);

			saveMenuItem.setEnabled(false);

			algoMenuItem.setEnabled(false);
			showLogMenuItem.setEnabled(true);
			showStatMenuItem.setEnabled(false);
			printMenuItem.setEnabled(false);

			helpMenuItem.setEnabled(true);
			strukturItem.setEnabled(true);

			break;

		}
		case 1: // Nur Lehrer geladen
		{
			prefsMenuItem.setEnabled(true);
			exitMenuItem.setEnabled(true);

			loadMenuItem.setEnabled(true);
			loadLehrerMenuItemTxt.setEnabled(true);
			loadElternMenuItemTxt.setEnabled(true);

			editLehrerMenuItem.setEnabled(true);
			this.freeAllTeachersItem.setEnabled(true);

			editElternMenuItem.setEnabled(false);
			showElternMenuItem.setEnabled(false);
			crunchElternMenuItem.setEnabled(false);
			klassenDetailsMenuItem.setEnabled(false);

			saveMenuItem.setEnabled(false);

			algoMenuItem.setEnabled(false);
			showLogMenuItem.setEnabled(true);
			showStatMenuItem.setEnabled(false);
			printMenuItem.setEnabled(false);

			helpMenuItem.setEnabled(true);
			strukturItem.setEnabled(true);

			break;

		}
		case 2: // Nur Eltern geladen
		{
			prefsMenuItem.setEnabled(true);
			exitMenuItem.setEnabled(true);

			loadMenuItem.setEnabled(true);
			loadLehrerMenuItemTxt.setEnabled(true);
			loadElternMenuItemTxt.setEnabled(true);

			editLehrerMenuItem.setEnabled(false);
			this.freeAllTeachersItem.setEnabled(false);

			editElternMenuItem.setEnabled(false);
			showElternMenuItem.setEnabled(true);
			crunchElternMenuItem.setEnabled(false);
			klassenDetailsMenuItem.setEnabled(false);

			saveMenuItem.setEnabled(false);

			algoMenuItem.setEnabled(false);
			showLogMenuItem.setEnabled(true);
			showStatMenuItem.setEnabled(false);
			printMenuItem.setEnabled(false);

			helpMenuItem.setEnabled(true);
			strukturItem.setEnabled(true);

			break;

		}
		case 3: // Lehrer und Eltern geladen; vor Algo
		{
			prefsMenuItem.setEnabled(true);
			exitMenuItem.setEnabled(true);

			loadMenuItem.setEnabled(true);
			loadLehrerMenuItemTxt.setEnabled(true);
			loadElternMenuItemTxt.setEnabled(true);

			editLehrerMenuItem.setEnabled(true);
			this.freeAllTeachersItem.setEnabled(true);

			editElternMenuItem.setEnabled(true);
			showElternMenuItem.setEnabled(true);
			crunchElternMenuItem.setEnabled(true);
			klassenDetailsMenuItem.setEnabled(true);

			saveMenuItem.setEnabled(true);

			algoMenuItem.setEnabled(true);
			showLogMenuItem.setEnabled(true);
			showStatMenuItem.setEnabled(false);
			printMenuItem.setEnabled(true);

			helpMenuItem.setEnabled(true);
			strukturItem.setEnabled(true);

			break;

		}
		case 4: // Nach Algo
		{
			prefsMenuItem.setEnabled(true);
			exitMenuItem.setEnabled(true);

			loadMenuItem.setEnabled(true);
			loadLehrerMenuItemTxt.setEnabled(true);
			loadElternMenuItemTxt.setEnabled(true);

			editLehrerMenuItem.setEnabled(true);
			this.freeAllTeachersItem.setEnabled(true);

			editElternMenuItem.setEnabled(true);
			showElternMenuItem.setEnabled(true);
			crunchElternMenuItem.setEnabled(false);
			klassenDetailsMenuItem.setEnabled(true);

			saveMenuItem.setEnabled(true);

			algoMenuItem.setEnabled(false);
			showLogMenuItem.setEnabled(true);
			showStatMenuItem.setEnabled(true);
			printMenuItem.setEnabled(true);

			helpMenuItem.setEnabled(true);
			strukturItem.setEnabled(true);

			break;
		}
		}

	}

	private void writeKlassenstaerkenStatistic() {
		Statistic s = Statistic.getStatistic();
		s.initStatistic(this.myKollegium, this.myEltern);
		s.KlassenstaerkenStatistik();
		s.close();
	}

	private void klassenStatistik() {
		writeKlassenstaerkenStatistic();
		Statistic s = Statistic.getStatistic();
		s.oeffneDich();
	}

	private void strukturHelp() {
		JOptionPane
				.showMessageDialog(
						this,
						"[Lehrer: Name, Krz, mw] [Schueler: Name; Vorname; Klasse; Adresse]",
						"\u00dcber den Import", JOptionPane.INFORMATION_MESSAGE);
	}

	private void ueber() {
		JOptionPane.showMessageDialog(this, version, "\u00dcber EST",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
