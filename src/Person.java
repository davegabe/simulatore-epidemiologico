import java.util.ArrayList;
import java.util.HashMap;

public class Person {
    static final int rMax = 12;
    static final int rMin = 3;
    static int r = rMax;
    public Status condition = Status.GREEN;
    public boolean hasVirus;
    public boolean mobility = true;
    public int x;
    public int y;
    public Vector2 dir;
    private Manager manager;
    public HashMap<Integer ,ArrayList<Person>> contacts = new HashMap();
    private int prevX;
    private int prevY;
    private int infectedDays;
    private int symptomsDay;
    private int deathDay;
    public boolean swabResult = false;
    public int Vd;
    public boolean checkedContacts=false;

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
        Vd++;
        if(condition == Status.BLUE || other.condition== Status.BLUE)
            return;

        ArrayList<Person> personArrayList = contacts.getOrDefault(manager.day, new ArrayList<>());
        personArrayList.add(other);
        contacts.put(manager.day, personArrayList);

        if(other.condition== Status.YELLOW || other.condition== Status.RED){
            if(!hasVirus)
                hasVirus = Math.random()*100<=manager.infectivity; //rolling dice...
        }
    }

    public boolean doSwab(){
        if(condition==Status.RED)
            return true;
        if(condition==Status.BLACK || condition==Status.BLUE)
            return false;

        if(condition == Status.YELLOW){
            swabResult = true;
            mobility = false;
        }
        manager.resources-=manager.swabCost;
        return swabResult;
    }

    public void dayEvent(){
        if(hasVirus) {
            switch (condition) {
                case GREEN:
                    turningYellow();
                    break;
                case YELLOW:
                    turningRed();
                    break;
                case RED:
                    turningBlack();
                    break;
            }
            infectedDays++;
        }
        if(!contacts.containsKey(manager.day))
            contacts.put(manager.day, new ArrayList<>());
        if(contacts.size()>manager.duration){
            contacts.remove(manager.day-manager.duration-1);
        }
    }

    private void turningYellow() {
        if(infectedDays>=manager.duration/6){
            condition = Status.YELLOW;
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
            condition = Status.RED;
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
            condition = Status.BLACK;
        }
    }

    private void turningBlue() {
        condition = Status.BLUE;
        startMovement();
    }


    public void forceIllness(){
        hasVirus=true;
        infectedDays=manager.duration/6;
        turningYellow();
    }

    public void stopMovement(){
        mobility=false;
        dir = new Vector2(0,0);//
    }

    public void startMovement(){
        mobility=true;
        if(dir.speedX==0 && dir.speedY==0)//
            dir = new Vector2();
    }

    public boolean canMove(){
        return (mobility && condition!=Status.BLACK);
    }
}

