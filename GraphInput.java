import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphInput extends JFrame implements ItemListener {

    private JFrame graphinput = new JFrame("Graph Input");
    private Container pane;
    private JPanel cards;

    final static String LINEPANEL = "Line Graph";
    final static String BARPANEL = "Bar Graph";
    final static String SCATTERPANEL = "Scatter Plot";
    final static String PIEPANEL = "Pie Graph";
    final static String HISTOPANEL = "Histogram";
    final String[] sortOptions = {"No Sort","Ascending","Descending"};
    public int rows;
    public int cols;
    public int[][] histoTable = new int[rows][cols];
    
    public GraphInput(){
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setSize(600,300);
	this.setLocation(300,300);
	graphinput.setLayout(new FlowLayout());
	JPanel comboBoxPane = new JPanel();
	String[] graphs = {LINEPANEL, BARPANEL, SCATTERPANEL, PIEPANEL, HISTOPANEL};
	JComboBox comboBox = new JComboBox(graphs);
	comboBox.setEditable(false);
	comboBox.addItemListener(this);
	comboBoxPane.add(comboBox);

	JPanel lineCard = new JPanel();
	lineCard.setLayout(new BoxLayout(lineCard,BoxLayout.Y_AXIS));
	lineCard.add(new JLabel("Input:"));
	JTextField tf = new JTextField("INPUT RANGE", 5);
	lineCard.add(tf);
	JCheckBox cb = new JCheckBox("Labels");
	lineCard.add(cb);
	cb.addItemListener(this);
	JPanel lineButtons = new JPanel();
	lineButtons.add(new JButton("Ok"));
	lineButtons.add(new JButton("Cancel"));
	lineCard.add(lineButtons);

	JPanel card1 = new JPanel();
	card1.setLayout(new BoxLayout(card1,BoxLayout.Y_AXIS));
	card1.add(new JLabel("Input:"));
	card1.add(new JTextField("INPUT RANGE", 5));
	card1.add(new JCheckBox("Labels"));
	JPanel c1buttons = new JPanel();
	c1buttons.add(new JButton("Ok"));
	c1buttons.add(new JButton("Cancel"));
	card1.add(c1buttons);

	JPanel barCard = new JPanel();
	barCard.setLayout(new BoxLayout(barCard,BoxLayout.Y_AXIS));
	barCard.add(new JLabel("Input:"));
	barCard.add(new JTextField("INPUT RANGE", 5));
	barCard.add(new JCheckBox("Labels"));
	barCard.add(new JLabel("Output"));
	JComboBox cb1 = new JComboBox(sortOptions);
	cb1.setEditable(false);
	cb1.addItemListener(this);
	barCard.add(cb1);
	JPanel barButtons = new JPanel();
	barButtons.add(new JButton("Ok"));
	barButtons.add(new JButton("Cancel"));
	barCard.add(barButtons);
	
	JPanel scatterCard = new JPanel();
	scatterCard.setLayout(new BoxLayout(scatterCard,BoxLayout.Y_AXIS));
	scatterCard.add(new JLabel("Input:"));
	scatterCard.add(new JTextField("INPUT RANGE",5));
	scatterCard.add(new JTextField("INPUT BIN RANGE",5));
	scatterCard.add(new JCheckBox("Labels"));
	scatterCard.add(new JLabel("Output:"));
	scatterCard.add(new JTextField("OUTPUT RANGE"));
	scatterCard.add(new JCheckBox("Table"));
	JComboBox cb2 = new JComboBox(sortOptions);
	cb2.setEditable(false);
	cb2.addItemListener(this);
	scatterCard.add(cb2);
	JPanel scatterButtons = new JPanel();
	scatterButtons.add(new JButton("Ok"));
	scatterButtons.add(new JButton("Cancel"));
	scatterCard.add(scatterButtons);
	
	JPanel pieCard = new JPanel();
	pieCard.setLayout(new BoxLayout(pieCard,BoxLayout.Y_AXIS));
	pieCard.add(new JLabel("Input:"));
	pieCard.add(new JTextField("INPUT RANGE", 5));
	pieCard.add(new JCheckBox("Labels"));
	JPanel pieButtons = new JPanel();
	pieButtons.add(new JButton("Ok"));
	pieButtons.add(new JButton("Cancel"));
	pieCard.add(pieButtons);
	
	JPanel histoCard = new JPanel();
	histoCard.setLayout(new BoxLayout(histoCard,BoxLayout.Y_AXIS));
	histoCard.add(new JLabel("Input:"));
	histoCard.add(new JTextField("INPUT RANGE", 5));
	histoCard.add(new JTextField("INPUT BIN RANGE",5));
	histoCard.add(new JCheckBox("Labels"));
	histoCard.add(new JLabel("Output:"));
	histoCard.add(new JTextField("OUTPUT RANGE"));
	histoCard.add(new JCheckBox("Chart"));
	JComboBox cb3 = new JComboBox(sortOptions);
	cb3.setEditable(false);
	cb3.addItemListener(this);
	histoCard.add(cb3);		
	JPanel histoButtons = new JPanel();
	histoButtons.add(new JButton("Ok"));
	histoButtons.add(new JButton("Cancel"));
	histoCard.add(histoButtons);
	
	cards = new JPanel(new CardLayout());
	cards.add(lineCard, LINEPANEL);
	cards.add(barCard, BARPANEL);
	cards.add(scatterCard, SCATTERPANEL);
	cards.add(pieCard, PIEPANEL);
	cards.add(histoCard, HISTOPANEL);
	
	pane = this.getContentPane();
	pane.add(comboBoxPane, BorderLayout.PAGE_START);
	pane.add(cards, BorderLayout.CENTER);
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
	
