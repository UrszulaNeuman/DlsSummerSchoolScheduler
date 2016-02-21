/*
 * Diamond User Administration System
 * Copyright � 2015 Diamond Light Source Ltd
 */

package uk.ac.diamond.ss.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facility {

	private static final Map<String, Facility> facilityByName = new HashMap<String, Facility>();

	public static Facility getOrCreate(String name, int aID) {
		Facility fac = facilityByName.get(name);
		if (fac == null) {
			fac = new Facility(name);
			fac.setID(aID);
			facilityByName.put(name, fac);
		}
		return fac;
	}

	public static Map<String, Facility> returnAll() {
		return facilityByName;
	}

	private final String name;
	private int id;
	private List<Correlation> correlations = new ArrayList<Correlation>();
	boolean longExperiment = false;

	public Facility(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		Facility f = (Facility) o;
		return f.getName().equals(name);
	}

	@Override
	public int hashCode() {
		return id;
	}

	private void setID(int aID) {
		id = aID;
	}

	public void addCorrelation(Correlation c) {
		if (!correlations.contains(c)) {
			correlations.add(c);
		}
	}

	public List<Correlation> getCorrelations() {
		return correlations;
	}

	public int checkCorrelations(Facility facility) {
		for (Correlation c : correlations) {
			if (!facility.equals(this) && c.getSetFacility().contains(facility)) {
				return c.getRate();
			}
		}
		return 0;
	}

	public void setLong() {
		longExperiment  = true;
	}
}
