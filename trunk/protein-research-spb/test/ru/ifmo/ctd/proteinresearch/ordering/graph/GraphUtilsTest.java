package ru.ifmo.ctd.proteinresearch.ordering.graph;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 08.02.13
 *         Time: 18:34
 */
public class GraphUtilsTest {
    public Graph graph;

    @Before
    public void constructGraph() throws IOException {
        graph = GraphParser.parseMatrixGraphFromFile("resources/1BTB.txt");
    }

    @Test
    public void testIsMetric() throws Exception {
        Assert.assertEquals(false, GraphUtils.isMetric(graph));
    }
}
