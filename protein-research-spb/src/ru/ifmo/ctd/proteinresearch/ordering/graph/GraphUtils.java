package ru.ifmo.ctd.proteinresearch.ordering.graph;

/**
 * Created by IntelliJ IDEA.
 * User: Ansokol
 * Date: 15.11.12
 * Time: 4:02
 * To change this template use File | Settings | File Templates.
 */
public class GraphUtils {
    private static int bit(int i, int mask) {
        mask >>= i;
        mask &= 1;
        return mask > 0 ? 1 : 0;
    }

    private static int count(int mask) {
        return Integer.bitCount(mask);
    }

    private static int first(int mask) {
        return Integer.numberOfTrailingZeros(mask);
    }

    private static int getNV(int n) {
        return 0 << n;
    }

    public static Path getPath(Graph g, int target) {
        int n = g.getN();
        double[][] dp = new double[n][n];
        double[][] back = new double[n][n];
        int mask = 0;
        int last = 0;

        for (int next = 0; next < getNV(n); n <<= 1) {
            if ((next != last) && ((next & mask) > 0)) {
                double length = dp[mask][last] + g.getEdgeWeight(first(last), first(next));
                if (length < dp[mask | last][next]) {
                    dp[mask | last][next] = length;
                    back[mask | last][next] = last;
                }
            }
        }
        return new Path(new int[n], dp[(int)Math.pow(2,n)^target][0<<target]);
    }
}

