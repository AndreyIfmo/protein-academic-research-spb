package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import ru.ifmo.ctd.proteinresearch.ordering.graph.*;

import java.io.*;
import java.util.*;

/**
 * @author Maxim Buzdalov
 */
public class BadPathRemovalSolver {
    static List<Integer> shortestPath(double[][] mx, double[][] fl, int source, int target) {
        List<Integer> shortest = new ArrayList<>();
        int cur = source;
        do {
            int next = -1;
            for (int i = 0; i < mx.length; ++i) {
                if (i != cur && Math.abs(fl[cur][target] - mx[cur][i] - fl[i][target]) < 1e-6) {
                    next = i;
                }
            }
            shortest.add(cur);
            cur = next;
        } while (cur != target);
        shortest.add(target);
        return shortest;
    }

    public static void main(String[] args) throws IOException {
        Graph g = GraphParser.parseMatrixGraphFromFile("resources/2LJI_optim_costs.txt");

        int n = g.getN();

        double[][] mx = new double[n][n];
        for (double[] t : mx) {
            Arrays.fill(t, Double.POSITIVE_INFINITY);
        }

        for (int i = 0; i < n; ++i) {
            mx[i][i] = 0;
            for (int j = 0; j < n; ++j) {
                if (g.hasEdge(i, j)) {
                    mx[j][i] = mx[i][j] = g.getEdgeWeight(i, j);
                }
            }
        }

        double[][] fl = mx.clone();
        for (int i = 0; i < fl.length; ++i) {
            fl[i] = fl[i].clone();
        }
        for (int k = 0; k < n; ++k) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    fl[i][j] = Math.min(fl[i][j], fl[i][k] + fl[k][j]);
                }
            }
        }

        double[] avgDist = new double[n];

        int maxI = 0, maxJ = 0;
        double maxD = 0;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                avgDist[i] += fl[i][j] / (n - 1);
                if (fl[i][j] > maxD) {
                    maxD = fl[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        Z[] rec = new Z[n];
        for (int i = 0; i < n; ++i) {
            rec[i] = new Z(i, avgDist[i]);
        }
        Arrays.sort(rec);
        for (Z z : rec) {
            System.out.println(z.index + ": " + z.value);
        }

        System.out.println("Max distance: " + maxD + " between " + maxI + " and " + maxJ);
        System.out.println("Shortest path: " + shortestPath(mx, fl, maxI, maxJ));

        boolean[][] faultyEdges = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i == j) {
                    continue;
                }
                for (int k = 0; k < n; ++k) {
                    if (i == k || j == k) {
                        continue;
                    }
                    if (mx[i][j] >= fl[i][j] * 1.1) {
                        faultyEdges[i][j] = true;
                    }
                }
            }
        }
        Z[] byEdges = new Z[n];
        for (int i = 0; i < n; ++i) {
            int count = 0;
            for (int j = 0; j < n; ++j) {
                if (i != j && !faultyEdges[i][j]) {
                    ++count;
                }
            }
            byEdges[i] = new Z(i, count);
            System.out.println(i + ": " + count + " normal edges");
            for (int j = 0; j < n; ++j) {
                if (i != j && !faultyEdges[i][j]) {
                    System.out.println(i + "<->" + j + ": " + mx[i][j] + "; " + fl[i][j]);
                }
            }
        }
        Arrays.sort(byEdges);
        for (Z z : byEdges) {
            System.out.println(z.index + ": " + z.value);
        }
        System.out.println("Shortest path between " + byEdges[0].index + " and " + byEdges[1].index + ":");
        System.out.println("    " + shortestPath(mx, fl, byEdges[0].index, byEdges[1].index));

        Set<Integer> cliques = new HashSet<>();

        for (int mask = 1; mask < 1 << n; ++mask) {
            boolean isClique = true;
            for (int i = 0; i < n; ++i) {
                for (int j = i + 1; j < n; ++j) {
                    if (((1 << i) & mask) != 0 && ((1 << j) & mask) != 0 && faultyEdges[i][j]) {
                        isClique = false;
                    }
                }
            }
            if (isClique) {
                for (int i = 0; i < n; ++i) {
                    cliques.remove(mask & ~(1 << i));
                }
                cliques.add(mask);
            }
        }

        System.out.println("Cliques:");
        for (int i : cliques) {
            System.out.print(Integer.bitCount(i) + ":");
            for (int t = 0; t < n; ++t) {
                if (((1 << t) & i) != 0) {
                    System.out.print(" " + t);
                }
            }
            System.out.println();
        }

        int[][] similar = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    if (faultyEdges[i][k] == faultyEdges[j][k]) {
                        similar[i][j]++;
                    }
                }
            }
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.printf("%3d", similar[i][j]);
            }
            System.out.println();
        }
    }

    static class Z implements Comparable<Z> {
        final int index;
        final double value;

        Z(int index, double value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Z o) {
            return Double.compare(value, o.value);
        }
    }
}
