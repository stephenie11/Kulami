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
    private Object clientSocket;
    private ServerSocket serverSocket;

    public ServerMode() {
    }
    
    ServerMode(ResourceSingleton resources, InetAddress ipAddress,int portNumber) {
        this.resources = resources;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        //new Thread(this,"Server").start();
    }
    

    @Override
    public void run() {
//          public ServerSocket(int port,int backlog,InetAddress bindAddr)
//             throws IOException
//          port - the port number, or 0 to use a port number that is automatically allocated.
//          backlog - requested maximum length of the queue of incoming connections.
//          bindAddr - the local InetAddress the server will bind to
        
        try {
            //serverSocket  = new ServerSocket(portNumber,1,ipAddress);
            serverSocket  = new ServerSocket(8080);
            Socket clientSocket = serverSocket.accept();
            ObjectOutputStream out = 
                    new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = 
                    new ObjectInputStream(clientSocket.getInputStream());
            
            Scanner scIn = new Scanner(System.in);
            Object userInput = null, receivedInput = null;
            
            
            receivedInput = in.readObject();
            while(!(serverSocket.isClosed()) && receivedInput != null) {
              
                System.out.println("Mesaj de la client " + receivedInput );
                System.out.println("Mesaj pentru client |Intoduceti coordonatele  punctelor :");
                Point p = new Point(scIn.nextInt(),scIn.nextInt());
                out.writeObject(p);    
            }
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
        
        try {
            serverSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ServerMode.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println(serverSocket.isClosed());

            System.out.println("Conexiunea cu clientul a fost inchisa");


     
        }    
    }
    

