import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.text.NumberFormatter;

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
		
		File folder = new File("2LJI_optim");
		String[] files = folder.list();

		PrintWriter timings = new PrintWriter("fullTreeTimings");
			
		
		for (int currentFile = 0; currentFile != files.length; ++currentFile) {
			System.out.println("Working on: " + files[currentFile]);
			Structure structure = fileReader.getStructure("2LJI_optim/"
					+ files[currentFile]);
			//Structure structure = fileReader.getStructure("2LJI_optim/2LJI_optim4_16.pdb");

			int length = structure.nrModels();
			int groupsCount = structure.getChain(0).getAtomLength();
			int count = 3 * groupsCount;

			PointsChain[] pointsChains = new PointsChain[length];

			for (int i = 0; i < length; ++i) {
				Point[] points = new Point[count];
				for (int j = 0; j < groupsCount; ++j) {
					double[] coords1 = structure.getModel(i).get(0)
							.getAtomGroup(j).getAtom(0).getCoords();
					double[] coords2 = structure.getModel(i).get(0)
							.getAtomGroup(j).getAtom(1).getCoords();
					double[] coords3 = structure.getModel(i).get(0)
							.getAtomGroup(j).getAtom(2).getCoords();
					points[3 * j] = new Point(coords1[0], coords1[1],
							coords1[2]);
					points[3 * j].inChainIndex = 3 * j;
					points[3 * j + 1] = new Point(coords2[0], coords2[1],
							coords2[2]);
					points[3 * j + 1].inChainIndex = 3 * j + 1;
					points[3 * j + 2] = new Point(coords3[0], coords3[1],
							coords3[2]);
					points[3 * j + 2].inChainIndex = 3 * j + 2;
				}
				pointsChains[i] = new PointsChain(points);
			}
			
			ChainSequence chains = new ChainSequence(pointsChains);
			
			long timeBefore = System.nanoTime();
			
//			CalcEqualRectangularCuboid calc = new CalcEqualRectangularCuboid(chains);
			CalcRectangularTree calc = new CalcRectangularTree(chains);
//			long timeBefore1 = System.nanoTime();
//			calc.Initalize(5,5,5);
				
			calc.initalize();
			long timeBefore2 = System.nanoTime();
						
//			CalcNaive calc = new CalcNaive(chains);
//			CalcMax calc = new CalcMax(chains);
			
			int result = calc.hasIntersections();

			long timeAfter = System.nanoTime();
			
			
			NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMANY);
			formatter.setMaximumFractionDigits(3);
			formatter.setMinimumFractionDigits(3);
			
			/*timings.print(files[currentFile] + "------");
			timings.print(formatter.format(((double)(timeBefore1-timeBefore))/1000000));
			timings.print(files[currentFile] + "------");
			timings.print(formatter.format(((double)(timeBefore2-timeBefore1))/1000000));
			timings.print(files[currentFile] + "------");
			*/
			timings.print(calc.functionCalculations+"---");
			timings.println(formatter.format(((double)(timeAfter-timeBefore))/1000000));
			
			if (result!=-1)
				System.out.println(files[currentFile] + " --- result: " + result);
			pw.print("yaaay! " + result);
		}
		pw.close();
		timings.close();
	}
	
}