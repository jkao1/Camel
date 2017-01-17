import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import javax.swing.*;
import java.awt.*;

/**
 *@author Hovercraft Full Of Eels
 *@url http://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-i *n-java
 */

public class LineGraph extends JPanel {
    
    private static final int MAX_SCORE = 20;
    private static final int GRAPH_W = 800;
    private static final int GRAPH_H = 650;
    private static final int BORDER_GAP = 30;
    private static final Color GRAPH_COLOR = Color.black;
    private static final Color GRAPH_POINT_COLOR = Color.red;
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    private static final int GRAPH_POINT_WIDTH = 10;
    private static int Y_HATCH_CNT;
    private List<Integer> scores;

    //initializes LineGraph with the set of points
    public LineGraph(List<Integer> scores) {
	this.scores = scores;
    }

    //paints the axes and points on the graphics object
    protected void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D)g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
	double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scores.size() - 1);
	double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
	
	List<Point> graphPoints = new ArrayList<Point>();
	for (int i = 0; i < scores.size(); i++) {
	    int x1 = (int)(i * xScale + BORDER_GAP);
	    int y1 = (int)((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
	    graphPoints.add(new Point(x1, y1));
	}
	
	// create x and y axes 
	g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
	g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);
	
	// create hatch marks for y axis. 
	Y_HATCH_CNT = Collections.max(scores, null);
	for (int i = 0; i < Y_HATCH_CNT; i++) {
	    int x0 = BORDER_GAP;
	    int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
	    int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
	    int y1 = y0;
	    g2.drawLine(x0, y0, x1, y1);
	}
	
	// and for x axis
	for (int i = 0; i < scores.size() - 1; i++) {
	    int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
	    int x1 = x0;
	    int y0 = getHeight() - BORDER_GAP;
	    int y1 = y0 - GRAPH_POINT_WIDTH;
	    g2.drawLine(x0, y0, x1, y1);
	}
	
	Stroke oldStroke = g2.getStroke();
	g2.setColor(GRAPH_COLOR);
	g2.setStroke(GRAPH_STROKE);
	for (int i = 0; i < graphPoints.size() - 1; i++) {
	    int x1 = graphPoints.get(i).x;
	    int y1 = graphPoints.get(i).y;
	    int x2 = graphPoints.get(i + 1).x;
	    int y2 = graphPoints.get(i + 1).y;
	    g2.drawLine(x1, y1, x2, y2);         
	}
	
	g2.setStroke(oldStroke);      
	g2.setColor(GRAPH_POINT_COLOR);
	for (int i = 0; i < graphPoints.size(); i++) {
	    int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
	    int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
	    int ovalW = GRAPH_POINT_WIDTH;
	    int ovalH = GRAPH_POINT_WIDTH;
	    g2.fillOval(x, y, ovalW, ovalH);
	}
    }

    //Creates the set dimensions for the graph
    public Dimension getPreferredSize() {
	return new Dimension(GRAPH_W, GRAPH_H);
    }

     private static void createAndShowGui(){
	List<Integer> scores = new ArrayList<Integer>();
	//insert points into score here
	LineGraph mainPanel = new LineGraph(scores);

	JFrame frame = new JFrame("Line Graph");
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
