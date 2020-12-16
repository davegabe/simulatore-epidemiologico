import java.awt.*;

public class Physics {
    private Manager manager;
    private int collisionCounter=0;

    Physics(Manager manager) {
        this.manager = manager;
    }

    public void update() {
        movePlayers();
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

    private void checkCollisionQuadratic() {
        for (int i = 0; i < manager.people.length; i++) {
            //check with other players
            for (int j = i + 1; j < manager.people.length; j++) {
                if(manager.people[i].condition == Status.BLACK || manager.people[j].condition == Status.BLACK) continue;
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
