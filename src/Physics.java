import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Physics {
    private int x1, x2, y1, y2;
    private Manager manager;
    private QuadTree quadTree;
    private int collisionCounter=0;

    Physics(Manager manager, int x1, int x2, int y1, int y2) {
        this.manager = manager;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        quadTree = new QuadTree(x1, x2, y1, y2, -1);
    }

    Physics(Manager manager, int w, int h) {
        this.manager = manager;
        this.x1 = 0;
        this.x2 = w;
        this.y1 = 0;
        this.y2 = h;
        quadTree = new QuadTree(x1, x2, y1, y2, -1);
    }

    public void update() {
        movePlayers();
        //updateQuadTree();
        checkCollisionQuadratic();
        checkWalls();
        moveBackPlayers();
        movePlayers();
        checkForDay();
    }

    private void movePlayers() {
        for (int i = 0; i < manager.people.length; i++) {
            if(manager.people[i].dir==null)
                manager.people[i].dir = new Vector2();
            manager.people[i].move();
        }
    }

    private void updateQuadTree() {
        quadTree.clear();
        for (int i = 0; i < manager.people.length; i++) {
            quadTree.insert(manager.people[i]);
        }
        findNearPlayers();
    }


    private void findNearPlayers() {
        ArrayList<Person> returnObjects = new ArrayList();
        HashMap<Person, Boolean> alreadyCalculated = new HashMap();
        for (int i = 0; i < manager.people.length; i++) {
            returnObjects.clear();
            quadTree.retrieve(returnObjects, manager.people[i]);
            checkCollision(manager.people[i], returnObjects, alreadyCalculated);
        }
    }

    private void checkCollisionQuadratic() {
        for (int i = 0; i < manager.people.length; i++) {
            //check with other players
            for (int j = i + 1; j < manager.people.length; j++) {
                if(manager.people[i].condition== Status.black||manager.people[j].condition== Status.black) continue;
                double distance = Math.sqrt(Math.pow(manager.people[i].x - manager.people[j].x, 2) + Math.pow(manager.people[i].y - manager.people[j].y, 2));
                if (distance <= Person.r * 2) {
                    manager.people[i].meeting(manager.people[j]);
                    manager.people[j].meeting(manager.people[i]);
                    collisionCounter+=2;

                    if(!manager.people[i].mobility){
                        manager.people[j].dir.speedX *= -1;
                        manager.people[j].dir.speedY *= -1;
                    } else if(!manager.people[j].mobility){
                        manager.people[i].dir.speedX *= -1;
                        manager.people[i].dir.speedY *= -1;
                    } else {
                        int tempSpeedX = manager.people[j].dir.speedX;
                        int tempSpeedY = manager.people[j].dir.speedY;
                        manager.people[j].dir.speedX = manager.people[i].dir.speedX;
                        manager.people[j].dir.speedY = manager.people[i].dir.speedY;
                        manager.people[i].dir.speedX = tempSpeedX;
                        manager.people[i].dir.speedY = tempSpeedY;
                    }
                }
            }
        }
    }

    private void checkCollision(Person person, ArrayList<Person> otherPeople, HashMap<Person, Boolean> alreadyCalculated) {
        for (int i = 0; i < otherPeople.size(); i++) {
            if(person==otherPeople.get(i) || alreadyCalculated.containsKey(otherPeople.get(i))) continue;
            if(person.condition== Status.black||otherPeople.get(i).condition== Status.black) continue;
            double distance = Math.sqrt(Math.pow(person.x - otherPeople.get(i).x, 2) + Math.pow(person.y - otherPeople.get(i).y, 2));
            if (distance <= Person.r * 2) {
                alreadyCalculated.put(person, true);
                person.meeting(otherPeople.get(i));
                otherPeople.get(i).meeting(person);
                collisionCounter+=2;

                if(!person.mobility){
                    otherPeople.get(i).dir.speedX *= -1;
                    otherPeople.get(i).dir.speedY *= -1;
                } else if(!otherPeople.get(i).mobility){
                    person.dir.speedX *= -1;
                    person.dir.speedY *= -1;
                } else {
                    int tempSpeedX = otherPeople.get(i).dir.speedX;
                    int tempSpeedY = otherPeople.get(i).dir.speedY;
                    otherPeople.get(i).dir.speedX = person.dir.speedX;
                    otherPeople.get(i).dir.speedY = person.dir.speedY;
                    person.dir.speedX = tempSpeedX;
                    person.dir.speedY = tempSpeedY;
                }
            }
        }
    }

    private void checkWalls() {
        for (int i = 0; i < manager.people.length; i++) {
            for (int j = 0; j < 4; j++) {
                Point upper = new Point(manager.people[i].x, manager.people[i].y - Person.r);
                Point bottom = new Point(manager.people[i].x, manager.people[i].y + Person.r);
                Point left = new Point(manager.people[i].x - Person.r, manager.people[i].y);
                Point right = new Point(manager.people[i].x + Person.r, manager.people[i].y);
                if (manager.walls[j].contains(upper) || manager.walls[j].contains(bottom)) {
                    manager.people[i].dir.speedY *= -1;
                    break;
                }
                if (manager.walls[j].contains(left) || manager.walls[j].contains(right)) {
                    manager.people[i].dir.speedX *= -1;
                    break;
                }
            }
        }
    }

    private void moveBackPlayers() {
        for (int i = 0; i < manager.people.length; i++) {
            manager.people[i].moveBack();
        }
    }

    private void checkForDay(){
        if(manager.movingDuringDay<=0)
            return;
        if(collisionCounter/manager.movingDuringDay>=manager.meetings){
            manager.anotherDay();
            collisionCounter=0;
        }
    }
}
