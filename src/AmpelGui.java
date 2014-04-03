import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;

public class AmpelGui extends JFrame {

	private Anlage anlage;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AmpelGui frame = new AmpelGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AmpelGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelF = new JPanel();
		contentPane.add(panelF, BorderLayout.WEST);
		panelF.setLayout(new BoxLayout(panelF, BoxLayout.Y_AXIS));

		JLabel RedLabelF = new JLabel("xxxxx");
		RedLabelF.setAlignmentX(Component.CENTER_ALIGNMENT);
		RedLabelF.setOpaque(true);
		panelF.add(RedLabelF);

		JLabel GreenLabelF = new JLabel("xxxxx");
		GreenLabelF.setAlignmentX(Component.CENTER_ALIGNMENT);
		GreenLabelF.setOpaque(true);
		panelF.add(GreenLabelF);

		JPanel panelA = new JPanel();
		contentPane.add(panelA, BorderLayout.EAST);
		panelA.setLayout(new BoxLayout(panelA, BoxLayout.Y_AXIS));

		JLabel RedLabelA = new JLabel("xxxxx");
		RedLabelA.setAlignmentX(Component.CENTER_ALIGNMENT);
		RedLabelA.setOpaque(true);
		panelA.add(RedLabelA);

		JLabel YellowLabelA = new JLabel("xxxxx");
		YellowLabelA.setAlignmentX(Component.CENTER_ALIGNMENT);
		YellowLabelA.setOpaque(true);
		panelA.add(YellowLabelA);

		JLabel GreenLabelA = new JLabel("xxxxx");
		GreenLabelA.setAlignmentX(Component.CENTER_ALIGNMENT);
		GreenLabelA.setOpaque(true);
		panelA.add(GreenLabelA);

		this.anlage = new Anlage(RedLabelF, GreenLabelF, RedLabelA,
				YellowLabelA, GreenLabelA);
		
		JLabel lblNewLabel = new JLabel("Fu\u00DFg\u00E4ngerampel");
		panelF.add(lblNewLabel);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				next ();
			}
		});
		contentPane.add(btnNext, BorderLayout.CENTER);
	}
	
	public void next () {
		this.anlage.next();
	}
}
