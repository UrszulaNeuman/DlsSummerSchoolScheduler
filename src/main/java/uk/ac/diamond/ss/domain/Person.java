/*
 * Diamond User Administration System
 * Copyright � 2015 Diamond Light Source Ltd
 */

package uk.ac.diamond.ss.domain;

import java.util.HashMap;
import java.util.Map;

import uk.ac.diamond.ss.domain.in.KeyValuesReader;
import uk.ac.diamond.ss.domain.in.WeightsReader;

public class Person {

	private String name;
	private final int ID;
	private Map<Facility, Integer> preferences = new HashMap<Facility, Integer>();

	public Person(String name, int id) {
		this.name = name;
		this.ID = id;
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public Map<Facility, Integer> getPreferences() {
		return preferences;
	}

	public void setPereferences(Map<Facility, Integer> pref) {
		preferences = pref;
	}

	public int checkPreference(Shift s) {
		Facility k = s.getFacility();
		if (preferences.containsKey(k)) {
			int pref = preferences.get(k);
			return pref;
		}
		return 0;
	}

	public boolean isPreference(Shift s) {
		return (checkPreference(s) > 0 ? true: false);
	}

	public boolean isFirstPreference(Shift s) {
			return checkPreference(s)  == KeyValuesReader.MAX_PREFERENCES;
	}

	public boolean isFirstOrSecondPreference(Shift s) {
		if(WeightsReader.FIRST_SECOND == 0) {
			return true;
		}
		if(preferencesInclude(KeyValuesReader.MAX_PREFERENCES)||preferencesInclude(KeyValuesReader.MAX_PREFERENCES-1)){
			return checkPreference(s)  == KeyValuesReader.MAX_PREFERENCES||checkPreference(s)  == KeyValuesReader.MAX_PREFERENCES-1;
		}
		return true;
	}

	public int getSumPreference() {
		int sum = 0;
		for(Facility f: preferences.keySet()){
			int value = preferences.get(f).intValue();
			sum = sum + value;
			if(f.longExperiment){
				sum = sum + value;
				//long experiments twice
			}
		}
		return sum;
	}

	public boolean preferencesInclude(int num) {
		if(preferences.values().contains(num)){
				return true;
		}
		return false;
	}

	public static int mapPreference(int pref) {
		// invert
		int mappedPreference = 0;
		if (pref != 0) {
			mappedPreference = 1 + KeyValuesReader.MAX_PREFERENCES - pref;
		}
		// scale
		if (mappedPreference == KeyValuesReader.MAX_PREFERENCES) {
			mappedPreference = WeightsReader.PREFERENCE1 * mappedPreference;
		}
		if (mappedPreference == KeyValuesReader.MAX_PREFERENCES - 1) {
			mappedPreference = WeightsReader.PREFERENCE2 * mappedPreference;
		}
		if (mappedPreference == KeyValuesReader.MAX_PREFERENCES - 2) {
			mappedPreference = WeightsReader.PREFERENCE3 * mappedPreference;
		}
		if (mappedPreference == KeyValuesReader.MAX_PREFERENCES - 3) {
			mappedPreference = WeightsReader.PREFERENCE4 * mappedPreference;
		}
		if (mappedPreference == KeyValuesReader.MAX_PREFERENCES - 4) {
			mappedPreference = WeightsReader.PREFERENCE5 * mappedPreference;
		}
		return mappedPreference;
	}

}
