import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashMap;

public class DailyGraph extends JPanel {
    private final Color gunmetal = new Color(19, 39, 53);
    HashMap<Integer, ArrayList<Integer>> daily_status = new HashMap<>();
    Manager manager;
    ArrayList<Color> colors = new ArrayList<>();
    ArrayList<Integer> situation = new ArrayList<>();

    DailyGraph(Manager manager) {
        this.manager = manager;
        setVisible(true);
        colors.add(ColorsManager.blue);
        colors.add(ColorsManager.green);
        colors.add(ColorsManager.yellow);
        colors.add(ColorsManager.red);
        colors.add(ColorsManager.black);
    }

    public void initialize() {
        ArrayList<Integer> initial = new ArrayList<>();
        initial.add(0);
        initial.add(manager.people.length-1);
        initial.add(1);
        initial.add(0);
        initial.add(0);
        daily_status.put(0, initial);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(gunmetal);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (manager.people == null)
            return;
        if(daily_status.size()>1) {
            if (manager.day == 0) {            // se è il primo giorno
                g2d.fillRect(0, 0, getWidth(), manager.people.length);                        // allora disegno solo un rettangolo tutto verde con una riga gialla.
            } else {
                g2d.setStroke(new BasicStroke(4.0f));
                g2d.setPaint(ColorsManager.green);
                float currX = 0, step = getWidth()/(daily_status.size()-1);
                GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, daily_status.size());
                path.moveTo(currX, (float)daily_status.get(0).get(1)/manager.people.length*getHeight());
                for (int d = 1; d < daily_status.size()-1; d++) {
                    //for (int c = 0; c < colors.size(); c++) {
                        float y1 = (float)daily_status.get(d).get(1)/manager.people.length*getHeight();
                        float y2 = (float)daily_status.get(d+1).get(1)/manager.people.length*getHeight();
                        path.quadTo(currX+step/2,Math.max(y1,y2),currX+step, y2);
                    //}
                    currX+=step;
                }
                g2d.draw(path);
            }
        }
        g2d.setColor(ColorsManager.gray);
        g2d.drawString("TEST", 20, 20);
    }

    public void addNewDay(){               // aggiunge al dizionario che tiene i giorni con i valori di altezza dei rettangoli un giorno.
        situation.clear();
        situation.add(manager.num_blue);
        situation.add(manager.num_green);
        situation.add(manager.num_yellow);
        situation.add(manager.num_red);
        situation.add(manager.num_black);
        daily_status.put(manager.day, situation);
        repaint();
    }

}

// in un array mi salvo ogni giorno quanti neri, blu, rossi, gialli, .. c'erano e per ogni array c'è la chiave GIORNO del dizionario così
// che per ridisegnare faccio un for in cui per ogni chiave, disegno.
