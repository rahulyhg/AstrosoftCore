/**
 * ChartData.java
 * Created On 2006, Mar 31, 2006 4:59:20 PM
 * @author E. Rajasekar
 */

package app.astrosoft.beans;

import java.util.EnumSet;
import java.util.Set;

import app.astrosoft.consts.Rasi;


public interface ChartData {

	public Set<Rasi> getHouses();

	public String getChartName();
	
	public Rasi getAscendant();
}