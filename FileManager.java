import java.util.*;
import java.io.*;
import javax.swing.*;

public class FileManager {

    private static final String SEPARATOR = "\n"; 
    private static final int ROWS = Squirrel.ROWS;
    private static final int COLS = Squirrel.COLS;

    private JFileChooser fc;
    private int returnValue;
    private int result;
    private File selectedFile;
    private FileWriter fw;
    private Scanner in;

    private ArrayList<String> out;

    public FileManager() {
	fc = new JFileChooser();
	fc.setCurrentDirectory( fc.getCurrentDirectory() );
    }

    public void saveFile( ArrayList<Cell> c ) {
	result = fc.showSaveDialog(null);
	if ( result == JFileChooser.APPROVE_OPTION ) {
	    if ( fc.getSelectedFile().getName().indexOf(".txt") == -1 ) {
		// ERROR: file extension
		JOptionPane.showMessageDialog( null, "File must end with \".txt\".", "FileExtensionException", JOptionPane.ERROR_MESSAGE );
	    } else {
		try {
		    fw = new FileWriter( fc.getSelectedFile() );			
		    for ( int i = COLS + 1; i < lastCell(c); i++ ) {
			if ( ! (i % COLS == 0 || c.get(i).isEmpty() )) {
			    fw.write( i + ":" + c.get(i).getValue() );
			    fw.write( SEPARATOR );
			}
		    }
		    fw.close();
		} catch (IOException e) {
		    // ERROR: io exception
		    JOptionPane.showMessageDialog( null, "IO Exception", "IOException", JOptionPane.ERROR_MESSAGE );
		}
	    }
	}
    }

    public ArrayList<String> openFile() {
	result = fc.showOpenDialog(null);
	out = new ArrayList<String>();
	if (result == JFileChooser.APPROVE_OPTION ) {
	    if ( fc.getSelectedFile().getName().indexOf(".txt") == -1 ) {
		// ERROR: file extension
	    } else {
		try {		    
		    in = new Scanner( fc.getSelectedFile() );
		    while (in.hasNext()) {
			out.add(in.next());
		    }
		    in.close();
		} catch (IOException e) {
		    // ERROR: io exception
		    JOptionPane.showMessageDialog( null, "IO Exception", "IOException", JOptionPane.ERROR_MESSAGE );				    
		}
	    }
	}
	return out;
    }
		
    private int lastCell( ArrayList<Cell> c ) {
	for ( int i = c.size() - 1; i > 0; i-- ) {
	    if (!c.isEmpty()) return i;
	}
	return 0;
    }

}
