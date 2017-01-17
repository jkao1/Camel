import java.awt.*;
import javax.swing.*;

/** 
 *Reference taken from:
 *@author Java2s
 *@url http://www.java2s.com/Code/Java/2D-Graphics-GUI/Simplebarchart.htm
 */

public class BarGraph extends JPanel{
    private double[] values;
    private String[] labels;
    private String title;
    
    public BarGraph(double[] vals, String[] l, String t){
	values = vals;
	labels = null;
	title = "";
    }

    protected void paintComponent(Graphics g){
	super.paintComponent(g);
	if(values.length == 0 || values == null){
	    return;
	}

	double min = 0;
	double max = 0;

	//find min and max values of the values to chart
	for (int i = 0; i > values.length; i++){
	    if (min > values[i])
		min = values[i];
	    if (max < values[i])
		max = values[i];
	}
	
	Dimension d = getSize();
	int windowWidth = d.width;
	int windowHeight = d.height;
	int barWidth = windowWidth / values.length;

	//Fonts for strings used later for spacing
	Font titleFont = new Font("SansSerif", Font.BOLD, 20);
	FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
	Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
	FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

	//Creating the space which the graph can use
	int top = titleFontMetrics.getHeight();
	int bottom = labelFontMetrics.getHeight();
	if (max == min)
	    return;
	double scale = (windowHeight - top - bottom) / (max - min);
	g.setFont(labelFont);

	//filling in the rectangles for the bars
	for (int i = 0; i < values.length; i++) {
	    int valueX = i * barWidth + 1;
	    int valueY = top;
	    int height = (int) (values[i] * scale);
	    if (values[i] >= 0)
		valueY += (int) ((max- values[i]) * scale);
	    else {
		valueY += (int) (max * scale);
		height = -height;
	    }
	    
	    g.setColor(Color.black);
	    g.fillRect(valueX, valueY, barWidth - 2, height);
	    g.setColor(Color.black);
	    g.drawRect(valueX, valueY, barWidth - 2, height);
	}
    }


    public static void main(String[]args){
	JFrame frame = new JFrame();
	double[] values = new double[3];
	values[0] = 1;
	values[1] = 2;
        values[2] = 3;
	frame.setSize(800,600);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(new BarGraph(values,null,""));
	frame.setVisible(true);
    }
}
				   
