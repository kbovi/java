package estKonstanten;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import tools.TimePickerButton;

import com.toedter.calendar.JDateChooser;
import com.toedter.components.JSpinField;

/**
 * Eine GUI, um die EST-Properties zu lesen und zu veraendern.
 * 
 * @author Administrator
 */
public class ESTPropertiesFrame extends JDialog {

	private static final long serialVersionUID = -4197619405257080273L;

	private Properties props;

	private JPanel LabelPanel;
	private JSpinField TaktLaengeInput;
	private JLabel TaktLaengeLabel;
	private JTextField SpaetInput;
	private JPanel LuftPanel;
	private JTextField EditorInput;
	private JLabel EditorLabel;
	private JCheckBox ShuffleCheckBox;
	private JPanel Shufflepanel;
	private JLabel Shufflelabel;

	private JCheckBox TraenenCheckBox;
	private JPanel Traenenpanel;
	private JLabel Traenenlabel;

	private JSpinField LogLevelInput;
	private JLabel LogLevelLabel;
	private JSpinField MaxWunschInput;
	private JLabel MaxWunschLabel;
	private JRadioButton OhneVoroptiRadioButton;
	private JRadioButton MitVoroptiRadioButton;
	private JPanel VoroptiPanel;
	private ButtonGroup VoroptiRadioButtonGroup;
	private JRadioButton LuftNeinRadioButton;
	private JRadioButton LuftJaRadioButton;
	private ButtonGroup LuftButtonRadioGroup;
	private JLabel LuftLabel;
	private JLabel VoroptiLabel;
	private TimePickerButton Anf2Input;
	private TimePickerButton Anf1Input;
	private JLabel Anf2Label;
	private JLabel Anf1Label;
	private JTextField HalbJahrInput;
	private JLabel HalbJahrLabel;
	private JTextField FruehInput;
	private JLabel SpaetLabel;
	private JLabel FruehLabel;
	private JDateChooser Datum1Chooser;
	private JDateChooser Datum2Chooser;
	private JDateChooser BackDateChooser;
	private JLabel Datum2Label;
	private JLabel Datum1Label;
	private JLabel BackLabel;
	private JSpinField SchnittInput;
	private JLabel SchnittLabel;
	private JSpinField AnzahlTakteInput;
	private JLabel AnzahlTakteLabel;
	private JPanel InputPanel;
	private JButton saveButton;
	private JButton loadButton;
	private JTextField SchulnameInput;
	private JLabel SchulnameLabel;
	private JLabel ElternSperrLabel;
	private JTextField SperrInput;

	// private JLabel guiTitel; // um in der MainGUI den Titel updaten zu
	// können!

	/**
	 * Eine GUI, um die EST-Properties zu lesen und zu veraendern.<br>
	 * Die Titelzeile in der Main-GUI wird aktualisiert
	 * 
	 * @param titel
	 *            (Die Titelzeile in der Main-GUI wird aktualisiert)
	 */
	public ESTPropertiesFrame(JFrame owner) {// , JLabel titel) {
		super(owner, "EST-Prefernces", true);
		props = ESTEigenschaften.getProps();

		// this.guiTitel = titel;

		GridLayout thisLayout = new GridLayout(1, 2);
		thisLayout.setHgap(3);
		thisLayout.setVgap(3);
		thisLayout.setColumns(1);

		/*--------------------------
		 
		 this.addWindowListener(new java.awt.event.WindowAdapter() 
		 {
		     public void windowClosing(WindowEvent winEvt) 
		     {
		 // Perhaps ask user if they want to save any unsaved files first.

		 
		             int response = JOptionPane.showConfirmDialog (null,"Properties sichern",
		             "Wollen Sie den aktuellen Stand sichern?",
		             JOptionPane.YES_NO_CANCEL_OPTION,
		             JOptionPane.QUESTION_MESSAGE);
		             
		             
		             if (response == JOptionPane.OK_OPTION) 
		             {
		             	saveAllProps ();
		             }
		             else if (response == JOptionPane.NO_OPTION)
		             {
		                 setDefaultCloseOperation(EXIT_ON_CLOSE);
		             }
		             else
		             {
		                 setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		                 
		             }

		     }
		 });            
		 
		 // --------------------- */

		getContentPane().setLayout(thisLayout);
		getContentPane().setBackground(new java.awt.Color(214, 211, 167));
		{
			{
				LuftButtonRadioGroup = new ButtonGroup();
			}
			{
				VoroptiRadioButtonGroup = new ButtonGroup();
			}
			LabelPanel = new JPanel();
			getContentPane().add(LabelPanel);
			GridLayout LabelPanelLayout = new GridLayout(21, 1);
			LabelPanelLayout.setHgap(5);
			LabelPanelLayout.setVgap(5);
			LabelPanelLayout.setColumns(1);
			LabelPanelLayout.setRows(21);
			LabelPanel.setLayout(LabelPanelLayout);
			LabelPanel.setBackground(new java.awt.Color(214, 211, 167));
			{
				loadButton = new JButton();
				LabelPanel.add(loadButton);
				loadButton.setText("Prg.Vorgaben Laden");
				loadButton.setFont(new java.awt.Font("Times New Roman", 1, 16));
				loadButton.setBackground(new java.awt.Color(215, 235, 238));
				loadButton.setPreferredSize(new java.awt.Dimension(20, 8));
				loadButton.setSize(100, 32);
				loadButton.setHorizontalTextPosition(SwingConstants.CENTER);
				loadButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						loadButtonMouseClicked(evt);
					}
				});
			}
			{
				SchulnameLabel = new JLabel();
				LabelPanel.add(SchulnameLabel);
				SchulnameLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				SchulnameLabel.setHorizontalAlignment(SwingConstants.CENTER);
				SchulnameLabel.setBackground(new java.awt.Color(128, 255, 0));
			}
			{
				AnzahlTakteLabel = new JLabel();
				LabelPanel.add(AnzahlTakteLabel);
				AnzahlTakteLabel.setHorizontalAlignment(SwingConstants.CENTER);
				AnzahlTakteLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				AnzahlTakteLabel.setBackground(new java.awt.Color(255, 0, 0));
				AnzahlTakteLabel
						.setToolTipText("Die Anzahl der Takte, die am EST verwaltet werden. \nAchtung: Nicht w\u00e4hrend des laufenden Prozesses \u00e4ndern!");
			}
			{
				SchnittLabel = new JLabel();
				LabelPanel.add(SchnittLabel);
				SchnittLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				SchnittLabel.setHorizontalAlignment(SwingConstants.CENTER);
				SchnittLabel.setBackground(new java.awt.Color(255, 0, 0));
				SchnittLabel
						.setToolTipText("In welchem Takt beginnt der zweite Abschnitt? Achtung: Nicht w\u00e4hrend des laufenden Prozesses \u00e4ndern!");
			}
			{
				TaktLaengeLabel = new JLabel();
				LabelPanel.add(TaktLaengeLabel);
				TaktLaengeLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				TaktLaengeLabel.setHorizontalAlignment(SwingConstants.CENTER);
				TaktLaengeLabel.setBackground(new java.awt.Color(255, 0, 0));
				TaktLaengeLabel
						.setToolTipText("L\u00e4nge eines Taktes (in Minuten). Achtung: Nicht w\u00e4hrend des laufenden Prozesses \u00e4ndern!");
			}
			{
				MaxWunschLabel = new JLabel();
				LabelPanel.add(MaxWunschLabel);
				MaxWunschLabel.setBackground(new java.awt.Color(255, 0, 0));
				MaxWunschLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				MaxWunschLabel.setHorizontalAlignment(SwingConstants.CENTER);
				MaxWunschLabel
						.setToolTipText("Maximale Anzahl der Wuensche pro Eltern. Achtung: Nicht \u00e4ndern w\u00e4hrend des Prozesses!");
			}
			{
				Datum1Label = new JLabel();
				LabelPanel.add(Datum1Label);
				Datum1Label.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Datum1Label.setHorizontalAlignment(SwingConstants.CENTER);
				Datum1Label.setBackground(new java.awt.Color(128, 255, 0));
				Datum1Label
						.setToolTipText("Datum eingeben oder ausw\u00e4hlen");
			}
			{
				Datum2Label = new JLabel();
				LabelPanel.add(Datum2Label);
				Datum2Label.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Datum2Label.setHorizontalAlignment(SwingConstants.CENTER);
				Datum2Label.setBackground(new java.awt.Color(128, 255, 0));
				Datum2Label
						.setToolTipText("Datum eingeben oder ausw\u00e4hlen");
			}
			{
				BackLabel = new JLabel();
				LabelPanel.add(BackLabel);
				BackLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				BackLabel.setHorizontalAlignment(SwingConstants.CENTER);
				BackLabel.setBackground(new java.awt.Color(128, 255, 0));
				BackLabel.setToolTipText("Datum eingeben oder ausw\u00e4hlen");
			}
			{
				FruehLabel = new JLabel();
				LabelPanel.add(FruehLabel);
				FruehLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				FruehLabel.setHorizontalAlignment(SwingConstants.CENTER);
				FruehLabel.setBackground(new java.awt.Color(128, 255, 0));
			}
			{
				SpaetLabel = new JLabel();
				LabelPanel.add(SpaetLabel);
				SpaetLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				SpaetLabel.setHorizontalAlignment(SwingConstants.CENTER);
				SpaetLabel.setBackground(new java.awt.Color(128, 255, 0));
			}
			{
				ElternSperrLabel = new JLabel();
				LabelPanel.add(ElternSperrLabel);
				ElternSperrLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				ElternSperrLabel.setHorizontalAlignment(SwingConstants.CENTER);
				ElternSperrLabel.setBackground(new java.awt.Color(128, 255, 0));
			}
			{
				HalbJahrLabel = new JLabel();
				LabelPanel.add(HalbJahrLabel);
				HalbJahrLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				HalbJahrLabel.setHorizontalAlignment(SwingConstants.CENTER);
				HalbJahrLabel.setBackground(new java.awt.Color(128, 255, 0));
			}
			{
				Anf1Label = new JLabel();
				LabelPanel.add(Anf1Label);
				Anf1Label.setBackground(new java.awt.Color(128, 255, 0));
				Anf1Label.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Anf1Label.setHorizontalAlignment(SwingConstants.CENTER);
				Anf1Label
						.setToolTipText("Wann beginnt der erste Abschnitt? (Angabe in Minuten nach Mitternacht!)");
			}
			{
				Anf2Label = new JLabel();
				LabelPanel.add(Anf2Label);
				Anf2Label.setBackground(new java.awt.Color(128, 255, 0));
				Anf2Label.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Anf2Label.setHorizontalAlignment(SwingConstants.CENTER);
				Anf2Label
						.setToolTipText("Wann beginnt der zweite Abschnitt? (Angabe in Minuten nach Mitternacht!)");
			}
			{
				VoroptiLabel = new JLabel();
				LabelPanel.add(VoroptiLabel);
				VoroptiLabel.setBackground(new java.awt.Color(128, 255, 0));
				VoroptiLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				VoroptiLabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				LuftLabel = new JLabel();
				LabelPanel.add(LuftLabel);
				LuftLabel.setBackground(new java.awt.Color(128, 255, 0));
				LuftLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				LuftLabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				Shufflelabel = new JLabel();
				LabelPanel.add(Shufflelabel);
				Shufflelabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Shufflelabel.setBackground(new java.awt.Color(128, 255, 0));
				Shufflelabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				Traenenlabel = new JLabel();
				LabelPanel.add(Traenenlabel);
				Traenenlabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Traenenlabel.setBackground(new java.awt.Color(128, 255, 0));
				Traenenlabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				LogLevelLabel = new JLabel();
				LabelPanel.add(LogLevelLabel);
				LogLevelLabel.setBackground(new java.awt.Color(128, 255, 0));
				LogLevelLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				LogLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				EditorLabel = new JLabel();
				LabelPanel.add(EditorLabel);
				EditorLabel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				EditorLabel.setBackground(new java.awt.Color(128, 255, 0));
				EditorLabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
		}
		{
			InputPanel = new JPanel();
			getContentPane().add(InputPanel);
			GridLayout InputPanelLayout = new GridLayout(21, 1);
			InputPanelLayout.setHgap(5);
			InputPanelLayout.setVgap(5);
			InputPanelLayout.setColumns(1);
			InputPanelLayout.setRows(21);
			InputPanel.setLayout(InputPanelLayout);
			InputPanel.setBackground(new java.awt.Color(214, 211, 167));
			{
				saveButton = new JButton();
				InputPanel.add(saveButton);
				saveButton.setText("Prg.Vorgaben Speichern");
				saveButton.setFont(new java.awt.Font("Times New Roman", 1, 14));
				saveButton.setBackground(new java.awt.Color(215, 235, 238));
				saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
				saveButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						saveButtonMouseClicked(evt);
					}
				});
			}
			{
				SchulnameInput = new JTextField();
				InputPanel.add(SchulnameInput);
				SchulnameInput.setFont(new java.awt.Font("Tahoma", 1, 12));
				SchulnameInput.setBackground(new java.awt.Color(192, 192, 192));
				SchulnameInput.setHorizontalAlignment(SwingConstants.CENTER);
				String sn = ESTStatics.SCHULNAME;
				SchulnameInput.setText(this.props.getProperty(sn, "???"));
			}
			{
				AnzahlTakteInput = new JSpinField();
				AnzahlTakteInput.setMinimum(0);
				AnzahlTakteInput.setMaximum(100);
				InputPanel.add(AnzahlTakteInput);
				AnzahlTakteInput.setBackground(new java.awt.Color(255, 0, 0));
				String sn = ESTStatics.ANZAHL_TAKTE;
				int oldAT = Integer.parseInt(this.props.getProperty(sn, "72"));
				AnzahlTakteInput.setValue(oldAT);
				AnzahlTakteInput.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));

			}
			{
				SchnittInput = new JSpinField();
				SchnittInput.setMinimum(0);
				SchnittInput.setMaximum(90);
				SchnittInput.setBackground(new java.awt.Color(255, 0, 0));
				InputPanel.add(SchnittInput);
				SchnittInput.setBackground(new java.awt.Color(255, 0, 0));
				SchnittInput.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				TaktLaengeInput = new JSpinField();
				TaktLaengeInput.setMinimum(5);
				TaktLaengeInput.setMaximum(20);
				InputPanel.add(TaktLaengeInput);
				TaktLaengeInput.setBackground(new java.awt.Color(255, 0, 0));
				TaktLaengeInput.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				MaxWunschInput = new JSpinField();
				MaxWunschInput.setMinimum(5);
				MaxWunschInput.setMaximum(10);
				InputPanel.add(MaxWunschInput);
				MaxWunschInput.setBackground(new java.awt.Color(255, 0, 0));
				MaxWunschInput.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				Datum1Chooser = new JDateChooser();
				Datum1Chooser.setLocale(Locale.GERMAN);
				InputPanel.add(Datum1Chooser);
			}
			{
				Datum2Chooser = new JDateChooser();
				Datum2Chooser.setLocale(Locale.GERMAN);
				InputPanel.add(Datum2Chooser);
			}
			{
				BackDateChooser = new JDateChooser();
				BackDateChooser.setLocale(Locale.GERMAN);
				InputPanel.add(BackDateChooser);
			}
			{
				FruehInput = new JTextField();
				InputPanel.add(FruehInput);
			}
			{
				SpaetInput = new JTextField();
				InputPanel.add(SpaetInput);
			}
			{
				SperrInput = new JTextField();
				InputPanel.add(SperrInput);
			}
			{
				HalbJahrInput = new JTextField();
				InputPanel.add(HalbJahrInput);
			}
			{
				Anf1Input = new TimePickerButton(owner);
				InputPanel.add(Anf1Input);
			}
			{
				Anf2Input = new TimePickerButton(owner);
				InputPanel.add(Anf2Input);
			}
			{
				VoroptiPanel = new JPanel();
				InputPanel.add(VoroptiPanel);
				VoroptiPanel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				VoroptiPanel.setBackground(new java.awt.Color(128, 255, 128));
				{
					MitVoroptiRadioButton = new JRadioButton();
					VoroptiPanel.add(MitVoroptiRadioButton);
					MitVoroptiRadioButton.setText("Mit Voroptimierung");
					this.VoroptiRadioButtonGroup
							.add(this.MitVoroptiRadioButton);
				}
				{
					OhneVoroptiRadioButton = new JRadioButton();
					VoroptiPanel.add(OhneVoroptiRadioButton);
					OhneVoroptiRadioButton.setText("Ohne Voroptimierung");
					this.VoroptiRadioButtonGroup
							.add(this.OhneVoroptiRadioButton);
				}
			}
			{
				LuftPanel = new JPanel();
				InputPanel.add(LuftPanel);
				LuftPanel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				LuftPanel.setBackground(new java.awt.Color(128, 255, 128));
				{
					LuftJaRadioButton = new JRadioButton();
					LuftPanel.add(LuftJaRadioButton);
					LuftJaRadioButton.setText("Mit Lufttakten");
					this.LuftButtonRadioGroup.add(this.LuftJaRadioButton);
				}
				{
					LuftNeinRadioButton = new JRadioButton();
					LuftPanel.add(LuftNeinRadioButton);
					LuftNeinRadioButton.setText("Ohne Lufttakte");
					this.LuftButtonRadioGroup.add(this.LuftNeinRadioButton);
				}
			}
			{
				Shufflepanel = new JPanel();
				InputPanel.add(Shufflepanel);
				Shufflepanel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Shufflepanel.setBackground(new java.awt.Color(128, 255, 128));
				{
					ShuffleCheckBox = new JCheckBox();
					Shufflepanel.add(ShuffleCheckBox);
					ShuffleCheckBox.setText("Mit Shuffle");
				}
			}
			{
				Traenenpanel = new JPanel();
				InputPanel.add(Traenenpanel);
				Traenenpanel.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
				Traenenpanel.setBackground(new java.awt.Color(128, 255, 128));
				{
					TraenenCheckBox = new JCheckBox();
					Traenenpanel.add(TraenenCheckBox);
					TraenenCheckBox.setText("Mit PP");
				}
			}
			{
				LogLevelInput = new JSpinField();
				LogLevelInput.setMinimum(0);
				LogLevelInput.setMaximum(11);
				InputPanel.add(LogLevelInput);
			}
			{
				EditorInput = new JTextField();
				InputPanel.add(EditorInput);
			}
		}
		this.pack();
		update();

	}

	private void update() {
		String sn;
		String snProp;

		sn = ESTStatics.SCHULNAME;
		snProp = this.props.getProperty(sn, "????");
		SchulnameLabel.setText(sn);
		SchulnameInput.setText(snProp);

		sn = ESTStatics.ANZAHL_TAKTE;
		snProp = this.props.getProperty(sn, "72");
		AnzahlTakteLabel.setText(sn);
		int oldAT = Integer.parseInt(snProp);
		AnzahlTakteInput.setValue(oldAT);
		AnzahlTakteInput.getSpinner()
				.setForeground(new java.awt.Color(0, 0, 0));
		AnzahlTakteInput.getSpinner().setBackground(
				new java.awt.Color(255, 128, 255));

		sn = ESTStatics.SCHNITT;
		snProp = this.props.getProperty(sn, "36");
		SchnittLabel.setText(sn);
		int oldS = Integer.parseInt(snProp);
		SchnittInput.setValue(oldS);

		sn = ESTStatics.TAKTLAENGE;
		snProp = this.props.getProperty(sn, "10");
		TaktLaengeLabel.setText(sn);
		int oldTL = Integer.parseInt(snProp);
		TaktLaengeInput.setValue(oldTL);

		sn = ESTStatics.MAX_WUENSCHE;
		snProp = this.props.getProperty(sn, "10");
		MaxWunschLabel.setText(sn);
		int oldMW = Integer.parseInt(snProp);
		MaxWunschInput.setValue(oldMW);

		{
			sn = ESTStatics.DATUM1;
			snProp = this.props.getProperty(sn, "1.1.2007");
			Datum1Label.setText(sn);
			GregorianCalendar oldDay = new GregorianCalendar();
			StringTokenizer st = new StringTokenizer(snProp, ".", false);
			String ds = st.nextToken();
			String ms = st.nextToken();
			String ys = st.nextToken();
			int di = Integer.parseInt(ds);
			int mi = Integer.parseInt(ms) - 1; // Achtung: Minus 1!!!
			int yi = Integer.parseInt(ys);
			oldDay.set(yi, mi, di);
			Datum1Chooser.setCalendar(oldDay);
		}

		{
			sn = ESTStatics.DATUM2;
			snProp = this.props.getProperty(sn, "1.1.2007");
			Datum2Label.setText(sn);
			GregorianCalendar oldDay = new GregorianCalendar();
			StringTokenizer st = new StringTokenizer(snProp, ".", false);
			String ds = st.nextToken();
			String ms = st.nextToken();
			String ys = st.nextToken();
			int di = Integer.parseInt(ds);
			int mi = Integer.parseInt(ms) - 1; // Achtung: Minus 1!!!
			int yi = Integer.parseInt(ys);
			oldDay.set(yi, mi, di);
			Datum2Chooser.setCalendar(oldDay);
		}

		{
			sn = ESTStatics.BACKDATE;
			snProp = this.props.getProperty(sn, "1.1.2007");
			BackLabel.setText(sn);
			GregorianCalendar oldDay = new GregorianCalendar();
			StringTokenizer st = new StringTokenizer(snProp, ".", false);
			String ds = st.nextToken();
			String ms = st.nextToken();
			String ys = st.nextToken();
			int di = Integer.parseInt(ds);
			int mi = Integer.parseInt(ms) - 1; // Achtung: Minus 1!!!
			int yi = Integer.parseInt(ys);
			oldDay.set(yi, mi, di);
			BackDateChooser.setCalendar(oldDay);
		}

		sn = ESTStatics.FRUEH;
		snProp = this.props.getProperty(sn, "7:30 - 10:30; 13:30 - 16:30");
		FruehLabel.setText(sn);
		FruehInput.setText(snProp);

		sn = ESTStatics.SPAET;
		snProp = this.props.getProperty(sn, "10:30 - 13:30; 16:30 - 19:30");
		SpaetLabel.setText(sn);
		SpaetInput.setText(snProp);

		sn = ESTStatics.ELTERNSPERRTAKTE;
		snProp = this.props.getProperty(sn, "13;14;15;16;17");
		ElternSperrLabel.setText(sn);
		SperrInput.setText(snProp);

		sn = ESTStatics.HALBJAHR;
		snProp = this.props.getProperty(sn, "2003-04-2");
		HalbJahrLabel.setText(sn);
		HalbJahrInput.setText(snProp);

		sn = ESTStatics.ANFANG1;
		snProp = this.props.getProperty(sn, "450");
		Anf1Label.setText(sn);
		Anf1Input.setText(snProp);

		sn = ESTStatics.ANFANG2;
		snProp = this.props.getProperty(sn, "810");
		Anf2Label.setText(sn);
		Anf2Input.setText(snProp);

		sn = ESTStatics.VOROPTI;
		snProp = this.props.getProperty(sn, "Ja");
		VoroptiLabel.setText(sn);
		if (snProp.equals("Ja")) {
			this.MitVoroptiRadioButton.setSelected(true);
			this.OhneVoroptiRadioButton.setSelected(false);
		} else {
			this.MitVoroptiRadioButton.setSelected(false);
			this.OhneVoroptiRadioButton.setSelected(true);
		}

		sn = ESTStatics.LUFT;
		snProp = this.props.getProperty(sn, "Ja");
		LuftLabel.setText(sn);
		if (snProp.equals("Ja")) {
			this.LuftJaRadioButton.setSelected(true);
			this.LuftNeinRadioButton.setSelected(false);
		} else {
			this.LuftJaRadioButton.setSelected(false);
			this.LuftNeinRadioButton.setSelected(true);
		}

		sn = ESTStatics.SHUFFLE;
		snProp = this.props.getProperty(sn, "Ja");
		Shufflelabel.setText(sn);
		if (snProp.equals("Ja")) {
			this.ShuffleCheckBox.setSelected(true);
		} else {
			this.ShuffleCheckBox.setSelected(false);
		}

		sn = ESTStatics.PAUSE;
		snProp = this.props.getProperty(sn, "Ja");
		Traenenlabel.setText(sn);
		if (snProp.equals("Ja")) {
			this.TraenenCheckBox.setSelected(true);
		} else {
			this.TraenenCheckBox.setSelected(false);
		}

		sn = ESTStatics.LOGLEVEL;
		snProp = this.props.getProperty(sn, "10");
		LogLevelLabel.setText(sn);
		int oldLL = Integer.parseInt(snProp);
		LogLevelInput.setValue(oldLL);

		sn = ESTStatics.EDITOR;
		snProp = this.props.getProperty(sn, "notepad.exe");
		EditorLabel.setText(sn);
		EditorInput.setText(snProp);
	}

	private void loadButtonMouseClicked(MouseEvent evt) {
		ESTEigenschaften.loadProps();
		update();
	}

	private void saveButtonMouseClicked(MouseEvent evt) {
		saveAllProps();
	}

	private void saveAllProps() {
		String sn = ESTStatics.SCHULNAME;
		String snProp = this.SchulnameInput.getText();
		this.props.setProperty(sn, snProp);
		/*
		 * if (this.guiTitel.getText().equals("XXX")) { //
		 * ?????????????????????? // nix ändern!! } else {
		 * this.guiTitel.setText(snProp); }
		 */

		sn = ESTStatics.ANZAHL_TAKTE;
		snProp = "" + this.AnzahlTakteInput.getValue();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.SCHNITT;
		snProp = "" + this.SchnittInput.getValue();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.TAKTLAENGE;
		snProp = "" + this.TaktLaengeInput.getValue();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.MAX_WUENSCHE;
		snProp = "" + this.MaxWunschInput.getValue();
		this.props.setProperty(sn, snProp);

		{
			sn = ESTStatics.DATUM1;
			Calendar c = Datum1Chooser.getCalendar();
			snProp = "";
			snProp += c.get(Calendar.DATE) + ".";
			snProp += (c.get(Calendar.MONTH) + 1) + ".";
			snProp += c.get(Calendar.YEAR);
			this.props.setProperty(sn, snProp);
		}

		{
			sn = ESTStatics.DATUM2;
			Calendar c = Datum2Chooser.getCalendar();
			snProp = "";
			snProp += c.get(Calendar.DATE) + ".";
			snProp += (c.get(Calendar.MONTH) + 1) + ".";
			snProp += c.get(Calendar.YEAR);
			this.props.setProperty(sn, snProp);
		}

		// this.props.setProperty(sn, snProp);

		{
			sn = ESTStatics.BACKDATE;
			Calendar c = BackDateChooser.getCalendar();
			snProp = "";
			snProp += c.get(Calendar.DATE) + ".";
			snProp += (c.get(Calendar.MONTH) + 1) + ".";
			snProp += c.get(Calendar.YEAR);
			this.props.setProperty(sn, snProp);
		}

		sn = ESTStatics.FRUEH;
		snProp = this.FruehInput.getText();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.SPAET;
		snProp = this.SpaetInput.getText();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.ELTERNSPERRTAKTE;
		snProp = this.SperrInput.getText();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.HALBJAHR;
		snProp = this.HalbJahrInput.getText();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.ANFANG1;
		snProp = this.Anf1Input.getName();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.ANFANG2;
		snProp = this.Anf2Input.getName();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.VOROPTI;
		if (this.MitVoroptiRadioButton.isSelected()) {
			snProp = "Ja";
		} else {
			snProp = "Nein";
		}
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.LUFT;
		if (this.LuftJaRadioButton.isSelected()) {
			snProp = "Ja";
		} else {
			snProp = "Nein";
		}
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.SHUFFLE;
		if (this.ShuffleCheckBox.isSelected()) {
			snProp = "Ja";
		} else {
			snProp = "Nein";
		}
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.PAUSE;
		if (this.TraenenCheckBox.isSelected()) {
			snProp = "Ja";
		} else {
			snProp = "Nein";
		}
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.LOGLEVEL;
		snProp = "" + this.LogLevelInput.getValue();
		this.props.setProperty(sn, snProp);

		sn = ESTStatics.EDITOR;
		snProp = this.EditorInput.getText();
		this.props.setProperty(sn, snProp);

		ESTEigenschaften.saveProps();
		JOptionPane.showMessageDialog(this, "Properties gesichert!", "",
				JOptionPane.INFORMATION_MESSAGE);

	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			int response = JOptionPane.showConfirmDialog(null,
					"Properties sichern",
					"Wollen Sie den aktuellen Stand sichern?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (response == JOptionPane.OK_OPTION) {
				saveAllProps();
			}
			// saveAllProps();
		}
	}

}
