import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingConstants.HORIZONTAL;

public class GUI extends JPanel {
    private final Color yellow = new Color(233, 196, 106);
    private final Color green = new Color(42, 157, 143);
    private final Color red = new Color(231, 111, 81);
    private final Color blue = new Color(45, 143, 171);
    private final Color black = new Color(26, 24, 27);
    private final Color gunmetal = new Color(19, 39, 53);
    private GridBagConstraints c;
    private JTextField population_txtfield;
    private JTextField swab_txtfield;
    private JTextField meetings_txtfield;
    private JTextField resources_txtfield;
    private JSlider inf_slider;
    private JSlider let_slider;
    private JSlider sint_slider;
    private JSlider duration_slider;
    private JSlider fps_slider;
    //private JOptionPane error_dialogue;
    private boolean once = false;
    private Manager manager;


    public GUI(Manager manager) {
        this.manager=manager;
    }

    public void initialize() {
        JFrame f = new JFrame("COVID-2027 GVNG");
        f.setSize(1500, 900);
        f.setLayout(new BorderLayout());
        f.getContentPane().setBackground(gunmetal); //gunmetal
        //f.setResizable(false);

        createLeftPanel(f, green);
        createRightPanel(f, green);
        createCenterPanel(f, gunmetal, green, yellow, red, blue, black);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void createLeftPanel(JFrame f, Color color) {
        JPanel left_p = new JPanel(); // this is the big left panel
        left_p.setBackground(color);         //set color of the panel
        left_p.setLayout(new BoxLayout(left_p, BoxLayout.Y_AXIS));
        f.add(left_p, BorderLayout.WEST);
        c = new GridBagConstraints();

        // INSERIMENTO POPOLAZIONE FIELD
        population_txtfield = createTextField("Popolazione", left_p);
        // INSERIMENTO RISORSE FIELD
        resources_txtfield = createTextField("Risorse", left_p);
        // INSERIMENTO TAMPONE FIELD
        swab_txtfield = createTextField("Costo tampone", left_p);
        // INSERIMENTO INCONTRI FIELD
        meetings_txtfield = createTextField("Incontri", left_p);

        //
        left_p.add(Box.createRigidArea(new Dimension(0, 120)));   // SPAZIO CHE DIVIDE SLIDER DA BOTTONI
        /*BufferedImage myPicture = ImageIO.read(new File("/Users/beatrice/IdeaProjects/covid-2027GVNG/src"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        left_p.add(picLabel);

        File img = new File("/Users/beatrice/IdeaProjects/covid-2027GVNG/src");
        JLabel imgLabel = new JLabel(new ImageIcon(img.getName()));
        left_p.add(imgLabel);*/

        JLabel img = new JLabel("IMAGE GOES HERE");
        img.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        left_p.add(img);

        population_txtfield.setDocument(new IntDocument(10));
        swab_txtfield.setDocument(new DoubleDocument(10));
        meetings_txtfield.setDocument(new DoubleDocument(10));
        resources_txtfield.setDocument(new IntDocument(10));
    }

    private JTextField createTextField(String label, JPanel parentPanel) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        parentPanel.add(jPanel);

        JLabel jLabel = new JLabel();
        jLabel.setText(label);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 30, 0, 0);
        jPanel.add(jLabel, c);


        JTextField jTextField = new JTextField("");
        jTextField.setPreferredSize(new Dimension(200, 24));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(8, 40, 0, 30);         // inserisce uno spazio tra gli elementi
        jPanel.add(jTextField, c);
        return jTextField;
        // http://www.davismol.net/2014/12/18/java-swing-un-esempio-di-utilizzo-del-gridbaglayout-per-la-realizzazione-di-un-form/
    }

    private void createRightPanel(JFrame f, Color color) {
        JPanel right_p = new JPanel(); // this is the big left panel
        right_p.setBackground(color);         //set color of the panel
        right_p.setLayout(new BoxLayout(right_p, BoxLayout.Y_AXIS));
        f.add(right_p, BorderLayout.EAST);

        // INSERIMENTO SLIDER INFETTIVITÁ
        inf_slider = createSlider("          Infettività", right_p, yellow, 10, 20, 100, 0);
        // INSERIMENTO SLIDER SINTOMATICITÀ
        sint_slider = createSlider("          Sintomaticità", right_p, red, 10, 20, 100, 0);
        // INSERIMENTO SLIDER LETALITÀ
        let_slider = createSlider("          Letalità", right_p, black, 10, 20, 100, 0);
        // INSERIMENTO SLIDER DURATA
        duration_slider = createSlider("          Durata", right_p, gunmetal, 5, 15, 45, 0);
        // INSERIMENTO SLIDER VELOCITÀ
        fps_slider = createSlider("          Velocità", right_p, blue, 10, 20, 100, 0);
        //30 max, 2000 min

        right_p.add(Box.createRigidArea(new Dimension(0, 120)));   // SPAZIO CHE DIVIDE SLIDER DA BOTTONI

        JPanel play_p = new JPanel();


        play_p.setBackground(color);
        JButton play = new JButton("PLAY");

        Dimension dim_buttons = new Dimension(200, 50);

        play.setPreferredSize(dim_buttons);
        play_p.add(play);
        right_p.add(play_p);

        right_p.add(Box.createRigidArea(new Dimension(0, 0)));   // SPAZIO CHE DIVIDE SLIDER DA BOTTONI

        JPanel stop_p = new JPanel();
        stop_p.setBackground(color);
        JButton stop = new JButton("STOP");

        stop.setPreferredSize(dim_buttons);
        stop_p.add(stop);
        right_p.add(stop_p);
        stop.setVisible(false);

        fps_slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (fps_slider.getMaximum()-fps_slider.getValue()==0)
                    manager.changeSpeed(1);
                else
                    manager.changeSpeed(fps_slider.getMaximum()-fps_slider.getValue());
            }
        });

        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (play.getText().equals("PLAY")) {

                    if (!once)              //if already started
                        once = true;
                    else
                        return;

                    int population = 1;
                    float swab = 1;
                    float meetings = 1;
                    int resources = 1;// gets meetings
                    int infectivity = inf_slider.getValue();
                    int symptomaticity = sint_slider.getValue();
                    int letality = let_slider.getValue();
                    int duration = duration_slider.getValue();



                    if (!population_txtfield.getText().equals("")) {
                        if(Integer.parseInt(population_txtfield.getText())>=1)
                            population = Integer.parseInt(population_txtfield.getText());
                    }                                                                                            // gets population
                    if (!swab_txtfield.getText().equals("")) {
                        if(Float.parseFloat(swab_txtfield.getText())>0)
                            swab = Float.parseFloat(swab_txtfield.getText());
                    }                                                                                            // gets swab
                    if (!meetings_txtfield.getText().equals("")){
                        if(Float.parseFloat(meetings_txtfield.getText())>0)
                            meetings = Float.parseFloat(meetings_txtfield.getText());
                    }
                    if (!resources_txtfield.getText().equals("")) {
                        if(Integer.parseInt(resources_txtfield.getText())>=1)
                            resources = Integer.parseInt(resources_txtfield.getText());
                        if(resources >= 10*population*swab) {
                            System.out.println(population);
                            String error = "You must chose a lower value for 'Resources'!";
                            JOptionPane.showMessageDialog(null, error);
                            System.out.println(resources);
                            once = false;
                            //new ResourcesChecker();
                        }
                    }

                    if(resources < 10*population*swab) {
                        play.setText("PAUSE");
                        stop.setVisible(true);
                    }
                    else{
                        return;
                    }

                    fps_slider.setEnabled(true);
                    if (fps_slider.getValue()==0)
                        manager.changeSpeed(1000);
                    else
                        manager.changeSpeed(fps_slider.getValue()*1000);

                    System.out.println("infectivity = " + infectivity);
                    System.out.println("symptomaticity = " + symptomaticity);
                    System.out.println("letality = " + letality);
                    System.out.println("duration = " + duration);
                    System.out.println("population = " + population);
                    System.out.println("swabone = " + swab);
                    System.out.println("meetings = " + meetings);
                    System.out.println("resources = " + resources);
                } else {
                    play.setText("PLAY");
                    manager.changeSpeed(-1);    //stop the timer
                    fps_slider.setEnabled(false);
                }
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                play.setText("PLAY");
                stop.setVisible(false);
                once = false;
            }
        });

    }

    private JSlider createSlider(String label, JPanel parentPanel, Color color, int minorTick, int majorTick, int maxValue, int minValue) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        parentPanel.add(jPanel);

        JLabel jLabel = new JLabel();
        jLabel.setText(label);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        jPanel.add(jLabel, c);

        JSlider jSlider = new JSlider(HORIZONTAL, minValue, maxValue, maxValue / 2);
        jSlider.setMinorTickSpacing(minorTick);
        jSlider.setMajorTickSpacing(majorTick);
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);

        //inf_slider.setBackground(new java.awt.Color(231, 111, 81));
        jSlider.setForeground(color);


        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 40, 0, 30);
        jPanel.add(jSlider, c);
        return jSlider;
    }

    private void createCenterPanel(JFrame f, Color background, Color healthy, Color asymptomatic, Color symptomatic, Color healed, Color dead) {

        // CREATE CENTER LAYOUT AS BORDERLAYOUT

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.setBackground(background);
        f.add(center, BorderLayout.CENTER);


        // INSERIMENTO PARTE 2D AL CENTRO DEL FRAME
        center.add(manager.twoDPart, BorderLayout.CENTER);


        // SIZE CHANGING BAR

        JPanel bar = new JPanel();
        bar.setBackground(green);
        bar.setLayout(new GridBagLayout());
        GridBagConstraints s = new GridBagConstraints();
        center.add(bar, BorderLayout.PAGE_END);


        JLabel green_bar = new JLabel();
        green_bar.setText(String.valueOf(manager.twoDPart.num_green));
        green_bar.setOpaque(true);
        green_bar.setBackground(healthy);

        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridy = 0;
        s.gridx = 0;
        s.weightx = manager.twoDPart.num_green;
        bar.add(green_bar, s);


        JLabel yellow_bar = new JLabel();
        yellow_bar.setText(String.valueOf(manager.twoDPart.num_yellow));
        yellow_bar.setOpaque(true);
        yellow_bar.setBackground(asymptomatic);


        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 1;
        s.gridy = 0;
        s.weightx = manager.twoDPart.num_yellow;
        bar.add(yellow_bar, s);


        JLabel red_bar = new JLabel();
        red_bar.setText(String.valueOf(manager.twoDPart.num_red));
        red_bar.setOpaque(true);
        red_bar.setBackground(symptomatic);


        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 2;
        s.gridy = 0;
        s.weightx = manager.twoDPart.num_red;
        bar.add(red_bar, s);


        JLabel dead_bar = new JLabel();
        dead_bar.setText(String.valueOf(manager.twoDPart.num_black));
        dead_bar.setOpaque(true);
        dead_bar.setBackground(dead);

        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 3;
        s.gridy = 0;
        s.weightx = manager.twoDPart.num_black;
        bar.add(dead_bar, s);


        JLabel blue_bar = new JLabel();
        blue_bar.setText(String.valueOf(manager.twoDPart.num_blue));
        blue_bar.setOpaque(true);
        blue_bar.setBackground(healed);


        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 4;
        s.gridy = 0;
        s.weightx = manager.twoDPart.num_blue;
        bar.add(blue_bar, s);
    }
}
