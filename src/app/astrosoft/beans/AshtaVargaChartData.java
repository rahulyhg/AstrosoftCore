/**
 * AshtavargaChartData.java
 * Created On 2006, Mar 31, 2006 5:12:23 PM
 * @author E. Rajasekar
 */

package app.astrosoft.beans;

import java.util.EnumMap;

import app.astrosoft.consts.AshtavargaName;
import app.astrosoft.consts.AstrosoftTableColumn;
import app.astrosoft.consts.Rasi;
import app.astrosoft.core.Ashtavarga;

public class AshtaVargaChartData extends AbstractChartData{

	private EnumMap<Rasi, Integer> varga;
	
	public AshtaVargaChartData(AshtavargaName name, EnumMap<Rasi, Integer> varga) {
		super();
		
		this.varga = varga;
		chartName = name.toString();
		
		int count = Ashtavarga.getCount(name);
		if ( count != -1){
			chartName = chartName + " ( " +String.valueOf(count) + " ) ";
		}
	}
	

	
	public EnumMap<Rasi, Integer> getVarga() {
		return varga;
	}


	
	
}
