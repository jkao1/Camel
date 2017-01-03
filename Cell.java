import javax.swing.*;
import java.awt.*;

public class Cell implements Comparable<Cell> {

    public JTextField textField; 
    public int cellNum;
    
    private boolean isSelected;

    public Cell(JTextField t, int i) { 
	textField = t;
	cellNum = i;
    }

    public void select() { 
	textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	isSelected = true;
    }

    public void deselect() {        
	textField.setBorder(UIManager.getBorder("TextField.border"));
	isSelected = false;
    }

    public void highlight() {
	if (!isSelected) textField.setBackground(Color.GRAY);
    }

    public void dehighlight() {
	textField.setBackground(Color.WHITE);
    }
    
    public int getValue() {
	return Integer.parseInt(textField.getText());
    }
    
    public void setValue(int v) {
	textField.setText(String.valueOf(v));
    }

    public int compareTo(Cell c) {
	return Integer.compare(getValue(), c.getValue());
    }
    
}
