import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Squirrel extends JFrame {

    public static String OS = System.getProperty("os.name").toLowerCase();
    public static final int ROWS = 30, COLS = 12;

    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 720;
    private static final int MATH_LABEL_START_LEVEL = 3;
    private static final int SS_START_LEVEL = 1;
    private static int BORDER_GAP;

    private JFrame frame;
    private Container ss;
    private GridBagConstraints c_Cell, c_MathLabel, c_CellID, c_TextInput;

    private JMenuBar mb;
    private JMenu fileMenu, dataMenu;
    private JMenuItem fileMenu_New, dataMenu_Graph;
    private JLabel count, sum, mean;
    private JTextField cellID, textInput;
	
    private Cell selected;

    private ArrayList<Cell> cells;
    private ArrayList<Cell> highlighted;

    public Squirrel()
    {
	frame = new JFrame("Camel");
	
	this.setTitle("Spreadsheet");
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setResizable(false);

	ss = this.getContentPane();
	osDependentStyles(); // styles based on OS
	ss.setLayout(new GridBagLayout());
	initializeConstraints();

	createMenuBar();	
	initializeCells();

	this.pack();
    }

    public void osDependentStyles()
    {
	if (OS.indexOf("mac") >= 0) {
	    BORDER_GAP = -1;
	} else {
	    BORDER_GAP = 0;
	}
    }

    public void initializeConstraints()
    {
	c_CellID = new GridBagConstraints();
	c_CellID.gridx = 0;
	c_CellID.gridy = 0;
	c_CellID.gridwidth = 1;
	c_CellID.weightx = 1;
	c_CellID.fill = GridBagConstraints.HORIZONTAL;
	
	c_TextInput = new GridBagConstraints();
	c_TextInput.gridx = 1;
	c_TextInput.gridy = 0;
	c_TextInput.gridwidth = COLS - c_CellID.gridwidth;
	c_TextInput.weightx = 1;
	c_TextInput.fill = GridBagConstraints.HORIZONTAL;

	c_Cell = new GridBagConstraints();
	c_Cell.weightx = 0;
	c_Cell.weighty = 0;
	c_Cell.fill = GridBagConstraints.HORIZONTAL;
	c_Cell.insets = new Insets(BORDER_GAP,BORDER_GAP,BORDER_GAP,BORDER_GAP);
	
	c_MathLabel = new GridBagConstraints();
	c_MathLabel.gridx = 0;
	c_MathLabel.gridy = ROWS + MATH_LABEL_START_LEVEL;
    }
    
    public void createMenuBar()
    {
	mb = new JMenuBar();

	fileMenu = new JMenu("File");
	fileMenu_New = new JMenuItem("New");		
	fileMenu.add(fileMenu_New);

	mb.add(fileMenu);

	dataMenu = new JMenu("Data");
	dataMenu_Graph = new JMenuItem("Graph");
	dataMenu_Graph.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    //GraphInput g = new GraphInput();
		}
	    });
	dataMenu.add(dataMenu_Graph);

	mb.add(dataMenu);

	this.setJMenuBar(mb);
    }

    // draws cells
    public  void initializeCells()
    {	
	cells = new ArrayList<Cell>();
	highlighted = new ArrayList<Cell>();

	cellID = new JTextField();
	ss.add(cellID, c_CellID);
	textInput = new JTextField();
	ss.add(textInput, c_TextInput);

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
			    cell.textField.getCaret().setVisible(true); // shows cursor
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

	    c_Cell.gridx = i % COLS;
	    c_Cell.gridy = i / COLS + SS_START_LEVEL;
	    
	    ss.add(cell.textField, c_Cell);
	    cells.add(cell);
	}
	
	count = new JLabel("COUNT: ");
	c_MathLabel.gridx++;
	ss.add(count, c_MathLabel);
	
	sum = new JLabel("SUM: ");
	c_MathLabel.gridx++;
	ss.add(sum, c_MathLabel);
	
	mean = new JLabel("MEAN: ");
	c_MathLabel.gridx++;
	ss.add(mean, c_MathLabel);	
    }

    public void select(Cell c) {
	selected.unSelect();
	for (Cell h : highlighted) h.deHighlight();
	highlighted.clear();
	c.select();
	selected = c;
	updateMathLabels();
    }

    public int releasedCellNum(Point p)
    {
	int tfWidth = (int) (cells.get(1).textField.getLocationOnScreen().getX() - cells.get(0).textField.getLocationOnScreen().getX());
	int tfHeight = (int) (cells.get(12).textField.getLocationOnScreen().getY() - cells.get(0).textField.getLocationOnScreen().getY());

        int i = 0;
	try {
	    // moves X by an interval of textfield's width until reaches Point p's X
	    while (cells.get(i).textField.getLocationOnScreen().getX() + tfWidth <= p.getX()) {
		i++;
	    }
	    // moves Y by an interval of textfield's height until reaches Point p's Y
	    while (cells.get(i).textField.getLocationOnScreen().getY() + tfHeight <= p.getY()) {
		i += COLS;
	    }
	} catch (IndexOutOfBoundsException e) {
	    // pointer went out of window
	}

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

	updateMathLabels();
	return true;
    }

    // updates COUNT/SUM/MEAN, called in public boolean highlightCells(int x, int y)
    private void updateMathLabels()
    {
	int s = 0;
	int n = 0;
	for (Cell c : highlighted) {
	    if (c.textField.getText().equals("")) continue;
	    s += c.getIntValue();
	    n++;
	}
	count.setText("COUNT: " + highlighted.size());
	sum.setText("SUM: " + s);
	mean.setText("MEAN: " + ((double) (s) / n));
    }
	
    public static void main(String[] args)
    {
	// just for testing
	if (args.length > 0 && args[0].equals("cmd")) {
	    System.out.println(OS);
	} else {
	    Squirrel s = new Squirrel();
	    s.setVisible(true);
	}
    }
    
}
