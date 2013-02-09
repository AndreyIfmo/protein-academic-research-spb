package ru.ifmo.ctd.proteinresearch.ordering.graph;

import org.junit.*;

import java.util.*;

/**
 * Tests for {@link ExactMinPathCoverage}.
 *
 * @author Maxim Buzdalov
 */
public class ExactMinPathCoverageTests {
    private static final double INF = Double.POSITIVE_INFINITY;

    static Comparator<Path> lexCmp = new Comparator<Path>() {
        @Override
        public int compare(Path o1, Path o2) {
            int[] p1 = o1.vertices;
            int[] p2 = o2.vertices;
            for (int i = 0; i < p1.length && i < p2.length; ++i) {
                if (p1[i] != p2[i]) {
                    return p1[i] - p2[i];
                }
            }
            return p1.length - p2.length;
        }
    };

    @Test
    public void onePath() {
        Graph g = new MatrixGraph(4, new double[][] {
                {INF, 1, 1, 9},
                {1, INF, 7, 8},
                {1, 7, INF, 1},
                {9, 8, 1, INF}
        });

        ExactMinPathCoverage cov = new ExactMinPathCoverage(g, 1);
        Path[] ps = cov.getPaths(1, 3);
        Assert.assertEquals(1, ps.length);
        Assert.assertEquals(3, ps[0].cost, 1e-9);
        Assert.assertArrayEquals(new int[]{1, 0, 2, 3}, ps[0].vertices);
    }

    @Test
    public void twoPaths() {
        Graph g = new MatrixGraph(4, new double[][] {
                {INF, 1, 6, 1},
                {1, INF, 1, 8},
                {6, 1, INF, 1},
                {1, 8, 1, INF}
        });

        ExactMinPathCoverage cov = new ExactMinPathCoverage(g, 1);
        Path[] ps = cov.getPaths(2, 3);
        Assert.assertEquals(2, ps.length);
        Arrays.sort(ps, lexCmp);
        Assert.assertEquals(2, ps[0].cost, 1e-9);
        Assert.assertEquals(2, ps[1].cost, 1e-9);
        Assert.assertArrayEquals(new int[]{1, 0, 3}, ps[0].vertices);
        Assert.assertArrayEquals(new int[]{1, 2, 3}, ps[1].vertices);
    }

    @Test
    public void threePaths() {
        Graph g = new MatrixGraph(8, new double[][] {
                {INF, 1, 8, 8, 8, 1, 8, 8},
                {1, INF, 1, 8, 8, 8, 8, 8},
                {8, 1, INF, 1, 8, 8, 8, 1},
                {8, 8, 1, INF, 1, 8, 8, 8},
                {8, 8, 8, 1, INF, 1, 8, 8},
                {1, 8, 8, 8, 1, INF, 1, 8},
                {8, 8, 8, 8, 8, 1, INF, 1},
                {8, 8, 1, 8, 8, 8, 1, INF},
        });
        ExactMinPathCoverage cov = new ExactMinPathCoverage(g, 2);
        Path[] ps = cov.getPaths(3, 5);
        Assert.assertEquals(3, ps.length);
        Arrays.sort(ps, lexCmp);
        Assert.assertEquals(3, ps[0].cost, 1e-9);
        Assert.assertEquals(3, ps[1].cost, 1e-9);
        Assert.assertEquals(3, ps[2].cost, 1e-9);
        Assert.assertArrayEquals(new int[]{2, 1, 0, 5}, ps[0].vertices);
        Assert.assertArrayEquals(new int[]{2, 3, 4, 5}, ps[1].vertices);
        Assert.assertArrayEquals(new int[]{2, 7, 6, 5}, ps[2].vertices);
    }

    @Test
    public void fourPaths() {
        Graph g = new MatrixGraph(8, new double[][] {
                {INF, 1, 8, 8, 8, 1, 8, 8},
                {1, INF, 1, 8, 8, 8, 8, 8},
                {8, 1, INF, 1, 8, 3, 8, 1},
                {8, 8, 1, INF, 1, 8, 8, 8},
                {8, 8, 8, 1, INF, 1, 8, 8},
                {1, 8, 3, 8, 1, INF, 1, 8},
                {8, 8, 8, 8, 8, 1, INF, 1},
                {8, 8, 1, 8, 8, 8, 1, INF},
        });
        ExactMinPathCoverage cov = new ExactMinPathCoverage(g, 2);
        Path[] ps = cov.getPaths(4, 5);
        Assert.assertEquals(4, ps.length);
        Arrays.sort(ps, lexCmp);
        Assert.assertEquals(3, ps[0].cost, 1e-9);
        Assert.assertEquals(3, ps[1].cost, 1e-9);
        Assert.assertEquals(3, ps[2].cost, 1e-9);
        Assert.assertEquals(3, ps[3].cost, 1e-9);
        Assert.assertArrayEquals(new int[]{2, 1, 0, 5}, ps[0].vertices);
        Assert.assertArrayEquals(new int[]{2, 3, 4, 5}, ps[1].vertices);
        Assert.assertArrayEquals(new int[]{2, 5}, ps[2].vertices);
        Assert.assertArrayEquals(new int[]{2, 7, 6, 5}, ps[3].vertices);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fivePaths() {
        new ExactMinPathCoverage(new MatrixGraph(10, new double[10][10]), 2).getPaths(5, 3);
    }

    @Test
    public void simpleTSP() {
        Graph g = new MatrixGraph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(0, 2, 2);
        g.addEdge(2, 0, 2);
        g.addEdge(0, 3, 2);
        g.addEdge(3, 0, 2);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 1, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(3, 1, 2);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);

        Path p = new ExactMinPathCoverage(g, 0).getPaths(1, 3)[0];
        Assert.assertEquals(3, p.cost, 1e-14);
        Assert.assertArrayEquals(new int[]{0, 1, 2, 3}, p.vertices);
    }
}
