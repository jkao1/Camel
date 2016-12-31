import javax.swing.*;
import java.awt.*;

public class Spreadsheet extends JFrame {

    private Container ss;
    private Cell[] cells;

    private int rows = 5;
    private int cols = 5;

    public Spreadsheet() {
	this.setTitle("Squirrel");
	this.setSize(1280,960);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(rows,cols));
	
	initCells();
	for (Cell c : cells) {
	    JTextField l = new JTextField(5);
	    l.setText(String.valueOf(c.getValue));	    
	}
    }

    private void initCells() {
	cells = new Cell[rows*cols];
	for (int i = 0; i < rows * cols; i++) {
	    cells[0] = new Cell();
	}
    }

    private int getSum() {
	int s = 0;
	for (Cell c : cells) {
	    if (c.isSelected) {
		s += c.getValue();
	    }
	}
	return s;
    }

}