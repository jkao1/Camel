import javax.swing.*;
import java.awt.*;

public class Spreadsheet extends JFrame {

    private Container ss;
    private Cell[] cells;    

    private int rows = 10, cols = 10;

    public Spreadsheet() {
	this.setTitle("Squirrel");
	this.setSize(960,720);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(rows,cols));
	
	initCells();
    }

    private void initCells() {
	cells = new Cell[rows*cols];
	
	for (int i = 0; i < cells.length; i++) {
	    cells[i] = new Cell();
	    JTextField t = new JTextField();
	    t.setText(String.valueOf(cells[i].getValue()));
	    ss.add(t);		      
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
