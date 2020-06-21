public class Vector2 {
    static float speed = Person.r / 2;
    public int speedX;
    public int speedY;

    Vector2() {
        do {
            int angle = (int)(Math.random()*365);
            this.speedX = (int) (Math.cos(Math.toRadians(angle))*speed);
            this.speedY = (int) (Math.sin(Math.toRadians(angle))*speed);
        } while (speedY == speedX && speedY == 0);
    }

    Vector2(int speedX, int speedY) {
        this.speedX = (int) (speedX*speed);
        this.speedY = (int) (speedY*speed);
    }

}
