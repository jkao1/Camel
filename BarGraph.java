import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BarGraph extends JPanel{
    private static final int MAX_SCORE = 20;
    private static final int PREF_W = 800;
    private static final int PREF_H = 650;
    private static final int BORDER_GAP = 30;
    private static final Color GRAPH_COLOR = Color.black;
    private static final int Y_HATCH_CNT = 10;
    private List<Integer> scores;
    	     
    public BarGraph(List<Integer> scores){
	this.scores = scores;
    }


    public Dimension getPreferredSize(){
	return new Dimension(PREF_W, PREF_H);
    }

    private static void createAndShowGui(){
	List<Integer> scores = new ArrayList<Integer>();
	//insert points into score here
	BarGraph mainPanel = new BarGraph(scores);

	JFrame frame = new JFrame("Bar Graph");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(mainPanel);
	frame.pack();
	frame.setLocationByPlatform(true);
	frame.setVisible(true);
    }
    
    public static void main(String[]args){
	SwingUtilities.invokeLater(new Runnable() {
		public void run(){
		    createAndShowGui();
		}
	    });
	
    }
}
