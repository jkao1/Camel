import javax.swing.*;
import java.awt.*;

public class Cell {

    public JTextField textField;
    public boolean isSelected;

    public Cell(JTextField t) {
	textField = t;
    }

    public void select() {
	textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    }

    public void deselect() {
	textField.setBorder(UIManager.getBorder("TextField.border"));
    }
    
    public int getValue() {
	return Integer.parseInt(textField.getText());
    }
    
    public void setValue(int v) {
	textField.setText(String.valueOf(v));
    }
    
}
