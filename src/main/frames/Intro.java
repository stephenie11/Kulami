package main.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.objects.PlayerNames;

public class Intro extends JFrame {

	private JPanel contentPane;
	
	private Socket s;
	private ServerSocket ss;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String iAm = null;
	
	private String Player1 = null,Player2 = null;

	public void write(Object o){
		
		 try {
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void read(){
		
		 try {
			in = new ObjectInputStream(s.getInputStream());
			PlayerNames o = (PlayerNames) in.readObject();
			if(iAm.equals("Client")) Player1 = o.getName();
			else Player2 = o.getName();
			
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void networkInit(){
		try {
			ss = new ServerSocket(4444);
			s = ss.accept();
			iAm = "Server";
		} catch (IOException e) {
			try {
				s = new Socket("localhost",4444);
				iAm = "Client";
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println(iAm);
	}
	
	

	public static void main(String[] args) {

					Intro frame = new Intro();
					
	}


	
	public Intro() {
		   contentPane = new JPanel();
	       JButton Play = new JButton();
	       Play.addActionListener(new ActionListener() {
	       	public void actionPerformed(ActionEvent e) {
	       		boolean loop = true;
	       		while(loop){
	       			loop = false;
	       		String st = JOptionPane.showInputDialog("Enter your name");
	       		if(st != null)
	       		if(st.length() != 0 ) {
	       			int input = JOptionPane.showConfirmDialog(null, "Are you ready to start the game?");
	       				if(input == 0){
	       					networkInit();
	       					if(iAm.equals("Client")) Player2 = st;
	       					else Player1 = st;
	       					write(new PlayerNames(st));
	       					read();
	       					Game game = new Game(Player1,Player2,iAm,s,ss,in,out);
	       					dispose();
	       				}
	       				else if(input == 1){loop=true;}
	       		}
	       		
	       		}
	       		
	       	}
	       });
	       JButton Exit = new JButton();
	       Exit.addActionListener(new ActionListener() {
	       	public void actionPerformed(ActionEvent e) {
	       		dispose();
	       		System.exit(0);
	       	}
	       });
	       JLabel jLabel1 = new JLabel();
	

	        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

	        contentPane.setLayout(null);
	        setBounds(100, 100, 460, 400);
	        Play.setFont(new java.awt.Font("Chiller", 1, 24));
	        Play.setText("Play");

	        contentPane.add(Play);
	        Play.setBounds(120, 270, 90, 30);

	        Exit.setFont(new java.awt.Font("Chiller", 1, 24)); 
	        Exit.setText("Exit");

	        contentPane.add(Exit);
	        Exit.setBounds(250, 270, 90, 30);

	        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        jLabel1.setIcon(new javax.swing.ImageIcon("src/kulamiBG.png"));
	        contentPane.add(jLabel1);
	        jLabel1.setBounds(0, 0, 450, 370);

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(contentPane, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(contentPane, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
	        );
		
		setVisible(true);
		setResizable(false);
		setTitle("Kulami");
	}

}
