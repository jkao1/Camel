import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class Squirrel extends JFrame implements ActionListener {

    // Constants used for spreadsheet body
    public static String OS = System.getProperty("os.name").toLowerCase();
    public static final int ROWS = 30, COLS = 12;
    
    private static int BORDER_GAP;
    private static int HISTOGRAM_TABLE_WIDTH = 5;
    private static String currentFileName;

    private JFrame frame; // Squirrel frame
    private Container pane; // Squirrel container
    private JPanel ss; // spreadsheet panel
    private JLabel count, sum, mean; // for selected areas	
    private Cell selected; // selected cell
    private JTextField currentCellID, currentCellText;
    
    private ArrayList<Cell> cells; // stores all cells
    private ArrayList<Cell> highlighted; // stores highlighted cells
    private ArrayList<String> functions; // stores available math functions
    
    // Constants used for graph input body
    private static final String GREET = "Greet";
    private static final String LINE_GRAPH = "Line Graph";
    private static final String BAR_GRAPH = "Bar Graph";
    private static final String SCATTER_GRAPH = "Scatter Graph";
    private static final String PIE_GRAPH = "Pie Graph";
    private static final String HISTOGRAM = "Histogram";
    private static final String[] graphLabels = { LINE_GRAPH, BAR_GRAPH, SCATTER_GRAPH, PIE_GRAPH, HISTOGRAM };    
       
    private JFrame graphFrame; // graph input frame
    private Container graphPane; // graph input container

    private CardLayout cl;
    private JPanel cards, greetPanel; // unique panels
    private JPanel nextPanel; // shared panels
    private JPanel[] graphPanels; // graph panels

    private JRadioButton[] radioButtons; // stores radio buttons
    private ButtonGroup group; // contains radio buttons
    private ActionListener exitSystem; // shared ActionListeners
    private JButton nextButton, cancelButton; // shared buttons

    public Squirrel()
    {
	frame = new JFrame("Squirrel");

	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	pane = this.getContentPane();

	osDependentStyles();	
	createMenuBar();
	initializeSpreadsheet();
	initializeFunctions();

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
	JMenuBar mb = new JMenuBar();

	// creates file menu
	JMenu fileMenu = new JMenu("File");
	JMenuItem fileMenu_Open = new JMenuItem("Open");
	fileMenu_Open.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    FileManager fm = new FileManager();
		    openFileWithData( fm.openFile() );
		}
	    });
	fileMenu.add( fileMenu_Open );
	JMenuItem fileMenu_Save = new JMenuItem("Save");
	fileMenu_Save.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    FileManager fm = new FileManager();
		    fm.saveFile(cells);
		}
	    });
	fileMenu.add( fileMenu_Save );
	mb.add(fileMenu);

	// creates data analysis menu
	JMenu dataMenu = new JMenu("Data");
	JMenuItem dataMenu_Graph = new JMenuItem("Graph");
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
    public void initializeSpreadsheet()
    {
	ss = new JPanel( new GridLayout(0,COLS,BORDER_GAP,BORDER_GAP));

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
	    // TODO: remove tab original function
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
			    if ( !cell.isEditable() ) {
				cell.clear();
				cell.makeEditable();
			    }
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

	pane.add(ss);
    }

    /**
     * Initializes functions ArrayList, adds suggestion boxes to each textfield.
     */
    public void initializeFunctions()
    {

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
	selected.unselect();
	for (Cell h : highlighted) h.dehighlight();
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

	for ( Cell h : highlighted ) h.dehighlight();
	highlighted.clear();
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
	nextButton.setActionCommand("nextGraphCard");
	nextButton.addActionListener(this);
	nextPanel.add(nextButton);

	cancelButton = new JButton("Cancel");
	cancelButton.setActionCommand("closeGraphFrame");
	cancelButton.addActionListener(this);
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
	
	graphPane = graphFrame.getContentPane();
	graphPane.add(cards, BorderLayout.CENTER);
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

	/* creates bin range
	JPanel bin = new JPanel();
	bin.setLayout(new FlowLayout( FlowLayout.LEADING )); // left-aligned
	bin.add(new JLabel("bin range (optional):"));
	JTextField binRange = new JTextField(10);
	bin.add(binRange);
	*/
	
	// creates output range
	JPanel output = new JPanel();
	output.setLayout(new FlowLayout( FlowLayout.LEADING ));
	output.add(new JLabel("output:"));
	JTextField outputRange = new JTextField(10);
	output.add(outputRange);

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
		    if ( !inputRange.getText().matches("\\w\\d{0,2}:\\w\\d{0,2}") ) {
			JOptionPane.showMessageDialog( null, "Input must match the pattern \"\\w\\d{0,2}:\\w\\d{0,2}.\"", "Input Pattern Error", JOptionPane.ERROR_MESSAGE );
			return;
		    }
			
		    highlightInputRange( inputRange.getText() );
		    int inSeparator = inputRange.getText().indexOf(":");
		    int outSeparator = inputRange.getText().indexOf(":");
		    switch ( s.charAt(0) ) {
		    case 'L':
			// checks if the input is one column or one row
			if ( inputRange.getText().charAt(0) == inputRange.getText().charAt( inSeparator+1 ) || inputRange.getText().charAt(1) == inputRange.getText().charAt( inSeparator+2 )) {
			    makeLineGraph();
			} else {
			    // ERROR: line graph input
			    JOptionPane.showMessageDialog( null, "Line graphs only take one column or one row of inputs.", "Graph Input Error", JOptionPane.ERROR_MESSAGE );
			}
			break;
		    case 'H':
			// checks if the input is two columns
			if ( Math.abs( inputRange.getText().charAt(0) - inputRange.getText().charAt( inSeparator+1 )) == 1 )
			{
			    if ( !inputRange.getText().matches("\\w\\d{0,2}:\\w\\d{0,2}") ) {
				JOptionPane.showMessageDialog( null, "Output must match the pattern \"\\w\\d{0,2}:\\w\\d{0,2}.\"", "Output Pattern Error", JOptionPane.ERROR_MESSAGE );
			    } else {
				makeHistogram( toCellNum(outputRange.getText().substring( 0,outputRange.getText().indexOf(":") )));
			    }
			} else {
			    // ERROR: histogramInput
			    JOptionPane.showMessageDialog( null, "Histograms take two columns of input; the first is the data; the second is the bin.", "Graph Input Error", JOptionPane.ERROR_MESSAGE );
			}
			break;
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
	case 'L': break; // line graph
	case 'B': p.add(sortOptions); break; // bar graph
	case 'S': break; // scatter graph
	case 'P': p.add(sortOptions); break; // pie graph
	case 'H': p.add(output); p.add(chart); p.add(sortOptions); break; // histogram
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
	highlightCells( toCellNum( bounds[0]),toCellNum( bounds[1]));
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
	List<Integer> data = new ArrayList<Integer>();
	for (Cell c : highlighted) data.add(c.getIntValue());

	LineGraph l = new LineGraph(data);
	JFrame lineGraph = new JFrame(LINE_GRAPH);

	lineGraph.getContentPane().add(l);
	lineGraph.pack();
	lineGraph.setVisible(true);
    }

    /**
     * Makes a histogram with a 2-colum input.
     * Written to a cell area are: bin range, count, cum. count, percent, and cum. percent.
     */
    public void makeHistogram(int start)
    {
	List<Double> data = new ArrayList<Double>(); // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
	List<Double> binRange = new ArrayList<Double>(); // 2, 5, 8, 10
	List<Integer> bin = new ArrayList<Integer>();
	for (int i = 0; i < highlighted.size(); i++) {
	    if ( highlighted.get(i).isEmpty() ) {
		
	    } else if ( i % 2 == 0 ) {
		data.add( highlighted.get(i).getDoubleValue() );
	    } else {
		binRange.add( highlighted.get(i).getDoubleValue() );
	    }
	}

	Collections.sort(data);
	Collections.sort(binRange);

	int b = 0; // index to loop through bin
	int c = 0; // stores count for each bin range
	for (int d = 0; d < data.size(); d++)
	{
	    if ( b >= binRange.size() ) {
		bin.add( data.size() - d + 1 ); // adds the count of remaining data values
		break;
	    }
	    
	    if ( data.get(d) <= binRange.get(b) ) {
		c++;
	    } else {
		b++; // move on to next bin		
		bin.add(c);
		c = 0; // reset count for next bin
		d--; // value data.get(d) stays the same for next loop
	    }

	    if ( d == data.size()-1 ) {
		bin.add(c);
	    }	    
	}
       	
	b = 0;
	int cumCount = 0;
	double sum = 0.0;
	for (int n : bin) sum += (double) n;
	

	decorateTable(start);

	int end = start + bin.size() * COLS + HISTOGRAM_TABLE_WIDTH + 1;
	for (int i = start + COLS; i < end; i++) {
	    switch ( i % COLS ) {
	    case 1: // bin range
		if ( b == 0 ) cells.get(i).setValue( "up to " + binRange.get(b) );
		else cells.get(i).setValue( binRange.get(b-1) + " to " + binRange.get(b) );
		break;
	    case 2: // count
		cells.get(i).setValue( bin.get(b) );
		cumCount += bin.get(b);
		break;
	    case 3: // cum. count
		cells.get(i).setValue( cumCount );
		break;
	    case 4: // percent
		double percent = bin.get(b) / sum;
		cells.get(i).setValue( String.format( "%.5g%n",percent )); // rounds to 5 decimal places
		break;
	    case 5: // cum. percent
		double cumPercent = cumCount / sum;
		cells.get(i).setValue( String.format( "%.5g%n",cumPercent ));
		break;
	    case 6: // next line
		i += COLS - 6;
		b++;
		break;
	    default:
		System.out.println("line 571");
		break;
	    }	    
	}
    }

    /**
     * Returns cell index.
     *
     * @param s cell location in the form \\w\\d.
     */
    private int toCellNum(String s) {
	try {
	    return Integer.parseInt( s.substring( 1,s.length() )) * COLS + s.charAt(0) - 'A' + 1;
	} catch (NumberFormatException e) {
	    return -1;
	}
    }
    
    /**
     * Adds borders and histogram table headings.
     */
    private void decorateTable(int start) {
	for (int i = start; i < start + HISTOGRAM_TABLE_WIDTH; i++) {
	    switch ( i % COLS ) {
	    case 1: cells.get(i).setValue("range"); break;
	    case 2: cells.get(i).setValue("count"); break;
	    case 3: cells.get(i).setValue("cum. count"); break;
	    case 4: cells.get(i).setValue("percent"); break;
	    case 5: cells.get(i).setValue("cum. percent"); break;
	    }
	    cells.get(i).decorate("tableHead");
	}
    }

    public void actionPerformed(ActionEvent e) {
	String s = e.getActionCommand();
	if ( s.equals("closeGraphFrame") ) {
	    graphFrame.dispose();
	} else if ( s.equals("nextGraphCard") ) {
	    cl = (CardLayout) ( cards.getLayout() );
	    for ( JRadioButton rb : radioButtons ) {
		if ( rb.isSelected() ) {
		    cl.show( cards,graphLabels[ Integer.parseInt( rb.getActionCommand() )]);
		}
	    }
	}
    }
	
    public static void main(String[] args)
    {
	Squirrel s = new Squirrel();
	s.setVisible(true);
    }
    
}
