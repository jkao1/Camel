import javax.swing.*;
import java.awt.*;

public class Cell {

    public JTextField textField;
    public boolean isSelected;

    public Cell(JTextField t) {
	textField = t;
    }

    public void clearBorder() {
	textField.setBorder(UIManager.getBorder("TextField.border"));
    }
    
    public int getValue() {
	return Integer.parseInt(textField.getText());
    }
    
    public void setValue(int v) {
	textField.setText(String.valueOf(v));
    }
    
}
