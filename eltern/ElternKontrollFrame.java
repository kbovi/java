package eltern;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Administrator
 */
public class ElternKontrollFrame extends JFrame {

	/**
     * 
     */
	private static final long serialVersionUID = -9114131544548361987L;
	private Eltern meineEltern;
	private JPanel contentPane;

	private String Namen[];

	private JList ElternAusgabeList;

	public ElternKontrollFrame(Eltern eltern) {
		super("Elternnamen alphabetisch");
		this.meineEltern = eltern;
		this.setResizable(false);

		this.Namen = new String[this.meineEltern.getAnzahlEltern()];

		TreeSet<String> ts = new TreeSet<String>();
		for (int i = 0; i < this.meineEltern.getAnzahlEltern(); i++) {
			String s = this.meineEltern.getFall(i).getNachname();
			s += " (";
			Fall dieserFall = this.meineEltern.get(i);
			String kn = dieserFall.getKindernamen();
			s += kn;
			s += ") ";
			s += this.meineEltern.getFall(i).getFallNr();
			ts.add(s);
		}

		Iterator<String> it = ts.iterator();
		int i = 0;
		while (it.hasNext()) {
			String s = it.next();
			this.Namen[i] = s;
			i++;
		}

		//
		// for (int i = 0; i < this.meineEltern.getAnzahlEltern(); i++)
		// {
		// this.Namen [i] = "";
		// this.Namen [i] += this.meineEltern.getFall(i).getFallNr();
		// this.Namen [i] += ": ";
		// this.Namen [i] += this.meineEltern.getFall(i).getNachname();
		// Fall dieserFall = this.meineEltern.get(i);
		// String kn = dieserFall.getKindernamen();
		// this.Namen [i] += " (" + kn + ")";
		// }

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
		zusatz = "ansehen";
		this.setTitle("Elterndaten " + zusatz);

		contentPane = (JPanel) this.getContentPane();

		contentPane.setLayout(new BorderLayout());

		{//
			this.ElternAusgabeList = new JList();
			this.ElternAusgabeList.setListData(Namen);
			JScrollPane kasp = new JScrollPane(this.ElternAusgabeList);
			contentPane.add(kasp, BorderLayout.CENTER);
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
		}
	}
}
