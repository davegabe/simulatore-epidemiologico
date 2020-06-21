import java.awt.*;

public class Wall {
    private Rectangle rect;
    static int border = Person.r;

    Wall(int upperLeftX, int upperLeftY, int width, int height){
        this.rect = new Rectangle(upperLeftX, upperLeftY, width, height);
    }

    public boolean contains(Point other){
        return rect.contains(other);
    }

    public Point getUpperLeft(){
        return new Point((int)rect.getMinX(), (int)rect.getMinY());
    }
    public int getHeight(){
        return rect.height;
    }
    public int getWidth(){
        return rect.width;
    }
}
