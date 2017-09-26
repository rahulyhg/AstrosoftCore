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
import app.astrosoft.consts.WeekDay;

import app.astrosoft.util.AstroUtil;
import swisseph.SweDate;

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
  
    private SweDate birthSD;
    
    private double time;
	private double birthGMT;
	private Calendar calendar;
	private WeekDay weekDay;

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
        birthGMT = AstroUtil.decimal(hr, min, 0) - this.place.timeZone();
        birthSD = new SweDate(year, month, date, birthGMT);
 		calendar = new GregorianCalendar(year, month - 1, date, hour, minutes, seconds);
 		weekDay = WeekDay.ofDay(year, month, date);

    }
    
    public AstroData(
            String name, int date, int month, int year, int hr, int min, int secs,
            Place place){
    	
    	this(name,null,date,month,year,hr,min,secs,place);
    }
    
    public AstroData(String name, Sex sex, Calendar calendar, Place place) {
		this(name, sex, calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), place);
	}
    
    public AstroData(String name, Calendar calendar, Place place){
    	this(name, null, calendar, place);
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

	public SweDate birthSD() {
		
		return birthSD;
	}
	
	public double birthTime() {
		return time;
	}
	
	public double birthGMT() {
		return birthGMT;
	}
	
	public Calendar birthDay() {
		return calendar;
	}
	
	public WeekDay weekDay() {
		return weekDay;
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
