package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ansokol
 * Date: 15.11.12
 * Time: 4:02
 * To change this template use File | Settings | File Templates.
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

    public static Path[] getPaths(Graph g, int source, int target, int k) {
        int n = g.getN();
        double[][] dp = new double[1 << n][n];
        int[][] back = new int[1 << n][n];

        computeTables(g, source, dp, back);

        switch (k) {
            case 1: {
                int[] result = new int[n];
                int finalMask = ((1 << n) - 1) ^ (1 << target);
                for (int i = n - 1, last = target, mask = finalMask; i >= 0; --i) {
                    result[i] = last;
                    last = back[mask][last];
                    mask ^= 1 << last;
                }
                return new Path[] {new Path(result, dp[finalMask][target])};
            }
            default:
                throw new IllegalArgumentException("K = " + k + " is unsupported");
        }

    }
}
