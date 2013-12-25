package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
}
