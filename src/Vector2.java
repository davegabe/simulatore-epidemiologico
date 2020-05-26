public class Vector2 {
    static final int speed = Person.r / 3;
    public int speedX;
    public int speedY;

    Vector2(int speedX, int speedY) {
        this.speedX = speedX * speed;
        this.speedY = speedY * speed;
    }

    Vector2() {
        do {
            int angle = (int)(Math.random()*365);
            this.speedX = (int) (Math.cos(Math.toRadians(angle))*speed);
            this.speedY = (int) (Math.sin(Math.toRadians(angle))*speed);
        } while (speedY == speedX && speedY == 0);
    }

}
