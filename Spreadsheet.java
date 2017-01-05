import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Spreadsheet extends JFrame {

    public static final int ROWS = 30, COLS = 12;

    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 720;
    private static final int BORDER_GAP = 0;

    private JFrame frame;
    private Container ss;
    private Cell selected;
    private JLabel SUM, MEAN, STDEV;

    private ArrayList<Cell> cells;
    private int[] highlighted;

    public Spreadsheet()
    {
	frame = new JFrame("Camel");

	this.setTitle("Spreadsheet");
	this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(0,COLS,BORDER_GAP,BORDER_GAP));

	SUM = new JLabel("SUM: ");
	MEAN = new JLabel("MEAN: ");
	STDEV = new JLabel("STDEV: ");
	
	initializeCells();
    }

    // draws cells
    private void initializeCells()
    {
	cells = new ArrayList<Cell>();

	highlighted = new int[ROWS*COLS];
	
	for (int i = 0; i < ROWS*COLS; i++) {

	    final Cell cell = new Cell(new JTextField(5),i);

	    if (i == 0) selected = cell;

	    cell.textField.addMouseListener(new MouseListener() {
		    public void mousePressed(MouseEvent e) {
			selected.deselect();
			for (int i : highlighted) {
			    cells.get(i).dehighlight();
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
	    cells.add(cell);
	}
    }

    public int releasedCellNum(Point p)
    {
	int tfWidth = (int) (cells.get(1).textField.getLocationOnScreen().getX() - cells.get(0).textField.getLocationOnScreen().getX());
	int tfHeight = (int) (cells.get(12).textField.getLocationOnScreen().getY() - cells.get(0).textField.getLocationOnScreen().getY());

        int i = 0;
	try {
	    while (cells.get(i).textField.getLocationOnScreen().getX() + tfWidth <= p.getX()) {
		i++;
	    }
	    while (cells.get(i).textField.getLocationOnScreen().getY() + tfHeight <= p.getY()) {
		i += COLS;
	    }
	} catch (ArrayIndexOutOfBoundsException e) {}

	return i;
    }

    public boolean highlightCells(int x, int y)
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
		cells.get(i).highlight(); 
		highlighted[j] = i;
		j++;
	    }
	}

	return true;
    }

    private void updateLabels()
    {
	int s = 0;
	for (int i : highlighted) {
	    s += cells.get(i).getValue();
	}
	SUM.setText("SUM: " + s);
	MEAN.setText("MEAN: " + (s / highlighted.length));
    }

    private int getSum() 
    {
	int s = 0;
	for (int i : highlighted) {
	    s += cells.get(i).getValue();
	}
	return s;
    }    
	 
    public static void main(String[] args)
    {
	Spreadsheet s = new Spreadsheet();
	s.setVisible(true);
    }
    
}
