/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kulami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefania
 */
public class ClientMode implements Runnable {
    private ResourceSingleton resources;
    private InetAddress ipAddress;
    private int portNumber;
    Socket echoSocket;
    private final int PANELSNR = 17;
    private final int MATRIXLENGTH  = 8;
    
    
    public ClientMode(ResourceSingleton resources, InetAddress ipAddress, int portNumber) {
        this.resources = resources;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber; 
        //new Thread(this,"Client").start();
    }

    public ClientMode(ResourceSingleton resources) {
        this.resources = resources;
    }
    
    /*
        Aceasta este cea de-a doua miscare din joc - cea a jucatorului client
        El poate pune bile doar pe linia sau coloana coresp locului in care a pus serverul
        Mai mult el nu poate pune bile in casuta in care a pus serverul anterior
    */
    private int firstMoveClient(Point move) {
        int x = move.getX();
        int y = move.getY();
        // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
        if( !(x >= 0 && x <= PANELSNR || y >= 0 && y <= PANELSNR)) return -1;
        
        //Pozitia locului in care serverul a pus bila anterior
        int lastX = resources.getLastMove1().getX();
        int lastY = resources.getLastMove1().getY();
        
        // Verifica daca locul in care clientul vrea sa puna bila este pe linia si coloana care trebuie(cea coresp mutarii serverului) si daca locul este liber
        if(resources.getSpot(x, y).getStatus() != 0) return -1;
        if(lastX != x && lastY != y) return -1;
        
        // Verifica daca locul in care a vrea sa puna clientul bila se afla pe aceeasi piesa pe care a pus serverul bila la pasul anterior
        int lastMoveIdPanel = resources.getSpot(lastX, lastY).getIdPanel();
        int moveIdPanel = resources.getSpot(x, y).getIdPanel();
        if(lastMoveIdPanel == moveIdPanel) return -1;
        
        // Daca ajunge aici inseamna ca a trecut de constrangeri , deci locul ales este unul valid
        resources.getSpot(x, y).setStatus(2);
        resources.setMarbles(resources.getMarbles() - 1);
        resources.setLastMove2(move);
        
        return 1;
//        client = false;
//        server = true;    
    }

    // functia modifica tabla de joc si ultimele doua miscari care corescp miscarii primite de la celalalt jucator. miscarea se considera valida deoarece ea a fost validata de celalalt jucator
    private void updateServerMove(Point move) {
       
        int x = move.getX();
        int y = move.getY();
        resources.getSpot(x, y).setStatus(1);
        resources.setLastMove1(resources.getLastMove2());
        resources.setLastMove2(move);
        resources.setMarbles(resources.getMarbles() - 1);
    }
    
    //inregistreaza schimbarile survenite de efectuarea unei miscari
        public void updateClientMove(Point move) {
        int x = move.getX();
        int y = move.getY();
        resources.getSpot(x, y).setStatus(2);
        resources.setMarbles(resources.getMarbles() - 1);
        resources.setLastMove1(resources.getLastMove2());
        resources.setLastMove2(move);     
    }
    
    //
    private void updateServerFirstMove(Point move) {
       
        int x = move.getX();
        int y = move.getY();
        resources.getSpot(x, y).setStatus(1);
        resources.setLastMove1(move);
        resources.setMarbles(resources.getMarbles() - 1);
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
        
        try {
            
            Scanner scIn = new Scanner(System.in);
            //echoSocket = new Socket(ipAddress,portNumber);
            echoSocket = new Socket("localhost",8080);
            ObjectOutputStream out = 
                    new ObjectOutputStream(echoSocket.getOutputStream());
            ObjectInputStream in = 
                    new ObjectInputStream(echoSocket.getInputStream());
            
            Point userInput = null;
            Point receivedInput = null;
            
            // Asteapta sa primeasca prima miscare de la server 
            while(receivedInput == null) {
                receivedInput = (Point)in.readObject();
            }
            
            // Cand ajunge aici inseamna ca a primit mutarea care a facut o serverul(prima din joc)
            //updateaza resursele pentru motarea openentului
            updateServerFirstMove(receivedInput);
            
            //Roaga clientul sa si spuna prima miscare
            Point move  = new Point();
             do{
                showBoard();
                System.out.println("Introduceti coordonatele primei miscari: ");
                move.setLocation(scIn.nextInt(), scIn.nextInt());
                System.out.println(move.toString());
            } while(firstMoveClient(move) == -1);
             
            // Daca a ajuns aici inseamna ca s-a introdus prima miscare valida . Aceasta trebuie transmisa serverului
            out.writeObject(move); // trimite miscarea facuta serverului
            System.out.println("Prima miscare efectuata client");
            showBoard();
            while(!(endOfGame())) {
                // trebuie sa primeasca miscare de la server
                move = new Point();
                receivedInput = null;
                while(!(echoSocket.isClosed()) && receivedInput == null) {
                    receivedInput = (Point) in.readObject();
                }
                // a primit miscarea
                updateServerMove(receivedInput);
                System.out.println("Miscari valabile");
                availableMoves();
                while(!(validMove(move))) {
                    System.out.println("Your turn move | Introduceti coordonatele miscarii");
                    move.setLocation(scIn.nextInt(), scIn.nextInt());
                }
                updateClientMove(move);
                out.writeObject(move);
                
            }
            
            
            echoSocket.close();
            in.close();
            out.close();
            System.out.println("Am inchis conexiunea cu serverul");
        }
        catch(IOException e) {
            if(echoSocket.isClosed()) {
                try {
                    echoSocket.close();
                }
                catch(IOException e1) {
                    e1.getMessage();
                }
            }  
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientMode.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
    
}
