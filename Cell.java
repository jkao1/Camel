import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cell implements Comparable<Cell> {

    public JTextField textField; 
    public int cellNum;
    public boolean isLabel;

    private boolean isSelected;
    
    public Cell(JTextField t, int i) { 
	textField = t;
	cellNum = i;
    	isLabel = true;

	textField.addMouseListener(new MouseListener() {
		public void mousePressed(MouseEvent e) {
		    selected.deselect();
		    for (int i : highlighted) {
			Spreadsheet.cells[i].dehighlight();
		    }
		    highlighted = new int[Spreadsheet.ROWS*Spreadsheet.COLS];
			
		    cell.select();
		    selected = cell;
		}
		public void mouseClicked(MouseEvent e){}
		public void mouseReleased(MouseEvent e){
		    Point p = e.getLocationOnScreen();
		    highlightCells(cell.cellNum, releasedCellNum(p));
		}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
	    });
	
	if (i / Spreadsheet.COLS == 0 && i % Spreadsheet.COLS == 0) {}
	else if (i / Spreadsheet.COLS == 0) setValue(String.valueOf((char) ('A'+i-1)));
	else if (i % Spreadsheet.COLS == 0) setValue(i / Spreadsheet.COLS);
        else isLabel = false;
	
	if (isLabel) {
	    textField.setEditable(false);
	    Font bold = new Font(textField.getFont().getName(), Font.BOLD, textField.getFont().getSize());
	    textField.setFont(bold);
	}
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

    public void setValue(String v) {
	textField.setText(v);
    }

    public int compareTo(Cell c) {
	return Integer.compare(getValue(), c.getValue());
    }
    
}
