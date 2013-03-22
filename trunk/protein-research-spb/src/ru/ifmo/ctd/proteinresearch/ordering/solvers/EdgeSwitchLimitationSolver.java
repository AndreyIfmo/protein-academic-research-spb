package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import ru.ifmo.ctd.proteinresearch.ordering.graph.*;

import java.io.*;
import java.util.*;

/**
 * @author Maxim Buzdalov
 */
public class EdgeSwitchLimitationSolver {
    public static void main(String[] args) throws IOException, StructureException {
        ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        int n = cg.graph.getN();
        boolean[][][] mayConnect = new boolean[n][n][n];
        for (int vertex = 0; vertex < n; ++vertex) {
            double[][] d = new double[n][];
            for (int i = 0; i < n; ++i) {
                if (i != vertex) {
                    d[i] = cg.getEdgeSourceTorsionAngleDiff(vertex, i);

                }
            }
            d[vertex] = new double[d[vertex == 0 ? 1 : 0].length];
            double[][] sim = new double[n][n];
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    double sumSq = 0;
                    for (int t = 0; t < d[i].length; ++t) {
                        double dv = d[i][t] + d[j][t];
                        sumSq += dv * dv;
                    }
                    sim[i][j] = Math.sqrt(sumSq);
                }
            }
            System.out.println("Vertex " + vertex + ":");
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    System.out.printf("%9f ", sim[i][j]);
                    if (sim[i][j] < 1.4 && i != j && i != vertex && j != vertex) {
                        mayConnect[vertex][i][j] = true;
                    }
                }
                System.out.println();
            }
        }

        double[][] shortest = new double[n][];
        for (int i = 0; i < n; ++i) {
            shortest[i] = shortestPath(cg.graph, i).distance;
        }

        //[src] <- source vertices of the base graph
        //[n + src] <- target vertices of the base graph
        //[2n + 0 * n^2 + n * src + trg] <- input vertices for edge (src --> trg)
        //[2n + 1 * n^2 + n * src + trg] <- output vertices for edge (src --> trg)

        Graph limited = new ListGraph(n * n * 2 + n * 2);
        //Edges from source vertices to input edge vertices
        for (int sv = 0; sv < n; ++sv) {
            for (int iev = 0; iev < n; ++iev) {
                limited.addEdge(sv, 2 * n + n * sv + iev, 0);
            }
        }
        //Edges from output edge vertices to target vertices
        for (int tv = 0; tv < n; ++tv) {
            for (int oev = 0; oev < n; ++oev) {
                limited.addEdge(2 * n + n * n + n * oev + tv, n + tv, 0);
            }
        }
        //Edges from input edge vertices to output edge vertices
        for (int src = 0; src < n; ++src) {
            for (int trg = 0; trg < n; ++trg) {
                limited.addEdge(2 * n + n * src + trg, 2 * n + n * n + n * src + trg, cg.graph.getEdgeWeight(src, trg));
            }
        }
        //Edges from output edge vertices to input edge vertices, where allowed
        for (int vx = 0; vx < n; ++vx) {
            for (int prev = 0; prev < n; ++prev) {
                for (int next = 0; next < n; ++next) {
                    if (mayConnect[vx][prev][next]) {
                        limited.addEdge(2 * n + n * n + n * prev + vx,
                                        2 * n + n * vx + next, 0);
                    }
                }
            }
        }
        double[][] newShortest = new double[n][n];
        int[][][] paths = new int[n][n][];
        for (int vx = 0; vx < n; ++vx) {
            DijkstraResult shp = shortestPath(limited, vx);
            System.out.print("From " + vx + ":");
            for (int i = 0; i < n; ++i) {
                newShortest[vx][i] = shp.distance[i + n];
                if (vx == i) {
                    paths[vx][i] = new int[] {vx};
                } else {
                    List<Integer> path = new ArrayList<>();
                    int cur = i + n;
                    do {
                        cur = shp.back[cur];
                        if (cur >= 2 * n + n * n) {
                            path.add(cur % n);
                        }
                    } while (cur != vx);
                    path.add(vx);
                    int[] fromTo = new int[path.size()];
                    for (int s = path.size() - 1, t = 0; s >= 0; --s, ++t) {
                        fromTo[t] = path.get(s);
                    }
                    paths[vx][i] = fromTo;
                }
                if (i != vx && !Double.isInfinite(shp.distance[i + n])) {
                    System.out.printf("   %d:%.02f/%.02f/%.02f  |", i, shp.distance[i + n], cg.graph.getEdgeWeight(vx, i), shortest[vx][i]);
                }
            }
            System.out.println();
        }

        int maxI = 0, maxJ = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (newShortest[i][j] > newShortest[maxI][maxJ]) {
                    maxI = i;
                    maxJ = j;
                }
            }
        }
        System.out.println("Longest shortest path:");
        System.out.println(Arrays.toString(paths[maxI][maxJ]));
        System.out.println("Length: " + newShortest[maxI][maxJ]);

        try (PrintWriter pdb = new PrintWriter("New.pdb")) {
            pdb.print(cg.forPath(new Path(paths[maxI][maxJ], newShortest[maxI][maxJ])));
        }
    }

    static class DijkstraResult {
        public final double[] distance;
        public final int[] back;

        DijkstraResult(double[] distance, int[] back) {
            this.distance = distance;
            this.back = back;
        }
    }

    static DijkstraResult shortestPath(Graph g, int srcVertex) {
        int n = g.getN();
        double[] currentShortest = new double[n];
        int[] back = new int[n];
        Arrays.fill(back, -1);
        boolean[] finalized = new boolean[n];
        Arrays.fill(currentShortest, Double.POSITIVE_INFINITY);
        currentShortest[srcVertex] = 0;
        while (true) {
            int smallest = -1;
            for (int i = 0; i < n; ++i) {
                if (!finalized[i] && (smallest == -1 || currentShortest[i] < currentShortest[smallest])) {
                    smallest = i;
                }
            }
            if (smallest == -1) {
                break;
            }
            finalized[smallest] = true;
            for (int i = 0; i < n; ++i) {
                if (g.hasEdge(smallest, i)) {
                    double nw = g.getEdgeWeight(smallest, i);
                    if (currentShortest[i] > currentShortest[smallest] + nw) {
                        currentShortest[i] = currentShortest[smallest] + nw;
                        back[i] = smallest;
                    }
                }
            }
        }
        return new DijkstraResult(currentShortest, back);
    }
}
