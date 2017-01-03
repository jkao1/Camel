import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ExcelGUI extends JFrame implements ActionListener{
    private JFrame frame = new JFrame("Squirrel Database Broswer");
    private Container pane;
    
    public ExcelGUI(){
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new BoxLayout(frame,BoxLayout.Y_AXIS));
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
	frame.setJMenuBar(mb);
	pane.add(b);
	pane.add(b2);
    }

    public void actionPerformed(ActionEvent e){
	String event = e.getActionCommand();
    }

    public static void main(String[]args){
	ExcelGUI g = new ExcelGUI();
	g.pack();
	g.setDefaultLookAndFeelDecorated(true);
	g.setVisible(true);
    }
}
	
