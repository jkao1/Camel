import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Spreadsheet extends JFrame {

    private Container ss;
    private Cell[] cells;
    private Cell selected;

    private int rows = 25, cols = 12;

    public Spreadsheet() {
	this.setTitle("Squirrel");
	this.setSize(960,720);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(rows,cols,-6,-6));

	initialize();
    }

    private void initialize() {
	cells = new Cell[rows*cols];
	
	for (int i = 0; i < cells.length; i++) {
	    
	    Cell cell = new Cell(new JTextField());
	    
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
			System.out.println(""+releasedCell(p));
		    }
		    public void mouseEntered(MouseEvent e){}
		    public void mouseExited(MouseEvent e){}
		});

	    ss.add(cell.textField);
	    cells[i] = cell;
	}
    }
    
    private int releasedCell(Point p) {
	int tfWidth = (int) (cells[1].textField.getLocationOnScreen().getX() - cells[0].textField.getLocationOnScreen().getX());
	int tfHeight = (int) (cells[1].textField.getLocationOnScreen().getY() - cells[0].textField.getLocationOnScreen().getY());
	
        int i = 0;
	while (cells[i].textField.getLocationOnScreen().getX() + tfWidth <= p.getX()) {
	    i++;
	}
	while (cells[i].textField.getLocationOnScreen().getY() + tfHeight <= p.getY()) {
	    i += cols;
	}
	return i;
    }
    
    public static void main(String[] args) {
	Spreadsheet s = new Spreadsheet();
	s.setVisible(true);
    }
    
}
