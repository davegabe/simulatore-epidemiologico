import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumbersOnlyInput extends KeyAdapter {
    JTextField textField;
    int counterSign = 0;
    boolean isFloat;

    NumbersOnlyInput(JTextField textField, boolean isFloat){
        this.textField=textField;
        this.isFloat=isFloat;
    }

    public void keyPressed(KeyEvent key) {
        if(isFloat){
            if (key.getKeyChar() >= '0' && key.getKeyChar() <= '9' || key.getKeyCode() == 8 || key.getKeyChar() == ',' || key.getKeyChar() == '.' ) {
                if(textField.getText().charAt(0)==',' || textField.getText().charAt(0)=='.'){

                }
                textField.setEditable(true);
            } else {
                textField.setText(textField.getText().substring(0,textField.getText().length()-1));
            }
        } else {
            if (key.getKeyChar() >= '0' && key.getKeyChar() <= '9' || key.getKeyCode() == 8) {
                textField.setEditable(true);
            } else {
                textField.setEditable(false);
            }
        }
    }
}
