public class Cell {

    private int _value;
    public boolean isSelected;

    public Cell() {	
	_value = 0;
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
