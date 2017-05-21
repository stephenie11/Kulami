package kulami;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author Stefania
 */
public class Panel implements Serializable{
    
    private int size;
    private Point upperLeft , lowerRight;

    public Panel(int size, int upperLeftX, int upperLeftY, int lowerRightX, int lowerRightY) {
        this.size = size;
        upperLeft = new Point();
        upperLeft.setLocation(upperLeftX, upperLeftY);
        lowerRight = new Point();
        lowerRight.setLocation(lowerRightX, lowerRightY);
    }
    
  
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Point getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(Point upperLeft) {
        this.upperLeft = upperLeft;
    }

    public Point getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(Point lowerRight) {
        this.lowerRight = lowerRight;
    }

    @Override
    public String toString() {
        return "Panel{" + "size=" + size + "\n upperX=" + upperLeft.getX() + ", upperY=" + upperLeft.getY() + "\n lowerX=" + lowerRight.getX()+ ", lowerY=" + lowerRight.getY() + + '}';
    }
    
}
