/**
 * Internalization.java
 * Created On 2005, Dec 21, 2005 3:13:24 PM
 * @author E. Rajasekar
 */

package app.astrosoft.util;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import app.astrosoft.consts.DisplayFormat;
import app.astrosoft.consts.Language;
import app.astrosoft.exception.AstrosoftException;
import app.astrosoft.pref.AstrosoftPref;


public class Internalization  {

	private static final Logger log = Logger.getLogger(Internalization.class.getName());
	
	public static String bundleName = "resources.AstrosoftBundle";

	private static Map<Language,ResourceBundle> bundles;

	static {
		//setPreferedLanguate();
		loadBundles();
		/*AstroSoft.getPreferences().addPreferenceChangeListener(
				new PreferenceChangeListener() {

					public void preferenceChange(PreferenceChangeEvent evt) {
						if (evt.getKey().equals(
								AstrosoftPref.Preference.Language.name())) {
							setLanguage(Enum.valueOf(Language.class, evt
									.getNewValue()));
						}
					}
				});*/
	}

	private static void loadBundles(){
		
		bundles = new EnumMap<Language,ResourceBundle>(Language.class);
		
		for (Language l : Language.values()){
			
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName, new Locale(l.isoCode()));
			bundles.put(l,bundle);
		}
	}


	/*private static void setPreferedLanguate() {
		setLanguage(AstroSoft.getPreferences().getLanguage());
	}

	public static void setLanguage(Language language) {
		
		log.info("Language changed to " + language);
		Locale locale = new Locale(language.isoCode());
		bundle = ResourceBundle.getBundle(bundleName, locale);
		
	}*/

	/*public static void useLanguage(Language language, CallBack caller) {
		setLanguage(language);
		caller.call();
		setPreferedLanguate();
	}*/

	public static String getString(Language language, String key) {

		ResourceBundle bundle = bundles.get(language);
		log.finer("Bundle : " + bundle.getLocale() + " Key : " + key);
		boolean langNotEnglish = !language.isEnglish();

		if (langNotEnglish) {
			throw new AstrosoftException("Unsupported Language:  " + language);
		}


		return bundle.getString(key);
	}

	public static String getString(String key) {
		/*String str = "";
		setLanguage(language);
		str = getString(key);
		setPreferedLanguate();*/
		
		//setLanguage(language);
		return  getString(AstrosoftPref.get().getLanguage(), key);
		//setPreferedLanguate(Ast);
		//return str;
	}

	public static String getString(Language language, Enum string){
		return getString(language, string.name());
	}
	
	public static String getString(DisplayFormat format, String... args) {

		switch (format) {
			case FULL_NAME:
			default:
				return getString(args[0]);
	
			case SYMBOL:
				return getString(args[1]);
		}
	}
}
