/**
 * MuhurthaTest.java
 Created On 2007, Nov 6, 2007 4:51:24 PM
 @author E. Rajasekar
 */

package app.astrosoft.test;

import java.util.GregorianCalendar;

import app.astrosoft.beans.Interval;
import app.astrosoft.beans.MuhurthaBean;
import app.astrosoft.consts.Nakshathra;
import app.astrosoft.consts.Rasi;
import app.astrosoft.core.Muhurtha;


public class MuhurthaTest {

	public static void main(String[] args) {
		
		Muhurtha m = new Muhurtha(new GregorianCalendar(2005, 11,01), Rasi.Mesha, Nakshathra.Bharani, true, true);
		m.calcMuhurtha();

		m.printFavLongitudes();
	}
}

