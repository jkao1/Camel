import javax.swing.*;
import java.awt.*;

public class Cell implements Comparable<Cell> {

    public JTextField textField; 
    public int cellNum;
    public boolean isLabel;
    
    public Cell(JTextField t, int i) { 
	textField = t;
	cellNum = i;
	isLabel = true;
	
	if (i / Spreadsheet.COLS == 0 && i % Spreadsheet.COLS == 0) {}
	else if (i / Spreadsheet.COLS == 0) setValue(String.valueOf((char) ('A'+i-1)));
	else if (i % Spreadsheet.COLS == 0) setValue(i / Spreadsheet.COLS);
        else isLabel = false;
	
	if (isLabel) {
	    textField.setEditable(false);
	    deHighlight(); // uniform coloration
	    Font bold = new Font(textField.getFont().getName(), Font.BOLD, textField.getFont().getSize());
	    textField.setFont(bold);
	}
    }

    public void select() {
	textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    }

    public void unSelect() {        
	textField.setBorder(UIManager.getBorder("TextField.border"));
    }

    public void highlight() {
        textField.setBackground(new Color(178,215,254));
    }

    public void deHighlight() {
	textField.setBackground(Color.WHITE);
    }
    
    public int getValue() {
	if (textField.getText().equals("")) return 0;
	return Integer.parseInt(textField.getText());
    }
    
    public void setValue(int v) {
	textField.setText(String.valueOf(v));
    }

    public void setValue(String v) {
	textField.setText(v);
    }

    public int compareTo(Cell c) {
	return Integer.compare(getValue(), c.getValue());
    }
    
}
