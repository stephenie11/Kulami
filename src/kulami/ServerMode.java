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
            out.close();
            in.close();
            serverSocket.close();
            
//            while(userInput == null)
//            receivedInput = in.readObject();
//            while(!(serverSocket.isClosed()) && receivedInput != null) {
//              
//                System.out.println("Mesaj de la client " + receivedInput );
//                System.out.println("Mesaj pentru client |Intoduceti coordonatele  punctelor :");
//                Point p = new Point(scIn.nextInt(),scIn.nextInt());
//                out.writeObject(p);    
//            }
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
    

