package frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import kulami.Kulami;
import objects.ButtonPosition;
import objects.WinnerFrame;

public class Game extends JFrame {

	private JPanel Pane;
	private String SPlayer1,SPlayer2,B1,B2;
	private int nrBileNegre = 28,nrBileRosii = 28;
	private int buttonStart = 69;
	private int labelStart = 4;
	private Boolean isMe;
	private int end = 0;
	
	private Socket s;
	private ServerSocket ss;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String iAm ;
	
	private Kulami k ;
	
	
	private boolean firstMove = false,secondMove = false;

	
	public void write(Object o){
		
		 try {
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void read(){
		 try {
			Object o = in.readObject();
			if(o instanceof ButtonPosition){
				
				int it = ((ButtonPosition) o).getIndex();
				
				JLabel a = (JLabel) Pane.getComponents()[it];
				JLabel bilePlayer1 = (JLabel) Pane.getComponent(1);
				JLabel bilePlayer2= (JLabel) Pane.getComponent(2);
				if(!isMe && iAm.equals("Server")){
					a.setIcon(new ImageIcon("src/redDot.jpg"));
					isMe = true;
					nrBileRosii--;
					bilePlayer2.setText("Bile : " + nrBileRosii);
					a.setVisible(true);
				}
				else if(!isMe && iAm.equals("Client")){
					a.setIcon(new ImageIcon("src/blackDot.jpg"));
					isMe = true;
					nrBileNegre--;
					bilePlayer1.setText("Bile : " + nrBileNegre);
					a.setVisible(true);
				}
			}
			if(o instanceof Kulami){
				k = (Kulami) o;
			}
			
			if(o instanceof WinnerFrame){
				WinnerFrame winnerFrame = (WinnerFrame) o;
				RestulFrame rf = new RestulFrame(winnerFrame.getS(),winnerFrame.getScor());
				 dispose();	
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			try {
				in.close();
				out.close();
				s.close();
				if(ss != null)ss.close();
				System.exit(0);
			} catch (IOException e1) {
				
			}
		}
		 read();
	}
	
	public void endGame(String Player1,String Player2){
		if(firstMove==true&&secondMove==true && end > 0)
			if(k.endOfGame()){
				int winner = k.winner();
				WinnerFrame winnerFrame = new WinnerFrame("","");
				 if(winner == 1)
					 winnerFrame.setS(Player1 + " won!");
		        else 
		            if(winner == 2)
		            	winnerFrame.setS(Player2 + " won!");
		            else
		            	winnerFrame.setS("Tie!");
				 winnerFrame.setScor(Player1 + " " + k.getServerPoints() + " - " + k.getClientPoints() + " " + Player2); 
				 write(winnerFrame);
				 RestulFrame rf = new RestulFrame(winnerFrame.getS(),winnerFrame.getScor());
				 dispose();							
				
			
			}
	}

	
	

	public Game(String Player1,String Player2,String iAm,Socket s,ServerSocket ss,ObjectInputStream in,ObjectOutputStream out) {
		this.SPlayer1 = Player1 + " - ";
		this.SPlayer2 = Player2 + " - ";
		this.iAm = iAm;
		this.s = s;
		this.ss = ss;
		this.in = in;
		this.out = out;
		
		k= new Kulami();
		k.initGame();
		
		
		if(iAm.equals("Client")) {isMe = false;firstMove=true;} 
		else {isMe = true;k.initGame();secondMove=true;}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 572, 462);
		Pane = new JPanel();
		Pane.setBackground(Color.DARK_GRAY);
		Pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(Pane);
		Pane.setLayout(null);
		

		//Stringuri----
		
		
		JLabel Player1Label = new JLabel(SPlayer1);
		Player1Label.setForeground(Color.BLACK);
		Player1Label.setFont(new Font("Chaparral Pro", Font.PLAIN, 24));
		Player1Label.setBounds(23, 11, 127, 32);
		Player1Label.setHorizontalAlignment(SwingConstants.RIGHT);
		Player1Label.setVerticalAlignment(SwingConstants.CENTER);
		Pane.add(Player1Label);
		
		JLabel Bile1 = new JLabel("Bile : " + 28);
		Bile1.setForeground(Color.BLACK);
		Bile1.setFont(new Font("Chaparral Pro", Font.BOLD, 22));
		Bile1.setBounds(151, 12, 100, 32);
		Bile1.setHorizontalAlignment(SwingConstants.LEFT);
		Bile1.setVerticalAlignment(SwingConstants.CENTER);
		Pane.add(Bile1);
		
		
		
		
		JLabel Bile2 = new JLabel("Bile : " + 28);
		Bile2.setForeground(Color.RED);
		Bile2.setFont(new Font("Chaparral Pro", Font.BOLD, 22));
		Bile2.setBounds(432, 12, 100, 32);
		Bile2.setHorizontalAlignment(SwingConstants.LEFT);
		Bile2.setVerticalAlignment(SwingConstants.CENTER);
		Pane.add(Bile2);
		
		JLabel Player1Labe2 = new JLabel(SPlayer2);
		Player1Labe2.setForeground(Color.RED);
		Player1Labe2.setFont(new Font("Chaparral Pro", Font.PLAIN, 24));
		Player1Labe2.setBounds(299, 11, 132, 32);
		Player1Labe2.setHorizontalAlignment(SwingConstants.RIGHT);
		Player1Labe2.setVerticalAlignment(SwingConstants.CENTER);
		Pane.add(Player1Labe2);
		
		//Labeluri--
		
		for(int j=0;j<8;j++)
			for(int i=0;i<8;i++){
				JLabel label = new JLabel("");
				label.setBounds(121 + (int)(41.3 * i)+i, 76+(int)(42.3*j), 33, 33);
				Pane.add(label);
				label.setVisible(false);
				
			}
	
		
		//Tabla----
		

		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("src/board.jpg"));
		lblNewLabel.setBounds(116, 71, 340, 340);
		Pane.add(lblNewLabel);
		
		

		
		for(int j=0;j<8;j++)
		for(int i=0;i<8;i++){
			int x = j;
			int y = i;
			JButton Button = new JButton("");
	Button.addMouseListener(new MouseAdapter() {
				
				public void mouseEntered(MouseEvent arg0) {
					
					int it = labelStart + (y+x*8);
					JLabel a = (JLabel) Pane.getComponents()[it];
					
					if(firstMove==true && secondMove==true && k.check(x, y)==true && isMe == true){
						Point move = new Point(x,y);
					if(k.validMove(move)){
						a.setIcon(new ImageIcon("src/goodDot.jpg"));
						a.setVisible(true);
					}
					else{
						a.setIcon(new ImageIcon("src/wrongDot.jpg"));
						a.setVisible(true);
					}
					}
					else if (secondMove == false && isMe == true){
						
						if(k.checksecondMove(new Point(x,y)) == 1){
							a.setIcon(new ImageIcon("src/goodDot.jpg"));
							a.setVisible(true);
						}
						else if(k.checksecondMove(new Point(x,y)) == -1 && k.check(x, y)==true){
							a.setIcon(new ImageIcon("src/wrongDot.jpg"));
							a.setVisible(true);
						}
						
					}
					else if (firstMove == false && isMe == true){
						a.setIcon(new ImageIcon("src/goodDot.jpg"));
						a.setVisible(true);
					}
				}
				
				public void mouseExited(MouseEvent e) {
					int it = labelStart + (y+x*8);
					JLabel a = (JLabel) Pane.getComponents()[it];
					if( k.check(x, y)==true)
						{a.setIcon(null);
						a.setVisible(false);
						}
				}
			});
			Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
				
					
					
					int it = labelStart + (y+x*8);
					//System.out.println(x+ " " + y);
					
					JLabel a = (JLabel)Pane.getComponents()[it];
					int ok=0;
			if(isMe){
				
					if(firstMove==false){
						ok = k.firstMove(new Point(x, y));
						if(ok==1){
						write(k);
						firstMove = true;
						}
					}
					else if(secondMove==false){
						ok = k.secondMove(new Point(x, y));
						if(ok==1){
						write(k);
						secondMove=true;
						}
					}
					
					else if(iAm.equals("Client")) {
		                Point move = new Point(x,y);
		               
		                if(k.validMove(move)) {
		                	ok=1;
		                	k.clientMoves(move);
		                	write(k);
		                }
		                
		                
		            }
					
					else if(iAm.equals("Server")) {
		                Point move = new Point(x,y);
		               
		                if(k.validMove(move)) {
		                	ok=1;
		                	k.serverMoves(move);
		                	write(k);
		                }
		                
		                
		            }
					
					if(ok==1){
					ButtonPosition objSent = new ButtonPosition(it);
					write(objSent);
					
						if(isMe && iAm.equals("Server")){
							a.setIcon(new ImageIcon("src/blackDot.jpg"));
							isMe = false;
							nrBileNegre--;
							Bile1.setText("Bile : " + nrBileNegre);
							a.setVisible(true);
						}
						else if(isMe && iAm.equals("Client")){
							a.setIcon(new ImageIcon("src/redDot.jpg"));
							isMe = false;
							nrBileRosii--;
							Bile2.setText("Bile : " + nrBileRosii);
							a.setVisible(true);
						}
					}
					
			}
			
				
					
			endGame(Player1, Player2);
			end++;
					
				}
			});
			
			
			
			Button.setIcon(new ImageIcon("src/button.jpg"));
			Button.setBounds(126+41*i+i, 80+j*42, 23, 23);
			Button.setBorderPainted(false);
			Button.setContentAreaFilled(false);
			Button.setFocusPainted(false);
			Button.setOpaque(false);
			Pane.add(Button);
			
		}
		
		 new Thread(new Runnable() {
		
		@Override
		public void run() {
			read();
			
		}
	}).start();;
	

		
		setTitle("Kulami");
		setResizable(false);
		setVisible(true);

		

	}
}
