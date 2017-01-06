import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Spreadsheet extends JFrame {

    public static String OS = System.getProperty("os.name").toLowerCase();
    public static final int ROWS = 30, COLS = 12;

    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 720;
    private static int BORDER_GAP;

    private JFrame frame;
    private Container ss;
    private Cell selected;
    private JLabel COUNT, SUM, MEAN;

    private ArrayList<Cell> cells;
    private ArrayList<Cell> highlighted;

    public Spreadsheet()
    {
	frame = new JFrame("Camel");
	
	this.setTitle("Spreadsheet");
	this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	variedStyles(); // styles based on OS
	ss.setLayout(new GridLayout(0,COLS,BORDER_GAP,BORDER_GAP));

	COUNT = new JLabel("COUNT: ");
	SUM = new JLabel("SUM: ");
	MEAN = new JLabel("MEAN: ");
	
	initializeCells();

	ss.add(new JLabel(""));
	ss.add(COUNT);
	ss.add(SUM);
	ss.add(MEAN);
    }

    public void variedStyles()
    {
	if (OS.indexOf("mac") >= 0) {
	    BORDER_GAP = -6;
	} else {
	    BORDER_GAP = 0;
	}
    }

    // draws cells
    public  void initializeCells()
    {
	cells = new ArrayList<Cell>();
	highlighted = new ArrayList<Cell>();
	
	for (int i = 0; i < ROWS*COLS; i++) {

	    final Cell cell = new Cell(new JTextField(5),i);

	    if (i == 0) selected = cell;

	    cell.textField.addMouseListener(new MouseListener()
		{		    
		    public void mousePressed(MouseEvent e) {
			select(cell);
		    }		    
		    public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
			    cell.textField.setEditable(true);
			    cell.textField.getCaret().setVisible(true);
			}
		    }		    
		    public void mouseReleased(MouseEvent e) {
			Point p = e.getLocationOnScreen();
			highlightCells(cell.cellNum, releasedCellNum(p));
		    }		    
		    public void mouseEntered(MouseEvent e) {}
		    public void mouseExited(MouseEvent e) {}
		});
	    cell.textField.addKeyListener(new KeyListener()
		{
		    public void keyPressed(KeyEvent e)
		    {
			// up: arrow key up and shift-enter
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getModifiers() == InputEvent.SHIFT_MASK && e.getKeyCode() == KeyEvent.VK_ENTER) {
			    select(cells.get(cell.cellNum - COLS));
			}
			// right: arrow key right and tab
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_TAB) {
			    select(cells.get(cell.cellNum + 1));
			}
			// down: arrow key down and enter
			else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
			    select(cells.get(cell.cellNum + COLS));
			}
			// left: arrow key left and shift-tab
			else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getModifiers() == InputEvent.SHIFT_MASK && e.getKeyCode() == KeyEvent.VK_TAB) {
			    select(cells.get(cell.cellNum - 1));
			}
			// catch independence
			else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {}
			// other characters: types in field
			else {
			    cell.textField.setEditable(true);
			    cell.textField.getCaret().setVisible(true);
			}
		    }
		    public void keyReleased(KeyEvent e) {}
		    public void keyTyped(KeyEvent e) {}
		});
	    ss.add(cell.textField);
	    cells.add(cell);
	}
    }

    public void select(Cell c) {
	selected.unSelect();
	for (Cell h : highlighted) h.deHighlight();
	highlighted.clear();
	c.select();
	selected = c;
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
	} catch (IndexOutOfBoundsException e) {}

	return i;
    }

    public boolean highlightCells(int x, int y) // to do: highlight labels
    {
	if (x == y) return true;
	
	// switches a % COLS and b % COLS to maintain top-left/bottom-right endpoints
	int a = Math.min(x,y);
	int b = Math.max(x,y);
	if (a % COLS > b % COLS) {
	    int sw = a % COLS - b % COLS;
	    a -= sw;
	    b += sw;
	}

	if (b > ROWS * COLS) return false; // off the screen
	
	for (int i = a; i <= b; i++) {
	    if (i % COLS >= a % COLS && i / COLS >= a / COLS && i % COLS <= b % COLS && i / COLS <= b / COLS) {
		cells.get(i).highlight(); 
		highlighted.add(cells.get(i));
	    }
	}

	updateLabels();
	return true;
    }

    // updates COUNT/SUM/MEAN, called in public boolean highlightCells(int x, int y)
    private void updateLabels()
    {
	int s = 0;
	int n = 0;
	for (Cell c : highlighted) {
	    if (c.textField.getText().equals("")) continue;
	    s += c.getIntValue();
	    n++;
	}
	COUNT.setText("COUNT: " + highlighted.size());
	SUM.setText("SUM: " + s);
	MEAN.setText("MEAN: " + ((double) (s) / n));	
    }

    public static void main(String[] args)
    {
	if (args.length > 0 && args[0].equals("cmd")) {
	    System.out.println(OS);
	} else {
	    Spreadsheet s = new Spreadsheet();
	    s.setVisible(true);
	}
    }
    
}
