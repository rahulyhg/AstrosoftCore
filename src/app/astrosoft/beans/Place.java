/**
 * Place.java
 * Created On 2005, Nov 19, 2005 1:21:26 PM
 * @author E. Rajasekar
 */

package app.astrosoft.beans;

import java.util.EnumSet;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.astrosoft.consts.XmlConsts;
import app.astrosoft.util.AstroUtil;
import app.astrosoft.util.AstrosoftTimeZone;

public class Place {

	public static enum LocationType{
		Longitude, Latitude;
	}
	
	public static enum Direction {
		EAST(1),WEST(-1),NORTH(1),SOUTH(-1);
		
		private int val = 1;
		
		private Direction(int val) {
			this.val = val;
		}
		
		public static Direction ofChar(char c){
			for(Direction d : values()){
				if(d.charVal() == Character.toUpperCase(c)){
					return d;
				}
			}
			return null;
		}
		
		public char charVal(){
			return this.name().charAt(0);
		}
		
		public static EnumSet NS(){
			return EnumSet.of(NORTH, SOUTH);
		}
		
		public static EnumSet EW(){
			return EnumSet.of(EAST, WEST);
		}
		
		public static Direction ofVal(double val, LocationType type){
			switch(type){
				case Latitude: return val < 0 ? SOUTH : NORTH;
				case Longitude: return val < 0 ? WEST : EAST;
			}
			return null;
		}
	};
	
	public static class Location{
		private int deg;
		private int min;
		Direction dir;
		
		public Location(int deg, int min, Direction dir) {
			this.deg = deg;
			this.min = min;
			this.dir = dir;
		}
		
		public Location(String deg, String min, Direction dir) {
			this(Integer.parseInt(deg), Integer.parseInt(min), dir);
		}
		
		public Location(double value, LocationType type) {
			int []degmin = AstroUtil.int_dms(value);
			
			deg = Math.abs(degmin[0]);
			min = Math.abs(degmin[1]);
			dir = Direction.ofVal(value, type);
		}
		
		public double value(){
			return AstroUtil.decimal(deg, min, 0) * dir.val;
		}
		
		public int deg(){
			return deg;
		}
		
		public int min(){
			return min;
		}
		
		public Direction dir(){
			return dir;
		}
		
		@Override
		public String toString() {
			
			return deg + "." + min + dir;
		}
		
		public String format(){
			
			return AstroUtil.twoDigit(deg) + "." + AstroUtil.twoDigit(min);
		}
	}
	
	private String name;
	private double longitude;
	private double latitude;
	private double timeZone;
	private String timeZoneId;
	
	public Place(String name, double latitude, double longitude, double timeZone) {
		this.name = name;
		this.longitude = longitude; 
		this.latitude = latitude;
		this.timeZone = timeZone;
	}

	//TODO REMOVE TIMEZONEID AFTER ASTROSOFTPERF
	public Place(String name, double latitude, double longitude, String timeZoneId) {
		this(name, latitude, longitude, AstrosoftTimeZone.offset(timeZoneId));
		this.timeZoneId = timeZoneId;
	}
	
	public Place(String name,  double latitude, Direction latDir, double longitude, Direction longDir, double timeZone) {
		this(name, latitude * latDir.val, longitude * longDir.val, timeZone);
	}
	
	public Place(String city, String latitude, char latDir, String longitude, char longDir, String timeZoneId) {
		this(city, AstroUtil.toDouble(latitude,"\\."), Direction.ofChar(latDir), AstroUtil.toDouble(longitude, "\\."), Direction.ofChar(longDir), AstrosoftTimeZone.offset(timeZoneId));
		this.timeZoneId = timeZoneId;
	}
	
	public Place(String name, Location latitude, Location longitude, String timeZoneId) {
		this(name, latitude.value(), longitude.value(), AstrosoftTimeZone.offset(timeZoneId));
		this.timeZoneId = timeZoneId;
	}
	
	public static Place getDefault(){
		return new Place("Erode", new Location(11, 22, Direction.NORTH), new Location(77,44, Direction.EAST), "IST");
	}
	
	public String city() {
		return name;
	}
	


	public String display(){
		return String.format("[%s , %s , %s, %s]", name, longitude, latitude, timeZone) ;
	}
	
	public String toString(){
		
		return name;
	}
	
	public double longitude() {
		return longitude;
	}

	public double latitude() {
		return latitude;
	}
	
	public double timeZone() {
		return timeZone;
	}
	
	public void setTimeZone(double timeZone) {
		this.timeZone = timeZone;
	}
	
	public AstrosoftTimeZone astrosoftTimeZone(){
		return new AstrosoftTimeZone(timeZoneId);
	}
	
	public Location latitudeLocation(){
		return new Location(latitude, LocationType.Latitude);
	}
	
	public Location longitudeLocation(){
		return new Location(longitude, LocationType.Longitude);
	}
	

	public static void main(String[] args) {
		System.out.println(Place.getDefault());
	}


}
