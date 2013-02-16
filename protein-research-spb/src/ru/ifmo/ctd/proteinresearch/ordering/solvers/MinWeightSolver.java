package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import ru.ifmo.ctd.proteinresearch.ordering.graph.*;

import java.io.*;
import java.util.*;

/**
 * @author Maxim Buzdalov
 */
public class MinWeightSolver {
    static boolean isConnected(Graph g) {
        int[] col = new int[g.getN()];
        col[0] = 1;
        while (true) {
            boolean has1 = false;
            for (int i = 0; i < g.getN(); ++i) {
                if (col[i] == 1) {
                    has1 = true;
                    col[i] = 2;
                    for (int j = 0; j < g.getN(); ++j) {
                        if (g.hasEdge(i, j) && col[j] == 0) {
                            col[j] = 1;
                        }
                    }
                }
            }
            if (!has1) {
                for (int i1 = 0; i1 < col.length; i1++) {
                    if (i1 == 8) {
                        continue;
                    }
                    int i = col[i1];
                    if (i == 0) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    static Graph filter(Graph g, double weight) {
        Graph rv = new MatrixGraph(g.getN());
        for (int i = 0; i < g.getN(); ++i) {
            for (int j = 0; j < g.getN(); ++j) {
                if (g.hasEdge(i, j) && g.getEdgeWeight(i, j) <= weight) {
                    rv.addEdge(i, j, g.getEdgeWeight(i, j));
                }
            }
        }
        return rv;
    }

    public static void main(String[] args) throws IOException {
        Graph g = GraphParser.parseMatrixGraphFromFile("resources/table-2LJI.txt");

        for (int i = 0; i < g.getN(); ++i) {
            for (int j = 0; j < g.getN(); ++j) {
                for (int k = 0; k < g.getN(); ++k) {
                    if (i == j || i == k || j == k) continue;
                    if (g.hasEdge(i, j) && g.hasEdge(j, k) && g.hasEdge(i, k) && g.getEdgeWeight(i, k) > g.getEdgeWeight(i, j) + g.getEdgeWeight(j, k)) {
                        System.out.println("Non-triangle");
                        System.out.printf("    %d -> %d: %f%n", i, j, g.getEdgeWeight(i, j));
                        System.out.printf("    %d -> %d: %f%n", j, k, g.getEdgeWeight(j, k));
                        System.out.printf("    %d -> %d: %f%n", i, k, g.getEdgeWeight(i, k));
                    }
                }
            }
        }

        Set<Double> weights = new TreeSet<>();
        for (int i = 1; i < g.getN(); ++i) {
            for (int j = 0; j < i; ++j) {
                if (g.hasEdge(i, j)) {
                    weights.add(g.getEdgeWeight(i, j));
                }
                if (g.hasEdge(j, i)) {
                    weights.add(g.getEdgeWeight(j, i));
                }
            }
        }
        for (double max : weights) {
            Graph filtered = filter(g, max);
            if (isConnected(filtered)) {
                System.err.println("Max weight: " + max);
                for (int i = 0; i < filtered.getN(); ++i) {
                    for (Edge e : filtered.edges(i)) {
                        if (e.from != e.to) {
                            System.out.println(e.from + " " + e.to + " " + e.weight);
                        }
                    }
                }
                break;
            }
        }
    }
}
