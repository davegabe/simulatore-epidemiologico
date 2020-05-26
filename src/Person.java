import java.util.ArrayList;
import java.util.HashMap;

public class Person {
    enum Status {green, yellow, blue, red, black}
    static final int r = 10;
    public Status condition = Status.green;
    public boolean hasVirus;
    public boolean mobility;
    public int x;                                      //x, y -> person's position  in the space
    public int y;
    public Vector2 dir = new Vector2();
    private Manager manager;
    private HashMap contacts;
    private int prevX;
    private int prevY;
    private int infectedDays;
    private int symptomsDay;
    private int deathDay;

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

    public void meeting(Person other) {
        if(condition==Status.blue || other.condition==Status.blue)
            return;
        //contacts.put(manager.getDay(), contacts.getOrDefault(manager.getDay(), new ArrayList<Person>()));
        if(other.condition==Status.yellow || other.condition==Status.red){
            hasVirus = Math.random()*100<manager.infectivity; //rolling dice...
        }
    }

    public void dayEvent(){
        if(hasVirus) {
            switch (condition) {
                case green:
                    turningYellow();
                    break;
                case yellow:
                    turningRed();
                    break;
                case red:
                    turningBlack();
                    break;
            }
        }
    }

    private void turningYellow() {
        if(infectedDays==1/6*manager.duration){
            condition=Status.yellow;
            if(Math.random()*100<manager.infectivity) { //rolling dice...
                symptomsDay = (int) (Math.random() * 1 / 6 * manager.duration);
            } else {
                symptomsDay=-1;
            }
        }
    }

    private void turningBlue() {
        condition = Status.blue;
    }

    private void turningRed() {
        if(infectedDays>=1/3*manager.duration){ //if the incubation time is over
            turningBlue();
        }
        if(infectedDays==symptomsDay) { //if today is the day
            condition = Status.red;
            if(Math.random()*100<manager.letality) { //rolling dice...
                deathDay = (int) (Math.random() * (manager.duration-infectedDays));
            } else {
                deathDay=-1;
            }
        }
    }

    private void turningBlack() {
        if(infectedDays==manager.duration){ //if the sickness time is over
            turningBlue();
        }
        if(infectedDays==symptomsDay) { //if today is the day
            condition=Status.black;
        }
    }
}

