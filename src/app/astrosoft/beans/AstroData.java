/**
 * BirthData.java
 *
 * Created on December 14, 2002, 12:08 PM
 *
 * @author  E. Rajasekar
 */
package app.astrosoft.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.astrosoft.consts.Sex;
import app.astrosoft.consts.WeekDay;
import app.astrosoft.consts.XmlConsts;

import app.astrosoft.util.AstroUtil;
import swisseph.SweDate;

public class BirthData {

    private String personName;
    private Sex sex;
    private Place birthPlace;
    private int birthYear;
    private int birthMonth;
    private int birthDate;
    private int birthHour;
    private int birthMinutes;
    private int birthSeconds;
  
    private SweDate birthSD;
    
    private double birthTime;
	private double birthGMT;
	private Calendar birthDay;
	private WeekDay birthWeekDay;

    public BirthData(
        String name, Sex sex, int date, int month, int year, int hr, int min, int secs,
        Place place) {

        personName = name;
        this.sex = sex;
        birthDate = date;
        birthMonth = month;
        birthYear = year;
        birthHour = hr;
        birthMinutes = min;
        birthSeconds = secs;
        birthPlace = place;
        
        birthTime = AstroUtil.decimal(hr, min, 0);
        birthGMT = AstroUtil.decimal(hr, min, 0) - birthPlace.timeZone();
        birthSD = new SweDate(year, month, date, birthGMT);
 		birthDay = new GregorianCalendar(year, month - 1, date, birthHour, birthMinutes, birthSeconds);
 		birthWeekDay = WeekDay.ofDay(year, month, date);

    }
    
    public BirthData( 
            String name, int date, int month, int year, int hr, int min, int secs,
            Place place){
    	
    	this(name,null,date,month,year,hr,min,secs,place);
    }
    
    public BirthData(String name, Sex sex, Calendar birthDay, Place place) {
		this(name, sex, birthDay.get(Calendar.DATE), birthDay.get(Calendar.MONTH) + 1, birthDay.get(Calendar.YEAR), birthDay.get(Calendar.HOUR_OF_DAY), birthDay.get(Calendar.MINUTE),birthDay.get(Calendar.SECOND), place);
	}
    
    public BirthData(String name, Calendar birthDay, Place place){
    	this(name, null, birthDay, place);
    }

	public String name() {
		return personName;
	}
    
    public int year() {
		return birthYear;
	}

	public int month() {
		return birthMonth;
	}

	public int date() {
		return birthDate;
	}
    
    public int hour() {
		return birthHour;
	}
    
    public int minutes() {
		return birthMinutes;
	}

	public double latitude() {
		return birthPlace.latitude();
	}
    
    public double longitude() {
		return birthPlace.longitude();
	}
    
    public String place() {
		return birthPlace.city();
	}
    
    public double timeZone() {
		return birthPlace.timeZone();
	}

	public SweDate birthSD() {
		
		return birthSD;
	}
	
	public double birthTime() {
		return birthTime;
	}
	
	public double birthGMT() {
		return birthGMT;
	}
	
	public Calendar birthDay() {
		return birthDay;
	}
	
	public WeekDay weekDay() {
		return birthWeekDay;
	}
	
	public Place getBirthPlace() {
		return birthPlace;
	}
	public String birthDayString(){
		return AstroUtil.formatDate(birthDay.getTime());
	}
	
	public Sex sex(){
		return sex;
	}
	

}
