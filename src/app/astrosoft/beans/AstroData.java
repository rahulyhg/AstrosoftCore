/**
 * AstroData.java
 *
 * Created on December 14, 2002, 12:08 PM
 *
 * @author  E. Rajasekar
 */
package app.astrosoft.beans;

import java.util.Calendar;
import java.util.GregorianCalendar;

import app.astrosoft.consts.Sex;

import app.astrosoft.util.AstroUtil;

public class AstroData {

    private String personName;
    private Sex sex;
    private Place place;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minutes;
    private int seconds;

    private double time;
	private Calendar calendar;

    public AstroData(
        String name, Sex sex, int date, int month, int year, int hr, int min, int secs,
        Place place) {

        personName = name;
        this.sex = sex;
        this.date = date;
        this.month = month;
        this.year = year;
        hour = hr;
        minutes = min;
        seconds = secs;
        this.place = place;
        
        time = AstroUtil.decimal(hr, min, 0);
     	calendar = new GregorianCalendar(year, month - 1, date, hour, minutes, seconds);

    }
    
    public AstroData(
            String name, int date, int month, int year, int hr, int min, int secs,
            Place place){
    	
    	this(name,null,date,month,year,hr,min,secs,place);
    }

	public String name() {
		return personName;
	}
    
    public int year() {
		return year;
	}

	public int month() {
		return month;
	}

	public int date() {
		return date;
	}
    
    public int hour() {
		return hour;
	}
    
    public int minutes() {
		return minutes;
	}

	public double latitude() {
		return place.latitude();
	}
    
    public double longitude() {
		return place.longitude();
	}
    
    public String place() {
		return place.city();
	}
    
    public double timeZone() {
		return place.timeZone();
	}

	
	public double time() {
		return time;
	}

	public Calendar calender() {
		return calendar;
	}

	public Place getPlace() {
		return place;
	}
	public String birthDayString(){
		return AstroUtil.formatDate(calendar.getTime());
	}
	
	public Sex sex(){
		return sex;
	}
	

}
