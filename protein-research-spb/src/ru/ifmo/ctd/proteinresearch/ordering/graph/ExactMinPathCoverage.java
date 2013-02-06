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

    /**
     * Builds the coverage object for the given graph and the start vertex.
     * @param g the graph.
     * @param startVertex the starting vertex.
     */
    public ExactMinPathCoverage(Graph g, int startVertex) {
        this.startVertex = startVertex;
        n = g.getN();
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

        for (int mask = 1, limit = 1 << n; mask < limit; ++mask) {
            if ((mask & (mask - 1)) == 0) {
                int idx = Integer.numberOfTrailingZeros(mask);
                if (idx == startVertex) {
                    length[mask][idx] = 0;
                }
            } else {
                double[] lMask = length[mask];
                for (int last = 0; last < n; ++last) {
                    if (!Double.isInfinite(lMask[last])) {
                        double[] ig = graph[last];
                        for (int next = 0; next < n; ++next) {
                            double w = ig[next];
                            if ((mask & (1 << next)) == 0 && !Double.isInfinite(w)) {
                                double nd = lMask[last] + w;
                                int nextMask = mask | (1 << last);
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
    }

    private Path eatPath(int mask, int last) {
        int[] vertices = new int[Integer.bitCount(mask)];
        int idx = vertices.length - 1;
        double length = 0;
        while (mask != 0) {
            vertices[idx--] = last;
            int prev = back[mask][last];
            mask ^= 1 << last;
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
                int[] second = new int[n + 2 - first.length];
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

    /**
     * Returns k paths, each starting at the starting vertex and ending at the given target vertex.
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
                return new Path[] {eatPath((1 << n) - 1, target)};
            }
            case 2: {
                double loopLength = Double.POSITIVE_INFINITY;
                int loopTarget = -1;
                for (int trg = 0; trg < n; ++trg) {
                    if (trg != startVertex) {
                        double a = length[(1 << n) - 1][trg] + graph[startVertex][trg];
                        if (a < loopLength) {
                            loopLength = a;
                            loopTarget = trg;
                        }
                    }
                }
                return eatPathsFromLoop((1 << n) - 1, loopTarget, target);
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
                    int loopTarget = -1;
                    double loopLength = Double.POSITIVE_INFINITY;
                    for (int i = 0; i < n; ++i) {
                        if (i == target || (currLoopMask & (1 << i)) != 0) {
                            double localLoopLength = length[currLoopMask | maskEnds][i] + graph[startVertex][i];
                            if (localLoopLength < loopLength) {
                                loopLength = localLoopLength;
                                loopTarget = i;
                            }
                        }
                    }
                    if (pathLength + loopLength < bestLength) {
                        bestLength = pathLength + loopLength;
                        bestPathMask = currPathMask;
                        bestLoopTarget = loopTarget;
                    }
                } while (currPathMask != 0);
                Path[] loop = eatPathsFromLoop(maskEnds | (maskNoEnds & ~bestPathMask), bestLoopTarget, target);
                Path path = eatPath(maskEnds | bestPathMask, target);
                return new Path[] {path, loop[0], loop[1]};
            }
            default: throw new IllegalArgumentException("k = " + k + " is unsupported");
        }
    }
}
