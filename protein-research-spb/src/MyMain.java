import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.text.NumberFormatter;

import org.biojava.bio.*;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureImpl;
import org.biojava.bio.structure.io.PDBFileReader;

import ru.ifmo.ctd.proteinresearch.intersections.*;

/**
 * @author Smetannikov
 * 
 */
public class MyMain {

	private Structure storage = new StructureImpl();
	private final static PDBFileReader fileReader = new PDBFileReader();

	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter("output.txt");

		File preWorkingFolder = new File("1Y8B/1Y8B");

		String[] workingFolders = preWorkingFolder.list();

		PrintWriter timings = new PrintWriter("fullCalcTreeTimingsNew");
		PrintWriter iterations = new PrintWriter("fullCalcTreeIterationsNew");

		for (int qwe = 0; qwe != workingFolders.length; qwe++) {
			File folder = new File(preWorkingFolder + "/" + workingFolders[qwe]);
			String[] files = null;
			if (folder.isDirectory()) {
				files = folder.list();
			}else{
				continue;
			}

			// PrintWriter timings = new PrintWriter("fullCalcTreeTimingsNew");
			PrintWriter intersections = new PrintWriter("intersections");

			for (int currentFile = 0; currentFile < files.length; ++currentFile) {

				System.out.println("Working on: " + files[currentFile]);

				int length;
				int groupsCount;
				int count;

				PointsChain[] pointsChains;

				try {
					Structure structure = fileReader.getStructure(preWorkingFolder + "/" + workingFolders[qwe] + "/" + files[currentFile]);
					// Structure structure =
					// fileReader.getStructure("2LJI_optim/2LJI_optim9_13.pdb");

					length = structure.nrModels();
					groupsCount = structure.getChain(0).getAtomLength();
					count = 3 * groupsCount;

					pointsChains = new PointsChain[length];

					for (int i = 0; i < length; ++i) {
						Point[] points = new Point[count];
						for (int j = 0; j < groupsCount; ++j) {
							double[] coords1 = structure.getModel(i).get(0).getAtomGroup(j).getAtom(0).getCoords();
							double[] coords2 = structure.getModel(i).get(0).getAtomGroup(j).getAtom(1).getCoords();
							double[] coords3 = structure.getModel(i).get(0).getAtomGroup(j).getAtom(2).getCoords();
							points[3 * j] = new Point(coords1[0], coords1[1], coords1[2]);
							points[3 * j].inChainIndex = 3 * j;
							points[3 * j + 1] = new Point(coords2[0], coords2[1], coords2[2]);
							points[3 * j + 1].inChainIndex = 3 * j + 1;
							points[3 * j + 2] = new Point(coords3[0], coords3[1], coords3[2]);
							points[3 * j + 2].inChainIndex = 3 * j + 2;
						}
						pointsChains[i] = new PointsChain(points);
					}
				} catch (Exception e) {
					System.err.print(" corrupted pdb");
					continue;
				}

				ChainSequence chains = new ChainSequence(pointsChains);

				long timeBefore = System.nanoTime();

				// CalcEqualRectangularCuboid calc = new
				// CalcEqualRectangularCuboid(chains);
				// CalcRectangularTree calc = new CalcRectangularTree(chains);
				// calc.initalize(); 
				// int result = calc.hasIntersections();
				// int functionCalculations = calc.functionCalculations;

//				CalcRectangularTreeNew calc;
//				CalcNaive calc;
//				 CalcMax calc;
				 CalcEqualRectangularCuboidNew calc;
				int result = 0;
				int functionCalculations = 0;
				for (int i = 1; i < length; i++) {
					PointsChain[] newChains = new PointsChain[2];
					newChains[0] = pointsChains[i - 1];
					newChains[1] = pointsChains[i];
//					calc = new CalcRectangularTreeNew(new ChainSequence(newChains));
//					 calc = new CalcMax(new ChainSequence(newChains));
//					calc = new CalcNaive(new ChainSequence(newChains));
					 calc = new CalcEqualRectangularCuboidNew(new
					 ChainSequence(newChains) );
//					calc.initalize();
					 calc.Initalize(5,5,5);

					result += calc.hasIntersections();
					System.err.print(result + " "); 
					functionCalculations += calc.functionCalculations;
				}

				// long timeBefore1 = System.nanoTime();
				// calc.Initalize(5,5,5);
				long timeBefore2 = System.nanoTime();

				//
				//
				// int result = calc.hasIntersections();
				// int functionCalculations = calc.functionCalculations;

				long timeAfter = System.nanoTime();

				NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMAN);
				formatter.setMaximumFractionDigits(3);
				formatter.setMinimumFractionDigits(3);
				formatter.setGroupingUsed(false);

				/*
				 * timings.print(files[currentFile] + "------");
				 * timings.print(formatter
				 * .format(((double)(timeBefore1-timeBefore))/1000000));
				 * timings.print(files[currentFile] + "------");
				 * timings.print(formatter
				 * .format(((double)(timeBefore2-timeBefore1))/1000000));
				 * timings.print(files[currentFile] + "------");
				 */
				iterations.println(workingFolders[qwe] + " " + functionCalculations + " " + formatter.format(((double) (timeAfter - timeBefore)) / 1000000));
				// timings.println();

				intersections.println(result);

				System.out.print(workingFolders[qwe]);

				// if (result != -1)
				// System.out.println(files[currentFile] + " --- result: " +
				// result);

				pw.print("yaaay! " + result);
			}
			pw.close();

			intersections.close();
		}
		iterations.close();
		timings.close();
	}
}