import java.util.ArrayList;
import java.util.List;

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
    private Container ss, top;
    private GridBagConstraints topC; // for elements in the top Container

    private JMenuBar mb;
    private JMenu fileMenu, dataMenu;
    private JMenuItem fileMenu_New, dataMenu_Graph;
    private JLabel count, sum, mean;
    private JTextField cellID, textInput;
	
    private Cell selected;

    private ArrayList<Cell> cells;
    private ArrayList<Cell> highlighted;
    
    // graph input variables
    private static final String GREET = "Greet";
    private static final String LINE_GRAPH = "Line Graph";
    private static final String BAR_GRAPH = "Bar Graph";
    private static final String SCATTER_GRAPH = "Scatter Graph";
    private static final String PIE_GRAPH = "Pie Graph";
    private static final String HISTOGRAM = "Histogram";
    private static final String[] graphLabels = { LINE_GRAPH, BAR_GRAPH, SCATTER_GRAPH, PIE_GRAPH, HISTOGRAM };    
       
    private JFrame graphFrame;
    private Container pane;

    private CardLayout cl;
    private JPanel cards, nextPanel, greetPanel;
    private JPanel[] graphPanels;

    private JRadioButton[] radioButtons;
    private ButtonGroup group;
    private ActionListener exitSystem;
    private JButton nextButton, cancelButton;

    private List<Integer> data;

    public Squirrel()
    {
	frame = new JFrame("Camel");

	// a few default settings
	this.setTitle("Spreadsheet");
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	//this.setResizable(false);

	ss = this.getContentPane();
	osDependentStyles(); // styles based on OS (specifically border gap)
	ss.setLayout(new GridLayout(0,COLS,BORDER_GAP,BORDER_GAP));
	createMenuBar();
	initializeCells();

        this.pack();
    }
    
    public void osDependentStyles()
    {
	if (OS.indexOf("mac") >= 0) {
	    BORDER_GAP = -6;
	} else {
	    BORDER_GAP = 0;
	}
    }
    
    public void createMenuBar()
    {
	mb = new JMenuBar();

	// creates file menu
	fileMenu = new JMenu("File");
	fileMenu_New = new JMenuItem("New");		
	fileMenu.add(fileMenu_New);

	mb.add(fileMenu);

	// creates data analysis menu
	dataMenu = new JMenu("Data");
	dataMenu_Graph = new JMenuItem("Graph");
	dataMenu_Graph.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    makeGraphInput();
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

	for (int i = 0; i < ROWS*COLS; i++) {

	    final Cell cell = new Cell(new JTextField(10),i);

	    // sets default select to the first enabled cell
	    if (i == COLS + 1) selected = cell;

	    cell.getTextField().addMouseListener(new MouseListener()
		{		    
		    public void mousePressed(MouseEvent e) {
			select(cell);
		    }		    
		    public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) { // double click will allow cell to be editable
			    cell.makeEditable();
			}
		    }		    
		    public void mouseReleased(MouseEvent e) {
			Point p = e.getLocationOnScreen();
			highlightCells(cell.cellNum, releasedCellNum(p));
		    }		    
		    public void mouseEntered(MouseEvent e) {}
		    public void mouseExited(MouseEvent e) {}
		});
	    cell.getTextField().addKeyListener(new KeyListener()
		{
		    public void keyPressed(KeyEvent e)
		    {
			// up: arrow key up and shift-enter
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getModifiers() == InputEvent.SHIFT_MASK && e.getKeyCode() == KeyEvent.VK_ENTER) {
			    select(cells.get(cell.cellNum - COLS));
			    updateTexts();
			}
			// right: arrow key right and tab
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_TAB) {
			    select(cells.get(cell.cellNum + 1));
			    updateTexts();
			}
			// down: arrow key down and enter
			else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
			    select(cells.get(cell.cellNum + COLS));
			    updateTexts();
			}
			// left: arrow key left and shift-tab
			else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getModifiers() == InputEvent.SHIFT_MASK && e.getKeyCode() == KeyEvent.VK_TAB) {
			    select(cells.get(cell.cellNum - 1));
			    updateTexts();
			}
			else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			    selected.clear();
			    for (Cell c : highlighted) c.clear();
			}
			// catch independence
			else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {}
			// other characters: types in field
			else {
			    cell.clear();
			    cell.makeEditable();
			}
		    }
		    public void keyReleased(KeyEvent e) {}
		    public void keyTyped(KeyEvent e) {}
		});

	    ss.add(cell.getTextField());
	    cells.add(cell);
	}
	
	count = new JLabel("COUNT: ");
	ss.add(count);
	
	sum = new JLabel("SUM: ");
	ss.add(sum);
	
	mean = new JLabel("MEAN: ");
	ss.add(mean);	
    }

    public void select(Cell c) {
	selected.unSelect();
	for (Cell h : highlighted) h.deHighlight();
	highlighted.clear();

	if (c.isLabel && cells.get(c.cellNum+1).isLabel) { // if cell is alphabetical label
	    selected = cells.get(c.cellNum + COLS);
	    selected.select(); // selects cell directly underneath
	    highlightCells(selected.cellNum, c.cellNum+(ROWS)*COLS); // highlights column
	}
	else if (c.isLabel && cells.get(c.cellNum+COLS).isLabel) { // if cell is numerical label
	    selected = cells.get(c.cellNum + 1);
	    selected.select(); // selects cell directly to the right
	    highlightCells(c.cellNum+1, c.cellNum+COLS-1); // highlights row
	} else {
	    c.select();
	    selected = c;
	}
	updateTexts(); // updates COUNT/SUM/MEAN
    }

    public int releasedCellNum(Point p)
    {
	int tfWidth = (int) (cells.get(1).getX() - cells.get(0).getX()); // interval width for the while loop
	int tfHeight = (int) (cells.get(12).getY() - cells.get(0).getY()); // interval height for the while loop

        int i = 0;
	try {
	    // moves X by an interval of textfield's width until reaches Point p's X
	    while (cells.get(i).getX() + tfWidth <= p.getX()) {
		i++;
	    }
	    // moves Y by an interval of textfield's height until reaches Point p's Y
	    while (cells.get(i).getY() + tfHeight <= p.getY()) {
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

	updateTexts();
	return true;
    }

    // updates COUNT/SUM/MEAN, called in public boolean highlightCells(int x, int y)
    private void updateTexts()
    {
	cells.get(0).setValue(selected.toString() + ": " + selected.getValue());

	// math labels sum and mean count, respectively
	int s = 0; 
	int n = 0;

	if (highlighted.size() == 0) {
	    s = selected.getIntValue();
	    n = 1;
	    count.setText("COUNT: " + 1);
	} else {
	    for (Cell c : highlighted) {
		if (c.isEmpty()) continue;
		s += c.getIntValue(); // this and previous line check for String or empty values and ignores them for the mean count
		n++;
	    }		
	    count.setText("COUNT: " + highlighted.size());
	}
	sum.setText("SUM: " + s);
	mean.setText("MEAN: " + ((double) (s) / n));
    }

    public void createAndAddDefault(JPanel p, String s)
    {
	// creates input range
	JPanel input = new JPanel();
	input.setLayout(new FlowLayout(FlowLayout.LEADING)); // left-aligned
	input.add(new JLabel("input range:"));
        JTextField inputRange = new JTextField(10);
	input.add(inputRange);

	// creates bin range
	JPanel bin = new JPanel();
	bin.setLayout(new FlowLayout(FlowLayout.LEADING)); // left-aligned
	bin.add(new JLabel("bin range (optional):"));
        JTextField binRange = new JTextField(10);
	bin.add(binRange);

	// creates output range
	JPanel output = new JPanel();
	output.setLayout(new FlowLayout(FlowLayout.LEADING));
	output.add(new JLabel("output:"));
	output.add(new JTextField(10));

	// creates "sort by" dropdown
	JPanel sort = new JPanel();
	sort.setLayout(new FlowLayout(FlowLayout.LEADING));
	sort.add(new JLabel("sort by:")); // DOES NOT WORK
	JComboBox<String> sortOptions = new JComboBox<>(new String[] {"none", "ascending", "descending"});
	sortOptions.setEditable(false);

	// chart creation check box (histogram-exclusive)
	JCheckBox chart = new JCheckBox("Chart");

	// creates the default buttons "Ok" and "Cancel
	JPanel defaultButtons = new JPanel();
	JButton ok = new JButton("Ok");
	ok.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (false){// !( inputRange.getText().matches("\w\d\:\w\d") ) || inputRange.getText().length() != 5 ) { // matches Letter_Int_Letter_Int
			throw new IllegalArgumentException("error: input must be of the form Letter_Int_Letter_Int. graph creation unsucessful.");
		    } else {
			highlightInputRange(inputRange.getText());
			switch (s.charAt(0)) {
			case 'L':
			    if ( inputRange.getText().charAt(0) == inputRange.getText().charAt( inputRange.getText().indexOf(":")+1 ) ) makeLineGraph();
			    else System.out.println("error: line graphs can only take one column of data.");
			}
		    }
		    graphFrame.dispose();
		}
	    });
	defaultButtons.add(ok);
	defaultButtons.add(cancelButton);

	// sets default settings for JPanel p
	p.setLayout(new GridLayout(0, 1));
	p.add(new JLabel(s));
	p.add(input);
	switch (s.charAt(0)) {
	case 'L': p.add(bin); break; // line graph
	case 'B': p.add(sortOptions); break; // bar graph
	case 'S': break; // scatter graph
	case 'P': p.add(sortOptions); break; // pie graph
	case 'H': p.add(bin); p.add(output); p.add(chart); p.add(sortOptions); break; // histogram
	}
	p.add(defaultButtons);
    }

    private void highlightInputRange(String inputRange)
    {	
	String[] bounds = inputRange.split(":");
	String b1 = bounds[0];
	String b2 = bounds[1];

	select( cells.get(Integer.parseInt(b1.substring(1,b1.length())) * COLS + b1.charAt(0) - 'A' + 1) );
	highlightCells( Integer.parseInt(b1.substring(1,b1.length())) * COLS + b1.charAt(0) - 'A' + 1, Integer.parseInt(b2.substring(1,b2.length())) * COLS + b2.charAt(0) - 'A' + 1 );
    }

    public void makeGraphInput()
    {
	graphFrame = new JFrame("Graph Input");
	graphFrame.setLayout(new FlowLayout());

	graphFrame.setLocation(300,300);

	cards = new JPanel(new CardLayout());

	greetPanel = new JPanel(new GridLayout(0,1));
	greetPanel.add(new JLabel("Choose a graph:"));
	group = new ButtonGroup();
	
	radioButtons = new JRadioButton[graphLabels.length];
	for (int i = 0; i < radioButtons.length; i++) {
	    JRadioButton rb = new JRadioButton(graphLabels[i]);
	    rb.setActionCommand(String.valueOf(i));
	    radioButtons[i] = rb;
	    group.add(rb);
	    greetPanel.add(rb);
	};
	// moves radio button panel to panel of selected graph
	nextPanel = new JPanel(new FlowLayout());		    
	nextButton = new JButton("Next");
	nextButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    cl = (CardLayout) (cards.getLayout());
		    for (JRadioButton rb : radioButtons) {
			if (rb.isSelected()) {			    
			    cl.show( cards, graphLabels[Integer.parseInt(rb.getActionCommand())] );
			}
		    }
		}
	    });
	nextPanel.add(nextButton);

	// for cancel buttons
	exitSystem = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    graphFrame.dispose();
		}
	    };	
	cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(exitSystem);
	nextPanel.add(cancelButton);
	greetPanel.add(nextPanel);
	cards.add(greetPanel, "Greet");

	// all the panels for actual graph input
	graphPanels = new JPanel[graphLabels.length];
	for (int i = 0; i < graphPanels.length; i++) {
	    JPanel p = new JPanel();	    
	    createAndAddDefault(p, graphLabels[i]); // cannot add same component to multiple cards, so declares new components for each panel
	    cards.add(p, graphLabels[i]);
	}
	
	pane = graphFrame.getContentPane();
	pane.add(cards, BorderLayout.CENTER);
	graphFrame.pack();
	graphFrame.setVisible(true);

    }

    public void makeLineGraph()
    {
        data = new ArrayList<Integer>();
	for (Cell c : highlighted) data.add(c.getIntValue());

	LineGraph l = new LineGraph(data);
	JFrame lineGraph = new JFrame(LINE_GRAPH);

	lineGraph.getContentPane().add(l);
	lineGraph.pack();
	lineGraph.setVisible(true);
    }

    public static void main(String[] args)
    {
	Squirrel s = new Squirrel();
	s.setVisible(true);
	LineGraph l = new LineGraph(new ArrayList<Integer>());
    }
    
}
