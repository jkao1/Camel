import java.util.*;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

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
		// ERROR DISPLAY
	    }
	}
    }

    public ArrayList<String> openFile() {
	result = fc.showOpenDialog(null);
	out = new ArrayList<String>();
	if (result == JFileChooser.APPROVE_OPTION ) {
	    try {		    
	        in = new Scanner( fc.getSelectedFile() );
		while (in.hasNext()) {
		    out.add(in.next());
		}
		in.close();
	    } catch (IOException e) {
		// ERROR DISPLAY
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
