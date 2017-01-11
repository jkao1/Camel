import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphInput extends JFrame implements ItemListener {

    private JFrame frame;
    private Container pane; // gi: graph input
    private JPanel graphComboBoxPanel, cards;

    private JPanel lineCard, barCard, scatterCard, pieCard, histogramCard;

    private JComboBox<String> graphComboBox;

    public GraphInput()
    {
	frame = new JFrame("Graph Input");
	frame.setLayout(new FlowLayout());
	
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setSize(400,250);
	this.setLocation(300,300);	

        graphComboBoxPanel = new JPanel();
	graphComboBox = new JComboBox<>(new String[] {"Line Graph", "Bar Graph", "Scatter Plot", "Pie Chart", "Histogram"});
	graphComboBox.addItemListener(this);
	graphComboBoxPanel.add(graphComboBox);

	cards = new JPanel(new CardLayout());
	
	lineCard = new JPanel();
	createAndAddDefault(lineCard, 'l');
	cards.add(lineCard, "Line Graph");
	
        barCard = new JPanel();
        createAndAddDefault(barCard, 'b');
	cards.add(barCard, "Bar Graph");
	
        scatterCard = new JPanel();
        createAndAddDefault(scatterCard, 's');
	cards.add(scatterCard, "Scatter Plot");
	
	pieCard = new JPanel();
        createAndAddDefault(pieCard, 'p');
	cards.add(pieCard, "Pie Chart");
	
	histogramCard = new JPanel();
        createAndAddDefault(histogramCard, 'h');
	cards.add(histogramCard, "Histogram");
	
	pane = this.getContentPane();
	pane.add(graphComboBoxPanel, BorderLayout.PAGE_START);
	pane.add(cards, BorderLayout.CENTER);
    }

    // cannot add same component to multiple cards
    public void createAndAddDefault(JPanel p, char c)
    {
	JPanel input = new JPanel();
	input.setLayout(new FlowLayout(FlowLayout.LEADING)); // left-aligned
	input.add(new JLabel("input range:"));
	input.add(new JTextField(10));

	JPanel bin = new JPanel();
	bin.setLayout(new FlowLayout(FlowLayout.LEADING)); // left-aligned
	bin.add(new JLabel("bin range (optional):"));
	bin.add(new JTextField(10));

	JPanel output = new JPanel();
	output.setLayout(new FlowLayout(FlowLayout.LEADING));
	output.add(new JLabel("output:"));
	output.add(new JTextField(10));

	JPanel sort = new JPanel();
	sort.setLayout(new FlowLayout(FlowLayout.LEADING));
	sort.add(new JLabel("sort by:"));
	JComboBox<String> sortOptions = new JComboBox<>(new String[] {"none", "ascending", "descending"});
	sortOptions.setEditable(false);
	sortOptions.addItemListener(this);

	JCheckBox chart = new JCheckBox("Chart");
	
	JPanel defaultButtons = new JPanel();
	JButton ok = new JButton("Ok");
	defaultButtons.add(ok);
	JButton cancel = new JButton("Cancel");
	defaultButtons.add(cancel);

	p.setLayout(new GridLayout(0, 1));
	p.add(input);
	switch (c) {
	case 'l': p.add(bin); break; // line
	case 'b': p.add(sortOptions); break; // bar
	case 's': break; // scatter
	case 'p': p.add(sortOptions); break; // pie
	case 'h': p.add(bin); p.add(output); p.add(chart); p.add(sortOptions); break; // histogram
	}	
	p.add(defaultButtons);
    }

    public void itemStateChanged(ItemEvent e){
	CardLayout c1 = (CardLayout)(cards.getLayout());
	c1.show(cards, (String)e.getItem());
	if(e.getStateChange() == 1){
	}
    }

    public void actionPerformed(ActionEvent e){
	String event = e.getActionCommand();
    }
    
    public static void main(String[]args){
	GraphInput g = new GraphInput();
	g.setVisible(true);
    }
}
	
