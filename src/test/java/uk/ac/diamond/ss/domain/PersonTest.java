package uk.ac.diamond.ss.domain;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import uk.ac.diamond.ss.domain.in.KeyValuesReader;
import uk.ac.diamond.ss.domain.in.WeightsReader;

public class PersonTest {
	private Map<Facility, Integer> preferences = new HashMap<Facility, Integer>();
	Shift sh, sh1, sh2;
	Person p;

	@Test
	public void checkPreferenceTest() {
		createPreferences();

		assertEquals(p.checkPreference(sh), 3);
		assertEquals(p.checkPreference(sh1), 0);
		Shift sh2 = new Shift(null, 3);
		assertEquals(p.checkPreference(sh2), 0);
	}

	@Test
	public void isPreferenceTest() {
		createPreferences();
		assertFalse(p.isPreference(sh1));
		assertTrue(p.isPreference(sh));
	}
	@Test
	public void isFirstPreference() {
		createPreferences();
		assertTrue(p.isFirstPreference(sh2));
		assertFalse(p.isFirstPreference(sh));
	}

	@Test
	public void sumPreferenceTest() {
		createPreferences();
		assertEquals(p.getSumPreference(), 15);
	}
	@Test
	public void preferenceIncludeTest() {
		createPreferences();
		assertFalse(p.preferencesInclude(0));
		assertFalse(p.preferencesInclude(1));
		assertTrue(p.preferencesInclude(2));
	}
	@Test
	public void mapPreferencesTest() {
		createPreferences();
		assertEquals(Person.mapPreference(0), 0);
		assertEquals(Person.mapPreference(KeyValuesReader.MAX_PREFERENCES),1);
		assertEquals(Person.mapPreference(10),
				KeyValuesReader.MAX_PREFERENCES + 1 - 10);
		assertEquals(Person.mapPreference(4), WeightsReader.PREFERENCE2 * 2);
	}

	private void createPreferences() {
		Facility frac1 = Facility.getOrCreate("A1", 101);
		Facility frac2 = Facility.getOrCreate("A2", 102);
		Facility frac3 = Facility.getOrCreate("A3", 103);
		frac3.setLong();
		Facility frac4 = Facility.getOrCreate("A4", 104);
		preferences.put(frac1, 3);
		preferences.put(frac2, 2);
		preferences.put(frac3, 5);
		sh = new Shift(frac1, 0, 0, 1);
		sh1 = new Shift(frac4, 1, 0, 1);
		sh2 = new Shift(frac3, 2, 0, 1);
		p = new Person("Aureliusz",55);
		p.setPereferences(preferences);
	}

}
