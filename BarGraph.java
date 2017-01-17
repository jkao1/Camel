import java.awt.*;
import javax.swing.*;

public class BarGraph extends JPanel{
    private double[] values;

    public BarGraph(double[] vals){
	values = vals;
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

	Dimension window = getSize();
	int windowWidth = window.width;
	int windowHeight = window.height;
	int barsWidth = windowWidth/values.length;

	for(int i = 0; i<values.length; i++){
	    int Xcoord = i * barsWidth + 1;
	    int Ycoord = windowHeight;
	    int barHeight = (int)values[i];
		if(values[i] >= 0){
		    Ycoord += (int)(max - values[i]);
		}
		else{
		    Ycoord += max;
		    barHeight = -barHeight;
		}
	

	g.setColor(Color.black);
	g.fillRect(Xcoord, Ycoord, barsWidth - 2, barHeight);
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
	frame.getContentPane().add(new BarGraph(values));
	frame.setVisible(true);
    }
}
				   
