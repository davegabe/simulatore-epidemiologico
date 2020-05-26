public class Manager {
    public int duration;
    public int infectivity;
    public int symptomaticity;
    private int resources;
    private float swabCost;
    private float speed;
    public Person[] people;
    public Wall[] walls;
    public Physics physics;
    public TwoDPart twoDPart;

    public static void main(String[] args) {
        new Manager().initialize();
    }

    public void initialize(){
        int nPeople = 80;
        twoDPart = new TwoDPart(this);
        int width=twoDPart.getWidth();
        int heigth=twoDPart.getHeight();
        people = new Person[nPeople];
        for (int i = 0; i < nPeople; i++) {
            people[i] = new Person((int) (Math.random()*(width - Wall.border*2 - Person.r)) + Wall.border + Person.r, (int)(Math.random()*(heigth - Wall.border*2 - Person.r)) + Wall.border + Person.r, this);
            if(i>0){    //check if overlaps other persons
                for (int j = 0; j < i; j++) {
                    double distance = Math.sqrt( Math.pow(people[i].x - people[j].x, 2) +Math.pow(people[i].y - people[j].y,2));
                    if(distance <= Person.r*2){ //if they overlaps
                        i--;
                        break;
                    }
                }
            }
        }
        walls = new Wall[4];
        walls[0] = new Wall( 0,0, width+ Wall.border, Wall.border);//top
        walls[1] = new Wall( 0,0, Wall.border, heigth+ Wall.border);//left
        walls[2] = new Wall( width,0, Wall.border,heigth+ Wall.border);//right
        walls[3] = new Wall( 0,heigth, width+ Wall.border, Wall.border);//bottom
        physics = new Physics(this, 0,width,0, heigth);
        GUI gui = new GUI(this);
        gui.initialize();
        twoDPart.initialize();
    }

    public void changeSpeed(int tick){
        twoDPart.setTick(tick);
    }
}
