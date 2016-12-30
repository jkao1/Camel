public class Cell {

    private int _value;
    private boolean isSelected;

    public Cell() {	
    }

    public Cell(int v) {
	_value = v;
    }

    public int getValue() {
	return _value;
    }
    
    public void setValue(int v) {
	_value = v;
    }
           
}
