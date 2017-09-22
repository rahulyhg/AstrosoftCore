/**
 * Compactibility.java
 *
 * Created on October 11, 2003, 12:31 PM
 *
 * @author E. Rajasekar
 */
package app.astrosoft.core;

import java.util.EnumMap;
import java.util.Map;

import app.astrosoft.beans.BirthData;
import app.astrosoft.beans.PlanetaryInfo;
import app.astrosoft.consts.AstroConsts;
import app.astrosoft.consts.Kuta;
import app.astrosoft.consts.Nakshathra;
import app.astrosoft.consts.Planet;
import app.astrosoft.consts.Rasi;

import app.astrosoft.util.FileOps;

public class Compactibility  {

	private static enum KutaFile {
		RasiKuta,
		YoniKuta,
		NadiKuta;

		public String file(){
			return name() + ".txt";
		}
	};

	private String boyName;

	private String girlName;

	private int boyNak;

	private int girlNak;

	private int boyRasi;

	private int girlRasi;

	private BirthData boyBirthData;

	private BirthData girlBirthData;

	private EnumMap<Kuta, Integer> kutas;


	private int totalKutaGained = 0;

	private PlanetaryInfo boyPlanetaryInfo;

	private PlanetaryInfo girlPlanetaryInfo;

	private EnumMap<Planet, Double> boyDosha;

	private EnumMap<Planet, Double> girlDosha;

	private double bDoshaTotal = 0;

	private double gDoshaTotal = 0;

	private String bCurPeriod;

	private String gCurPeriod;

	private Rasi beejaRasi;

	private Rasi kshetraRasi;

	private double beejaPos;

	private double kshetraPos;

	private boolean hasHoroscope = false;

	/**
	 * Creates a new instance of Compactibility
	 */
	public Compactibility(String boyName, String girlName, Nakshathra boyNak,
			Nakshathra girlNak, Rasi boyRasi, Rasi girlRasi) {

		this.boyName = boyName;
		this.girlName = girlName;
		this.boyNak = boyNak.ordinal();
		this.girlNak = girlNak.ordinal();
		this.boyRasi = boyRasi.ordinal();
		this.girlRasi = girlRasi.ordinal();
		kutas = new EnumMap<Kuta, Integer>(Kuta.class);
		totalKutaGained = 0;
		calcKutas();

		for (Kuta k : Kuta.values()) {

			totalKutaGained = totalKutaGained + kutas.get(k);
		}

	}

	public Compactibility(Horoscope b, Horoscope g) {
		this(b.getBirthData().name(), g.getBirthData().name(), b.getNakshathra().getNak(), g.getNakshathra().getNak(), b.getRasi(), g.getRasi());

		this.boyBirthData = b.getBirthData();
		this.girlBirthData = g.getBirthData();

		this.bCurPeriod = b.getCurrentDasa();
		this.gCurPeriod = g.getCurrentDasa();

		this.boyPlanetaryInfo = b.getPlanetaryInfo();
		this.girlPlanetaryInfo = g.getPlanetaryInfo();
		calcDoshas();
		calcBeejaKshetra();
		hasHoroscope = true;
	}



	private void calcKutas() {

		kutas.put(Kuta.Dina, calcDinaKuta());
		kutas.put(Kuta.Gana, calcGanaKuta());
		kutas.put(Kuta.Mahendra, calcMahendraKuta());
		kutas.put(Kuta.StreeDeergha, calcStreeDeerghaKuta());
		kutas.put(Kuta.Yoni, calcYoniKuta());
		kutas.put(Kuta.Rasi, calcRasiKuta());
		kutas.put(Kuta.RasiAdhipathi, calcRasiAdhipathiKuta());
		kutas.put(Kuta.Vasya, calcVasyaKuta());
		kutas.put(Kuta.Rajju, calcRajjuKuta());
		kutas.put(Kuta.Vedha, calcVedhaKuta());
		kutas.put(Kuta.Varna, calcVarnaKuta());
		kutas.put(Kuta.Nadi, calcNadiKuta());

	}

	private int calcDinaKuta() {

		int count = (boyNak - girlNak + 1);

		if (count <= 0) {

			count = count + 27;

		}

		count = count % 9;

		// System.out.println(boyNak + " - " + girlNak + " Count : " + count);
		if ((count % 2) == 0) {

			return 3;

		} else {

			return 0;

		}

	}

	private int calcGanaKuta() {

		int boyGana = findGana(boyNak);
		int girlGana = findGana(girlNak);

		// System.out.println(" BG: " + boyGana + " GG: " + girlGana);
		int gana = 0;

		if ((boyGana == girlGana) || ((boyGana == 1) && (girlGana == 2))) {

			gana = 6;

		} else if (((boyGana == 2) && (girlGana == 1))) {

			gana = 4;

		} else if ((boyGana == 3) && ((girlGana == 1) || (girlGana == 2))) {

			gana = 3;

		} else if ((girlGana == 3) && ((boyGana == 1) || (boyGana == 2))) {

			int count = (girlNak - boyNak + 1);

			if (count <= 0) {

				count = count + 27;

			}

			if (count > 14) {

				gana = 3;

			} else {

				gana = 0;

			}

		}

		return gana;

	}

	private int findGana(int nak) {

		int gana = 0;

		switch (nak) {

		case 0:
		case 4:
		case 6:
		case 7:
		case 12:
		case 14:
		case 16:
		case 21:
		case 26:
			gana = 1;

			break;

		case 1:
		case 3:
		case 5:
		case 10:
		case 11:
		case 19:
		case 20:
		case 24:
		case 25:
			gana = 2;

			break;

		case 2:
		case 8:
		case 9:
		case 13:
		case 15:
		case 17:
		case 18:
		case 22:
		case 23:
			gana = 3;

			break;

		}

		return gana;

	}

	private int calcMahendraKuta() {

		int count = (boyNak - girlNak + 1);

		if (count <= 0) {

			count = count + 27;

		}

		if ((count == 4) || (count == 7) || (count == 10) || (count == 13)
				|| (count == 16) || (count == 19) || (count == 22)
				|| (count == 25)) {

			return 1;

		} else {

			return 0;

		}

	}

	private int calcStreeDeerghaKuta() {

		int count = (boyNak - girlNak + 1);

		if (count <= 0) {

			count = count + 27;

		}

		if (count > 9) {

			return 1;

		} else {

			return 0;

		}

	}

	private int calcYoniKuta() {

		int boyYoni = findYoni(boyNak);
		int girlYoni = findYoni(girlNak);

		int yoni = FileOps.getFromFile(KutaFile.YoniKuta.file(), boyYoni,
				girlYoni);

		return yoni;

	}

	private int findYoni(int nak) {

		int yoni = 0;

		switch (nak) {

		case 0:
		case 23:
			yoni = 1;

			break;

		case 1:
		case 26:
			yoni = 2;

			break;

		case 7:
		case 2:
			yoni = 3;

			break;

		case 3:
		case 4:
			yoni = 4;

			break;

		case 18:
		case 5:
			yoni = 5;

			break;

		case 8:
		case 6:
			yoni = 6;

			break;

		case 9:
		case 10:
			yoni = 7;

			break;

		case 11:
		case 25:
			yoni = 8;

			break;

		case 14:
		case 12:
			yoni = 9;

			break;

		case 15:
		case 13:
			yoni = 10;

			break;

		case 17:
		case 16:
			yoni = 11;

			break;

		case 19:
		case 21:
			yoni = 12;

			break;

		case 20:
			yoni = 13;

			break;

		case 24:
		case 22:
			yoni = 14;

			break;

		}

		return yoni;

	}

	private int calcRasiKuta() {

		int kuta = FileOps.getFromFile(KutaFile.RasiKuta.file(), girlRasi + 1,
				boyRasi + 1);

		return kuta;

	}

	private int calcRasiAdhipathiKuta() {

		int kuta = 0;

		Planet boyLord = Rasi.ofIndex(boyRasi).owner();
		Planet girlLord = Rasi.ofIndex(girlRasi).owner();

		// System.out.println("BL: " + boyLord + " GL: " + girlLord);
		if (boyLord == girlLord) {

			kuta = 5;

			return kuta;

		}

		int boy_girl_rel = AstroConsts.permanentRel(boyLord,girlLord);
		int girl_boy_rel = AstroConsts.permanentRel(girlLord,boyLord);

		// System.out.println("BR: " + boy_girl_rel + " GR: " + girl_boy_rel);
		if ((boy_girl_rel < 0) || (girl_boy_rel < 0)) {

			kuta = 0;

		} else if ((boy_girl_rel == 0) && (girl_boy_rel == 0)) {

			kuta = 2;

		} else if ((boy_girl_rel == 1) && (girl_boy_rel == 1)) {

			kuta = 5;

		} else if (((boy_girl_rel == 1) && (girl_boy_rel == 0))
				|| ((boy_girl_rel == 0) && (girl_boy_rel == 1))) {

			kuta = 4;

		}

		return kuta;

	}

	private int calcVasyaKuta() {

		int vasya = 0;

		switch (girlRasi) {

		case 0:

			if ((boyRasi == 0) || (boyRasi == 4) || (boyRasi == 7)) {

				vasya = 2;

			}

			break;

		case 1:

			if ((boyRasi == 1) || (boyRasi == 3) || (boyRasi == 6)) {

				vasya = 2;

			}

			break;

		case 2:

			if ((boyRasi == 2) || (boyRasi == 5)) {

				vasya = 2;

			}

			break;

		case 3:

			if ((boyRasi == 3) || (boyRasi == 7) || (boyRasi == 8)) {

				vasya = 2;

			}

			break;

		case 4:

			if ((boyRasi == 4) || (boyRasi == 6)) {

				vasya = 2;

			}

			break;

		case 5:

			if ((boyRasi == 5) || (boyRasi == 2) || (boyRasi == 11)) {

				vasya = 2;

			}

			break;

		case 6:

			if ((boyRasi == 6) || (boyRasi == 5) || (boyRasi == 9)) {

				vasya = 2;

			}

			break;

		case 7:

			if ((boyRasi == 7) || (boyRasi == 3)) {

				vasya = 2;

			}

			break;

		case 8:

			if ((boyRasi == 8) || (boyRasi == 11)) {

				vasya = 2;

			}

			break;

		case 9:

			if ((boyRasi == 9) || (boyRasi == 0) || (boyRasi == 10)) {

				vasya = 2;

			}

			break;

		case 10:

			if ((boyRasi == 10) || (boyRasi == 0)) {

				vasya = 2;

			}

			break;

		case 11:

			if ((boyRasi == 11) || (boyRasi == 9)) {

				vasya = 2;

			}

			break;

		}

		return vasya;

	}

	private int calcRajjuKuta() {

		int[] pada = { 0, 8, 9, 17, 18, 26 };
		int[] kati = { 1, 7, 10, 16, 19, 25 };
		int[] nabhi = { 2, 6, 11, 15, 20, 24 };
		int[] kanta = { 3, 5, 12, 14, 21, 23 };
		int[] siro = { 4, 13, 22 };

		if (found(pada, boyNak) && found(pada, girlNak)) {

			return 0;

		}

		if (found(kati, boyNak) && found(kati, girlNak)) {

			return 0;

		}

		if (found(nabhi, boyNak) && found(nabhi, girlNak)) {

			return 0;

		}

		if (found(kanta, boyNak) && found(kanta, girlNak)) {

			return 0;

		}

		if (found(siro, boyNak) && found(siro, girlNak)) {

			return 0;

		}

		return 1;

	}

	private boolean found(int[] arr, int val) {

		for (int i = 0; i < arr.length; i++) {

			if (arr[i] == val) {

				return true;

			}

		}

		return false;

	}

	private int calcVedhaKuta() {

		if (((boyNak == 0) && (girlNak == 17))
				|| ((boyNak == 17) && (girlNak == 0))) {

			return 0;

		}

		if (((boyNak == 1) && (girlNak == 16))
				|| ((boyNak == 16) && (girlNak == 1))) {

			return 0;

		}

		if (((boyNak == 2) && (girlNak == 15))
				|| ((boyNak == 15) && (girlNak == 2))) {

			return 0;

		}

		if (((boyNak == 3) && (girlNak == 14))
				|| ((boyNak == 14) && (girlNak == 3))) {

			return 0;

		}

		if (((boyNak == 5) && (girlNak == 21))
				|| ((boyNak == 21) && (girlNak == 5))) {

			return 0;

		}

		if (((boyNak == 6) && (girlNak == 20))
				|| ((boyNak == 20) && (girlNak == 6))) {

			return 0;

		}

		if (((boyNak == 7) && (girlNak == 19))
				|| ((boyNak == 19) && (girlNak == 7))) {

			return 0;

		}

		if (((boyNak == 8) && (girlNak == 18))
				|| ((boyNak == 18) && (girlNak == 8))) {

			return 0;

		}

		if (((boyNak == 9) && (girlNak == 26))
				|| ((boyNak == 26) && (girlNak == 9))) {

			return 0;

		}

		if (((boyNak == 10) && (girlNak == 25))
				|| ((boyNak == 25) && (girlNak == 10))) {

			return 0;

		}

		if (((boyNak == 11) && (girlNak == 24))
				|| ((boyNak == 24) && (girlNak == 11))) {

			return 0;

		}

		if (((boyNak == 12) && (girlNak == 23))
				|| ((boyNak == 12) && (girlNak == 23))) {

			return 0;

		}

		if (((boyNak == 4) && (girlNak == 22))
				|| ((boyNak == 22) && (girlNak == 4))) {

			return 0;

		}

		return 1;

	}

	private int calcVarnaKuta() {

		int bg = findGrade(boyRasi);
		int gg = findGrade(girlRasi);

		if (gg > bg) {

			return 0;

		}

		return 1;

	}

	private int findGrade(int rasi) {

		int grade = 0;

		switch (rasi) {

		case 11:
		case 7:
		case 3:
			grade = 4;

			break;

		case 4:
		case 8:
		case 6:
			grade = 3;

			break;

		case 0:
		case 2:
		case 10:
			grade = 2;

			break;

		case 1:
		case 5:
		case 9:
			grade = 1;

			break;

		}

		return grade;

	}

	private int calcNadiKuta() {

		int nadi = FileOps.getFromFile(KutaFile.NadiKuta.file(), girlNak + 1,
				boyNak + 1);

		return nadi;

	}

	private void calcDoshas() {

		EnumMap<Planet, Integer> boyPos = boyPlanetaryInfo.getPlanetLocation();

		EnumMap<Planet, Integer> girlPos = girlPlanetaryInfo.getPlanetLocation();

		EnumMap<Planet, Rasi> boyPlanetRasis  = boyPlanetaryInfo.getPlanetRasi();

		EnumMap<Planet, Rasi> girlPlanetRasis = girlPlanetaryInfo.getPlanetRasi();

		boyDosha = new EnumMap<Planet, Double>(Planet.class);
		girlDosha = new EnumMap<Planet, Double>(Planet.class);
		boyDosha.put(Planet.Mars, calcMarsDosha(boyPos.get(Planet.Mars), boyPlanetRasis.get(Planet.Mars)));
		boyDosha.put(Planet.Sun, calcSunDosha(boyPos.get(Planet.Sun), boyPlanetRasis.get(Planet.Sun)));
		boyDosha.put(Planet.Saturn, calcSatDosha(boyPos.get(Planet.Saturn), boyPlanetRasis.get(Planet.Saturn)));
		boyDosha.put(Planet.Ra_Ke, calcRahuKetuDosha(boyPos.get(Planet.Rahu), boyPos.get(Planet.Ketu),
				boyPlanetRasis.get(Planet.Rahu), boyPlanetRasis.get(Planet.Ketu)));

		girlDosha.put(Planet.Mars, calcMarsDosha(girlPos.get(Planet.Mars), girlPlanetRasis.get(Planet.Mars)));
		girlDosha.put(Planet.Sun, calcSunDosha(girlPos.get(Planet.Sun), girlPlanetRasis.get(Planet.Sun)));
		girlDosha.put(Planet.Saturn, calcSatDosha(girlPos.get(Planet.Saturn), girlPlanetRasis.get(Planet.Saturn)));
		girlDosha.put(Planet.Ra_Ke, calcRahuKetuDosha(girlPos.get(Planet.Rahu), girlPos.get(Planet.Ketu),
				girlPlanetRasis.get(Planet.Rahu), girlPlanetRasis.get(Planet.Ketu)));

		for (Double v : boyDosha.values()) {
			bDoshaTotal = bDoshaTotal + v;
		}

		for (Double v : girlDosha.values()) {
			gDoshaTotal = gDoshaTotal + v;
		}
	}

	private double calcMarsDosha(int pos, Rasi rasi) {

		double dosha = 0;

		if ((pos == 7) || (pos == 8) || (pos == 2) || (pos == 4) || (pos == 12)) {

			switch (rasi) {

			case Mesha:
			case Vrichika:
				dosha = 60;

				break;

			case Vrishabha:
			case Thula:
			case Kumbha:
				dosha = 80;

				break;

			case Mithuna:
			case Kanya:
				dosha = 90;

				break;

			case Kataka:
				dosha = 100;

				break;

			case Simha:
			case Dhanus:
			case Meena:
				dosha = 70;

				break;

			case Makara:
				dosha = 50;

				break;

			}

			if ((pos == 2) || (pos == 4) || (pos == 12)) {

				dosha = dosha / 2;

			}

		} else {

			dosha = 0;

		}

		return dosha;

	}

	private double calcSunDosha(int pos, Rasi rasi) {

		double dosha = 0;

		if ((pos == 7) || (pos == 8) || (pos == 2) || (pos == 4) || (pos == 12)) {

			switch (rasi) {

			case Vrichika:
			case Dhanus:
			case Meena:
				dosha = 35;

				break;

			case Vrishabha:
			case Makara:
			case Kumbha:
				dosha = 45;

				break;

			case Mesha:
				dosha = 25;

				break;

			case Thula:
				dosha = 50;

				break;

			case Mithuna:
			case Kataka:
			case Kanya:
				dosha = 40;

				break;

			case Simha:
				dosha = 30;

				break;

			}

			if ((pos == 2) || (pos == 4) || (pos == 12)) {

				dosha = dosha / 2;

			}

		} else {

			dosha = 0;

		}

		return dosha;

	}

	private double calcSatDosha(int pos, Rasi rasi) {

		double dosha = 0;

		if ((pos == 7) || (pos == 8) || (pos == 2) || (pos == 4) || (pos == 12)) {

			switch (rasi) {

			case Vrishabha:
			case Mithuna:
			case Kanya:
				dosha = 52.50;

				break;

			case Kataka:
			case Simha:
			case Vrichika:
				dosha = 67.50;

				break;

			case Mesha:
				dosha = 75;

				break;

			case Thula:
				dosha = 37.50;

				break;

			case Dhanus:
			case Meena:
				dosha = 60;

				break;

			case Makara:
			case Kumbha:
				dosha = 45;

				break;

			}

			if ((pos == 2) || (pos == 4) || (pos == 12)) {

				dosha = dosha / 2;

			}

		} else {

			dosha = 0;

		}

		return dosha;

	}

	private double calcRahuKetuDosha(int posRa, int posKe, Rasi rasiRa, Rasi rasiKe) {

		double rahuDosha = 0;
		double ketuDosha = 0;

		if ((posRa == 7) || (posRa == 8) || (posRa == 2) || (posRa == 4)
				|| (posRa == 12)) {

			switch (rasiRa) {

			case Mesha:
			case Kataka:
			case Simha:
			case Kumbha:
				rahuDosha = 67.50;

				break;

			case Mithuna:
			case Kanya:
			case Dhanus:
			case Makara:
			case Meena:
			case Thula:
				rahuDosha = 52.50;

				break;

			case Vrishabha:
				rahuDosha = 75;

				break;

			case Vrichika:
				rahuDosha = 37.50;

				break;

			}

			if ((posRa == 2) || (posRa == 4) || (posRa == 12)) {

				rahuDosha = rahuDosha / 2;

			}

		}

		if ((posKe == 7) || (posKe == 8) || (posKe == 2) || (posKe == 4)
				|| (posKe == 12)) {

			switch (rasiKe) {

				case Mesha:
				case Kataka:
				case Simha:
				case Kumbha:
				ketuDosha = 67.50;

				break;

				case Mithuna:
				case Kanya:
				case Dhanus:
				case Makara:
				case Meena:
				case Thula:

				ketuDosha = 52.50;

				break;

				case Vrishabha:
				ketuDosha = 75;

				break;

				case Vrichika:
				ketuDosha = 37.50;

				break;

			}

			if ((posKe == 2) || (posKe == 4) || (posKe == 12)) {

				ketuDosha = ketuDosha / 2;

			}

		}

		return (rahuDosha + ketuDosha);

	}

	private void calcBeejaKshetra() {

		EnumMap<Planet, Double> boyPlanetPos = boyPlanetaryInfo.getPlanetPosition();

		EnumMap<Planet, Double> girlPlanetPos = girlPlanetaryInfo.getPlanetPosition();


		double beeja = (boyPlanetPos.get(Planet.Sun) + boyPlanetPos.get(Planet.Venus) + boyPlanetPos.get(Planet.Jupiter));
		double kshetra = (girlPlanetPos.get(Planet.Moon) + girlPlanetPos.get(Planet.Mars) + girlPlanetPos.get(Planet.Jupiter));

		beejaRasi = Rasi.ofDeg(beeja);
		beejaPos = beeja % 30;

		kshetraRasi = Rasi.ofDeg(kshetra);
		kshetraPos = kshetra % 30;

	}

	private Rasi boyRasi() {
		return Rasi.ofIndex(boyRasi);
	}

	private Rasi girlRasi() {
		return Rasi.ofIndex(girlRasi);
	}

	private Nakshathra boyNak() {
		return Nakshathra.ofIndex(boyNak);
	}

	private Nakshathra girlNak() {
		return Nakshathra.ofIndex(girlNak);
	}

	public EnumMap<Kuta, Integer> getKutas() {
		return kutas;
	}

	public PlanetaryInfo getBoyPlanetaryInfo() {
		return boyPlanetaryInfo;
	}

	public PlanetaryInfo getGirlPlanetaryInfo() {
		return girlPlanetaryInfo;
	}

	public BirthData getBoyBirthData() {
		return boyBirthData;
	}

	public BirthData getGirlBirthData() {
		return girlBirthData;
	}

	public String getBoyName() {
		return boyName;
	}

	public String getGirlName() {
		return girlName;
	}

	public Rasi getBoyRasi() {
		return Rasi.ofIndex(boyRasi);
	}

	public Rasi getGirlRasi() {
		return Rasi.ofIndex(girlRasi);
	}

	public Nakshathra getBoyNak() {
		return Nakshathra.ofIndex(boyNak);
	}

	public Nakshathra getGirlNak() {
		return Nakshathra.ofIndex(girlNak);
	}


	public boolean hasHoroscope() {
		return hasHoroscope;
	}


	public String getTitle(){
		return "Compactibility Report of " + boyName + " and " + girlName;
	}

	public static void main(String[] args) {


		Horoscope b = new Horoscope("Elango", 17, 4, 1957, 7, 10,
				77 + (44.00 / 60.00), 11 + (22.00 / 60.00), 5.5, "Erode");

		Horoscope g = new Horoscope("Mani", 10, 8, 1960, 5, 30,
				77 + (44.00 / 60.00), 11 + (22.00 / 60.00), 5.5, "Erode");

		Compactibility c = new Compactibility(b, g);

		for (Kuta k : Kuta.values()) {

			System.out.println(k + " Kuta: " + c.kutas.get(k));
		}

		System.out.println("Total Kutas: " + c.totalKutaGained);

		System.out.println("\n Boy Dosha");
		for (Map.Entry e : c.boyDosha.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}

		System.out.println("\n Girl Dosha");
		for (Map.Entry e : c.girlDosha.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}


		}





}

