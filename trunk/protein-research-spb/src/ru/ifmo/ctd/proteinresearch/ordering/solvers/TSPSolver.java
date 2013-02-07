package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import ru.ifmo.ctd.proteinresearch.ordering.graph.*;

import java.io.IOException;
import java.util.*;

/**
 */
public class TSPSolver {
    public static void main(String[] args) throws IOException {
        Graph g2 = GraphParser.parseMatrixGraphFromFile("resources/table-2LJI.txt");

        double[] bestCost = new double[4];
        int[] bestSource = new int[4];
        int[] bestTarget = new int[4];
        Path[][] bestPaths = new Path[4][];

        Arrays.fill(bestCost, Double.POSITIVE_INFINITY);

        for (int source = 0; source < g2.getN(); ++source) {
            ExactMinPathCoverage cov = new ExactMinPathCoverage(g2, source);
            for (int target = source + 1; target < g2.getN(); ++target) {
                System.out.println("Computing source = " + source + ", target = " + target);
                for (int pathCount = 0; pathCount < 4; ++pathCount) {
                    Path[] paths = cov.getPaths(pathCount + 1, target);
                    double sum = 0;
                    for (Path p : paths) {
                        sum += p.cost;
                    }
                    if (bestCost[pathCount] > sum) {
                        bestCost[pathCount] = sum;
                        bestSource[pathCount] = source;
                        bestTarget[pathCount] = target;
                        bestPaths[pathCount] = paths;
                    }
                }
            }
        }

        for (int pathCount = 0; pathCount < 4; ++pathCount) {
            System.out.println("For " + (pathCount + 1) + " paths:");
            System.out.println("   Best cost: " + bestCost[pathCount]);
            System.out.println("   " + bestSource[pathCount] + " ==> " + bestTarget[pathCount]);
            for (Path p : bestPaths[pathCount]) {
                System.out.println("   " + p);
            }
        }
    }
}
