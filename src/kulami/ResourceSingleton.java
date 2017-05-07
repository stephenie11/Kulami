/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kulami;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Stefania
 */
public final class ResourceSingleton {
    
    private static ResourceSingleton instance = null;
    
    private Panel[] panels;
    private Spot[][] spots;
    
    // constante panelsnr = nr de forme de lemn , matrixlength = nr de locuri pe linie-coloana a placii de joc
    private final int MATRIXLENGTH = 8;
    private final int PANELSNR = 17;
    
    // al cui este randul , probabil o sa le elimin
    private boolean client = false;
    private boolean server = false;
    
    // ultimele doua mutari
    private Point lastMove1;
    private Point lastMove2;
    
    // Numarul de bile ramase in joc total 28+28  =56
    private int marbles;
    
    
    // Initializare coada piese
    private void initPanels() {
        
        File file = new File("Panels.in");
        
        try {
            Scanner sc = new Scanner(file);
            panels = new Panel[PANELSNR];
            int size,lowerX,lowerY,upperX,upperY;
            for(int i = 0; i < PANELSNR; i++) {
                size = sc.nextInt();
                upperX = sc.nextInt();
                upperY = sc.nextInt();
                lowerX = sc.nextInt();
                lowerY = sc.nextInt();
                panels[i] = new Panel(size,upperX,upperY,lowerX,lowerY);
            } 
            sc.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Eroare la deschiderea fiserului" + e.getMessage());
        }
        
    }
    
    //Initializare matrice locuri 
    private void initSpotMatrix() {
        
        File file = new File("BoardSpotMatrix.in");
        
        try {
            Scanner sc = new Scanner(file);
            spots = new Spot[MATRIXLENGTH][MATRIXLENGTH];
            int idPanel, spotStatus;
            for(int i = 0; i < MATRIXLENGTH; i++) 
                for(int j = 0; j < MATRIXLENGTH; j++) {
                    idPanel = sc.nextInt();
                    spotStatus = sc.nextInt();
                    spots[i][j] = new Spot(idPanel,spotStatus);
                }
            sc.close();  
        }
        catch(FileNotFoundException e) {
            System.out.println("Eroare la deschiderea fisierului " + e.getMessage());
            
        }
    }
    
    // Constructor
    private ResourceSingleton() {
        initPanels();
        initSpotMatrix();
        lastMove1 = new Point(-1,-1);
        lastMove2 = new Point(-1,-1);
        marbles = 56;
        
    }
    
    public static ResourceSingleton getInstance() {
        if(instance == null) {
            instance = new ResourceSingleton();
        }
        return instance;
    }

    public Panel getPanel(int i) {
        return panels[i];
    }

    public Spot getSpot(int i,int j) {
        return spots[i][j];
    }
    
    

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public Point getLastMove1() {
        return lastMove1;
    }

    public void setLastMove1(Point lastMove1) {
        this.lastMove1 = lastMove1;
    }

    public Point getLastMove2() {
        return lastMove2;
    }

    public void setLastMove2(Point lastMove2) {
        this.lastMove2 = lastMove2;
    }

    public int getMarbles() {
        return marbles;
    }

    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }
    
}
