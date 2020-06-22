import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static javax.swing.SwingConstants.HORIZONTAL;

public class GUI extends JPanel {
    private final int CONSTANT_RESOURCES = 100000;
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
    private JPanel center;
    private JLabel counter;
    private JPanel bar;
    private JButton play;
    private JButton stop;
    private JComboBox strategy_cmbx;
    private boolean once = false;
    private Manager manager;

    public GUI(Manager manager) {
        this.manager = manager;
    }

    public void initialize() {
        JFrame f = new JFrame("COVID-2027 GVNG");
        f.setSize(1500, 900);
        f.setLayout(new BorderLayout());
        f.getContentPane().setBackground(ColorsManager.background);

        createLeftPanel(f);
        createRightPanel(f);
        createCenterPanel(f);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        System.out.print(manager.twoDPart.getSize());
    }

    private void createLeftPanel(JFrame f) {
        JPanel left_p = new JPanel();                              // this is the big left panel
        left_p.setBackground(ColorsManager.green);          //set color of the panel
        left_p.setLayout(new BoxLayout(left_p, BoxLayout.Y_AXIS));
        f.add(left_p, BorderLayout.WEST);
        c = new GridBagConstraints();

        population_txtfield = createTextField("Popolazione", left_p);   // Inserting POPOLAZIONE FIELD
        resources_txtfield = createTextField("Risorse", left_p);        // Inserting RISORSE FIELD
        swab_txtfield = createTextField("Costo tampone", left_p);       // Inserting TAMPONE FIELD
        meetings_txtfield = createTextField("Incontri", left_p);        // Inserting INCONTRI FIELD
        strategy_cmbx = createComboBox("Strategia", left_p);            // Inserting STRATEGY JCOMBOBOX

        left_p.add(Box.createRigidArea(new Dimension(0, 120)));  // SPAZIO CHE DIVIDE SLIDER DA BOTTONI

        left_p.add(manager.dailyGraph);

        left_p.add(createDaysCounter(left_p, manager.day));                 // instead of days_value, we should put the manager's days counter

        left_p.add(Box.createRigidArea(new Dimension(0, 120)));

        population_txtfield.setDocument(new IntDocument(10));
        swab_txtfield.setDocument(new DoubleDocument(10));
        meetings_txtfield.setDocument(new DoubleDocument(10));
        resources_txtfield.setDocument(new IntDocument(10));
    }

    private void createRightPanel(JFrame f) {
        JPanel right_p = new JPanel(); // this is the big left panel
        right_p.setBackground(ColorsManager.green);         //set color of the panel
        right_p.setLayout(new BoxLayout(right_p, BoxLayout.Y_AXIS));
        f.add(right_p, BorderLayout.EAST);

        // INSERIMENTO SLIDER INFETTIVITÁ
        inf_slider = createSlider("          Infettività", right_p, ColorsManager.yellow, 10, 20, 100, 0);
        // INSERIMENTO SLIDER SINTOMATICITÀ
        sint_slider = createSlider("          Sintomaticità", right_p, ColorsManager.red, 10, 20, 100, 0);
        // INSERIMENTO SLIDER LETALITÀ
        let_slider = createSlider("          Letalità", right_p, ColorsManager.black, 10, 20, 100, 0);
        // INSERIMENTO SLIDER DURATA
        duration_slider = createSlider("          Durata", right_p, ColorsManager.background, 5, 15, 45, 0);
        // INSERIMENTO SLIDER VELOCITÀ
        fps_slider = createSlider("          Velocità", right_p, ColorsManager.blue, 10, 20, 100, 0);
        //30 max, 2000 min

        right_p.add(Box.createRigidArea(new Dimension(0, 120)));   // SPAZIO CHE DIVIDE SLIDER DA BOTTONI

        JPanel play_p = new JPanel();


        play_p.setBackground(ColorsManager.green);
        play = new JButton("PLAY");

        Dimension dim_buttons = new Dimension(200, 50);

        play.setPreferredSize(dim_buttons);
        play_p.add(play);
        right_p.add(play_p);

        right_p.add(Box.createRigidArea(new Dimension(0, 0)));   // SPAZIO CHE DIVIDE SLIDER DA BOTTONI

        JPanel stop_p = new JPanel();
        stop_p.setBackground(ColorsManager.green);
        stop = new JButton("STOP");

        stop.setPreferredSize(dim_buttons);
        stop_p.add(stop);
        right_p.add(stop_p);
        stop.setVisible(false);

        fps_slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                fpsSliderEvent();
            }
        });

        play.addActionListener(e->play());

        stop.addActionListener(e ->stop());
    }

    private void createCenterPanel(JFrame f) {

        // CREATE CENTER LAYOUT AS BORDERLAYOUT

        center = new JPanel();
        center.setLayout(new BorderLayout());
        center.setBackground(ColorsManager.background);
        f.add(center, BorderLayout.CENTER);


        // INSERIMENTO PARTE 2D AL CENTRO DEL FRAME
        center.add(manager.twoDPart, BorderLayout.CENTER);


        // SIZE CHANGING BAR
        createBar();
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
    }

    public JComboBox createComboBox(String label, JPanel parentPanel) {
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


        JComboBox jComboBox = new JComboBox(Strategy.values());
        jComboBox.setPreferredSize(new Dimension(200, 24));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(8, 40, 0, 30);         // inserisce uno spazio tra gli elementi
        jPanel.add(jComboBox, c);
        return jComboBox;
    }

    public void fpsSliderEvent() {
        if (fps_slider.getMaximum() - fps_slider.getValue() == 0)
            manager.changeSpeed(1);
        else
            manager.changeSpeed(fps_slider.getMaximum() - fps_slider.getValue());
    }

    public void updateDayCounter() {
        counter.setText(String.valueOf(manager.day));
    }

    private JPanel createDaysCounter(JPanel parentPanel, int days_value) {
        JPanel counter_p = new JPanel();
        //counter_p.setBackground(blue);
        counter_p.setLayout(new FlowLayout());
        JLabel days = new JLabel("Days:");
        counter = new JLabel(String.valueOf(days_value));
        // whenever the meetings meet the velocity field, this should be upped by 1.

        counter_p.add(days);
        counter_p.add(counter);
        parentPanel.add(counter_p);
        return counter_p;

    }

    public void recreateBar() {
        center.remove(bar);
        createBar();
    }

    private void createBar() {
        bar = new JPanel();
        bar.setBackground(ColorsManager.green);
        bar.setLayout(new GridBagLayout());
        GridBagConstraints s = new GridBagConstraints();
        center.add(bar, BorderLayout.PAGE_END);


        JLabel green_bar = new JLabel();
        green_bar.setText(String.valueOf(manager.num_green));
        green_bar.setOpaque(true);
        green_bar.setBackground(ColorsManager.green);

        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridy = 0;
        s.gridx = 0;
        s.weightx = manager.num_green;
        bar.add(green_bar, s);


        JLabel yellow_bar = new JLabel();
        yellow_bar.setText(String.valueOf(manager.num_yellow));
        yellow_bar.setOpaque(true);
        yellow_bar.setBackground(ColorsManager.yellow);


        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 1;
        s.gridy = 0;
        s.weightx = manager.num_yellow;
        bar.add(yellow_bar, s);


        JLabel red_bar = new JLabel();
        red_bar.setText(String.valueOf(manager.num_red));
        red_bar.setOpaque(true);
        red_bar.setBackground(ColorsManager.red);


        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 2;
        s.gridy = 0;
        s.weightx = manager.num_red;
        bar.add(red_bar, s);


        JLabel dead_bar = new JLabel();
        dead_bar.setText(String.valueOf(manager.num_black));
        dead_bar.setOpaque(true);
        dead_bar.setBackground(ColorsManager.black);

        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 3;
        s.gridy = 0;
        s.weightx = manager.num_black;
        bar.add(dead_bar, s);


        JLabel blue_bar = new JLabel();
        blue_bar.setText(String.valueOf(manager.num_blue));
        blue_bar.setOpaque(true);
        blue_bar.setBackground(ColorsManager.blue);


        s.fill = GridBagConstraints.HORIZONTAL;
        s.gridx = 4;
        s.gridy = 0;
        s.weightx = manager.num_blue;
        bar.add(blue_bar, s);
    }

    public void OutcomeDialog(Outcomes outcome) {
        String ending = "";
        int type = JOptionPane.INFORMATION_MESSAGE;
        switch (outcome) {
            case Won:
                ending = "The virus has been eradicated!";
                type = JOptionPane.INFORMATION_MESSAGE;
                break;
            case Dead:
                ending = "Unfortunately everyone died..";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            case No_Money:
                ending = "The economy collapsed...";
                type = JOptionPane.ERROR_MESSAGE;
                break;
        }
        JOptionPane.showMessageDialog(null, ending, "END", type);
        stop();
    }

    public void resetBar() {
        manager.num_black = 0;
        manager.num_green = 0;
        manager.num_yellow = 0;
        manager.num_red = 0;
        manager.num_blue = 0;
        recreateBar();

    }

    private void play() {
        if (play.getText().equals("PLAY")) {

            if (!once) {
                once = true;
            } else {                              //if is paused
                fps_slider.setEnabled(true);
                fpsSliderEvent();
                play.setText("PAUSE");
                return;
            }

            float swab, meetings;
            int population, resources, strategy;
            int infectivity = inf_slider.getValue();
            int symptomaticQuality = sint_slider.getValue();
            int letality = let_slider.getValue();
            int duration = duration_slider.getValue();
            if (duration == 0) duration = 1;

            try {
                if (!population_txtfield.getText().equals("") && Integer.parseInt(population_txtfield.getText()) >= 1) {
                    population = Integer.parseInt(population_txtfield.getText());
                } else throw new InputException("Invalid people input!");

                if (!resources_txtfield.getText().equals("") && Integer.parseInt(resources_txtfield.getText()) >= 1) {
                    resources = Integer.parseInt(resources_txtfield.getText());
                } else throw new InputException("Invalid resources input!");

                if (!swab_txtfield.getText().equals("") && Float.parseFloat(swab_txtfield.getText()) > 0) {
                    swab = Float.parseFloat(swab_txtfield.getText());
                }                                                                                            // gets swab
                else throw new InputException("Invalid swab input!");

                if (!meetings_txtfield.getText().equals("") && Float.parseFloat(meetings_txtfield.getText()) > 0) {
                    meetings = Float.parseFloat(meetings_txtfield.getText());
                } else throw new InputException("Invalid meetings input!");

                if (resources >= CONSTANT_RESOURCES * population * swab)
                    throw new InputException("Resources are too high!");

                if (strategy_cmbx.getSelectedItem() != null) {
                    strategy = strategy_cmbx.getSelectedIndex();
                } else throw new InputException("Select a strategy to follow");

                manager.initialize(population, duration, infectivity, symptomaticQuality, letality, swab, meetings, resources, strategy);

                play.setText("PAUSE");
                stop.setVisible(true);
                fps_slider.setEnabled(true);
                fpsSliderEvent();
            } catch (InputException e) {
                once = false;
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            play.setText("PLAY");
            manager.changeSpeed(-1);    //stop the timer
            fps_slider.setEnabled(false);
        }
    }

    private void stop() {
        play.setText("PLAY");
        manager.destroy();
        resetBar();
        stop.setVisible(false);
        once = false;
        fps_slider.setEnabled(true);
    }
}
