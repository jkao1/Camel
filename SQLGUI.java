import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SQLGUI extends JFrame implements ActionListener{
    private Container pane;
     private JLabel j;
    private JTextField t;

   public SQLGUI() {
    this.setTitle("My first GUI");
    this.setSize(600,400);
    this.setLocation(100,100);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    pane = this.getContentPane();
       pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
      JButton b = new JButton("ByteMe");
      b.addActionListener(this);
      b.setActionCommand("Byte");
      JButton b2 = new JButton("No...");
      b2.addActionListener(this);
      b2.setActionCommand("NotByte");
     t = new JTextField(10);
     JCheckBox c = new JCheckBox("OverClock");
     j = new JLabel("Meaning of life is... ");
     pane.add(c);
     pane.add(t);
     pane.add(b);
     pane.add(b2);
     pane.add(j);
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
}
