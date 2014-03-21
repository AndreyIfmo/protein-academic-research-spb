package ru.ifmo.ctd.proteinresearch.ordering.gui;

import junit.framework.TestCase;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.MatrixGraph;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by AndreyS on 21.03.14.
 */
public class GraphVizUtilsTest extends TestCase {
    private static final double INF = Double.POSITIVE_INFINITY;
    public void testGraphToDotFile() throws Exception {

        Graph g = new MatrixGraph(4, new double[][] {
                {INF, 1, 1, 9},
                {1, INF, 7, 8},
                {1, 7, INF, 1},
                {9, 8, 1, INF}
        });
        String fileName = "GraphVizUtils1";
        File file = new File(fileName);
        GraphVizUtils.graphToDotFile(g, fileName);
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        assertEquals("graph g {", lines.get(0));
        assertEquals("0 -- 1 [len=1.0];", lines.get(1));
        file.delete();
    }
}
