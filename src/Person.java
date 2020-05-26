import java.util.HashMap;

public class Person {
    static final int r = 10;
    public Status condition = Status.green;
    public boolean hasVirus;
    public boolean mobility;
    public int x;                                      //x, y -> person's position  in the space
    public int y;
    public Vector2 dir = new Vector2();
    Manager manager;
    private HashMap contacts;
    private int prevX;
    private int prevY;
    private int infectedDays;
    private int symptomsDay;

    public Person(int x, int y, Manager manager) {
        this.x = x;
        this.y = y;
        this.manager = manager;
    }

    public void move() {
        prevX = x;
        prevY = y;
        x += dir.speedX;
        y -= dir.speedY;
    }

    public void moveBack() {
        x = prevX;
        y = prevY;
    }

    public void meeting(Person person) {
    }

    public void turningYellow() {
        condition = Status.yellow;
    }

    public void turningBlue() {
        condition = Status.blue;
    }

    public void turningRed() {
        condition = Status.red;
    }

    public void turningBlack() {
        condition = Status.black;
    }

    enum Status {green, yellow, blue, red, black}
}

