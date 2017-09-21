/**
 * MuhurthaBean.java
 * Created On 2005, Oct 15, 2005 7:24:41 PM
 * @author E. Rajasekar
 */
package app.astrosoft.beans;

import java.util.Comparator;

import app.astrosoft.consts.AstrosoftTableColumn;
import app.astrosoft.consts.DisplayFormat;
import app.astrosoft.consts.MuhurthaRank;
import app.astrosoft.consts.Nakshathra;
import app.astrosoft.consts.Rasi;


/**
 * Class holds Muhurtha Transit Information
 */
public class MuhurthaBean {

	private Interval period;

	private Interval longitude;

	private MuhurthaRank rank;

	private Nakshathra nak;

	private Rasi rasi;

	public MuhurthaBean(Interval period, Interval longitudes, MuhurthaRank rank) {

		this.period = period;
		this.longitude = longitudes;
		this.rank = rank;

		nak = Nakshathra.ofDeg(longitudes.getStart());

		rasi = Rasi.ofDeg(longitudes.getStart());
	}

	public Interval getPeriod() {
		return period;
	}

	public Interval getLongitude() {
		return longitude;
	}

	public MuhurthaRank getRank() {
		return rank;
	}

	public Nakshathra getNakshathra() {
		return nak;
	}

	public Rasi getRasi() {
		return rasi;
	}

	public String toString() {

		StringBuffer sb = new StringBuffer(period.toString(DisplayFormat.DATE_TIME) + " -> " + longitude.toString(DisplayFormat.DEG));
		sb.append(" -> ");
		sb.append("[ " + rasi + " , ");
		sb.append(nak + " ] ");
		sb.append(rank + "\n");
		return sb.toString();
	}


	public Object getColumnData(AstrosoftTableColumn col) {

		switch (col) {

			case Period:
				return getPeriod().toString(DisplayFormat.DATE);
			case Longitude:
				return getLongitude().toString(DisplayFormat.DEG);
			case Rank:
				return getRank();
			case Nakshathra:
				return getNakshathra();
			case Rasi:
				return getRasi();
			case PeriodPopup:
				return getPeriod().toString(DisplayFormat.DATE_TIME);
		}
		return null;
	}

}
