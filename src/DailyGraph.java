import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;

public class DailyGraph extends JPanel {
    public int dimension;
    private final Color gunmetal = new Color(19, 39, 53);
    HashMap<Integer, ArrayList<Integer>> daily_status = new HashMap<>();
    Manager manager;
    ArrayList<Color> colors = new ArrayList<>();
    ArrayList<Integer> situation = new ArrayList<>();

    DailyGraph(Manager manager) {
        this.dimension = this.getWidth();
        this.manager = manager;
        setVisible(true);
        final Color yellow = new Color(233, 196, 106);
        final Color green = new Color(42, 157, 143);
        final Color red = new Color(231, 111, 81);
        final Color blue = new Color(45, 143, 171);
        final Color black = new Color(26, 24, 27);
        colors.add(blue);
        colors.add(green);
        colors.add(yellow);
        colors.add(red);
        colors.add(black);
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

        daily_status.put(0, setUp());
        paintComponent(g);
    }


    public void paintComponent(Graphics g) {
        g.setColor(gunmetal);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (manager.people == null)
            return;

        getSituation();
        if(daily_status.size()>0) {
            if (manager.day == 0) {            // se è il primo giorno
                g.fillRect(0, 0, dimension, manager.people.length);                        // allora disegno solo un rettangolo tutto verde con una riga gialla.
            } else {
                int currX = 0;
                for (int i = 0; i < colors.size(); i++) {
                    g.setColor(colors.get(i));
                    g.fillRect(0, 0, dimension / manager.day, situation.get(i));
                }
            }
        }
    }

    private void getSituation(){
        situation.clear();
        situation.add(manager.num_blue);
        situation.add(manager.num_green);
        situation.add(manager.num_yellow);
        situation.add(manager.num_red);
        situation.add(manager.num_black);
    }

    private ArrayList<Integer> setUp() {
        ArrayList<Integer> initial = new ArrayList<>();
        initial.add(0);
        initial.add(manager.people.length-1);
        initial.add(1);
        initial.add(0);
        initial.add(0);
        return initial;
    }

    public void addDay(){               // aggiunge al dizionario che tiene i giorni con i valori di altezza dei rettangoli un giorno.
        getSituation();
        daily_status.put(manager.day, situation);
    }

}

// in un array mi salvo ogni giorno quanti neri, blu, rossi, gialli, .. c'erano e per ogni array c'è la chiave GIORNO del dizionario così
// che per ridisegnare faccio un for in cui per ogni chiave, disegno.
