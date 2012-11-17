package ru.ifmo.ctd.proteinresearch.ordering.graph;

import org.junit.*;

/**
 * Testing {@link GraphUtils#getPath(Graph, int, int)}
 *
 * @author Maxim Buzdalov
 */
public class TSPTest {
    @Test
    public void simpleTSP() {
        Graph g = new MatrixGraph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 2);
        g.addEdge(0, 3, 2);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 1);

        Path p = GraphUtils.getPath(g, 0, 3);
        Assert.assertEquals(3, p.cost, 1e-14);
        Assert.assertArrayEquals(new int[] {0, 1, 2, 3}, p.vertices);
    }
}
