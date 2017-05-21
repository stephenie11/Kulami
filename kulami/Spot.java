package kulami;

import java.io.Serializable;

/**
 *
 * @author Stefania
 */
public class Spot implements Serializable{
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

