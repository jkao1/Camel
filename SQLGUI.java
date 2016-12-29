import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SQLGUI extends JFrame implements ActionListener{
    private Container pane;
    private JLabel j;
    private JTextField t;
    
    public SQLGUI() {
	this.setTitle("Squirrel Database Browser");
	this.setSize(600,400);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	pane = this.getContentPane();
	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
	JButton b = new JButton("New Record");
	b.addActionListener(this);
	b.setActionCommand("New");
	JButton b2 = new JButton("Delete Record");
	b2.addActionListener(this);
	b2.setActionCommand("Del");
	JComboBox databaseChoose = new JComboBox();
	databaseChoose.setSelectedIndex(0);
	databaseChoose.addActionListener(this);
	pane.add(c);
	pane.add(t);
	pane.add(b);
	pane.add(b2);
	pane.add(j);
	pane.add(databaseChoose);
    }
    
    public void actionPerformed(ActionEvent e){
	String event = e.getActionCommand();
	if(event.equals("Byte")){
	    String s = t.getText();
	    s += "-0101000";
	    j.setText(s);
	}
	if(event.equals("NotByte")){
	    j.setText("Fish");
	}
    }

    public static void main(String[] args){
	SQLGUI g = new SQLGUI();
	g.setVisible(true);
    }
}
