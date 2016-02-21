/*
 * Diamond User Administration System
 * Copyright © 2015 Diamond Light Source Ltd
 */

package uk.ac.diamond.ss.out;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import uk.ac.diamond.ss.domain.Allocation;
import uk.ac.diamond.ss.domain.Facility;
import uk.ac.diamond.ss.domain.Person;
import uk.ac.diamond.ss.domain.Shift;
import uk.ac.diamond.ss.domain.in.KeyValuesReader;
import uk.ac.diamond.ss.domain.out.SolutionWriter;

public class SolutionWriterTest {

	private Person p = new Person("Ola",57);

	@Test
	public void findPeriodTest() {

		SolutionWriter sw = new SolutionWriter();
		sw.setAllocations(createAllocations());
		String test = sw.findPeriod(p, 0);
		String test1 = sw.findPeriod(p, 1);
		assertEquals(test, "I11");
		assertEquals(test1, "I06");
	}

	@Test
	public void checkPreferenceTest() {
		SolutionWriter sw = new SolutionWriter();
		sw.setAllocations(createAllocations());
		assertTrue(sw.checkPreference(p, 0));
		assertFalse(sw.checkPreference(p, 2));
	}

	private List<Allocation> createAllocations() {

		List<Allocation> alo = new ArrayList<Allocation>();
		Facility frac1 = Facility.getOrCreate("I11", 101);
		Facility frac2 = Facility.getOrCreate("I06", 102);
		Shift sh1 = new Shift(frac1, 0, 0, 1 * KeyValuesReader.SHIFTS_LENGHT);
		Shift sh2 = new Shift(frac2, 1, 1 * KeyValuesReader.SHIFTS_LENGHT,
				2 * KeyValuesReader.SHIFTS_LENGHT);
		// set person p for tests
		Map<Facility, Integer> preferences = new HashMap<Facility, Integer>();
		preferences.put(frac1, 2);// preference towards I11
		p.setPereferences(preferences);
		// create allocations
		Allocation allocation = new Allocation();
		allocation.setPerson(p);
		allocation.setID(0);
		allocation.setShift(sh1);
		alo.add(allocation);

		Allocation allocation1 = new Allocation();
		allocation1.setPerson(p);
		allocation1.setID(1);
		allocation1.setShift(sh2);
		alo.add(allocation1);
		return alo;
	}

}
