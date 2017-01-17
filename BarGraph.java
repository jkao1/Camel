import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class BarGraph extends JPanel {

    private JFrame frame;
    
    private List<Integer> values;
    private List<String> labels;
    private String title; 

    public BarGraph( List<Integer> v, List<String> l, String t) {	
	values = v;
	labels = l;
	title = t;
    }

    public void paintComponent( Graphics g )
    {
	super.paintComponent(g);

	if (values == null || values.size() == 0) {
	    return;
	}

	double minValue = 0;
	double maxValue = 0;
	for (int i = 0; i < values.size(); i++) {
	    if (minValue > values.get(i)) {
		minValue = values.get(i);
	    }
	    if (maxValue < values.get(i)) {		
		maxValue = values.get(i);
	    }
	}

	Dimension dim = getSize();
	int clientWidth = dim.width;
	int clientHeight = dim.height;
	int barWidth = clientWidth / values.size();

        Font titleFont = new Font("SansSerif", Font.BOLD, 20);
	FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
	Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
	FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

	int titleWidth = titleFontMetrics.stringWidth(title);
	int y = titleFontMetrics.getAscent();
	int x = (clientWidth - titleWidth) / 2;
	g.setFont(titleFont);
	g.drawString(title, x, y);

	int top = titleFontMetrics.getHeight();
	int bottom = labelFontMetrics.getHeight();
	if (maxValue == minValue) {
	    return;
	}
	double scale = (clientHeight - top - bottom) / (maxValue - minValue);
	y = clientHeight - labelFontMetrics.getDescent();
	g.setFont(labelFont);

	for (int i = 0; i < values.size(); i++) {
	    int valueX = i * barWidth + 1;
	    int valueY = top;
	    int height = (int) (values.get(i) * scale);
	    if (values.get(i) >= 0)
		valueY += (int) ((maxValue - values.get(i)) * scale);
	    else {
		valueY += (int) (maxValue * scale);
		height = -height;
	    }

	    g.setColor(Color.red);
	    g.fillRect(valueX, valueY, barWidth - 2, height);
	    g.setColor(Color.black);
	    g.drawRect(valueX, valueY, barWidth - 2, height);
	    int labelWidth = labelFontMetrics.stringWidth(labels.get(i));
	    x = i * barWidth + (barWidth - labelWidth) / 2;
	    g.drawString(labels.get(i) + ": " + values.get(i), x, y);
	}
    }

    
     private static void createAndShowGui(){
	List<Integer> scores = new ArrayList<Integer>();
	List<String> labels = new ArrayList<String>();
	String graph = "Graph";
	//insert points into score here
	Histogram mainPanel = new BarGraph(scores, labels, graph);

	JFrame frame = new JFrame("Line Graph");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(mainPanel);
	frame.pack();
	frame.setLocationByPlatform(true);
	frame.setVisible(true);
    }
}
