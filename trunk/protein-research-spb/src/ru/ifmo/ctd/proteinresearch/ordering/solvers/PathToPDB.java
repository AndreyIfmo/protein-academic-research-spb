package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import ru.ifmo.ctd.proteinresearch.ordering.graph.*;

import java.io.*;

/**
 * @author Maxim Buzdalov
 */
public class PathToPDB {
    public static void buildPDB(String costFile, String archive, String pathExpression, String output, Path path) throws Exception {
        ConformationGraph cg = new ConformationGraph(costFile, archive, pathExpression);
        cg.getEdgeSourceTorsionAngleDiff(0, 1);
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

    public static void main(String[] args) throws Exception {
       /* buildPDB("table-1BTB.txt", "1BTB.zip", "1BTB/%02d-%02d/Result.pdb", "ResultLong.pdb", new Path(
                new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                           10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
                Double.NaN
        ));*/
     buildPDB("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb", "Result_optim.pdb", new Path(
                   new int[] {8, 19, 6, 13, 11, 2, 1, 9, 7, 14, 17, 0, 10, 15, 5, 3, 16, 12, 4, 18},
             Double.NaN
          ));
//        buildPDB("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb", "Result_optim2.pdb", new Path(
//                new int[] {8, 19, 13, 18},
//                Double.NaN
//        ));
    }
}
