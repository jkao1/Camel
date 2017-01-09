import java.awt.*;
import javax.swing.*;

public class GBL extends JFrame {

    JFrame frame;
    Container pane;
    GridBagConstraints c;

    public GBL()
    {
	frame = new JFrame("GBL");
	this.setSize(100,100);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	pane = this.getContentPane();
	pane.setLayout(new GridBagLayout());
	c = new GridBagConstraints();

	JTextField button = new JTextField("Button 1");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(button, c);

	button = new JTextField("Button 2");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 0;
	pane.add(button, c);

	button = new JTextField("Button 3");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 2;
	c.gridy = 0;
	pane.add(button, c);
 
	button = new JTextField("Long-Named Button 4");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 40;      //make this component tall
	c.weightx = 0.0;
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(button, c);
    }

    public static void main(String[] args) {
	GBL g = new GBL();
	g.pack();
	g.setVisible(true);
    }

}
    
