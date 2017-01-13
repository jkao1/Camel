import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;

public class GraphInput extends JFrame {

    private static final String GREET = "Greet";
    private static final String LINE_GRAPH = "Line Graph";
    private static final String BAR_GRAPH = "Bar Graph";
    private static final String SCATTER_GRAPH = "Scatter Graph";
    private static final String PIE_GRAPH = "Pie Graph";
    private static final String HISTOGRAM = "Histogram";
    private static final String[] graphLabels = { LINE_GRAPH, BAR_GRAPH, SCATTER_GRAPH, PIE_GRAPH, HISTOGRAM };

    private JFrame frame;
    private Container pane; // gi: graph input
    private CardLayout cl;
    private JPanel cards, nextPanel, greetPanel;
    private JPanel[] graphPanels;

    private JRadioButton[] radioButtons;
    private ButtonGroup group;
    private ActionListener exitSystem, get;
    private JButton nextButton, cancelButton;    

    private ArrayList<Cell> highlighted;

    public GraphInput(ArrayList<Cell> highlighted)
    {
	frame = new JFrame("Graph Input");
	frame.setLayout(new FlowLayout());

	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setSize(400,250);
	this.setLocation(300,300);

	this.highlighted = highlighted;

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
	}

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
		    System.exit(0);
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
	
	pane = this.getContentPane();
	pane.add(cards, BorderLayout.CENTER);
    }

    // cannot add same component to multiple cards, so declares new components for each panel
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
	ok.addActionListener(get);
	defaultButtons.add(ok);
	JButton cancel = new JButton("Cancel");
	cancel.addActionListener(exitSystem);
	defaultButtons.add(cancel);

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

    public static void main(String[]args){
	GraphInput g = new GraphInput(new ArrayList<Cell>());
	g.setVisible(true);
    }
}
