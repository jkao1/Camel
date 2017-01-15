import java.util.ArrayList;
import java.util.List;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * TODO: topPanel
 */
public class Squirrel extends JFrame {

    // Constants used for spreadsheet body
    public static String OS = System.getProperty("os.name").toLowerCase();
    public static final int ROWS = 30, COLS = 12;
    
    private static int BORDER_GAP;
    private static String currentFileName;

    private JFrame frame; // spreadsheet frame
    private Container ss; // spreadsheet container
    private JMenuBar mb;
    private JMenu fileMenu, dataMenu;
    private JMenuItem fileMenu_Open, fileMenu_Save;
    private JMenuItem dataMenu_Graph;
    private JLabel count, sum, mean; // for selected areas	
    private Cell selected; // selected cell
    
    private ArrayList<Cell> cells; // stores all cells
    private ArrayList<Cell> highlighted; // stores highlighted cells
    
    // Constants used for graph input body
    private static final String GREET = "Greet";
    private static final String LINE_GRAPH = "Line Graph";
    private static final String BAR_GRAPH = "Bar Graph";
    private static final String SCATTER_GRAPH = "Scatter Graph";
    private static final String PIE_GRAPH = "Pie Graph";
    private static final String HISTOGRAM = "Histogram";
    private static final String[] graphLabels = { LINE_GRAPH, BAR_GRAPH, SCATTER_GRAPH, PIE_GRAPH, HISTOGRAM };    
       
    private JFrame graphFrame; // graph input frame
    private Container pane; // graph input container

    private CardLayout cl;
    private JPanel cards, greetPanel; // unique panels
    private JPanel nextPanel; // shared panels
    private JPanel[] graphPanels; // graph panels

    private JRadioButton[] radioButtons; // stores radio buttons
    private ButtonGroup group; // contains radio buttons
    private ActionListener exitSystem; // shared ActionListeners
    private JButton nextButton, cancelButton; // shared buttons

    private List<Integer> data; // stores graph data

    public Squirrel()
    {
	frame = new JFrame("Camel");

	this.setTitle("Spreadsheet");
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	ss = this.getContentPane();
	osDependentStyles();
	ss.setLayout(new GridLayout(0,COLS,BORDER_GAP,BORDER_GAP));
	createMenuBar();
	drawCells();

        this.pack();
    }
    
    /**
     * Customizes spreadsheet face based on user OS.
     */
    public void osDependentStyles()
    {
	if (OS.indexOf("mac") >= 0) {
	    BORDER_GAP = -6;
	} else {
	    BORDER_GAP = 0;
	}
    }

    /**
     * Creates the menu bar.
     */
    public void createMenuBar()
    {
	mb = new JMenuBar();

	// creates file menu
	fileMenu = new JMenu("File");
	fileMenu_Open = new JMenuItem("Open");
	fileMenu_Open.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    FileManager fm = new FileManager();
		    openFileWithData( fm.openFile() );
		}
	    });
	fileMenu.add( fileMenu_Open );
	fileMenu_Save = new JMenuItem("Save");
	fileMenu_Save.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    FileManager fm = new FileManager();
		    fm.saveFile(cells);
		}
	    });
	fileMenu.add( fileMenu_Save );
	mb.add(fileMenu);

	// creates data analysis menu
	dataMenu = new JMenu("Data");
	dataMenu_Graph = new JMenuItem("Graph");
	dataMenu_Graph.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    openGraphInput(); 
		}
	    });
	dataMenu.add( dataMenu_Graph );
	mb.add(dataMenu);

	this.setJMenuBar(mb);
    }

    /**
     * Draws ROWS*COLS cells (including alphanumeric labels).
     */
    public void drawCells()
    {	
	cells = new ArrayList<Cell>();
	highlighted = new ArrayList<Cell>();

	for (int i = 0; i < ROWS*COLS; i++) {

	    final Cell cell = new Cell(new JTextField(6),i);

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
			highlightCells(cell.getCellNum(), releasedCellNum(p));
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
			    select(cells.get(cell.getCellNum() - COLS));
			    updateTexts();
			}
			// right: arrow key right and tab
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_TAB) {
			    select(cells.get(cell.getCellNum() + 1));
			    updateTexts();
			}
			// down: arrow key down and enter
			else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
			    select(cells.get(cell.getCellNum() + COLS));
			    updateTexts();
			}
			// left: arrow key left and shift-tab
			else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getModifiers() == InputEvent.SHIFT_MASK && e.getKeyCode() == KeyEvent.VK_TAB) {
			    select(cells.get(cell.getCellNum() - 1));
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

    /**
     * TODO Column label highlighting
     * 
     * Selects a cell and clears all other selections.
     * If Cell c is a row label, the cell directly to the right will be selected and the row will be highlighted.
     * If Cell c is a column label, the cell directly underneath will be selected and the column will be highlighted.
     * 
     * @param c Cell to be selected
     * @
     */
    public void select(Cell c) {
	selected.unSelect();
	for (Cell h : highlighted) h.deHighlight();
	highlighted.clear();

	if (c.isLabel() && !cells.get(c.getCellNum()+COLS).isLabel()) { // column label
	    selected = cells.get(c.getCellNum() + COLS);
	    selected.select();
	    highlightCells(selected.getCellNum(), c.getCellNum()+(ROWS)*COLS);
	}
	else if (c.isLabel() && !cells.get(c.getCellNum()+1).isLabel()) { // row label
	    selected = cells.get(c.getCellNum() + 1);
	    selected.select();
	    highlightCells(c.getCellNum()+1, c.getCellNum()+COLS-1);
	} else {
	    selected = c;
	    selected.select();
	}
	updateTexts(); // updates COUNT/SUM/MEAN
    }

    /**
     * Selects all cells within a specified range of cell indexes.
     */
    public boolean highlightCells(int x, int y)
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
    
    /**
     * Returns index of cell the pointer was released on.
     * This index is then used in public boolean highlightCells(int x, int y).
     * 
     * @param p location of pointer on screen
     * @return index of cell from mouseReleased
     */
    private int releasedCellNum(Point p)
    {
	int tfWidth = (int) (cells.get(1).getX() - cells.get(0).getX());
	int tfHeight = (int) (cells.get(12).getY() - cells.get(0).getY());

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
	} catch (IndexOutOfBoundsException e) {} // pointer went out of window
	
	return i;
    }

    /**
     * After each selection: updates count/sum/mean labels and ID label (located in the top-left of the spreadsheet).
     * Called in public boolean highlightCells(int x, int y).
     */
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

    /**
     * Opens a new window for inputting Graph data.
     */
    public void openGraphInput()
    {
	graphFrame = new JFrame("Graph Input");
	graphFrame.setLayout( new FlowLayout() );

	graphFrame.setLocation(300,300);

	cards = new JPanel( new CardLayout() );

	greetPanel = new JPanel( new GridLayout(0,1) );
	greetPanel.add( new JLabel("Choose a graph:") );
	group = new ButtonGroup();
	
	radioButtons = new JRadioButton[ graphLabels.length ];
	for (int i = 0; i < radioButtons.length; i++) {
	    JRadioButton rb = new JRadioButton( graphLabels[i] );
	    rb.setActionCommand( String.valueOf(i) ); // for chosen graph identification
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
			    cl.show( cards, graphLabels[ Integer.parseInt(rb.getActionCommand()) ] );
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

    /**
     * Creates new instances of GraphInput features and adds them to a JPanel.
     * Note: components can only be added to the same cards.
     * 
     * @param p JPanel the components are added to.
     * @param s identifies graph type, as different GraphInputs have different components.
     */
    public void createAndAddDefault(JPanel p, String s)
    {
	// creates input range
	JPanel input = new JPanel();
	input.setLayout(new FlowLayout( FlowLayout.LEADING )); // left-aligned
	input.add(new JLabel("input range:"));
	JTextField inputRange = new JTextField(10);
	inputRange.setText( toInputRange() );
	input.add(inputRange);

	// creates bin range
	JPanel bin = new JPanel();
	bin.setLayout(new FlowLayout( FlowLayout.LEADING )); // left-aligned
	bin.add(new JLabel("bin range (optional):"));
	JTextField binRange = new JTextField(10);
	bin.add(binRange);

	// creates output range
	JPanel output = new JPanel();
	output.setLayout(new FlowLayout( FlowLayout.LEADING ));
	output.add(new JLabel("output:"));
	output.add(new JTextField(10));

	// creates "sort by" dropdown
	JPanel sort = new JPanel();
	sort.setLayout(new FlowLayout( FlowLayout.LEADING ));
	sort.add(new JLabel("sort by:")); // DOES NOT WORK
	JComboBox<String> sortOptions = new JComboBox<>(new String[] { "none", "ascending", "descending" });
	sortOptions.setEditable(false);

	// chart creation check box (histogram-exclusive)
	JCheckBox chart = new JCheckBox("Chart");

	// creates the default buttons "Ok" and "Cancel
	JPanel defaultButtons = new JPanel();
	JButton ok = new JButton("Ok");
	ok.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (false) {// !( inputRange.getText().matches("\w\d\:\w\d") ) || inputRange.getText().length() != 5 ) { // matches Letter_Int_Letter_Int
			throw new IllegalArgumentException("error: input must be of the form Letter_Int_Letter_Int. graph creation unsucessful.");
		    } else {
			highlightInputRange( inputRange.getText() );
			switch ( s.charAt(0) ) {
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
	p.setLayout( new GridLayout(0,1) );
	p.add( new JLabel(s) );
	p.add(input);
	switch ( s.charAt(0) ) {
	case 'L': p.add(bin); break; // line graph
	case 'B': p.add(sortOptions); break; // bar graph
	case 'S': break; // scatter graph
	case 'P': p.add(sortOptions); break; // pie graph
	case 'H': p.add(bin); p.add(output); p.add(chart); p.add(sortOptions); break; // histogram
	}
	p.add(defaultButtons);
    }

    /**
     * Clears spreadsheet and writes data.
     * Data gathered from (FileManager fm).openFile().
     * 
     * @param v ArrayList which contains information in the form cellIndex:cellValue.
     */
    private void openFileWithData( ArrayList<String> v )
    {
	for ( Cell c : cells) c.clear();
	for ( String s : v ) {
	    String[] info = s.split( ":" );
	    cells.get( Integer.parseInt( info[0] )).setValue( info[1] );
	}
    }

    /**
     * Highlights the input range from GraphInput.
     * The selection is used in reading data for making graphs.
     */
    private void highlightInputRange( String inputRange )
    {	
	String[] bounds = inputRange.split( ":" );
	int a = Integer.parseInt( bounds[0].substring( 1,bounds[0].length() )) * COLS + bounds[0].charAt(0) - 'A' + 1;
	int b = Integer.parseInt( bounds[1].substring( 1,bounds[1].length() )) * COLS + bounds[1].charAt(0) - 'A' + 1;

	highlightCells( a,b );
    }

    /**
     * Returns String inputRange to be put as default in GraphInput.
     */
    private String toInputRange()
    {
	if ( highlighted.size() == 0 ) return "";

	return highlighted.get(0).toString() + ":" + highlighted.get( highlighted.size()-1 ).toString();
    }

    /**
     * Makes a line graph with a 1-column input.
     */
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
