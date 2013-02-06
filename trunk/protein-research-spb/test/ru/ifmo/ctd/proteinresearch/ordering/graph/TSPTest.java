package ru.ifmo.ctd.proteinresearch.ordering.graph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Testing {@link GraphUtils#getPaths(Graph, int, int, int)}
 *
 * @author Maxim Buzdalov
 */
public class TSPTest {
    Graph g1;
    Graph g2;
    @Before
    public void init() throws IOException {
        g1 = GraphParser.parseMatrixGraphFromFile("resources\\table-1BTB.txt");
        g2 = GraphParser.parseMatrixGraphFromFile("resources\\table-2LJI.txt");
    }
    @Test
    public void simpleTSP() {
        Graph g = new MatrixGraph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 2);
        g.addEdge(0, 3, 2);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 1);

        Path p = GraphUtils.getPaths(g, 0, 3, 1)[0];
        Assert.assertEquals(3, p.cost, 1e-14);
        Assert.assertArrayEquals(new int[]{0, 1, 2, 3}, p.vertices);
    }
    @Test
    public void simpleTSPForFiles() {
        Path p = GraphUtils.getPaths(g1, 0, 19, 1)[0];
        System.out.print(p.cost);
    }
}
