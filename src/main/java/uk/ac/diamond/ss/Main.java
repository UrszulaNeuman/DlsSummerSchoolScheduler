package uk.ac.diamond.ss;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.optaplanner.core.api.solver.Solver;

import uk.ac.diamond.ss.domain.CorrelationManager;
import uk.ac.diamond.ss.domain.Person;
import uk.ac.diamond.ss.domain.Shift;
import uk.ac.diamond.ss.domain.in.CorrelationReader;
import uk.ac.diamond.ss.domain.in.KeyValuesReader;
import uk.ac.diamond.ss.domain.in.PersonReader;
import uk.ac.diamond.ss.domain.in.ShiftReader;
import uk.ac.diamond.ss.domain.in.WeightsReader;
import uk.ac.diamond.ss.domain.out.ScorePrinter;
import uk.ac.diamond.ss.domain.out.SolutionWriter;
import uk.ac.diamond.ss.domain.out.SummaryWriter;

public class Main {

	public static void main(String[] args) throws Throwable {
		String filename = "test.xlsx";// problem

		if (args.length > 0) {
			filename = args[0];
		}

		String filaname_copy = "solution_"+filename;

		InputStream inputStream = new FileInputStream(filename);

		Workbook wb = WorkbookFactory.create(inputStream);


		PersonReader pr = new PersonReader(wb.getSheet("candidate_preferences"));

		ShiftReader sr = new ShiftReader(wb.getSheet("periods"));

		CorrelationReader cr = new CorrelationReader(wb.getSheet("correlation"));

		(new KeyValuesReader(wb.getSheet("key_values"))).read();

		(new WeightsReader(wb.getSheet("weights"))).read();

		PlannerEngine pe = new PlannerEngine();
		Solver solver = pe.getSolver();

		PlannerSolution prob = new PlannerSolution();
		java.util.List<Person> people = pr.read();
		prob.setPeople(people);
		List<Shift> shifts = sr.read();
		new CorrelationManager().assign(cr.read());
		prob.setShifts(shifts);
		prob.setAllocations();

		System.out.println("Solver started!");
		solver.solve(prob);

		PlannerSolution ps = (PlannerSolution) solver.getBestSolution();


		Sheet solutionSheet = null;
		if (wb.getSheet("solution") == null) {
			solutionSheet = wb.createSheet("solution");
		}
		solutionSheet = wb.getSheet("solution");

		Sheet summarySheet = null;
		if (wb.getSheet("summary") == null) {
			summarySheet = wb.createSheet("summary");
		}
		summarySheet = wb.getSheet("summary");

		CellStyle style = wb.createCellStyle();

		Font font = wb.createFont();
		font.setColor(IndexedColors.GREEN.getIndex());
		style.setFont(font);

		new SolutionWriter(solutionSheet, people).write(ps, style);

		new SummaryWriter(summarySheet, people, shifts).writeAvg(ps);
		wb.write(new FileOutputStream(filaname_copy));

		new ScorePrinter(solver).print(ps);
	}
}