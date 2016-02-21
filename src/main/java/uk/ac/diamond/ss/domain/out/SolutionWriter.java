package uk.ac.diamond.ss.domain.out;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import uk.ac.diamond.ss.PlannerSolution;
import uk.ac.diamond.ss.domain.Allocation;
import uk.ac.diamond.ss.domain.Person;
import uk.ac.diamond.ss.domain.in.KeyValuesReader;

public class SolutionWriter {
	private Sheet sheet;
	private List<Person> people;
	private List<Allocation> allocations = new ArrayList<Allocation>();

	public SolutionWriter(Sheet sheet, List<Person> people) {
		this.people = people;
		this.sheet = sheet;
	}

	public SolutionWriter() {// tests only
	}

	public void setAllocations(List<Allocation> alo) {
		allocations = alo;
	}

	public void write(PlannerSolution solution, CellStyle style) {

		allocations = solution.getAllocations();
		Row headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("Person");
		headerRow.createCell(1).setCellValue("Period1");
		headerRow.createCell(2).setCellValue("Period2");
		headerRow.createCell(3).setCellValue("Period3");
		headerRow.createCell(4).setCellValue("Period4");
		headerRow.createCell(5).setCellValue("Period5");

		int i = 1;

		for (Person p : people) {
			Row row = sheet.createRow(i);

			Cell c1 = row.createCell(0);
			c1.setCellValue(p.getName());

			Cell a0 = row.createCell(1);
			a0.setCellValue(findPeriod(p, 0));
			if (checkPreference(p, 0)) {
				a0.setCellStyle(style);
			}

			Cell a1 = row.createCell(2);
			a1.setCellValue(findPeriod(p, 1));
			if (checkPreference(p, 1)) {
				a1.setCellStyle(style);
			}

			Cell a2 = row.createCell(3);
			a2.setCellValue(findPeriod(p, 2));
			if (checkPreference(p, 2)) {
				a2.setCellStyle(style);
			}

			Cell a3 = row.createCell(4);
			a3.setCellValue(findPeriod(p, 3));
			if (checkPreference(p, 3)) {
				a3.setCellStyle(style);
			}
			
			Cell a4 = row.createCell(5);
			a4.setCellValue(findPeriod(p, 4));
			if (checkPreference(p, 4)) {
				a4.setCellStyle(style);
			}

			i = i + 1;
		}
	}

	public String findPeriod(Person p, int num) {
		for (Allocation a : allocations) {
			if (a.getPerson().getID() == p.getID()
					&& a.getShift().getStartTime() == num
							* KeyValuesReader.SHIFTS_LENGHT) {
				return a.getShift().getFacility().getName();
			}
		}
		return null;
	}

	public boolean checkPreference(Person p, int num) {
		for (Allocation a : allocations) {
			if (a.getPerson().getID() == p.getID()
					&& a.getShift().getStartTime() == num
							* KeyValuesReader.SHIFTS_LENGHT
					&& p.isPreference(a.getShift())) {
				return true;
			}
		}
		return false;
	}

}
