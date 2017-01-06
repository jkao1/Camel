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
	JMenu menu1 = new JMenu("File"); //
	mb.add(menu1);
	JMenuItem menuitem = new JMenuItem("New");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Open");
	menu1.add(menuitem);
	menuitem = new JMenuItem("Save");
	menu1.add(menuitem);

	JMenu menu2 = new JMenu("Graph");
	menu1.add(menu2);
	JMenuItem submenu = new JMenuItem("Data Plot");
	menu2.add(submenu);
	submenu = new JMenuItem("Histogram");
	menu2.add(submenu);
	submenu = new JMenuItem("Line Graph");
	menu2.add(submenu);
	submenu = new JMenuItem("Bar Graph");
	menu2.add(submenu);
	this.setJMenuBar(mb);
	pane.add(b);
	pane.add(b2);
    }

    public void actionPerformed(ActionEvent e){
	    String event = e.getActionCommand();
    }

    public static void main(String[]args){
	ExcelGUI g = new ExcelGUI();
	g.setDefaultLookAndFeelDecorated(true);
	g.setVisible(true);
    }
}
	
