package ru.ifmo.ctd.proteinresearch.ordering.graph;

import ru.ifmo.ctd.proteinresearch.ordering.BitUtils;

import java.util.Arrays;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 15.11.12
 *         Time: 4:02
 */

public class GraphUtils {
    private static void computeTables(Graph g, int source, double[][] dp, int[][] back) {
        int n = g.getN();
        for (double[] t : dp) {
            Arrays.fill(t, Double.POSITIVE_INFINITY);
        }
        dp[0][source] = 0;

        for (int[] b : back) {
            Arrays.fill(b, -1);
        }

        for (int mask = 0, limit = 1 << n; mask < limit; ++mask) {
            for (int last = 0; last < n; ++last) {
                if (!Double.isInfinite(dp[mask][last])) {
                    for (int next = 0; next < n; ++next) {
                        if (g.hasEdge(last, next)) {
                            double nd = dp[mask][last] + g.getEdgeWeight(last, next);
                            int nextMask = mask | (1 << last);
                            if (dp[nextMask][next] > nd) {
                                dp[nextMask][next] = nd;
                                back[nextMask][next] = last;
                            }
                        }
                    }
                }
            }
        }
    }

    public static Path getPath(Graph g) {
        Path p = null;
        Path temp;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < g.getN(); i++) {
            for (int j = i + 1; j < g.getN(); j++) {
                temp = GraphUtils.getPath(g, i, j);
                if (min > temp.cost) {
                    p = temp;
                    min = p.cost;
                }
            }
        }
        return p;
    }

    public static Path[] getPaths(Graph g, int k) {
        Path[] p = null;
        Path[] temp;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < g.getN(); i++) {
            for (int j = i + 1; j < g.getN(); j++) {
                temp = GraphUtils.getPaths(g, i, j, k);
                double cost = 0;
                for (Path it : temp) {
                    cost += it.cost;
                }
                if (min > cost) {
                    p = temp;
                    min = cost;
                }
            }
        }
        return p;

    }

    public static Path getPath(Graph g, int source, int target) {
        int n = g.getN();
        double[][] dp = new double[1 << n][n];
        int[][] back = new int[1 << n][n];
        computeTables(g, source, dp, back);
        int[] result = new int[n];
        int finalMask = ((1 << n) - 1) ^ (1 << target);
        for (int i = n - 1, last = target, mask = finalMask; i >= 0; --i) {
            result[i] = last;
            last = back[mask][last];
            mask ^= 1 << last;
        }
        return new Path(result, dp[finalMask][target]);
    }

    public static Path[] getPaths(Graph g, int source, int target, int k) {
        switch (k) {
            case 1:
                return new Path[]{getPath(g, source, target)};
            case 2:
                int limit = (int) Math.pow(2, g.getN());
                double minCost = Double.MAX_VALUE;
                Path[] answer = new Path[2];
                for (int i = 0; i < limit; i++) {
                    int cur = i;
                    cur = BitUtils.setNBit(cur, source, true);
                    cur = BitUtils.setNBit(cur, target, true);
                    Graph subGraph1 = g.getSubGraph(cur);
                    Path p1 = getPath(subGraph1, source, target);
                    cur = -i;
                    cur = BitUtils.setNBit(cur, source, true);
                    cur = BitUtils.setNBit(cur, target, true);
                    Graph subGraph2 = g.getSubGraph(cur);
                    Path p2 = getPath(subGraph2, source, target);
                    if (p1.cost < minCost - p2.cost) {
                        minCost = p1.cost + p2.cost;
                        answer[0] = p1;
                        answer[1] = p2;
                    }
                }
                return answer;
            default:
                throw new IllegalArgumentException("K = " + k + " is unsupported");
        }
    }
}
