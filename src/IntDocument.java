import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class IntDocument extends PlainDocument {

    int size;

    public IntDocument(int size){
        this.size = size;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str == null){ return; }
        if(getLength() + str.length() > size){
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        boolean isValid = true;
        for(int i = 0; i < str.length(); i++){
            if(!Character.isDigit(str.charAt(i))){
                    isValid = false;
                    break;}
        }

        if(isValid){
            super.insertString(offs, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
