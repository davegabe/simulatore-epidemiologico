import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class TwoDPart extends JPanel implements Runnable {
    static final int MAX_TIME = 400;
    private Manager manager;
    private Timer t;
    private int counterMsLastDay;

    public TwoDPart(Manager manager) {
        this.manager = manager;
        setVisible(true);
    }

    public void initialize() {
        Graphics g = new Graphics() {
            @Override
            public Graphics create() {
                return null;
            }

            @Override
            public void translate(int i, int i1) {

            }

            @Override
            public Color getColor() {
                return null;
            }

            @Override
            public void setColor(Color color) {

            }

            @Override
            public void setPaintMode() {

            }

            @Override
            public void setXORMode(Color color) {

            }

            @Override
            public Font getFont() {
                return null;
            }

            @Override
            public void setFont(Font font) {

            }

            @Override
            public FontMetrics getFontMetrics(Font font) {
                return null;
            }

            @Override
            public Rectangle getClipBounds() {
                return null;
            }

            @Override
            public void clipRect(int i, int i1, int i2, int i3) {

            }

            @Override
            public void setClip(int i, int i1, int i2, int i3) {

            }

            @Override
            public Shape getClip() {
                return null;
            }

            @Override
            public void setClip(Shape shape) {

            }

            @Override
            public void copyArea(int i, int i1, int i2, int i3, int i4, int i5) {

            }

            @Override
            public void drawLine(int i, int i1, int i2, int i3) {

            }

            @Override
            public void fillRect(int i, int i1, int i2, int i3) {

            }

            @Override
            public void clearRect(int i, int i1, int i2, int i3) {

            }

            @Override
            public void drawRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {

            }

            @Override
            public void fillRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {

            }

            @Override
            public void drawOval(int i, int i1, int i2, int i3) {

            }

            @Override
            public void fillOval(int i, int i1, int i2, int i3) {

            }

            @Override
            public void drawArc(int i, int i1, int i2, int i3, int i4, int i5) {

            }

            @Override
            public void fillArc(int i, int i1, int i2, int i3, int i4, int i5) {

            }

            @Override
            public void drawPolyline(int[] ints, int[] ints1, int i) {

            }

            @Override
            public void drawPolygon(int[] ints, int[] ints1, int i) {

            }

            @Override
            public void fillPolygon(int[] ints, int[] ints1, int i) {

            }

            @Override
            public void drawString(String s, int i, int i1) {

            }

            @Override
            public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i, int i1) {

            }

            @Override
            public boolean drawImage(Image image, int i, int i1, ImageObserver imageObserver) {
                return false;
            }

            @Override
            public boolean drawImage(Image image, int i, int i1, int i2, int i3, ImageObserver imageObserver) {
                return false;
            }

            @Override
            public boolean drawImage(Image image, int i, int i1, Color color, ImageObserver imageObserver) {
                return false;
            }

            @Override
            public boolean drawImage(Image image, int i, int i1, int i2, int i3, Color color, ImageObserver imageObserver) {
                return false;
            }

            @Override
            public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, ImageObserver imageObserver) {
                return false;
            }

            @Override
            public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver) {
                return false;
            }

            @Override
            public void dispose() {

            }
        };
        paintComponent(g);
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
    }

    public void setTick(int tick) {
        if (t != null) {
            t.stop();
            t = null;
        }
        if (tick < 0){
            return;
        }
        t = new Timer(tick, e -> update());
        resetCounter();
        t.start();
    }

    public void resetCounter(){
        counterMsLastDay = MAX_TIME;
    }

    public void update(){
        if(manager.physics!=null) {
            manager.physics.update();
            repaint();
            counterMsLastDay--;
            if(counterMsLastDay<0){
                manager.anotherDay();
            }
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(ColorsManager.background);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (manager.people == null)
            return;

        for (int i = 0; i < manager.people.length; i++) {
            if (manager.people[i].condition == Status.yellow) {
                g.setColor(ColorsManager.yellow);
            } else if (manager.people[i].condition == Status.green) {
                g.setColor(ColorsManager.green);
            } else if (manager.people[i].condition == Status.red) {
                g.setColor(ColorsManager.red);
            } else if (manager.people[i].condition == Status.blue) {
                g.setColor(ColorsManager.blue);
            } else if (manager.people[i].condition == Status.black) {
                g.setColor(ColorsManager.black);
            }
            g.fillOval(manager.people[i].x - Person.r, manager.people[i].y - Person.r, Person.r * 2, Person.r * 2);
        }

        for (int i = 0; i < manager.walls.length; i++) {
            g.setColor(ColorsManager.black);
            g.fillRect(manager.walls[i].getUpperLeft().x, manager.walls[i].getUpperLeft().y, manager.walls[i].getWidth(), manager.walls[i].getHeight());
        }

        g.setColor(Color.gray);
        g.drawString(String.valueOf(counterMsLastDay), 20, 20);
    }
}
