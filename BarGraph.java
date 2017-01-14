import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class BarGraph extends JPanel{
    
    private static final int MAX_SCORE = 20;
    private static final int PREF_W = 800;
    private static final int PREF_H = 650;
    private static final int BORDER_GAP = 30;
    private static final Color GRAPH_COLOR = Color.black;
    private static final int Y_HATCH_CNT = 10;
    private List<Integer> scores;
    	     
    public BarGraph(List<Integer> scores) {
	this.scores = scores;
    }

    protected void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	Graphics2D g2D = (Graphics2D)g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	double xScale = ((double) getWidth() - 2*BORDER_GAP) / (scores.size() - 1);
	double yScale = ((double)getWidth() - 2*BORDER_GAP) / (scores.size() - 1);

	List<Point> graphPoints = new ArrayList<Point>();
	for(int i = 0; i < scores.size(); i++){
	    int x1 = (int) (i * xScale + BORDER_GAP);
	    int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
	}			 
    }

    public Dimension getPreferredSize() {
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
