public class Manager {
    public int duration;
    public int infectivity;
    public int symptomaticQuality;
    public int letality;
    public Person[] people;
    public Wall[] walls;
    public Physics physics;
    public TwoDPart twoDPart;
    private GUI gui;
    private int resources;
    private float swabCost;
    public float meetings;

    public int day=0;

    public int num_green = 0;
    public int num_yellow = 0;
    public int num_red = 0;
    public int num_blue = 0;
    public int num_black = 0;

    public static void main(String[] args) {
        new Manager().start();
    }
    public void start(){
        twoDPart = new TwoDPart(this);
        gui = new GUI(this);
        gui.initialize();
    }

    public void initialize(int population, int duration, int infectivity, int symptomaticQuality, int letality, float swabCost, float meetings, int resources) {
        int width = twoDPart.getWidth();
        int heigth = twoDPart.getHeight();
        people = new Person[population];
        this.duration= duration;
        this.infectivity= infectivity;
        this.symptomaticQuality=symptomaticQuality;
        this.letality=letality;
        this.swabCost=swabCost;
        this.meetings=meetings;
        this.resources=resources;

        walls = new Wall[4];
        walls[0] = new Wall(0, 0, width + Wall.border, Wall.border);        //top
        walls[1] = new Wall(0, 0, Wall.border, heigth + Wall.border);       //left
        walls[2] = new Wall(width-Wall.border, 0, Wall.border, heigth + Wall.border);           //right
        walls[3] = new Wall(0, heigth-Wall.border, width + Wall.border, Wall.border);           //bottom

        for (int i = 0; i < population; i++) {
            people[i] = new Person((int) (Math.random() * (width - Wall.border * 2 - Person.r * 2)) + Wall.border + Person.r, (int) (Math.random() * (heigth - Wall.border * 2 - Person.r * 2)) + Wall.border + Person.r, this);
            if (i > 0) {    //check if overlaps other persons
                for (int j = 0; j < i; j++) {
                    double distance = Math.sqrt(Math.pow(people[i].x - people[j].x, 2) + Math.pow(people[i].y - people[j].y, 2));
                    if (distance <= Person.r * 3) { //if they overlaps
                        i--;
                        break;
                    }
                }
            }
        }
        people[0].forceIllness();

        physics = new Physics(this, 0, width, 0, heigth);
        twoDPart.initialize();
    }

    public void changeSpeed(int tick) {
        twoDPart.setTick(tick);
    }

    public void destroy(){
        people = null;
        physics = null;
        twoDPart.repaint();
        twoDPart.setTick(-1);
    }

    public void anotherDay(){
        day++;
        num_green=0;
        num_yellow=0;
        num_red=0;
        num_blue=0;
        num_black=0;
        int lastBlack=-1;
        for (int i = 0; i < people.length; i++) {
            switch (people[i].dayEvent()){
                case green:
                    num_green+=1;
                    break;
                case yellow:
                    num_yellow+=1;
                    break;
                case red:
                    num_red+=1;
                    break;
                case blue:
                    num_blue+=1;
                    break;
                case black:
                    //order array so dead people are on top of array
                    if(num_green==0 && num_yellow==0 && num_red==0 && num_blue==0){
                        lastBlack=i;
                    }
                    else {
                        lastBlack++;
                        Person temp = people[lastBlack];
                        people[lastBlack]=people[i];
                        people[i]=temp;
                    }
                    //
                    num_black+=1;
                    break;
            }
            if (!people[i].canMove()){
                resources--;
            }
        }
        gui.recreateBar();
        gui.updateDayCounter();
    }
}
