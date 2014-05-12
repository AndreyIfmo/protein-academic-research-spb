package ru.ifmo.ctd.proteinresearch.ordering.graph;

import weka.core.pmml.Array;

import java.io.*;
import java.util.*;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 15.11.12
 *         Time: 4:02
 */

public class GraphUtils {
    public static boolean isMetric(Graph g) {
        for (int i = 0; i < g.getN(); i++) {
            for (int j = 0; j < g.getN(); j++) {
                for (int k = 0; k < g.getN(); k++) {
                    if (!(isTriangle(g, i, j, k) && isSymmetric(g, i, j, k))) {
                        System.err.print(i + " " + j + " " + k + " " + isTriangle(g, i, j, k) + " " + g.getEdgeWeight(i, k) + " " + g.getEdgeWeight(j, i) + " " + g.getEdgeWeight(k, j));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean isTriangle(Graph g, int i, int j, int k) {
        return (g.getEdgeWeight(i, j) + g.getEdgeWeight(j, k) >= g.getEdgeWeight(i, k))
                && (g.getEdgeWeight(j, k) + g.getEdgeWeight(k, i) >= g.getEdgeWeight(j, i))
                && (g.getEdgeWeight(k, i) + g.getEdgeWeight(i, j) >= g.getEdgeWeight(k, j));
    }

    private static boolean isSymmetric(Graph g, int i, int j, int k) {
        return (g.getEdgeWeight(i, j) == g.getEdgeWeight(j, i)) && (g.getEdgeWeight(k, j) == g.getEdgeWeight(j, k)) && (g.getEdgeWeight(i, k) == g.getEdgeWeight(k, i));
    }

    private static void sqrt(File matrixFile) throws IOException {
        double[][] matrix = GraphParser.parseGraphMatrix(matrixFile.getPath());
        assert matrix.length == matrix[0].length;

        File outputFile = new File(matrixFile.getParentFile().getPath() + "/sqrt__" + matrixFile.getName());
        PrintWriter pw = new PrintWriter(outputFile);
        pw.println(matrix.length);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                pw.print(Math.sqrt(matrix[i][j]) + " ");
            }
            pw.println();
        }
        pw.close();
    }

    public static void main(String[] args) throws IOException {
        sqrt(new File("2LXG/matrix.txt"));
    }
}
