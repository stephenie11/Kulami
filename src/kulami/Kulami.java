/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kulami;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.util.Scanner;

/**
 *
 * @author Stefania
 */
public class Kulami {
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        
        ResourceSingleton r = ResourceSingleton.getInstance();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please select option : 1-server , 2 - client");
        int option = sc.nextInt();
       InetAddress ip;
       ServerMode serverThread;
       ClientMode clientThread;
       Thread t;
        if(option == 1) { 
            serverThread = new ServerMode(r);
            t = new Thread(serverThread,"Server");
            t.start();
            
        }else
        {
            clientThread = new ClientMode(r);
            t = new Thread(clientThread,"Client");
            t.start();
        }
        t.join();       
    }
    
}
