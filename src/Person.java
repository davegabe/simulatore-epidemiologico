import java.util.ArrayList;
import java.util.HashMap;

public class Person {
    enum Status {green, yellow, blue, red, black}
    static final int rMax = 12;
    static int r = rMax;
    public Status condition = Status.green;
    public boolean hasVirus;
    public boolean mobility = true;
    public int x;                                      //x, y -> person's position  in the space
    public int y;
    public Vector2 dir;
    private Manager manager;
    private HashMap contacts = new HashMap();
    private int prevX;
    private int prevY;
    private int infectedDays;
    private int symptomsDay;
    private int deathDay;
    public boolean swabResult = false;
    public int meetingsDay;

    public Person(int x, int y, Manager manager) {
        this.x = x;
        this.y = y;
        this.manager = manager;
    }

    public void move() {
        if(!canMove())
            return;
        prevX = x;
        prevY = y;
        x += dir.speedX;
        y -= dir.speedY;
    }

    public void moveBack() {
        if(!canMove())
            return;
        x = prevX;
        y = prevY;
    }

    public void meeting(Person other) {
        meetingsDay++;
        if(condition== Status.blue || other.condition== Status.blue)
            return;

        ArrayList<Person> personArrayList = (ArrayList<Person>) contacts.getOrDefault(manager.day, new ArrayList<Person>());
        personArrayList.add(other);
        contacts.put(manager.day, personArrayList);

        if(other.condition== Status.yellow || other.condition== Status.red){
            if(!hasVirus)
                hasVirus = Math.random()*100<=manager.infectivity; //rolling dice...
        }
    }

    public boolean doSwab(){
        if(condition == Status.red || condition == Status.yellow){
            swabResult = true;
        }
        manager.resources-=manager.swabCost;
        return swabResult;
    }

    public Status dayEvent(){
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
            infectedDays++;
        }
        if(!contacts.containsKey(manager.day))
            contacts.put(manager.day, new ArrayList<Person>());
        if(contacts.size()>manager.duration){
            contacts.remove(manager.day-manager.duration-1);
        }
        return condition;
    }

    private void turningYellow() {
        if(infectedDays>=manager.duration/6){
            condition = Status.yellow;
            if(manager.symptomaticQuality>0 && Math.random()*100<=manager.symptomaticQuality) { //rolling dice...
                symptomsDay = (int) (infectedDays + 1 + (Math.random() * 1 / 6 * manager.duration));
            } else {
                symptomsDay=-1;
            }
        }
    }

    private void turningRed() {
        if(infectedDays>manager.duration/3){ //if the incubation time is over
            turningBlue();
        }
        if(infectedDays==symptomsDay) { //if today is the day
            condition = Status.red;
            stopMovement();
            if(manager.letality>0 && Math.random()*100<=manager.letality) { //rolling dice...
                deathDay = (int) (infectedDays + 1 + (Math.random() * (manager.duration-infectedDays)));
            } else {
                deathDay=-1;
            }
        }
    }

    private void turningBlack() {
        if(infectedDays>manager.duration){ //if the sickness time is over
            turningBlue();
        }
        if(infectedDays==deathDay) { //if today is the day
            condition= Status.black;
        }
    }

    private void turningBlue() {
        condition = Status.blue;
        startMovement();
    }


    public void forceIllness(){
        hasVirus=true;
        infectedDays=manager.duration/6;
        turningYellow();
    }

    public void stopMovement(){
        mobility=false;
        dir = new Vector2(0,0);
    }

    public void startMovement(){
        mobility=true;
        if(dir.speedX==0 && dir.speedY==0)
            dir = new Vector2();
    }

    public boolean canMove(){
        return (mobility && condition!=Status.black);
    }
}

