import java.swing.*;
import java.awt.*;

public class Spreadsheet extends JFrame {

    private Container s;
    private Cell[] cells;

    public Spreadsheet() {
	this.setTitle("Squirrel");
	this.setSize(1280,960);
	this.setLocation(100,100);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private getSum() {
	int s = 0
	for (Cell c : cells) {
	    if (c.isSelected) {
		s += c.getValue();
	    }
	}
	return s;
    }

}