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
    
    
    public ClientMode(ResourceSingleton resources, InetAddress ipAddress, int portNumber) {
        this.resources = resources;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber; 
        //new Thread(this,"Client").start();
    }

    public ClientMode() {
    }
    

    @Override
    public void run() {
        Scanner scIn = new Scanner(System.in);
        
        try {
            //echoSocket = new Socket(ipAddress,portNumber);
            echoSocket = new Socket("localhost",8080);
            int nr = 2;
            ObjectOutputStream out = 
                    new ObjectOutputStream(echoSocket.getOutputStream());
            ObjectInputStream in = 
                    new ObjectInputStream(echoSocket.getInputStream());
            Object userInput, receivedInput;
            
            while (nr != 0) {
                Point p = new Point(scIn.nextInt(),scIn.nextInt());
                out.writeObject(p); // trimit la server obiectul (point)
                receivedInput = in.readObject();
                System.out.println("Mesaj de la server = " + receivedInput);
                nr--;
            }
           
            echoSocket.close();
            
            System.out.println("Am inchis conexiunea cu serverul");
   
        } catch (IOException ex) {
            Logger.getLogger(ClientMode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientMode.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
    
}
