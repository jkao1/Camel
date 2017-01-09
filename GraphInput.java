import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphInput extends JFrame implements ItemListener{
    private JFrame graphinput = new JFrame("Graph Input");
    private Container pane;
    private JPanel cards;
    final static String LINEPANEL = "Line Graph";
    final static String BARPANEL = "Bar Graph";
    final static String SCATTERPANEL = "Scatter Plot";
    final static String PIEPANEL = "Pie Graph";
    final static String HISTOPANEL = "Histogram";
    
    public GraphInput(){
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setSize(600,300);
	this.setLocation(300,300);
	graphinput.setLayout(new FlowLayout());
	JPanel comboBoxPane = new JPanel();
	String[] graphs = {LINEPANEL, BARPANEL, SCATTERPANEL, PIEPANEL, HISTOPANEL};
	JComboBox cb = new JComboBox(graphs);
	cb.setEditable(false);
	cb.addItemListener(this);
	comboBoxPane.add(cb);

	JPanel card1 = new JPanel();
	card1.setLayout(new BoxLayout(card1,BoxLayout.Y_AXIS));
	card1.add(new JLabel("Input:"));
	card1.add(new JTextField("INPUT RANGE", 5));
	card1.add(new JCheckBox("Labels"));
	JPanel c1buttons = new JPanel();
	c1buttons.add(new JButton("Ok"));
	c1buttons.add(new JButton("Cancel"));
	card1.add(c1buttons);

	JPanel card2 = new JPanel();
	card2.setLayout(new BoxLayout(card2,BoxLayout.Y_AXIS));
	card2.add(new JLabel("Input:"));
	card2.add(new JTextField("INPUT RANGE", 5));
	card2.add(new JCheckBox("Labels"));
	card2.add(new JLabel("Output"));
	final String[] sortOptions = {"No Sort","Ascending","Descending"};
	JComboBox cb1 = new JComboBox(sortOptions);
	cb1.setEditable(false);
	cb1.addItemListener(this);
	card2.add(cb1);
	JPanel c2buttons = new JPanel();
	c2buttons.add(new JButton("Ok"));
	c2buttons.add(new JButton("Cancel"));
	card2.add(c2buttons);
	
	JPanel card3 = new JPanel();
	card3.setLayout(new BoxLayout(card3,BoxLayout.Y_AXIS));
	card3.add(new JLabel("Input:"));
	card3.add(new JTextField("INPUT RANGE",5));
	card3.add(new JTextField("INPUT BIN RANGE",5));
	card3.add(new JCheckBox("Labels"));
	card3.add(new JLabel("Output:"));
	card3.add(new JTextField("OUTPUT RANGE"));
	card3.add(new JCheckBox("Table"));
	JComboBox cb2 = new JComboBox(sortOptions);
	cb2.setEditable(false);
	cb2.addItemListener(this);
	card3.add(cb2);
	JPanel c3buttons = new JPanel();
	c3buttons.add(new JButton("Ok"));
	c3buttons.add(new JButton("Cancel"));
	card3.add(c3buttons);
	
	JPanel card4 = new JPanel();
	card4.setLayout(new BoxLayout(card4,BoxLayout.Y_AXIS));
	card4.add(new JLabel("Input:"));
	card4.add(new JTextField("INPUT RANGE", 5));
	card4.add(new JCheckBox("Labels"));
	JPanel c4buttons = new JPanel();
	c4buttons.add(new JButton("Ok"));
	c4buttons.add(new JButton("Cancel"));
	card4.add(c4buttons);
	
	JPanel histocard = new JPanel();
	histocard.setLayout(new BoxLayout(histocard,BoxLayout.Y_AXIS));
	histocard.add(new JLabel("Input:"));
	histocard.add(new JTextField("INPUT RANGE", 5));
	histocard.add(new JTextField("INPUT BIN RANGE",5));
	histocard.add(new JCheckBox("Labels"));
	histocard.add(new JLabel("Output:"));
	histocard.add(new JTextField("OUTPUT RANGE"));
	histocard.add(new JCheckBox("Chart"));
	JComboBox cb3 = new JComboBox(sortOptions);
	cb3.setEditable(false);
	cb3.addItemListener(this);
	histocard.add(cb3);		
	JPanel histobuttons = new JPanel();
	histobuttons.add(new JButton("Ok"));
	histobuttons.add(new JButton("Cancel"));
	histocard.add(histobuttons);
	
	cards = new JPanel(new CardLayout());
	cards.add(card1, LINEPANEL);
	cards.add(card2, BARPANEL);
	cards.add(card3, SCATTERPANEL);
	cards.add(card4, PIEPANEL);
	cards.add(histocard, HISTOPANEL);
	
	pane = this.getContentPane();
	pane.add(comboBoxPane, BorderLayout.PAGE_START);
	pane.add(cards, BorderLayout.CENTER);
    }

    public void itemStateChanged(ItemEvent e){
	CardLayout c1 = (CardLayout)(cards.getLayout());
	c1.show(cards, (String)e.getItem());
    }

    public static void main(String[]args){
	GraphInput g = new GraphInput();
	g.setVisible(true);
    }
}
	
