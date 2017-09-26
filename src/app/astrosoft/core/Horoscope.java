/**
 * Horoscope.java
 *
 * Created on December 14, 2002, 12:17 PM
 * @author  E. Rajasekar
 */
package app.astrosoft.core;

import java.util.EnumMap;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;


import app.astrosoft.consts.*;
import swisseph.SweDate;
import app.astrosoft.beans.AstroData;
import app.astrosoft.beans.HousePosition;
import app.astrosoft.beans.NakshathraPada;
import app.astrosoft.beans.Place;
import app.astrosoft.beans.PlanetaryInfo;

import app.astrosoft.pref.AstrosoftPref;
import app.astrosoft.util.AstroUtil;
import app.astrosoft.util.SwissHelper;


public class Horoscope implements  PreferenceChangeListener  {

	private static final Logger log = Logger.getLogger(Horoscope.class.getName());

	private AstroData astroData;

	private SwissHelper swissHelper;

	private double ayanamsa;

	private double sunrise;

	private double sunset;

	private HousePosition housePosition;

	private PlanetaryInfo planetaryInfo;

	private Vimshottari vimDasa;

	private Ashtavarga ashtaVarga;

	private Thithi thithi;

	private Paksha pak;

	private Yoga yoga;

	private ShadBala shadBala;

	private String title;
	
	private boolean isBirthAtDay;


	/** Creates a new instance of Horoscope */
	public Horoscope(String name, int date, int month, int year, int hr,
			int min, Place place) {

		this(new AstroData(name, date, month, year, hr, min, 0, place));
	}

	public Horoscope(String name, int date, int month, int year, int hr,
			int min, double longi, double lati, double tz, String place) {
		this(name, date, month, year, hr, min, new Place(place, lati, longi, tz));
	}

	public Horoscope(AstroData astroData){

		this.astroData = astroData;
		swissHelper = new SwissHelper(astroData.year(), astroData.month(), astroData.date(), astroData.time(), astroData.timeZone());
		calculate();
	}


	/*public static Horoscope valueOfXMLNode(Node horoscopeNode){

		return new Horoscope(AstroData.valueOfXMLNode(horoscopeNode.getChildNodes().item(0)));
	}*/

	/*public Element toXMLElement(Document doc, String elementName){

		Element horElement = doc.createElement(elementName);
		horElement.appendChild(astroData.toXMLElement(doc));
		return horElement;
	}*/


	public void calculate() {

		calcPlanetaryInfo();
		//calcDashaBhukthis();
		//calcAshtaVarga();
		calcDetails();

		//TODO: Remove it after all testing
		//calcShadBala();
		//Reset lazy objects to null to get values updated
  	}
	
	public void calculateAll() {

		calculate();
		calcDashaBhukthis();
		calcAshtaVarga();
		calcShadBala();
		
	}

	public void setAyanamsa(Ayanamsa ayanamsa) {

		swissHelper.setAyanamsa(ayanamsa);
		calculateAll();
	}

	private void calcSunRiseSet() {

		sunrise = AstroUtil.getSunRise(astroData.year(), astroData.month(), astroData.date(),
				astroData.longitude(), astroData.latitude(), astroData.timeZone());
		sunset = AstroUtil.getSunSet(astroData.year(), astroData.month(), astroData.date(),
				astroData.longitude(), astroData.latitude(), astroData.timeZone());
		
		if ((astroData.time() > sunrise)
				&& (astroData.time() < sunset)) {
			isBirthAtDay = true;
		} else {
			isBirthAtDay = false;
		}
	}

	public void calcHousePositions() {

		housePosition = swissHelper.calcHousePosition(astroData.longitude(), astroData.latitude());
	}

	public void calcPlanetaryInfo() {

		calcHousePositions();
		EnumMap<Planet, Double> planetaryPosition = swissHelper.getPlanetaryPosition();
		planetaryInfo = new PlanetaryInfo(planetaryPosition, swissHelper.getPlanetDirection(), housePosition);

		ayanamsa = swissHelper.getAyanamsa();
	}


	public void calcDashaBhukthis() {

		vimDasa = new Vimshottari(getPlanetaryPosition(Planet.Moon), astroData.calender());

		/*setVimDasha(v.getDashaString());
		setCurrentDasha(v.getCurrentDasha());
		setBirthDasaLord(v.getBirthDasaLord());
		setDasaBal(v.getDasaBal());*/
	}

	public void calcAshtaVarga() {

		ashtaVarga= new Ashtavarga(getDivChart(Varga.Rasi));
	}

	private void calcDetails() {

		calcSunRiseSet();

		thithi = Thithi.ofDeg(getPlanetaryPosition(Planet.Sun), getPlanetaryPosition(Planet.Moon));
		pak = Paksha.ofDeg(getPlanetaryPosition(Planet.Sun), getPlanetaryPosition(Planet.Moon));
		yoga = Yoga.ofDeg(getPlanetaryPosition(Planet.Sun), getPlanetaryPosition(Planet.Moon));
	}

	private void calcShadBala() {

		if (astroData.year() < 1900){
			log.warning("Year should be less than 1900");

		}else{
			shadBala = new ShadBala(planetaryInfo, housePosition, astroData, ayanamsa, sunrise, sunset, getPaksha());
		}

	}

	public void preferenceChange(PreferenceChangeEvent evt) {

		/*if(evt.getKey().equals(AstrosoftPref.Preference.Ayanamsa.name())){
			setAyanamsa(Enum.valueOf(Ayanamsa.class, evt.getNewValue()));
		}else */
		if(evt.getKey().equals(AstrosoftPref.Preference.Language.name())){
			this.languageChanged();
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		int i = 0;

		sb.append("\n");
		//sb.append("cp " + getCurrentDasa());
		sb.append("\n");
		sb.append("Bhava\n");

		sb.append(housePosition);

		sb.append("\n");
		sb.append("Planetary Info\n");
		sb.append(planetaryInfo);
		sb.append("\n");
		sb.append("Thithi " + thithi
				+ " Paksha " + pak);
		sb.append("\n");

		sb.append("AshtaVarga \n");
		sb.append(ashtaVarga);
		sb.append("\n");
		sb.append("ShadBala \n");
		sb.append(shadBala);
		sb.append("\n");
		sb.append("Dasas\n");
		int j = 0;
        int k = 0;

        /*for ( i = 0; i < 9; i++ ) {

            for ( j = 0; j < 9; j++ ) {

                for ( k = 0; k < 9; k++ )
                	sb.append( getVimDasha()[i][j][k]  + "\t");

                sb.append("\n");
            }
            sb.append("\n");

        }*/
        sb.append(vimDasa);
        sb.append("\n");
		return sb.toString();

	}

	public HousePosition getHousePosition() {
		return housePosition;
	}

	public AstroData getAstroData() {
		return astroData;
	}


	public String getPersonName(){
		return astroData.name();
	}

	public Rasi getAscendant(){
		return housePosition.getAscendant();
	}

	public Rasi getRasi() {
		return planetaryInfo.getPlanetRasi().get(Planet.Moon);
	}

	public NakshathraPada getNakshathra() {
		return planetaryInfo.getPlanetNakshathra().get(Planet.Moon);
	}

	public PlanetaryInfo getPlanetaryInfo() {
		return planetaryInfo;
	}

	public EnumMap<Planet, Double> getPlanetaryPosition(){
		return planetaryInfo.getPlanetPosition();
	}

	public Double getPlanetaryPosition(Planet planet){
		return planetaryInfo.getPlanetPosition(planet);
	}

	public EnumMap<Planet, Integer> getPlanetaryLocation(){
		return planetaryInfo.getPlanetLocation();
	}

	public int getPlanetaryLocation(Planet p) {
		return planetaryInfo.getPlanetLocation().get(p);
	}


	public EnumMap<Planet, Rasi> getPlanetRasis() {
		return planetaryInfo.getPlanetRasi();
	}

	public EnumMap<Planet, Boolean> getPlanetDirection() {
		return planetaryInfo.getPlanetDirection();
	}

	public EnumMap<Varga, EnumMap<Planet, Integer>> getDivChart() {
		return planetaryInfo.getDivChart();
	}

	public EnumMap<Planet, Integer> getDivChart(Varga varga) {
		return planetaryInfo.getDivChart(varga);
	}

	public Thithi getThithi() {
		return thithi;
	}

	public Paksha getPaksha() {
		return pak;
	}

	public Yoga getYoga() {
		return yoga;
	}

	public double getAyanamsa() {
		return ayanamsa;
	}

	public double getSunrise() {
		return sunrise;
	}

	public double getSunset() {
		return sunset;
	}

	public boolean isBirthAtDay() {
		return isBirthAtDay;
	}
	
	public Sex getPersonSex(){
		return astroData.sex();
	}
	

	public ShadBala getShadBala() {

		if (shadBala == null){

			calcShadBala();
		}
		return shadBala;
	}

	public Ashtavarga getAshtaVarga() {
		
		if (ashtaVarga == null){
			calcAshtaVarga();
		}
		return ashtaVarga;
	}

	
	/**
	 * @return Returns the dasaBal.
	 */
	public String getDasaBal() {
		return getVimshottariDasa().getBalance();
	}

	/**
	 * @param vimDasa The vimDasa to set.

	private void setVimDasha(String[][][] vimDasa) {
		this.vimDasha = vimDasa;
	}*/

	/**
	 * @return Returns the vimDasa.
	 */
	public Vimshottari getVimshottariDasa() {
		
		if (vimDasa == null){
			calcDashaBhukthis();
		}
		return vimDasa;
	}

	/**
	 * @return Returns the currentDasha.
	 */
	public String getCurrentDasa() {

		VimDasa current = (VimDasa)getVimshottariDasa().getCurrent();
		if (current != null){
			return current.fullDasa();
		}else{
			return "No";
		}
	}



	public String getTitle() {

		if(title == null){
			StringBuilder sb = new StringBuilder("Horoscope of ");
			sb.append(astroData.name());
			sb.append(" born on ");
			sb.append(astroData.birthDayString());
			sb.append(" " + AstroUtil.timeFormat(astroData.time()));
			sb.append(" at ");
			sb.append(astroData.place());
			title = sb.toString();
		}
		return title;
	}

	public static void main(String[] args) {

	/*	Horoscope h = new Horoscope("Raja", 11, 12, 1980, 1, 44,
				77 + (44.00 / 60.00), 11 + (22.00 / 60.00), 5.5, "Erode");
		h.setAyanamsa(Ayanamsa.LAHARI);
		h.calculateAll();

		System.out.println(h);
		// Horoscope h = new
		// Horoscope("Suba",31,3,1988,18,10,77+(44.00/60.00),11+(22.00/60.00),5.5,"Erode");
		// Horoscope h = new
		// Horoscope("Viji",7,8,1982,11,45,77+(44.00/60.00),11+(22.00/60.00),5.5,"Erode");
		/*
		 * Horoscope h = new Horoscope( "BV", 16, 10, 1918, 14, 26, 77 + ( 34.00 /
		 * 60.00 ), 13 + ( 0.00 / 60.00 ), 5.5, "Banglore" );
		 */





	/*	int i = 0;

		for (i = 0; i < 10; i++) {

			System.out.println(Planet.ofIndex(i) + " --> "
					+ AstroUtil.dms(h.planetPositions[i]) + " Dir -->"
					+ h.planetDir[i]);

		}

		System.out.println("cp " + h.currentDasha);
		System.out.println("Bhava");

		for (i = 0; i <= 12; i++) {

			System.out.println(i + " --> " + AstroUtil.dms(h.housePositions[i])
					+ " sandi " + AstroUtil.dms(h.bhavaSandhis[i]));

		}

		/*
		 * for(i = 0; i < VargaCharts.noOfCharts; i++){
		 * System.out.println(VargaCharts.chartNames[i]+"\n");

		for (int j = 0; j < 10; j++) {

			System.out.println(Planet.ofIndex(j) + " --> "
					+ Rasi.ofIndex(h.charts[0][j] - 1) + " - "
					+ h.charts[0][j]);

		}

		// }*/
		/*System.out.println("Thithi " + h.thithi
				+ " Paksha " + h.pak);

		for (i = 0; i < 7; i++)
			System.out.println(i + " --> " + h.karakas[i] + "<--> "
					+ Karaka.ofIndex(h.getKaraka( i )).toString() + " <--> "
					+ AstroUtil.dms(h.planetPositions[h.karakas[i]] % 30));*/

		runTests();
	}

	/*
	public void preferenceChange(PreferenceChangeEvent evt) {

		if(evt.getKey().equals(AstrosoftPref.Preference.Ayanamsa.name())){
			setAyanamsa(Enum.valueOf(Ayanamsa.class, evt.getNewValue()));
		}else if(evt.getKey().equals(AstrosoftPref.Preference.Language.name())){
			log.info("Language option changed");
			infoTableData = null;
			calcDashaBhukthis();
		}
	}*/

	public void languageChanged(){
		log.entering("Horoscope" , "languageChanged()");
		calcDashaBhukthis();
	}

	public static void runTests(){

		AstrosoftPref.get().setLanguage(Language.ENGLISH);

		Horoscope h = new Horoscope("Raja", 11, 12, 1980, 1, 44,
				77 + (44.00 / 60.00), 11 + (22.00 / 60.00), 5.5, "Erode");
		h.calculateAll();

		System.out.println("----------------------Raja----------------------");
		System.out.println(h);

		Horoscope b = new Horoscope("Elango", 17, 4, 1957, 7, 10,
				77 + (44.00 / 60.00), 11 + (22.00 / 60.00), 5.5, "Erode");

		b.calculateAll();
		System.out.println("----------------------Elango----------------------");
		System.out.println(b);

		Horoscope g = new Horoscope("Mani", 10, 8, 1960, 5, 30,
				77 + (44.00 / 60.00), 11 + (22.00 / 60.00), 5.5, "Erode");
		g.calculateAll();
		System.out.println("----------------------Mani----------------------");
		System.out.println(g);

		Horoscope s = new Horoscope("Suba",31,3,1988,18,10,77+(44.00/60.00),11+(22.00/60.00),5.5,"Erode");
		s.calculateAll();
		System.out.println("----------------------Suba----------------------");
		System.out.println(s);
		h = new Horoscope("Viji",7,8,1982,11,45,77+(44.00/60.00),11+(22.00/60.00),5.5,"Erode");
		h.calculateAll();
		System.out.println("----------------------Viji----------------------");
		System.out.println(h);
	}

}
