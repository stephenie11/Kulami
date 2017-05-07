/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kulami;

import java.io.Serializable;

/**
 *
 * @author Stefania
 */
public class Point implements Serializable{
    private int x;
    private int y;
    
    Point() {
        
    }
    
    Point(int x,int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLocation(int x, int y) {
        this.y = y;
        this.x = x;
    }

    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
    
}
