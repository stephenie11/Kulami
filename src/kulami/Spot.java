/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kulami;

/**
 *
 * @author Stefania
 */
public class Spot {
    private int idPanel;
    private int status;

    public Spot() {
        idPanel = 0;
        status = 0;
    }
    public Spot(int idPanel, int status) {
        this.idPanel = idPanel;
        this.status = status;
    }

    public int getIdPanel() {
        return idPanel;
    }

    public int getStatus() {
        return status;
    }

    public void setIdPanel(int id) {
        this.idPanel = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "(" + idPanel + " , " + status + ')';
    }
    
    
    
    
    
}
