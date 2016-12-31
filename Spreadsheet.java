import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Spreadsheet extends JFrame {

    private Container ss;
    private Cell[] cells;    

    private int rows = 20, cols = 10;

    public Spreadsheet() {
	this.setTitle("Squirrel");
	this.setSize(960,720);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	ss.setLayout(new GridLayout(rows,cols,-5,-5));

	initialize();
    }

    private void initialize() {
	int prevSelectedCell = 0;
	cells = new Cell[rows*cols];
	
	for (int i = 0; i < cells.length; i++) {
	    
	    JTextField t = new JTextField();
	    	        
	    t.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			int cellNum = Integer.parseInt(e.getActionCommand());
			int v = Integer.parseInt(t.getText());
			cells[cellNum].setValue(v);		        
		    }
		});
	    t.setActionCommand(String.valueOf(i));

	    t.addMouseListener(new MouseListener() {
		    public void mousePressed(MouseEvent e) {
		        cells[prevSelectedCell].clearBorder();
			prevSelectedCell = i;
			Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
			t.setBorder(border);
		    }
		    public void mouseClicked(MouseEvent e){}
		    public void mouseReleased(MouseEvent e){}
		    public void mouseEntered(MouseEvent e){}
		    public void mouseExited(MouseEvent e){}
		});

	    cells[i] = new Cell(t);
	    ss.add(cells[i].textField);
	}
    }
    
}
