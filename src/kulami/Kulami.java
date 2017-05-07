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

    private static Panel[] panels ;
    private static Spot[][] spots;
    private static final int MATRIXLENGTH = 8;
    private static final int PANELSNR = 17;
    
    // Obtiunea pe care o alege userul ,vine de la interfata 1-start,2-help,3-exit 0-nu s a ales nimic
    private static int option = 0;
    private static int user = 0;
    private static String userName;
    
    // Al cui este randul de a muta
    private static boolean client = false;
    private static boolean server = true;
    
    // Ultimele doua mutari;
    private static Point lastMove1;
    private static Point lastMove2;
    
    // Numarul de bile ramase pentru fiecare jucator
    private static int redMarble;
    private static int blackMarble;
    
    
    // Initializare coada piese
    public static void initPanels() {
        
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
    public static void initSpotMatrix() {
        
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
    
    //Initializare joc
    public static void initGame() {
        initPanels();
        initSpotMatrix();
        lastMove1 = new Point(-1,-1);
        lastMove2 = new Point(-1,-1);
        blackMarble = redMarble = 28;
        
    }
    
    // Verifica daca jocul se sfarseste
    public static boolean endOfGame() {
        // Daca niciun jucator nu mai are bile
        if(redMarble == 0 && blackMarble == 0)
            return true;
        
        // Aici trebuie verificat daca avem locuri libere pe pozitiile care indeplinesc constrangerile
        int nrMovesAvailable = availableMoves();
        if(nrMovesAvailable == 0) return true;
            
        // Daca ajunge aici inseamna ca jocul poate continua
        return false;    
    }
    
    /* 
        Aceasta este prima miscare din joc - cea a jucatorului server
        El poate pune bile oriunde pe masa cat timp elementele transmise prin parametrul
        mutare sunt corecte Point(x,y) , x si y intre 0 si 7
    */
    public static int firstMove(Point move) {
        int x, y ;
        x = (int)move.getX();
        y = (int)move.getY();
        
        // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
        if( !(x >= 0 && x <= PANELSNR || y >= 0 && y <= PANELSNR)) return -1;
        
        spots[x][y].setStatus(1);
        lastMove1 = move;
        client = true;
        server = false;
        blackMarble --;
        return 1;
    }
     
    /*
        Aceasta este cea de-a doua miscare din joc - cea a jucatorului client
        El poate pune bile doar pe linia sau coloana coresp locului in care a pus serverul
        Mai mult el nu poate pune bile in casuta in care a pus serverul anterior
    */
    public static int secondMove(Point move) {
        int x, y ;
        x = (int)move.getX();
        y = (int)move.getY();
        // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
        if( !(x >= 0 && x <= PANELSNR || y >= 0 && y <= PANELSNR)) return -1;
        
        int lastX = (int)lastMove1.getX();
        int lastY = (int)lastMove1.getY();
        
 
        // Verifica daca locul in care a vrea sa puna clientul bila se afla pe aceeasi piesa pe care a pus serverul bila la pasul anterior
        int lastMoveIdPanel = spots[lastX][lastY].getIdPanel();
        int moveIdPanel = spots[x][y].getIdPanel();
        if(lastMoveIdPanel == moveIdPanel) return -1;
        
        // Verifica daca locul in care clientul vrea sa puna bila este pe linia si coloana care trebuie(cea coresp mutarii serverului) si daca locul este liber
        if(spots[x][y].getStatus() != 0) return -1;
        if(lastX != x && lastY != y) return -1;
        
        // Daca ajunge aici inseamna ca a trecut de constrangeri , deci locul ales este unul valid
        spots[x][y].setStatus(2);
        client = false;
        server = true;
        redMarble --;
        lastMove2 = move;
        
        return 1;
    
    }
    
    public static void showBoard() {
        for(int i = 0; i < MATRIXLENGTH; i++) {
            for(int j = 0; j < MATRIXLENGTH; j++) {
                System.out.print(spots[i][j].getStatus() + " ");
            }
            System.out.println();
        }
        System.out.println();
     
    }
    
    // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
    public static boolean validXY(int x, int y) {
        if( !(x >= 0 && x <= PANELSNR || y >= 0 && y <= PANELSNR)) return false;
        return true;
    }
    
    public static boolean validMove(Point move) {
        
        int x, y ;
        x = (int)move.getX();
        y = (int)move.getY();
        
        // Verifica daca coordonatele locului transmis prin parametru se afla pe placa de joc
        if(!(validXY(x,y))) return false;
        
        int lastX1 = (int)lastMove1.getX();
        int lastY1 = (int)lastMove1.getY();
        
        int lastX2 = (int)lastMove2.getX();
        int lastY2 = (int)lastMove2.getY();
        
        // Verifica daca locul in care jucatorul vrea sa puna bila este pe linia si coloana care trebuie(cea coresp mutarii anterioare) si daca locul este liber
        if(spots[x][y].getStatus() != 0) return false; // locul este ocupat
        if(lastX2 != x && lastY2 != y) return false; // nu se afla pe linie sau coloana
        
        // Verifica daca locul in care a vrea sa puna clientul bila se afla pe aceeasi piesa pe care a pus serverul bila la pasul anterior
        int lastMove1IdPanel = spots[lastX1][lastY1].getIdPanel();
        int lastMove2IdPanel = spots[lastX2][lastY2].getIdPanel();
        int moveIdPanel = spots[x][y].getIdPanel();
        if(lastMove1IdPanel == moveIdPanel || lastMove2IdPanel == moveIdPanel) return false;
       
        return true;
    }
    
    public static void clientMoves(Point move) {
        
        int x, y ;
        x = (int)move.getX();
        y = (int)move.getY();
        spots[x][y].setStatus(2);
        redMarble--;
        client = false;
        server = true;
        lastMove1 = lastMove2;
        lastMove2 = move;
        
    }
    
    public static void serverMoves(Point move) {
        
        int x, y ;
        x = (int)move.getX();
        y = (int)move.getY();
        spots[x][y].setStatus(1);
        blackMarble--;
        client = true;
        server = false;
        lastMove1 = lastMove2;
        lastMove2 = move;
        
    }
    
   // returneaza un int care reprezinta castigatorul ( 0 - egalitate , 1 - castigator este serverul , 2 - castigator este clientul ) 
    public static int winner() {
        int serverPoints , clientPoints ;
        serverPoints = clientPoints = 0;
        for (int i = 0; i < PANELSNR; i++) {
            int upperLeftX,upperLeftY,lowerRightX,lowerRightY;
            upperLeftX = (int) panels[i].getUpperLeft().getX();
            upperLeftY = (int) panels[i].getUpperLeft().getY();
            lowerRightX = (int) panels[i].getLowerRight().getX();
            lowerRightY = (int) panels[i].getLowerRight().getY();
            int red, black;
            red = black = 0;
            for (int k = upperLeftX; k <= lowerRightX; k++)
                for(int j = upperLeftY; j <= lowerRightY; j++) {
                    if(spots[k][j].getStatus() == 1) black++;
                    if(spots[k][j].getStatus() == 2) red ++;
                    
                }
            if(black > red) serverPoints += panels[i].getSize();
            if(black < red) clientPoints += panels[i].getSize();
        }
        System.out.println("Puncte Server : " + serverPoints);
        System.out.println("Puncte Client : " + clientPoints);
        if(serverPoints > clientPoints) return 1;
        if(serverPoints < clientPoints) return 2;
        return 0;
    }
    
    // Returneaza cate mutari disponibile exista si le afiseaza in consola
    public static int availableMoves() {
        int nrMoves = 0;
        int lastMove2X = (int)lastMove2.getX();
        int lastMove2Y = (int)lastMove2.getY();
        int lastMove1X = (int)lastMove1.getX();
        int lastMove1Y = (int)lastMove1.getY();
        
        int lastMove1IdPanel = spots[lastMove1X][lastMove1Y].getIdPanel();
        int lastMove2IdPanel = spots[lastMove2X][lastMove2Y].getIdPanel();
        int moveIdPanel;
        
        System.out.println("Available moves: ");
        // Parcurgem pe linie
        for(int i = 0 ; i < MATRIXLENGTH; i++){
            moveIdPanel = spots[lastMove2X][i].getIdPanel();
            if(spots[lastMove2X][i].getStatus() == 0 && moveIdPanel != lastMove1IdPanel  && moveIdPanel != lastMove2IdPanel) {
                   nrMoves++;
                   System.out.println("(" + lastMove2X + "," + i + ")");
            }  
        }
        //Parcurgem pe coloana
        for(int i = 0 ; i < MATRIXLENGTH; i++){
            moveIdPanel = spots[i][lastMove2Y].getIdPanel();
            if(spots[i][lastMove2Y].getStatus() == 0 && moveIdPanel != lastMove1IdPanel  && moveIdPanel != lastMove2IdPanel) {
                   nrMoves++;
                   System.out.println("(" + i + "," + lastMove2Y + ")");
            }
        }
        return nrMoves;
    }
    
   // Ce a fost in main inainte
    public static void exMain() {
        
        
        
//        public static void main(String[] args) throws FileNotFoundException {
//        
//        initGame();
//        showBoard();
//        int i ;
//        i = firstMove(new Point(2,7));
//        System.out.println(i);
//        showBoard();
//        i = secondMove(new Point(2,3));
//        System.out.println(i);
//        showBoard();
//        
//        Scanner in = new Scanner(System.in);
//        while(!(endOfGame())) {
//            showBoard();
//            if(client == true) {
//                System.out.println("Client Player Turn ");
//                Point move = new Point(-1,-1);
//                while(!(validMove(move))) {
//                    System.out.println("Please enter your move (x y) ");
//                    move.setLocation(in.nextInt(), in.nextInt());   
//                }
//                clientMoves(move);
//                
//            }
//            else {
//                System.out.println("Server Player Turn ");
//                Point move = new Point(-1,-1);
//                while(!(validMove(move))) {
//                    System.out.println("Please enter your move (x y) ");
//                    move.setLocation(in.nextInt(), in.nextInt());   
//                }
//                serverMoves(move);
//                
//            }
//        }
//        int winner = winner();
//        if(winner == 1)
//            System.out.println("Server won!");
//        else 
//            if(winner == 2)
//                 System.out.println("Client won!");
//            else
//                System.out.println("Tie");
//                
//        
//        
////        for(int i = 0; i < 17; i++)
////            System.out.println(panels[i].toString());
//            
//
//    }
        
    }
    
    public static void startGame() {
        while(user == 0) {
            
        }
        // userul este server
        if(user == 1) {
            userIsServer();
        }
        //userul este client
        if(user == 2) {
            userIsClient();
        }
        
    }
    public static void userIsServer() {
        
    }
    public static void userIsClient() { 
    
    }
    public static void help() {
        
    }
    
    public static void exitGame() {
        
    }
    
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
