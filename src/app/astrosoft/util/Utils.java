/**
 * Utils.java
 * Created On 2006, Feb 25, 2006 6:19:01 PM
 * @author E. Rajasekar
 */

package app.astrosoft.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {

	public static <K,V extends Comparable<V>> List<ComparableEntry<K,V>> sortMap(Set<Map.Entry<K,V>> entrySet, boolean isDesc, Comparator<ComparableEntry> comparator){
		
		List<ComparableEntry<K,V>> entries = new ArrayList<ComparableEntry<K,V>>();
		
		for (Map.Entry<K,V> entry : entrySet) {
			entries.add(new ComparableEntry<K,V>(entry));
		}
	
		return sortEntryList(entries, isDesc, comparator);
		
	}
	
	public static <K,V extends Comparable<V>> List<ComparableEntry<K,V>> sortMap(Set<Map.Entry<K,V>> entrySet){
		return sortMap(entrySet,false,null);
	}
	
	public static <K,V extends Comparable<V>> List<ComparableEntry<K,V>> sortMap(Set<Map.Entry<K,V>> entrySet, boolean isDesc){
		return sortMap(entrySet,isDesc,null);
	}
	
	public static <K,V extends Comparable<V>> List<ComparableEntry<K,V>> sortEntryList(List<ComparableEntry<K,V>> entries, boolean isDesc, Comparator<ComparableEntry> comparator){

		if (comparator == null){
			if (isDesc){
				Collections.sort(entries, Collections.reverseOrder());
			}else{
				Collections.sort(entries);
			}
		}else{
			Collections.sort(entries, comparator);
		}
		
		return entries;
	}
	
	public static <K,V extends Comparable<V>> List<ComparableEntry<K,V>> sortEntryList(List<ComparableEntry<K,V>> entries, boolean isDesc){
		return sortEntryList(entries, isDesc, null);
	}

}
