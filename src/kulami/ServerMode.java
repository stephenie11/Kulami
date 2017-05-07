/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kulami;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Stefania
 */
public class ServerMode implements Runnable {
    
    private ResourceSingleton resources;
    private int portNumber;
    private InetAddress ipAddress;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private final int PANELSNR = 17;
    private final int MATRIXLENGTH = 8;
    public ServerMode(ResourceSingleton resources) {
        this.resources = resources;
    }
    
    ServerMode(ResourceSingleton resources, InetAddress ipAddress,int portNumber) {
        this.resources = resources;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        //new Thread(this,"Server").start();
    }
    
    private int firstMoveServer(Point move) {
        int x  = move.getX();
        int y = move.getY();
        // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
        if( !(x >= 0 && x <= PANELSNR || y >= 0 && y <= PANELSNR)) return -1;
        resources.getSpot(x, y).setStatus(1);
        resources.setLastMove1(move);
        resources.setMarbles((resources.getMarbles()-1));
//        client = true;
//        server = false;
         return 1;
        
    }
    
    // Afiseaza boardul
    public void showBoard() {
        for(int i = 0; i < MATRIXLENGTH; i++) {
            for(int j = 0; j < MATRIXLENGTH; j++) {
                System.out.print(resources.getSpot(i, j).getStatus() + " ");
            }
            System.out.println();
        }
        System.out.println();
     
    }
    
    // inregistreaza schimbarile provenite de la prima mutare pe care o face openentul(clientul)
    public void updateClientFirstMove(Point move) {
        int x = move.getX();
        int y = move.getY();
        resources.getSpot(x, y).setStatus(2);
        resources.setMarbles(resources.getMarbles() - 1);
        resources.setLastMove2(move);
    }
     
    // inregistreaza schimbarile provenite de la mutarile adversarului (clientului)
    public void updateClientMove(Point move) {
        int x = move.getX();
        int y = move.getY();
        resources.getSpot(x, y).setStatus(2);
        resources.setMarbles(resources.getMarbles() - 1);
        resources.setLastMove1(resources.getLastMove2());
        resources.setLastMove2(move);
        
    }
    
    // inregistreaza schimbarile provenite de la mutarea pe care userul o face
    public void updateServerMove(Point move) {
        int x, y ;
        x = move.getX();
        y = move.getY();
        resources.getSpot(x, y).setStatus(1);
        resources.setMarbles(resources.getMarbles() - 1);
        resources.setLastMove1(resources.getLastMove2());
        resources.setLastMove2(move);
//        client = true;
//        server = false;
    }
    // Returneaza cate mutari disponibile exista si le afiseaza in consola
    public int availableMoves() {
        int nrMoves = 0;
        int lastMove2X = resources.getLastMove2().getX();
        int lastMove2Y = resources.getLastMove2().getY();
        int lastMove1X = resources.getLastMove1().getX();
        int lastMove1Y = resources.getLastMove1().getY();
        int lastMove1IdPanel = resources.getSpot(lastMove1X, lastMove1Y).getIdPanel();
        int lastMove2IdPanel = resources.getSpot(lastMove2X, lastMove2Y).getIdPanel();
        int moveIdPanel;
        
        System.out.println("Available moves: ");
        // Parcurgem pe linie
        for(int i = 0 ; i < MATRIXLENGTH; i++){
            moveIdPanel = resources.getSpot(lastMove2X, i).getIdPanel();
            if(resources.getSpot(lastMove2X, i).getStatus() == 0 && moveIdPanel != lastMove1IdPanel  && moveIdPanel != lastMove2IdPanel) {
                   nrMoves++;
                   System.out.println("(" + lastMove2X + "," + i + ")");
            }  
        }
        //Parcurgem pe coloana
        for(int i = 0 ; i < MATRIXLENGTH; i++){
            moveIdPanel = resources.getSpot(i, lastMove2Y).getIdPanel();
            if(resources.getSpot(i, lastMove2Y).getStatus() == 0 && moveIdPanel != lastMove1IdPanel  && moveIdPanel != lastMove2IdPanel) {
                   nrMoves++;
                   System.out.println("(" + i + "," + lastMove2Y + ")");
            }
        }
        return nrMoves;
    }
    
    // Verifica daca jocul se sfarseste
    public boolean endOfGame() {
        // Daca niciun jucator nu mai are bile
        if(resources.getMarbles() == 0)
            return true;
        
        // Aici trebuie verificat daca avem locuri libere pe pozitiile care indeplinesc constrangerile
        int nrMovesAvailable = availableMoves();
        if(nrMovesAvailable == 0) return true;
            
        // Daca ajunge aici inseamna ca jocul poate continua
        return false;    
    }
    
    // Functia winner decide cine este castigatorul jocului 0-tie 1-server 2 - client
    // aici serverul este user deci se afiseaza ai castigat daca functia returneaza 1
        public int winner() {
        int serverPoints , clientPoints ;
        serverPoints = clientPoints = 0;
        for (int i = 0; i < PANELSNR; i++) {
            int upperLeftX,upperLeftY,lowerRightX,lowerRightY;
            upperLeftX = resources.getPanel(i).getUpperLeft().getX();
            upperLeftY = resources.getPanel(i).getUpperLeft().getY();
            lowerRightX = resources.getPanel(i).getLowerRight().getX();
            lowerRightY = resources.getPanel(i).getLowerRight().getY();
            int red, black;
            red = black = 0;
            for (int k = upperLeftX; k <= lowerRightX; k++)
                for(int j = upperLeftY; j <= lowerRightY; j++) {
                    if(resources.getSpot(k, j).getStatus() == 1) black++;
                    if(resources.getSpot(k, j).getStatus() == 2) red ++;         
                }
            if(black > red) serverPoints += resources.getPanel(i).getSize();
            if(black < red) clientPoints += resources.getPanel(i).getSize();
        }
        System.out.println("Puncte Server : " + serverPoints);
        System.out.println("Puncte Client : " + clientPoints);
        if(serverPoints > clientPoints) return 1;
        if(serverPoints < clientPoints) return 2;
        return 0;
    }
        
    // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
    public boolean validXY(int x, int y) {
        if( !(x >= 0 && x <= PANELSNR || y >= 0 && y <= PANELSNR)) return false;
        return true;
    }
    
    public boolean validMove(Point move) {
        
        int x, y ;
        x = move.getX();
        y = move.getY();
        
        // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
        if(!(validXY(x,y))) return false;
        
        int lastX1 = resources.getLastMove1().getX();
        int lastY1 = resources.getLastMove1().getY();
        int lastX2 = resources.getLastMove2().getX();
        int lastY2 = resources.getLastMove2().getY();
        
        // Verifica daca locul in care jucatorul vrea sa puna bila este pe linia si coloana care trebuie(cea coresp mutarii anterioare) si daca locul este liber
        if(resources.getSpot(x, y).getStatus() != 0) return false; // locul este ocupat
        if(lastX2 != x && lastY2 != y) return false; // nu se afla pe linie sau coloana
        
        // Verifica daca locul in care a vrea sa puna clientul bila se afla pe aceeasi piesa pe care a pus serverul bila la pasul anterior
        int lastMove1IdPanel = resources.getSpot(lastX1, lastY1).getIdPanel();
        int lastMove2IdPanel = resources.getSpot(lastX2, lastY2).getIdPanel();
        int moveIdPanel = resources.getSpot(x, y).getIdPanel();
        if(lastMove1IdPanel == moveIdPanel || lastMove2IdPanel == moveIdPanel) return false;
       
        return true;
    }
    
  
    
    @Override
    public void run() {
//          public ServerSocket(int port,int backlog,InetAddress bindAddr)
//             throws IOException
//          port - the port number, or 0 to use a port number that is automatically allocated.
//          backlog - requested maximum length of the queue of incoming connections.
//          bindAddr - the local InetAddress the server will bind to
        
        try {
            
            Scanner scIn = new Scanner(System.in);
            //serverSocket  = new ServerSocket(portNumber,1,ipAddress);
            serverSocket  = new ServerSocket(8080);
            clientSocket = serverSocket.accept();
            
            ObjectOutputStream out = 
                    new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = 
                    new ObjectInputStream(clientSocket.getInputStream());
            
            
            Point userInput = null;
            Point receivedInput = null;
            Point move  = new Point();
            
            // Asteapta ca userul server sa introduca o locatie valida in care sa puna bila 
            // Fiind prima miscare din joc , userul poate pune oriunde pe placa doar sa nu depaseasca limitele acesteia
            while(firstMoveServer(move) == -1) {
                System.out.println("Introduceti coordonatele primei miscari: ");
                move.setLocation(scIn.nextInt(), scIn.nextInt());
                firstMoveServer(move);
            }

            // Daca a ajuns aici inseamna ca s-a introdus prima miscare valida . Aceasta trebuie transmisa clientului
            out.writeObject(move); // trimite miscarea facuta de server clientului
            
            // Aici urmeaza loopul cand userul asteapta sa primeasca raspunsul de la oponent-pozitia pe care acela a plasa bila
            while(receivedInput == null) {
                receivedInput = (Point)in.readObject();
            }
            
            // Daca ajunge aici inseamna ca a primit miscarea clientului(a doua miscare din joc)
            // Functia aceasta inregistreaza miscarea facuta de adversar in resursa userului
            updateClientFirstMove(receivedInput);
            showBoard();
            
            // Aici va fi loopul cu mutarile server-client se va termina cand nu mai sunt miscari disponibile sau cand userul nu mai poate muta
            while(!(endOfGame())) {
                move = new Point();
                receivedInput = null;
                System.out.println("Miscari valabile");
                availableMoves();
                while(!(validMove(move))) {
                    System.out.println("Your turn move | Introduceti coordonatele miscarii");
                    move.setLocation(scIn.nextInt(), scIn.nextInt());
                }
                updateServerMove(move);
                out.writeObject(move);
                while(!(serverSocket.isClosed()) && receivedInput == null) {
                    receivedInput = (Point) in.readObject();
                }
                updateClientMove(receivedInput);
            }
            int winner = winner();
            if(winner == 0) System.out.println("Tie");
            if(winner == 1) System.out.println("You won!");
            if(winner == 2) System.out.println("You lost!");
            
            out.close();
            in.close();
            serverSocket.close();
        }
        catch(IOException e) {
       
            if(serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                }
                catch(IOException e1) {
                    e1.getMessage();
                }
            }  
        }
        catch (ClassNotFoundException ex) {   
            Logger.getLogger(ServerMode.class.getName()).log(Level.SEVERE, null, ex);
        } 
        System.out.println("Conexiunea cu clientul a fost inchisa");
    }    
}
    

