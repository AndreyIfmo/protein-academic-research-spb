package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;

import ru.ifmo.ctd.proteinresearch.intersections.*;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;


public class IntersectionUtils {

    private final static PDBFileReader fileReader = new PDBFileReader();

    public static void main(String[] args) throws Exception {
        run();
    }

    private static void run() throws Exception {
        PrintWriter pw = new PrintWriter("output.txt");

        File folder = new File("2LJI_optim");

        String[] files = folder.list();

        PrintWriter timings = new PrintWriter("fullCalcTreeTimingsNew");
        PrintWriter intersections = new PrintWriter("intersections");
        File dir = new File("2LJI_optim_filtered");
        dir.mkdir();
        for (int currentFile = 0; currentFile != files.length; ++currentFile) {
            System.out.println("Working on: " + files[currentFile]);
            int result = checkFile(files[currentFile]);

            // timings.print(functionCalculations + "---");
            // timings.println(formatter.format(((double) (timeAfter - timeBefore)) / 1000000));

            // intersections.println(result);

            if (result != -1) {
                System.out.println(files[currentFile] + " --- result: " + result);
            }
        }
        pw.close();
        timings.close();
        intersections.close();
    }

    public static int checkFile(String file) throws Exception {
        Structure structure = fileReader.getStructure(file);

        int length = structure.nrModels();
        int groupsCount = structure.getChain(0).getAtomLength();
        int count = 3 * groupsCount;

        PointsChain[] pointsChains = new PointsChain[length];

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

        long timeBefore = System.nanoTime();


        CalcRectangularTreeNew calc;
        int result = 0;
        int functionCalculations = 0;
        for (int i = 1; i < length; i++) {
            PointsChain[] newChains = new PointsChain[2];
            newChains[0] = pointsChains[i - 1];
            newChains[1] = pointsChains[i];
            calc = new CalcRectangularTreeNew(new ChainSequence(newChains));
            calc.initalize();
            result += calc.hasIntersections();
            System.err.print(result + " ");

            functionCalculations += calc.functionCalculations;
        }

        long timeAfter = System.nanoTime();

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMANY);
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(3);
        return result;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}