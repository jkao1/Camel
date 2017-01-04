import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Spreadsheet extends JFrame {

    public static final int ROWS = 30, COLS = 12;

    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 720;
    private static final int BORDER_GAP = -6;

    private JFrame frame;
    private Container ss;
    private Cell selected;
    private Cell[] cells;
    private int[] highlighted;

    public Spreadsheet()
    {
	frame = new JFrame("Camel");

	this.setTitle("Spreadsheet");	
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(ROWS,COLS,BORDER_GAP,BORDER_GAP));
	
	initializeCells();
    }

    // draws cells
    private void initializeCells()
    {
	cells = new Cell[ROWS*COLS];
	highlighted = new int[ROWS*COLS];
	
	for (int i = 0; i < cells.length; i++) {
	    
	    final Cell cell = new Cell(new JTextField(5),i);

	    if (i == 0) selected = cell;

	    cell.textField.addMouseListener(new MouseListener() {
		    public void mousePressed(MouseEvent e) {
			selected.deselect();
			for (int i : highlighted) {
			    cells[i].dehighlight();
			}
			highlighted = new int[ROWS*COLS];
			
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
	    
	    ss.add(cell.textField);
	    cells[i] = cell;
	}
    }

    public int releasedCellNum(Point p)
    {
	int tfWidth = (int) (cells[1].textField.getLocationOnScreen().getX() - cells[0].textField.getLocationOnScreen().getX());
	int tfHeight = (int) (cells[12].textField.getLocationOnScreen().getY() - cells[0].textField.getLocationOnScreen().getY());

        int i = 0;
	try {
	    while (cells[i].textField.getLocationOnScreen().getX() + tfWidth <= p.getX()) {
		i++;
	    }
	    while (cells[i].textField.getLocationOnScreen().getY() + tfHeight <= p.getY()) {
		i += COLS;
	    }
	} catch (ArrayIndexOutOfBoundsException e) {}

	return i;
    }

    private boolean highlightCells(int x, int y)
    {
	int j = 0;
	int a = Math.min(x,y);
	int b = Math.max(x,y);

	if (a % COLS > b % COLS) {
	    int d = a % COLS - b % COLS;
	    a -= d;
	    b += d;
	}

	if (b > ROWS * COLS) return false;
	
	for (int i = a; i <= b; i++) {
	    if (i % COLS >= a % COLS && i / COLS >= a / COLS && i % COLS <= b % COLS && i / COLS <= b / COLS) {
		cells[i].highlight(); 
		highlighted[j] = i;
		j++;
	    }
	}

	return true;
    }

    private int sumCells() 
    {
	int s = 0;
	for (int i : highlighted) {
	    s += cells[i].getValue();
	}
	return s;
    }    
	 
    public static void main(String[] args)
    {
	Spreadsheet s = new Spreadsheet();
	s.pack();
	s.setVisible(true);
    }
    
}
