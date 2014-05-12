package ru.ifmo.ctd.proteinresearch.ordering.gui;

import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by AndreyS on 21.03.14.
 */
public class GraphVizUtils {
    public static void graphToDotFile(Graph graph, String fileName) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(fileName);
        pw.println("graph g {");
        int n = graph.getN();
        if (n > 1) {
            checkGraphIsSymmetric(graph, n);
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (graph.hasEdge(i, j)) {
                        pw.println(i + " -- " + j + " [len=" + graph.getEdgeWeight(i, j) + "];");
                    }
                }
            }
        }
        pw.println("}");
        pw.close();

    }

    private static void checkGraphIsSymmetric(Graph graph, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (graph.hasEdge(i, j)) {
                    assert graph.hasEdge(j, i);
                    assert graph.getEdgeWeight(j, i) == graph.getEdgeWeight(i, j);
                }
            }
        }
    }

}
