/**
 * Ephemeris.java
 *
 * Created on February 21, 2003, 4:20 PM
 * @author  E. Rajasekar.
 */
package app.astrosoft.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import app.astrosoft.consts.Ayanamsa;
import app.astrosoft.consts.DisplayFormat;
import app.astrosoft.consts.DisplayStrings;
import app.astrosoft.consts.Planet;
import app.astrosoft.consts.Rasi;
import app.astrosoft.pref.AstrosoftPref;
import app.astrosoft.util.AstroUtil;
import app.astrosoft.util.SwissHelper;

import swisseph.*;


public class Ephemeris implements PreferenceChangeListener {
	
	public static enum Mode {
		Daily(Calendar.MONTH, Calendar.DATE), Monthly(Calendar.YEAR, Calendar.MONTH);
		
		private Mode(int parentCalField, int incCalField) {
			this.parentCalField = parentCalField;
			this.incCalField = incCalField;
		}
		private int incCalField;
		private int parentCalField;
		
		public int incCalField() {
			return incCalField;
		}
		
		public int parentCalField() {
			return parentCalField;
		}
		
		public boolean isDaily(){
			return this == Daily;
		}
		
		public boolean isMonthly(){
			return this == Monthly;
		}
	};

	private static AstrosoftPref preferences = AstrosoftPref.get();

	private Calendar cal;
    private List<EnumMap<Planet, EphData>> ephemeris;
    
    private Mode mode;
    private SwissHelper swissHelper;

    /** Month is zero-based  */
    public Ephemeris(int month, int year, Mode mode) {

        this(new GregorianCalendar(year, month, 1), mode);
    }
    
    public Ephemeris(int month, int year) {

        this(year, month, Mode.Daily);
       
    }
    
    public Ephemeris(Calendar cal, Mode mode){
    
    	this.cal = cal;
    	this.mode = mode;
    	cal.set(Calendar.DATE, 1);
    	if (mode.isMonthly()){
    		cal.set(Calendar.MONTH, Calendar.JANUARY);
    	}
    	calcEphemeris();
        //preferences.addPreferenceChangeListener(this);
    }
    
    public void setAyanamsa( Ayanamsa ayanamsa ) {

        swissHelper.setAyanamsa(ayanamsa);
        calcEphemeris(  );
        
    }
    
   private void calcEphemeris(  ) {

	    ephemeris = new ArrayList<EnumMap<Planet, EphData>>();
	    double timeZone = preferences.getPlace().timeZone();
    	double ephTime = preferences.getEphCalcTime() - timeZone;
    	
    	swissHelper = new SwissHelper();
    	
    	Calendar ephCal = AstroUtil.getCalendar();
    	
    	ephCal.setTime(cal.getTime());
    	
    	//System.out.println("cal " + cal.getTime());
    	//System.out.println("eph cal " + ephCal.getTime());
    	//TODO: Initial calculation time is slow, see if we run it in thread
    	
        while(ephCal.get(mode.parentCalField) == cal.get(mode.parentCalField)) {

            SweDate sweDate = new SweDate( ephCal.get(Calendar.YEAR), ephCal.get(Calendar.MONTH) + 1, ephCal.get(Calendar.DAY_OF_MONTH), ephTime );
            swissHelper.setSweDate(sweDate);
            ephemeris.add(swissHelper.getEphData());
            ephCal.add(mode.incCalField, 1);
        }
       
    }
   
    public Mode getMode() {
    	return mode;
    }
    
    public Calendar getDate(){
    	return cal;
    }
    

    public void preferenceChange(PreferenceChangeEvent evt) {

		if(evt.getKey().equals(AstrosoftPref.Preference.Ayanamsa.name())){
			setAyanamsa(Enum.valueOf(Ayanamsa.class, evt.getNewValue()));
		}else if (evt.getKey().equals(AstrosoftPref.Preference.EphCalcTime.name())){
			calcEphemeris();
		}else if (evt.getKey().equals(AstrosoftPref.Preference.Place.name())){
			calcEphemeris();
		}
	}

    public static class EphData {
    	
    	private double position;
    	
    	private Rasi house;
    	
    	private boolean isReverse;
    	
    	public EphData(double position, Boolean isReverse){
    		this.position = position % 30;
    		this.house = Rasi.ofDeg(position);
    		this.isReverse = (isReverse != null) ? isReverse.booleanValue():false;
    	}
    	
    	public double getPosition() {
			return position;
		}
    	
    	public Rasi getHouse() {
			return house;
		}
    	
    	public boolean isReverse() {
			return isReverse;
		}
    	
    	@Override
    	public String toString() {
    		
    		StringBuilder sb = new StringBuilder();
    		sb.append(AstroUtil.todegmin(position, " " + house.toString(DisplayFormat.SYMBOL) + " "));
    		
    		if (isReverse){
    			sb.append(DisplayStrings.RETRO_SYM);
    		}
    		return sb.toString();
    	}
    }
    
    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder();
    	/*for (Planet p : Planet.planetsAsc()) {
    		sb.append(p + "\t");
		}
    	sb.append("\n");*/
    	for(EnumMap<Planet, EphData> planetEphData : ephemeris){
    		for(Planet p : planetEphData.keySet()){
    			sb.append(planetEphData.get(p) + "\t");
    		}
    		sb.append("\n");
    	}
    	return sb.toString();
    }
    
    public static void main( String[] args ) {

        Ephemeris eph = new Ephemeris( 01, 2003, Mode.Monthly);
        System.out.println(eph);
    }

}
