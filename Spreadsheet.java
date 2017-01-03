import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Spreadsheet extends JFrame {

    private Container ss;
    private Cell selected;
    private int rows = 25, cols = 12;

    private Cell[] cells;
    private int[] highlighted;

    private JLabel sum, mean;

    
    public Spreadsheet()
    {
	this.setTitle("Squirrel");
	this.setSize(960,720);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(rows,cols,-6,-6));

	initializeCells();
    }

    // draws cells
    private void initializeCells()
    {
	cells = new Cell[rows*cols];
	highlighted = new int[rows*cols];
	
	for (int i = 0; i < cells.length; i++) {
	    
	    Cell cell = new Cell(new JTextField(),i);
	    
	    if (i == 0) {
		cell.select();
		selected = cell;
	    }

	    cell.textField.addMouseListener(new MouseListener() {
		    public void mousePressed(MouseEvent e) {
			selected.deselect();
			for (int i : highlighted) {
			    cells[i].dehighlight();
			}
			highlighted = new int[rows*cols];			
			
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

	    cell.setValue(i);

	    ss.add(cell.textField);
	    cells[i] = cell;
	}
    }

    private int releasedCellNum(Point p)
    {
	int tfWidth = (int) (cells[1].textField.getLocationOnScreen().getX() - cells[0].textField.getLocationOnScreen().getX());
	int tfHeight = (int) (cells[12].textField.getLocationOnScreen().getY() - cells[0].textField.getLocationOnScreen().getY());

        int i = 0;
	while (cells[i].textField.getLocationOnScreen().getX() + tfWidth <= p.getX()) {
	    i++;
	}
	while (cells[i].textField.getLocationOnScreen().getY() + tfHeight <= p.getY()) {
	    i += cols;
	}
	return i;
    }

    private void highlightCells(int x, int y)
    {
	int j = 0;
	int a = Math.min(x,y);
	int b = Math.max(x,y);

	if (a % cols > b % cols) {
	    int d = a % cols - b % cols;
	    a -= d;
	    b += d;
	}
	
	for (int i = a; i <= b; i++) {
	    if (i % cols >= a % cols && i / cols >= a / cols && i % cols <= b % cols && i / cols <= b / cols) {
		cells[i].highlight(); 
		highlighted[j] = i;
		j++;
	    }
	}
    }

    private int sumCells() {
	int s = 0;
	for (int i : highlighted) {
	    s += cells[i].getValue();
	}
	return s;
    }    
	 
    public static void main(String[] args)
    {
	Spreadsheet s = new Spreadsheet();
	s.setVisible(true);
    }
    
}
