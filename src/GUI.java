import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.HORIZONTAL;

public class GUI extends JPanel {
    private final int CONSTANT_RESOURCES = 100000;
    private GridBagConstraints c;
    private JTextField population_txtfield, swab_txtfield, meetings_txtfield, resources_txtfield;
    private JSlider inf_slider, let_slider, sint_slider, duration_slider, fps_slider;
    private JPanel center, bar;
    private JLabel counterDays, counterResources, counterVd;
    private JButton play, stop;
    private JComboBox strategy_cmbx;
    private boolean once = false;
    private Manager manager;

    public GUI(Manager manager) {
        this.manager = manager;
    }

    public void initialize() {
        JFrame f = new JFrame("COVID-2027 GVNG");
        f.setMinimumSize(new Dimension(1500, 900));
        f.setLayout(new BorderLayout());
        f.getContentPane().setBackground(ColorsManager.background);

        createLeftPanel(f);
        createRightPanel(f);
        createCenterPanel(f);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createLeftPanel(JFrame f) {
        JPanel left_p = new JPanel();                              // this is the big left panel
        left_p.setBackground(ColorsManager.green);          //set color of the panel
        left_p.setLayout(new BoxLayout(left_p, BoxLayout.Y_AXIS));
        f.add(left_p, BorderLayout.WEST);
        c = new GridBagConstraints();

        population_txtfield = createTextField("Population", left_p);    // Inserting POPOLAZIONE FIELD
        resources_txtfield = createTextField("Resources", left_p);      // Inserting RISORSE FIELD
        swab_txtfield = createTextField("Cost of treatment", left_p);   // Inserting TAMPONE FIELD
        meetings_txtfield = createTextField("Meetings", left_p);        // Inserting INCONTRI FIELD
        strategy_cmbx = createComboBox("Strategy", left_p);             // Inserting STRATEGY JCOMBOBOX

        left_p.add(Box.createRigidArea(new Dimension(0, 120)));  //SPACE IN BETWEEN

        left_p.add(createStats(left_p, manager.day, manager.Vd, manager.resources));

        left_p.add(Box.createRigidArea(new Dimension(0, 120)));

        population_txtfield.setDocument(new IntDocument(5));
        swab_txtfield.setDocument(new DoubleDocument(8));
        meetings_txtfield.setDocument(new DoubleDocument(8));
        resources_txtfield.setDocument(new IntDocument(8));
    }

    private void createRightPanel(JFrame f) {
        JPanel right_p = new JPanel(); // this is the big left panel
        right_p.setBackground(ColorsManager.green);         //set color of the panel
        right_p.setLayout(new BoxLayout(right_p, BoxLayout.Y_AXIS));
        f.add(right_p, BorderLayout.EAST);

        //INSERT SLIDERS
        inf_slider =        createSlider("          Infectivity", right_p, ColorsManager.yellow, 10, 20, 100, 0);
        sint_slider =       createSlider("          Symptomaticity ", right_p, ColorsManager.red, 10, 20, 100, 0);
        let_slider =        createSlider("          Letality", right_p, ColorsManager.black, 10, 20, 100, 0);
        duration_slider =   createSlider("          Duration", right_p, ColorsManager.background, 5, 15, 45, 0);
        fps_slider =        createSlider("          Speed", right_p, ColorsManager.blue, 10, 20, 100, 0); //30 max, 2000 min


        right_p.add(Box.createRigidArea(new Dimension(0, 120)));   //SPACE IN BETWEEN

        JPanel play_p = new JPanel();


        play_p.setBackground(ColorsManager.green);
        play = new JButton("PLAY");

        Dimension dim_buttons = new Dimension(200, 50);

        play.setPreferredSize(dim_buttons);
        play_p.add(play);
        right_p.add(play_p);

        right_p.add(Box.createRigidArea(new Dimension(0, 0)));   //SPACE IN BETWEEN

        JPanel stop_p = new JPanel();
        stop_p.setBackground(ColorsManager.green);
        stop = new JButton("STOP");

        stop.setPreferredSize(dim_buttons);
        stop_p.add(stop);
        right_p.add(stop_p);
        stop.setVisible(false);

        fps_slider.addChangeListener(e -> fpsSliderEvent());

        play.addActionListener(e -> play());

        stop.addActionListener(e -> stop());
    }

    private void createCenterPanel(JFrame f) {
        // CREATE CENTER LAYOUT AS BORDERLAYOUT
        center = new JPanel();
        center.setLayout(new BorderLayout());
        center.setBackground(ColorsManager.background);
        f.add(center, BorderLayout.CENTER);

        //INSERT 2D PART AT CENTER
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
        c.insets = new Insets(8, 40, 0, 30);    //SPACE IN BETWEEN
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
        c.insets = new Insets(8, 40, 0, 30);    //SPACE IN BETWEEN
        jPanel.add(jComboBox, c);
        return jComboBox;
    }

    public void fpsSliderEvent() {
        if (fps_slider.getMaximum() - fps_slider.getValue() == 0)
            manager.changeSpeed(1);
        else
            manager.changeSpeed(fps_slider.getMaximum() - fps_slider.getValue());
    }

    public void updateStats() {
        counterDays.setText(String.valueOf(manager.day));
        counterResources.setText(String.valueOf(manager.resources));
        counterVd.setText(String.valueOf(manager.Vd));
    }

    private JPanel createStats(JPanel parentPanel, int day_value, float Vd_value, int resource_value) {
        JPanel stats_p = new JPanel(new FlowLayout());
        JLabel days = new JLabel("Days:");
        counterDays = new JLabel(String.valueOf(day_value));
        stats_p.add(days);
        stats_p.add(counterDays);
        parentPanel.add(stats_p);

        stats_p = new JPanel(new FlowLayout());
        JLabel resources = new JLabel("Resources:");
        counterResources = new JLabel(String.valueOf(resource_value));
        stats_p.add(resources);
        stats_p.add(counterResources);
        parentPanel.add(stats_p);

        stats_p = new JPanel(new FlowLayout());
        JLabel Vd = new JLabel("Vd:");
        counterVd = new JLabel(String.valueOf(Vd_value));
        stats_p.add(Vd);
        stats_p.add(counterVd);
        return stats_p;

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
            case WON:
                ending = "The virus has been eradicated!";
                type = JOptionPane.INFORMATION_MESSAGE;
                break;
            case DEAD:
                ending = "Unfortunately everyone died..";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            case NO_MONEY:
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
        updateStats();
        stop.setVisible(false);
        once = false;
        fps_slider.setEnabled(true);
    }
}
