/**
 * FileOps.java
 *
 * Created on December 28, 2002, 10:44 AM
 *
 * @author  E. Rajasekar
 */
package app.astrosoft.util;

import java.awt.Color;
import java.awt.Component;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import app.astrosoft.exception.AstrosoftException;
import app.astrosoft.pref.AstrosoftPref;



public class FileOps {
	
	private static final Logger log = Logger.getLogger(FileOps.class.getName());

	public static enum FileDialogMode {OPEN, SAVE};


	//TODO should not read from file
    public static int getFromFile( String filename, int row, int col ) {

        int value = 0;

        try {

            //File f = new File( getResourceURI("/resources/" + filename) );
            //FileInputStream fis = new FileInputStream( f );
            BufferedReader br =
                new BufferedReader( new InputStreamReader( FileOps.class.getResourceAsStream("/resources/" + filename) ) );
            int lineno = 1;

            while ( lineno++ < row )
                br.readLine(  );

            //String []values = br.readLine().split(",");
            String[] values = new String[30];
            java.util.StringTokenizer st =
                new java.util.StringTokenizer( br.readLine(  ), "," );
            int i = 0;

            while ( st.hasMoreTokens(  ) ) {

                values[i++] = st.nextToken(  );

            }

            value = Integer.parseInt( values[col - 1] );
            //fis.close(  );

        } catch ( IOException e ) {

            e.printStackTrace(  );

        } 
        return value;

    }
    


    
}


