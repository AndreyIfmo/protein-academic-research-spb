package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import ru.ifmo.ctd.proteinresearch.ordering.graph.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @author Maxim Buzdalov
 */
public class PathUtil {
    public static void buildPDB(String costFile, String archive, String pathExpression, String output, Path path) throws Exception {
        ConformationGraph cg = new ConformationGraph(costFile, archive, pathExpression);
        try (PrintWriter out = new PrintWriter(output)) {
            out.println(cg.forPath(path));
        }
        for (int i = 0; i < cg.graph.getN(); ++i) {
            for (int j = i + 1; j < cg.graph.getN(); ++j) {
                try {
                    double w0 = cg.graph.getEdgeWeight(i, j);
                    double w1 = cg.getChain(i, j).weight();
                    if (Math.abs(w0 - w1) / Math.max(w0, w1) > 0.1) {
                        System.out.println(i + " -> " + j + ": orig = " + w0 + ", new = " + w1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public Chain getInterpolatedChain(Chain from, Chain to, double k ) {
        getAtoms(from);
        return null;
    }

    public static List<Atom> getAtoms(Chain from) {
        Chain interpolatedChain = new ChainImpl();
        List<Group> atomGroups1 = from.getAtomGroups();

        List<Atom> atoms = new ArrayList<>();
        for (Group group:atomGroups1) {
            atoms.addAll(group.getAtoms());
        }
        return atoms;
    }

    public static List<Double> getTorsionAngles(List<Atom> atoms) throws StructureException {
        int length = atoms.size();
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < atoms.size()-3; i++) {
            list.add(Calc.torsionAngle(atoms.get(i), atoms.get(i + 1), atoms.get(i + 2), atoms.get(i + 3)));
        }
        return list;
    }


    public static void main(String[] args) throws Exception {
       /* buildPDB("table-1BTB.txt", "1BTB.zip", "1BTB/%02d-%02d/Result.pdb", "ResultLong.pdb", new Path(
                new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                           10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
                Double.NaN
        ));*/
        ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        ConformationChain cc = cg.getChain(0,1);
        System.out.print(Arrays.toString(getTorsionAngles(getAtoms(cc.getStructure().getChain(0))).toArray()));

        buildPDB("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb", "Result_optim.pdb", new Path(
                new int[]{0, 9, 15, 17, 8, 2, 17, 10, 11, 7, 14},
                Double.NaN
        ));
//        buildPDB("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb", "Result_optim2.pdb", new Path(
//                new int[] {8, 19, 13, 18},
//                Double.NaN
//        ));
    }
}
