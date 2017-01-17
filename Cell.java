import javax.swing.*;
import java.awt.*;

public class Cell implements Comparable<Cell> { 

    public static final int PREFERRED_HEIGHT = 26;
    public static final int PREFERRED_WIDTH = 70;
    public static final Color LABEL_COLOR = Color.LIGHT_GRAY;
    private final Font bold;
    private final Font reg;

    private JTextField textField;
 
    private int cellNum;
    private boolean isLabel;
    private boolean isEditable;
    private boolean hasError;
    
    public Cell(JTextField t, int i) { 
	textField = t;
	cellNum = i;
	isLabel = true;
	isEditable = false;
	bold = new Font(textField.getFont().getName(), Font.BOLD, textField.getFont().getSize());
	reg = new Font(textField.getFont().getName(), Font.PLAIN, textField.getFont().getSize());
	
	textField.setEditable(false);
	textField.setMinimumSize(new Dimension( PREFERRED_WIDTH, PREFERRED_HEIGHT ));
	textField.setPreferredSize(new Dimension( PREFERRED_WIDTH, PREFERRED_HEIGHT ));
	textField.setMaximumSize(new Dimension( PREFERRED_WIDTH, PREFERRED_HEIGHT ));
	
	if (i / Squirrel.COLS == 0 && i % Squirrel.COLS == 0) {}
	else if (i / Squirrel.COLS == 0) setValue(String.valueOf((char) ('A'+i-1)));
	else if (i % Squirrel.COLS == 0) setValue(i / Squirrel.COLS);
        else isLabel = false;
	
	if (isLabel) {
	    textField.setFont(bold);
	    textField.setBackground(LABEL_COLOR);
	}
	dehighlight(); // uniform coloration across OS's
    }

    public void select() {
	textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	textField.requestFocus();
    }

    public void unselect() {        
	textField.setBorder(UIManager.getBorder("TextField.border"));
	textField.setEditable(false);
	textField.getCaret().setVisible(false);
	isEditable = false;
    }

    public Cell highlight() {
	if (isLabel()) {
	    textField.setBackground(Color.GRAY);
	} else {
	    textField.setBackground(new Color(178,215,254));
	}
	return this;
    }

    public void dehighlight() {
	if (isLabel) {
	    textField.setBackground(LABEL_COLOR);
	} else {
	    textField.setBackground(Color.WHITE);
	}
    }

    public void decorate(String s) {
	if ( s.equals("tableHead")) {
	    textField.setFont( bold );
	}
    }

    public void makeEditable() {
	isEditable = true;
	textField.setEditable(true);
	textField.getCaret().setVisible(true);
    }

    public void clear() {
	if (!isLabel()) {
	    textField.setText("");
	    textField.setFont(reg);
	    dehighlight();
	    clearError();
	}
    }

    public boolean isEmpty() {
	return textField.getText().equals("");
    }

    public boolean isLabel() {
	return isLabel;
    }

    public boolean isEditable() {
	return isEditable;
    }

    public int getCellNum() {
	return cellNum;
    }

    public JTextField getTextField() { // why -JK
	return textField;
    }
    
    public int getIntValue() {
	try {
	    String s = textField.getText();
	    if (s.indexOf(".") > 0) {
		s = s.substring( 0, s.indexOf("."));
	    }
	    return Integer.parseInt(textField.getText());
	} catch (NumberFormatException e) { return 0; }
    }

    public double getDoubleValue() {
	try {
	    String s = textField.getText();
	    return Double.parseDouble(textField.getText());
	} catch (NumberFormatException e) { return 0.0; }
    }

    public String getValue() {
	return textField.getText();
    }

    public double getPreferredHeight() {
	return textField.getPreferredSize().getHeight();
    }

    public double getPreferredWidth() {
	return textField.getPreferredSize().getWidth();
    }

    public void setDefault() {
	if (getCellNum() / Squirrel.COLS == 0 && getCellNum() % Squirrel.COLS == 0) {
	    
	} else if (getCellNum() / Squirrel.COLS == 0) {
	    setValue(String.valueOf((char) ('A'+getCellNum()-1)));
	} else if (getCellNum() % Squirrel.COLS == 0) {
	    setValue(getCellNum() / Squirrel.COLS);
	} else {
	    setValue("");
	}
    }
    
    public void setValue(int v) {
	textField.setText(String.valueOf(v));
    }

    public void setValue(double v) {
	textField.setText(String.valueOf(v));
    }

    public void setValue(String v) {
	textField.setText(v);
    }

    public void setError(int i) {
	hasError = true;
        textField.setText("!#ERROR");
	switch (i) {
	case 0: textField.setToolTipText("available functions: SUM, MEAN"); break;
	case 1: textField.setToolTipText("input must match \"[\\(\\s]*(\\w\\d{0,2}:\\w\\d{0,2}).*\"."); break;
	default: System.out.println("uncaught error in Cell.setError"); break;
	}
    }

    public void clearError() {
	hasError = false;
	textField.setToolTipText(null);
    }

    public double getX() {
	return textField.getLocationOnScreen().getX();
    }

    public double getY() {
	return textField.getLocationOnScreen().getY();
    }

    public String toString() {
	return "" + (char) ('A' + cellNum % Squirrel.COLS - 1) + (cellNum / Squirrel.COLS);	
    }
    
    public int compareTo(Cell c) {
	return Integer.compare(getIntValue(), c.getIntValue());
    }
    
}
