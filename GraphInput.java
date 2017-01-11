import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;

public class GraphInput extends JFrame {

    private JFrame frame;
    private Container pane; // gi: graph input
    private CardLayout cl;
    private JPanel cards, greetPanel;
    private JPanel[] graphPanels;

    private JRadioButton[] radioButtons;
    private ButtonGroup group;
    private ActionListener nextRB;
    private JButton nextButton;    

    private static final String LINE_GRAPH = "Line Graph";
    private static final String BAR_GRAPH = "Bar Graph";
    private static final String SCATTER_GRAPH = "Scatter Graph";
    private static final String PIE_GRAPH = "Pie Graph";
    private static final String HISTOGRAM = "Histogram";
    private static final String[] graphLabels = { LINE_GRAPH, BAR_GRAPH, SCATTER_GRAPH, PIE_GRAPH, HISTOGRAM };

    private ArrayList<Cell> _highlighted;
    private JComboBox<String> graphComboBox;

    public GraphInput(ArrayList<Cell> highlighted)
    {
	frame = new JFrame("Graph Input");
	frame.setLayout(new FlowLayout());

	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setSize(400,250);
	this.setLocation(300,300);

	_highlighted = highlighted;

	cards = new JPanel(new CardLayout());

	greetPanel = new JPanel(new GridLayout(0,1));
	group = new ButtonGroup();

	radioButtons = new JRadioButton[graphLabels.length];
	for (int i = 0; i < radioButtons.length; i++) {
	    JRadioButton rb = new JRadioButton(graphLabels[i]);
	    rb.setActionCommand(graphLabels[i]);
	    radioButtons[i] = rb;
	    group.add(rb);
	    greetPanel.add(rb);
	}
	nextButton = new JButton("Next");
	nextButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {

		    cl = (CardLayout) (cards.getLayout());
		    for (JRadioButton rb : radioButtons) {
			if (rb.isSelected()) {
			    cl.show(cards, e.getActionCommand());
			    System.out.println(rb.getActionCommand());
			}
		    }
		}
	    });
	greetPanel.add(nextButton);
	cards.add(greetPanel, "Greet");

        graphPanels = new JPanel[graphLabels.length];
	for (int i = 0; i < graphPanels.length; i++) {
	    JPanel p = new JPanel();
	    createAndAddDefault(p, graphLabels[i].charAt(0));
	    cards.add(p, graphLabels[i]);
	}
	
	pane = this.getContentPane();
	pane.add(cards, BorderLayout.CENTER);
    }

    // cannot add same component to multiple cards
    public void createAndAddDefault(JPanel p, char c)
    {
	JPanel input = new JPanel();
	input.setLayout(new FlowLayout(FlowLayout.LEADING)); // left-aligned
	input.add(new JLabel("input range:"));
        JTextField inputRange = new JTextField(10);
	input.add(inputRange);

	JPanel bin = new JPanel();
	bin.setLayout(new FlowLayout(FlowLayout.LEADING)); // left-aligned
	bin.add(new JLabel("bin range (optional):"));
        JTextField binRange = new JTextField(10);
	bin.add(binRange);

	JPanel output = new JPanel();
	output.setLayout(new FlowLayout(FlowLayout.LEADING));
	output.add(new JLabel("output:"));
	output.add(new JTextField(10));

	JPanel sort = new JPanel();
	sort.setLayout(new FlowLayout(FlowLayout.LEADING));
	sort.add(new JLabel("sort by:"));
	JComboBox<String> sortOptions = new JComboBox<>(new String[] {"none", "ascending", "descending"});
	sortOptions.setEditable(false);

	JCheckBox chart = new JCheckBox("Chart");

	JPanel defaultButtons = new JPanel();
	JButton ok = new JButton("Ok");
	defaultButtons.add(ok);
	JButton cancel = new JButton("Cancel");
	defaultButtons.add(cancel);

	p.setLayout(new GridLayout(0, 1));
	p.add(input);
	switch (c) {
	case 'L': p.add(bin); break; // line
	case 'B': p.add(sortOptions); break; // bar
	case 'S': break; // scatter
	case 'P': p.add(sortOptions); break; // pie
	case 'H': p.add(bin); p.add(output); p.add(chart); p.add(sortOptions); break; // histogram
	}
	p.add(defaultButtons);
    }

    public static void main(String[]args){
	GraphInput g = new GraphInput(new ArrayList<Cell>());
	g.setVisible(true);
    }
}
