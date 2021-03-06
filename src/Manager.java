public class Manager {
    final static int MAX_PEOPLE = 5000;
    public Person[] people;
    public Wall[] walls;
    public Physics physics;
    public TwoDPart twoDPart;
    private GUI gui;
    public int duration, infectivity, symptomaticQuality, letality, movingDuringDay;
    public float meetings;
    public int day = 0;
    public float Vd = 0, R0;
    public int num_green, num_yellow, num_red, num_blue, num_black;
    public int resources = 0;
    public float swabCost;
    private Strategy chosenStrategy;
    private boolean isStrategyStarted, halfAlreadyPicked;
    private int height;

    public static void main(String[] args) {
        new Manager().start();
    }

    public void start() {
        twoDPart = new TwoDPart(this);
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
        halfAlreadyPicked = false;
        day = 0;
        gui.updateStats();

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

        physics = new Physics(this);
        twoDPart.initialize();
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
            people[i].dayEvent();
            switch (people[i].condition) {
                case GREEN:
                    num_green++;
                    Vd += people[i].Vd;
                    break;
                case YELLOW:
                    num_yellow++;
                    Vd += people[i].Vd;
                    break;
                case RED:
                    num_red++;
                    resources-=3*swabCost;
                    isStrategyStarted = true;
                    Vd += people[i].Vd;
                    break;
                case BLUE:
                    num_blue++;
                    Vd += people[i].Vd;
                    break;
                case BLACK:
                    //order array so dead people are on top of array (useful for drawing)
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
            }
            if (!people[i].mobility) {
                resources--;
            }
            people[i].Vd=0;

            //part of strategy
            if(isStrategyStarted){
                if(chosenStrategy==Strategy.LAZARETTO){
                    if(people[i].condition == Status.RED && people[i].y<=height){
                        people[i].y = (int)(height + Wall.border + Person.r*2 + Math.random()*height*0.1);
                    }
                    if(people[i].condition == Status.BLUE && people[i].y>=height){
                        people[i].y = (int)(Math.random()*height*0.7) + Wall.border + Person.r*2;
                    }
                }

                if(chosenStrategy==Strategy.SMART_SWAB){
                    if(people[i].condition == Status.RED && !people[i].checkedContacts) {
                        people[i].checkedContacts=true;
                        people[i].contacts.forEach((day, contactList)-> {
                            contactList.forEach((person ->  person.doSwab()));
                        });
                    }
                }
            }
            //
        }
        gui.recreateBar();
        twoDPart.repaint();
        twoDPart.resetCounter();
        if (num_black == people.length) {
            Vd=0;
            gui.updateStats();
            gui.OutcomeDialog(Outcomes.DEAD);
            return;
        }
        if (resources <= 0) {
            Vd=Vd/(people.length-num_black);
            gui.updateStats();
            gui.OutcomeDialog(Outcomes.NO_MONEY);
            return;
        }
        Vd=Vd/(people.length-num_black);
        R0=Vd*duration*infectivity;
        gui.updateStats();
        if (num_red+num_yellow==0){
            gui.OutcomeDialog(Outcomes.WON);
            return;
        }
        if (isStrategyStarted) {
            switch (chosenStrategy){
                case PRAY:
                    break;
                case HALF_RANDOM:
                    if(!halfAlreadyPicked)
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
            if(!people[temp].swabResult && (people[temp].condition == Status.YELLOW || people[temp].condition== Status.GREEN)){
                if(people[temp].doSwab()){
                    people[temp].stopMovement();
                }
            }
            i++;
        }
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
        halfAlreadyPicked = true;
    }
}