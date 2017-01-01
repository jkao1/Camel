import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Spreadsheet extends JFrame {

    private Container ss;
    private Cell[] cells;
    private Cell[] highlighted;
    private Cell selected;

    private int rows = 25, cols = 12;

    public Spreadsheet()
    {
	this.setTitle("Squirrel");
	this.setSize(960,720);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(rows,cols,-6,-6));

	initialize();
    }

    private void initialize()
    {
	cells = new Cell[rows*cols];
	
	for (int i = 0; i < cells.length; i++) {
	    
	    Cell cell = new Cell(new JTextField(),i);
	    
	    if (i == 0) {
		cell.select();
		selected = cell;
	    }

	    cell.textField.addMouseListener(new MouseListener() {
		    public void mousePressed(MouseEvent e) {
			selected.deselect();			
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

    private void highlightCells(int a, int b)
    {
	for (int i = a+1; i <= b; i++) {
	    if (i % cols >= a % cols && i / cols >= a / cols &&
		i % cols <= b % cols && i / cols <= b / cols) {
		cells[i].highlight();
	    }
	}
    }	
	 
    public static void main(String[] args)
    {
	Spreadsheet s = new Spreadsheet();
	s.setVisible(true);
    }
    
}
