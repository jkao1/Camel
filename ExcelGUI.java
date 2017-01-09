import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ExcelGUI extends JFrame implements ActionListener{
    private JFrame frame = new JFrame("Squirrel Database Broswer");
    private Container pane;
    
    public ExcelGUI(){
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new BoxLayout(frame,BoxLayout.Y_AXIS));
	this.setSize(720,360);
	pane = this.getContentPane();
	JButton b = new JButton("Find");
	b.addActionListener(this);
	b.setActionCommand("Find");
	JButton b2 = new JButton("Create");
	b2.addActionListener(this);
	b2.setActionCommand("Create");
	JMenuBar mb = new JMenuBar();
	JMenu menu1 = new JMenu("File");
	mb.add(menu1);
	JMenuItem menuitem = new JMenuItem("New");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Open");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Save");
	menu1.add(menuitem);
	menu1 = new JMenu("Edit");
	mb.add(menu1);
	menuitem = new JMenuItem("Copy");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Paste");
	menu1.add(menuitem);
	menu1 = new JMenu("Insert");
	mb.add(menu1);
	menuitem = new JMenuItem("Table");
	menu1.add(menuitem);
       	menu1 = new JMenu("Data");
	mb.add(menu1);
	menuitem = new JMenuItem("Sort");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Filter");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Flash Fill");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Remove Duplicates");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Data Validation");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Data Forecasting");
	menu1.add(menuitem);
	menu1.addSeparator();
	menuitem = new JMenuItem("Graph");
	menuitem.addActionListener(this);
	menuitem.setActionCommand("graph");
	menu1.add(menuitem);
	this.setJMenuBar(mb);
	pane.add(b);
	pane.add(b2);
    }

    
    
    public void actionPerformed(ActionEvent e){
	    String event = e.getActionCommand();
	    if(event.equals("graph")){
		GraphInput();
	    }	
    }

    public static void main(String[]args){
	ExcelGUI g = new ExcelGUI();
	g.setDefaultLookAndFeelDecorated(true);
	g.setVisible(true);
    }
}
	
