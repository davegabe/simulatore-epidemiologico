public class Manager {
    final static int MAX_PEOPLE = 5000;
    public int duration;
    public int infectivity;
    public int symptomaticQuality;
    public int letality;
    public Person[] people;
    public Wall[] walls;
    public Physics physics;
    public TwoDPart twoDPart;
    public DailyGraph dailyGraph;
    public float meetings;
    public int movingDuringDay;
    public int day = 0;
    public int Vd;
    public int num_green;
    public int num_yellow;
    public int num_red;
    public int num_blue;
    public int num_black;
    public int resources;
    public float swabCost;
    private GUI gui;
    private Strategy chosenStrategy;
    private boolean isStrategyStarted;
    private boolean doStrategy;
    private int height;

    public static void main(String[] args) {
        new Manager().start();
    }

    public void start() {
        twoDPart = new TwoDPart(this);
        dailyGraph = new DailyGraph(this);
        gui = new GUI(this);
        gui.initialize();
    }

    public void initialize(int population, int duration, int infectivity, int symptomaticQuality, int letality, float swabCost, float meetings, int resources, int strategy) throws InputException {
        if (population > MAX_PEOPLE) throw new InputException("Too many people");
        int width = twoDPart.getWidth();
        height = twoDPart.getHeight();
        this.chosenStrategy = Strategy.values()[strategy];

        if(chosenStrategy==Strategy.LAZARETTO)
            height*=0.8;

        people = new Person[population];
        this.duration = duration;
        this.infectivity = infectivity;
        this.symptomaticQuality = symptomaticQuality;
        this.letality = letality;
        this.swabCost = swabCost;
        this.meetings = meetings;
        this.resources = resources;
        isStrategyStarted = false;
        doStrategy = true;
        day = 0;
        gui.updateDayCounter();

        int maxRow = (int) Math.ceil(Math.sqrt(population));
        int stepX = (width - 2 * (Person.r + Wall.border)) / maxRow - 1;
        int stepY = (height - 2 * (Person.r + Wall.border)) / maxRow - 1;
        int currX = (width - (stepX * (maxRow - 1))) / 2;
        int currY = (height - (stepY * (maxRow - 1))) / 2;

        for (int i = 0; i < population; i++) {
            people[i] = new Person(currX, currY, this);
            currX += stepX;
            if ((i + 1) % maxRow == 0) {
                currX = (width - (stepX * (maxRow - 1))) / 2;
                currY += stepY;
            }
        }
        people[(int) (Math.random() * (population - 1))].forceIllness();
        movingDuringDay = population;

        if (stepX <= Person.rMin*2 || stepY <= Person.rMin*2) {
            destroy();
            throw new InputException("Too many people");
        } else {
            Person.r = Math.max(Math.min(Math.min(stepX, stepY) / 4, Person.rMax), Person.rMin);
            Vector2.speed = Math.max(Person.r / 2, 4);
            Wall.border = Person.r*2;
        }

        walls = new Wall[4];
        walls[0] = new Wall(0, 0, width, Wall.border);                      //top
        walls[1] = new Wall(0, 0, Wall.border, height);                     //left
        walls[2] = new Wall(width - Wall.border, 0, Wall.border, height);   //right
        walls[3] = new Wall(0, height - Wall.border, width, Wall.border);   //bottom

        physics = new Physics(this, 0, width, 0, height);
        twoDPart.initialize();
        dailyGraph.initialize();
    }

    public void changeSpeed(int tick) {
        twoDPart.setTick(tick);
    }

    public void destroy() {
        people = null;
        twoDPart.repaint();
        twoDPart.setTick(-1);
        physics = null;
        Person.r = Person.rMax;
    }

    public void anotherDay() {
        day++;
        movingDuringDay = 0;
        num_green = 0;
        num_yellow = 0;
        num_red = 0;
        num_blue = 0;
        num_black = 0;
        Vd = 0;
        int lastBlack = -1;
        for (int i = 0; i < people.length; i++) {
            switch (people[i].dayEvent()) {
                case green:
                    num_green++;
                    break;
                case yellow:
                    num_yellow++;
                    break;
                case red:
                    num_red++;
                    resources-=3*swabCost;
                    isStrategyStarted = true;
                    break;
                case blue:
                    num_blue++;
                    break;
                case black:
                    //order array so dead people are on top of array
                    if (num_green == 0 && num_yellow == 0 && num_red == 0 && num_blue == 0) {
                        lastBlack = i;
                    } else {
                        lastBlack++;
                        Person temp = people[lastBlack];
                        people[lastBlack] = people[i];
                        people[i] = temp;
                    }
                    //
                    num_black++;
                    break;
            }
            if (people[i].canMove()) {
                movingDuringDay++;
                Vd += people[i].meetingsDay;
            }
            if (!people[i].mobility) {
                resources--;
            }
            people[i].meetingsDay=0;

            //part of strategy
            if(isStrategyStarted){
                if(chosenStrategy==Strategy.LAZARETTO){
                    if(people[i].condition== Status.red && people[i].y<=height){
                        people[i].y = (int)(height + Wall.border + Person.r*2 + Math.random()*height*0.1);
                    }
                    if(people[i].condition == Status.blue && people[i].y>=height){
                        people[i].y = (int)(Math.random()*height*0.8) + Wall.border + Person.r*2;
                    }
                }

                if(chosenStrategy==Strategy.SMART_SWAB){
                    if(people[i].condition== Status.red){
                        people[i].contacts.forEach((day, contactList)->{
                            contactList.forEach((person -> {
                                person.doSwab();
                            }));
                        });
                    }
                }
            }
            //
        }
        Vd/=people.length-num_black;
        gui.recreateBar();
        twoDPart.repaint();
        dailyGraph.addDay();
        gui.updateDayCounter();
        if (num_black == people.length) {
            gui.OutcomeDialog(Outcomes.Dead);
            destroy();
            return;
        } else if (resources <= 0) {
            gui.OutcomeDialog(Outcomes.No_Money);
            destroy();
            return;
        } else if (Vd*duration*infectivity <1 || num_red+num_yellow==0){
            gui.OutcomeDialog(Outcomes.Won);
            destroy();
            return;
        }
        if (isStrategyStarted) {
            switch (chosenStrategy){
                case PRAY:
                    break;
                case HALF_RANDOM:
                    if(doStrategy)
                        halfRandom();
                    break;
                case RANDOM_SWAB:
                    randomSwab();
                    break;
            }
        }
    }

    private void randomSwab() {
        int i=0;
        while (i<(int)(Math.random()*people.length/2)){
            int temp = (int)(Math.random()*(people.length-1));
            if(!people[temp].swabResult && (people[temp].condition == Status.yellow || people[temp].condition== Status.green)){
                if(people[temp].doSwab()){
                    people[temp].stopMovement();
                }
            }
            i++;
        }
        doStrategy = false;
    }

    private void halfRandom() {
        int i=0;
        while (i<people.length/2){
            int temp = (int)(Math.random()*(people.length-1));
            if(people[temp].mobility){
                people[temp].stopMovement();
                i++;
            }
        }
        doStrategy = false;
    }


}