import javax.swing.*;
import java.awt.*;

public class Cell implements Comparable<Cell> {

    public static final Color LABEL_COLOR = Color.LIGHT_GRAY;

    private JTextField textField;
 
    public int cellNum;
    public boolean isLabel; 
    
    public Cell(JTextField t, int i) { 
	textField = t;
	cellNum = i;
	isLabel = true;

	textField.setEditable(false);
	//deHighlight(); // uniform coloration
	
	if (i / Squirrel.COLS == 0 && i % Squirrel.COLS == 0) {}
	else if (i / Squirrel.COLS == 0) setValue(String.valueOf((char) ('A'+i-1)));
	else if (i % Squirrel.COLS == 0) setValue(i / Squirrel.COLS);
        else isLabel = false;
	
	if (isLabel) {
	    Font bold = new Font(textField.getFont().getName(), Font.BOLD, textField.getFont().getSize());
	    textField.setFont(bold);
	    textField.setBackground(LABEL_COLOR);
	}
	deHighlight();
    }

    public void select() {
	textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	textField.requestFocus();
    }

    public void unSelect() {        
	textField.setBorder(UIManager.getBorder("TextField.border"));
	textField.setEditable(false);
	textField.getCaret().setVisible(false);
    }

    public void highlight() {
        textField.setBackground(new Color(178,215,254));
    }

    public void deHighlight() {
	if (isLabel) {
	    textField.setBackground(LABEL_COLOR);
	} else {
	    textField.setBackground(Color.WHITE);
	}
    }

    public void makeEditable() {
	textField.setEditable(true);
	textField.getCaret().setVisible(true);
    }

    public void clear() {
	textField.setText("");
	deHighlight();
    }

    public boolean isEmpty() {
	return textField.getText().equals("");
    }

    public JTextField getTextField() {
	return textField;
    }
    
    public int getIntValue() {
	try {
	    String s = textField.getText();
	    if (s.indexOf(".") > 0) {
		s = s.substring( 0, s.indexOf("."));
	    }
	    return Integer.parseInt(textField.getText());
	} catch (NumberFormatException e) { return 0; }
    }

    public String getValue() {
	return textField.getText();
    }
    
    public void setValue(int v) {
	textField.setText(String.valueOf(v));
    }

    public void setValue(double v) {
	textField.setText(String.valueOf(v));
    }

    public void setValue(String v) {
	textField.setText(v);
    }

    public double getX() {
	return textField.getLocationOnScreen().getX();
    }

    public double getY() {
	return textField.getLocationOnScreen().getY();
    }

    public String toString() {
	return "" + (char) ('A' + cellNum % Squirrel.COLS - 1) + (cellNum / Squirrel.COLS);	
    }
    
    public int compareTo(Cell c) {
	return Integer.compare(getIntValue(), c.getIntValue());
    }
    
}
