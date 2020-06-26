import javax.swing.*;
import java.awt.*;

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
            if (manager.people[i].condition == Status.YELLOW) {
                g.setColor(ColorsManager.yellow);
            } else if (manager.people[i].condition == Status.GREEN) {
                g.setColor(ColorsManager.green);
            } else if (manager.people[i].condition == Status.RED) {
                g.setColor(ColorsManager.red);
            } else if (manager.people[i].condition == Status.BLUE) {
                g.setColor(ColorsManager.blue);
            } else if (manager.people[i].condition == Status.BLACK) {
                g.setColor(ColorsManager.black);
            }
            g.fillOval(manager.people[i].x - Person.r, manager.people[i].y - Person.r, Person.r * 2, Person.r * 2);
        }

        for (int i = 0; i < manager.walls.length; i++) {
            g.setColor(ColorsManager.black);
            g.fillRect(manager.walls[i].getUpperLeft().x, manager.walls[i].getUpperLeft().y, manager.walls[i].getWidth(), manager.walls[i].getHeight());
        }

        g.setColor(ColorsManager.gray);
        g.drawString(String.valueOf(counterMsLastDay), 20, 20);
    }
}
