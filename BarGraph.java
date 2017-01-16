import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import javax.swing.*;

import java.awt.*;

public class BarGraph extends JPanel{
    
    private static final int MAX_SCORE = 20;
    private static final int PREF_W = 800;
    private static final int PREF_H = 650;
    private static final int BORDER_GAP = 30;
    private static final Color GRAPH_COLOR = Color.red;
    private static final int GRAPH_BAR_WIDTH = 30;
    private static int Y_HATCH_CNT = 10;
    private List<Integer> scores;
    	     
    public BarGraph(List<Integer> scores) {
	this.scores = scores;
    }

    protected void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	double xScale = ((double) getWidth() - 2*BORDER_GAP) / (scores.size() - 1);
	double yScale = ((double)getWidth() - 2*BORDER_GAP) / (scores.size() - 1);

	List<Point> graphPoints = new ArrayList<Point>();
	for(int i = 0; i < scores.size(); i++){
	    int x1 = (int) (i * xScale + BORDER_GAP);
	    int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
	    graphPoints.add(new Point(x1, y1));
	}

	//x and y axes
	g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
	g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

	//y axis value hatches
	Y_HATCH_CNT = Collections.max(scores, null);
	for(int i = 0; i < Y_HATCH_CNT; i++){
	    int x0 = BORDER_GAP;
	    int x1 = GRAPH_BAR_WIDTH + BORDER_GAP;
	    int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
	    int y1 = y0;
	    g2.drawLine(x0, y0, x1, y1);
	}

	//x axis value hatches
	for (int i = 0; i < scores.size() - 1; i++) {
	    int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
	    int x1 = x0;
	    int y0 = getHeight() - BORDER_GAP;
	    int y1 = y0 - GRAPH_BAR_WIDTH;
	    g2.drawLine(x0, y0, x1, y1);
	}

	for(int i = 0;i < graphPoints.size(); i++){
	    g2.drawRect((i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP, getHeight() - BORDER_GAP, 30, (int)graphPoints.get(i).getY()); 
	}
    }

    public Dimension getPreferredSize() {
	return new Dimension(PREF_W, PREF_H);
    }

    private static void createAndShowGui(){
	List<Integer> scores = new ArrayList<Integer>();
	//insert points into score here
	scores.add(5);
	scores.add(17);
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
