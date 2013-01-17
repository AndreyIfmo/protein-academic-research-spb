package ru.ifmo.ctd.proteinresearch.ordering.graph;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 17.01.13
 *         Time: 19:31
 */
public class TestMatrixGraph {
    public Graph graph;
    @Before
    public void constructGraph() throws IOException {
        graph = GraphParser.parseMatrixGraphFromFile("resources\\table-1BTB.txt");
    }
    @Test
    public void testMatrixGraph() throws Exception {
        Assert.assertEquals(30, graph.getN());
        Assert.assertEquals(0.0, graph.getEdgeWeight(1,1));
        Assert.assertEquals(5.124285, graph.getEdgeWeight(4,2));
    }
}