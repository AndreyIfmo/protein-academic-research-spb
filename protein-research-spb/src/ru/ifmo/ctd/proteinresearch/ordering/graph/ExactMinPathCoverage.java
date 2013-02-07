package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.*;

/**
 * A class that is able to give the shortest series of paths
 * that cover the given graph.
 *
 * @author Maxim Buzdalov
 */
public final class ExactMinPathCoverage {
    protected double[][] length;
    protected int[][] back;
    protected double[][] graph;
    protected int n;
    protected int startVertex;
    protected int maskAll;

    /**
     * Builds the coverage object for the given graph and the start vertex.
     * @param g the graph.
     * @param startVertex the starting vertex.
     */
    public ExactMinPathCoverage(Graph g, int startVertex) {
        this.startVertex = startVertex;
        n = g.getN();
        maskAll = (1 << n) - 1;
        graph = new double[n][n];
        length = new double[1 << n][n];
        back = new int[1 << n][n];
        for (double[] t : length) {
            Arrays.fill(t, Double.POSITIVE_INFINITY);
        }
        for (int[] t : back) {
            Arrays.fill(t, -1);
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (g.hasEdge(i, j)) {
                    graph[i][j] = g.getEdgeWeight(i, j);
                } else {
                    graph[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        for (int i = 1; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                if (graph[i][j] != graph[j][i]) {
                    throw new IllegalArgumentException(String.format(
                            "Graph is not undirected: G[%1$d][%2$d] = %3$f, G[%2$d][%1$d] = %4$f", i, j, graph[i][j], graph[j][i]
                    ));
                }
            }
        }

        for (int mask = 1; mask <= maskAll; ++mask) {
            double[] lMask = length[mask];
            if ((mask & (mask - 1)) == 0) {
                int idx = Integer.numberOfTrailingZeros(mask);
                if (idx == startVertex) {
                    lMask[idx] = 0;
                }
            }
            for (int last = 0; last < n; ++last) {
                if (!Double.isInfinite(lMask[last])) {
                    double[] ig = graph[last];
                    for (int next = 0; next < n; ++next) {
                        double w = ig[next];
                        if ((mask & (1 << next)) == 0 && !Double.isInfinite(w)) {
                            double nd = lMask[last] + w;
                            int nextMask = mask | (1 << next);
                            if (length[nextMask][next] > nd) {
                                length[nextMask][next] = nd;
                                back[nextMask][next] = last;
                            }
                        }
                    }
                }
            }
        }
    }

    private Path eatPath(int mask, int last) {
        int[] vertices = new int[Integer.bitCount(mask)];
        int idx = vertices.length - 1;
        double length = 0;
        while (true) {
            vertices[idx--] = last;
            int prev = back[mask][last];
            mask ^= 1 << last;
            if (mask == 0) {
                break;
            }
            length += graph[prev][last];
            last = prev;
        }
        return new Path(vertices, length);
    }

    private Path[] eatPathsFromLoop(int mask, int loopEnd, int realTarget) {
        Path tmp = eatPath(mask, loopEnd);
        for (int i = 0; i < tmp.vertices.length; ++i) {
            if (tmp.vertices[i] == realTarget) {
                int[] first = new int[i + 1];
                int[] second = new int[tmp.vertices.length + 2 - first.length];
                System.arraycopy(tmp.vertices, 0, first, 0, i + 1);
                second[0] = startVertex;
                second[second.length - 1] = tmp.vertices[i];
                for (int t = 1, z = tmp.vertices.length - 1; z >= i; ++t, --z) {
                    second[t] = tmp.vertices[z];
                }
                double firstLength = 0;
                for (int t = 1; t < first.length; ++t) {
                    firstLength += graph[first[t - 1]][first[t]];
                }
                double secondLength = 0;
                for (int t = 1; t < second.length; ++t) {
                    secondLength += graph[second[t - 1]][second[t]];
                }
                return new Path[] {new Path(first, firstLength), new Path(second, secondLength)};
            }
        }
        throw new AssertionError("!!!");
    }

    private LoopValues bestLoop(int mask) {
        double loopLength = Double.POSITIVE_INFINITY;
        int loopTarget = -1;
        for (int trg = 0; trg < n; ++trg) {
            if (trg != startVertex && (mask & (1 << trg)) != 0) {
                double a = length[mask][trg] + graph[startVertex][trg];
                if (a < loopLength) {
                    loopLength = a;
                    loopTarget = trg;
                }
            }
        }
        return new LoopValues(loopTarget, loopLength);
    }

    /**
     * Returns k paths, 1 &lt;= k &lt=; 4, each starting at the starting vertex and ending at the given target vertex.
     * The paths cover the entire graph and have only the starting and the target vertices in common.
     * @param k the number of paths (1..4).
     * @return the array of paths.
     */
    public Path[] getPaths(int k, int target) {
        if (target == startVertex || target < 0 || target >= n) {
            throw new IllegalArgumentException(
                    "target is wrong: " + target + ", where n = " + n + " and start vertex = " + startVertex
            );
        }
        switch (k) {
            case 1: {
                return new Path[] {eatPath(maskAll, target)};
            }
            case 2: {
                LoopValues best = bestLoop(maskAll);
                return eatPathsFromLoop(maskAll, best.target, target);
            }
            case 3: {
                int maskEnds = (1 << startVertex) ^ (1 << target);
                int maskNoEnds = ((1 << n) - 1) ^ maskEnds;
                int bestPathMask = -1;
                int bestLoopTarget = -1;
                double bestLength = Double.POSITIVE_INFINITY;

                int currPathMask = 0;
                do {
                    currPathMask = (currPathMask - 1) & maskNoEnds;
                    int currLoopMask = maskNoEnds & ~currPathMask;
                    double pathLength = length[currPathMask | maskEnds][target];
                    LoopValues loop = bestLoop(currLoopMask | maskEnds);
                    if (pathLength + loop.length < bestLength) {
                        bestLength = pathLength + loop.length;
                        bestPathMask = currPathMask;
                        bestLoopTarget = loop.target;
                    }
                } while (currPathMask != 0);
                Path[] loop = eatPathsFromLoop(maskEnds | (maskNoEnds & ~bestPathMask), bestLoopTarget, target);
                Path path = eatPath(maskEnds | bestPathMask, target);
                return new Path[] {path, loop[0], loop[1]};
            }
            case 4: {
                int maskEnds = (1 << startVertex) ^ (1 << target);
                int maskNoEnds = ((1 << n) - 1) ^ maskEnds;
                int bestLoop1Mask = -1;
                int bestLoop1Target = -1;
                int bestLoop2Target = -1;
                double bestLength = Double.POSITIVE_INFINITY;

                int currMask1 = 0;
                do {
                    currMask1 = (currMask1 - 1) & maskNoEnds;
                    int currMask2 = maskNoEnds & ~currMask1;
                    LoopValues loop1 = bestLoop(currMask1 | maskEnds);
                    LoopValues loop2 = bestLoop(currMask2 | maskEnds);
                    if (bestLength > loop1.length + loop2.length) {
                        bestLength = loop1.length + loop2.length;
                        bestLoop1Mask = currMask1;
                        bestLoop1Target = loop1.target;
                        bestLoop2Target = loop2.target;
                    }
                } while (currMask1 != 0);
                Path[] loop1 = eatPathsFromLoop(bestLoop1Mask | maskEnds, bestLoop1Target, target);
                Path[] loop2 = eatPathsFromLoop((maskNoEnds & ~bestLoop1Mask) | maskEnds, bestLoop2Target, target);
                return new Path[] { loop1[0], loop1[1], loop2[0], loop2[1]};
            }
            default: throw new IllegalArgumentException("k = " + k + " is unsupported");
        }
    }

    private static class LoopValues {
        public final int target;
        public final double length;

        private LoopValues(int target, double length) {
            this.target = target;
            this.length = length;
        }
    }
}
