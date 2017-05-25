package frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;

public class RestulFrame extends JFrame {

	private JPanel contentPane;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RestulFrame frame = new RestulFrame("","");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RestulFrame(String s,String scor) {
	
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 384, 177);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbScor = new JLabel(scor);
		lbScor.setHorizontalAlignment(SwingConstants.CENTER);
		lbScor.setVerticalAlignment(SwingConstants.CENTER);
		lbScor.setForeground(new Color(0, 0, 0));
		lbScor.setFont(new Font("Hobo Std", Font.PLAIN, 24));
		lbScor.setBounds(35, 87, 312, 50);
		contentPane.add(lbScor);
		
		JLabel lbS = new JLabel(s);
		lbS.setVerticalAlignment(SwingConstants.CENTER);
		lbS.setHorizontalAlignment(SwingConstants.CENTER);
		lbS.setForeground(new Color(0, 0, 0));
		lbS.setFont(new Font("Harrington", Font.BOLD, 28));
		lbS.setBounds(55, 11, 270, 52);
		
		contentPane.add(lbS);
		
		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(new ImageIcon("C:\\Users\\Miruna\\Desktop\\Kulami\\src\\bumperprize.png"));
		label.setBounds(35, -69, 300, 230);
		contentPane.add(label);
		
		
		setTitle("Kulami");
		setResizable(false);
		setVisible(true);
	}
}
