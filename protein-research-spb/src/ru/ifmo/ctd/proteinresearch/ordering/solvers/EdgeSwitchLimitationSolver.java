package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.StructureException;
import ru.ifmo.ctd.proteinresearch.ordering.algorithms.OptMethod;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.ListGraph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;
import ru.ifmo.ctd.proteinresearch.ordering.util.Function;
import ru.ifmo.ctd.proteinresearch.ordering.util.IntPair;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Maxim Buzdalov
 */
public class EdgeSwitchLimitationSolver {
    public ConformationGraph cg;

    public boolean isBanned(int first, int second) {
        return banned[first][second]||banned[second][first];
    }

    boolean[][] banned;
    boolean[][][] mayConnect;
    private int[][][] paths;
    private double[][] newShortest;
    private Function<Double, Double> f2;

    public int[][][] getPaths() {
        return paths;
    }

    public int[] getDistribution() {
        int[] answer = new int[getN()];
        for (int i = 0; i < getN(); i++) {
            for (int j = 0; j < getN(); j++) {
                int[] path = paths[i][j];
                if (path != null && path.length > 1) {
                    for (int k = 1; k < path.length - 1; k++) {
                        answer[path[k]]++;
                    }
                }
            }
        }
        return answer;
    }

    public int getN() {
        return cg.graph.getN();
    }

    public double[][] getNewShortest() {
        return newShortest;
    }

    public static void main(String[] args) throws Exception {
        final EdgeSwitchLimitationSolver edgeSwitchLimitationSolver = new EdgeSwitchLimitationSolver("2M2Y.properties");
        edgeSwitchLimitationSolver.run(0.001, 10);

//        edgeSwitchLimitationSolver.recalculate();
    }
/*
    private void recalculate() throws Exception {
        final EdgeSwitchLimitationSolver edgeSwitchLimitationSolver = this;
        f2 = new Function<Double, Double>() {
            @Override
            public Double apply(Double argument) throws Exception {
                edgeSwitchLimitationSolver.recalculatePath(edgeSwitchLimitationSolver.getN(), edgeSwitchLimitationSolver.mayConnect, argument);
                IntPair pathIndexes = findMaxShortestPath(edgeSwitchLimitationSolver.getN());
                int[] path = paths[pathIndexes.first][pathIndexes.second];
               // System.out.println(argument);
               // System.out.println(path.length);
               // System.out.println(Arrays.toString(path));
                return (double) path.length;
            }
        };
        f2.apply(8.0);

    }*/

    public List<Integer> sortedList(int[] distribution) {
        List<IntPair> sortedList = new ArrayList<>();
        for (int i=0; i < distribution.length; i++) {
            sortedList.add(new IntPair(i,distribution[i]));
        }
        Collections.sort(sortedList, new Comparator<IntPair>() {
            @Override
            public int compare(IntPair o1, IntPair o2) {
                return Integer.compare(o1.second, o2.second);
            }
        });

        List<Integer> answer = new ArrayList<>();
        for (IntPair it: sortedList) {
            answer.add(it.first);
        }
        return answer;
    }
    Function<Double, Double> f = new Function<Double, Double>() {
        @Override
        public Double apply(Double argument) throws StructureException, FileNotFoundException {
            int n= getN();
            mayConnect = new boolean[n][n][n];
            evaluateMayConnectMatrix(n, banned, mayConnect, argument);
            calculatePaths(n, banned, mayConnect);
            IntPair pathIndexes = findMaxShortestPath(n);
            int[] path = paths[pathIndexes.first][pathIndexes.second];
          //  System.out.println(Double.valueOf(path.length) + " " + Arrays.toString(path) + " argument: " + argument);
            return Double.valueOf(path.length);
        }
    };

    private void recalculatePath(int n, boolean[][][] mayConnect, double thresold) {
        int[] distribution= getDistribution();
        List<Integer> sortedList = sortedList(distribution);
        for (int i=0; i< sortedList.size(); i++) {
            for (int j =0; j< sortedList.size(); j++) {
                if (Math.abs(i-j)>sortedList.size()/thresold) {
                    int iv=sortedList.get(i);
                    int jv=sortedList.get(j);
                    banned[iv][jv]=true;
                    banned[jv][iv]=true;
             //       System.out.println(i+" "+j+" "+iv+" "+ jv);
                }
            }
        }
        calculatePaths(n, banned, mayConnect);
    }

    public EdgeSwitchLimitationSolver(ConformationGraph cg) {
        try {
            this.cg = cg;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public EdgeSwitchLimitationSolver(String propertiesFileName) {
        try {
            cg = PropertiesParser.getGraphData(propertiesFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public EdgeSwitchLimitationSolver(String matrixFileName, String zipArchive, String fileNamePattern) {
        try {
            cg = new ConformationGraph(matrixFileName, zipArchive, fileNamePattern, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void run(double border, List<Integer> bannedVertices) throws Exception {
        banned = calculateBannedEdges(getN());
        applyBanned(bannedVertices);
        f.apply(border);
    }

    private void applyBanned(List<Integer> bannedVertices) {
        for (Integer it:bannedVertices) {
            for (int i=0; i<banned.length; i++) {
                banned[it][i] = true;
                banned[i][it] = true;
            }
        }
    }

    public void run(double border) throws Exception {
        banned = calculateBannedEdges(getN());
        f.apply(border);
    }

    public void run(double minDiffValue, double maxDiffValue) throws Exception {
        banned = calculateBannedEdges(getN());
        double optimalValue = findOptimalValue(minDiffValue, maxDiffValue, f);
        System.out.println("OPT"+optimalValue);
        run(optimalValue);
    }

    public void run(double minDiffValue, double maxDiffValue, List<Integer> bannedVertices) throws Exception {
        banned = calculateBannedEdges(getN());
        applyBanned(bannedVertices);
        run(findOptimalValue(minDiffValue, maxDiffValue, f));
    }

    private double findOptimalValue(double minDiffValue, double maxDiffValue, Function f) throws Exception {
        double delta = 0.0000001;
        return OptMethod.gold(f, minDiffValue, maxDiffValue, delta);
     //   System.out.println("Value: " + border);
      //  System.out.println("ANSWER");
      //  System.out.println(f.apply(border));
    }

    public boolean[][] calculateBannedEdges(int n) throws Exception {
        banned = new boolean[n][n];
        int counter = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                banned[i][j] = false;
                for (int k = 0; k < n; ++k) {
                    banned[i][j] = banned[i][j] || !cg.graph.hasEdge(i, j) || cg.graph.getEdgeWeight(i, j) > 2 * (cg.graph.getEdgeWeight(i, k) + cg.graph.getEdgeWeight(k, j));
                }
                if (banned[i][j]) {
                    counter++;
                    //System.out.println(i + " <-> " + j + " banned");
                }
            }
        }
        System.out.println(counter);
        getBadFiles(n, banned);
        return banned;
    }

    public boolean[][] getBadFiles(int n, boolean[][] banned) throws Exception {
        int selfIntersectedCounter = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                try {
                    if (cg.files[i][j] == null) {
                        banned[i][j] = true;
                        banned[j][i] = true;
                        System.out.println(i + "<->" + j + "Not computed");
                        continue;
                    }
                    if (IntersectionUtils.checkFile(cg.files[i][j].getPath()) > 0) {
                        banned[i][j] = true;
                        banned[j][i] = true;
                        System.out.println(i + " <-> " + j + " self-intersected");
                        selfIntersectedCounter++;
                    }
                } catch (NullPointerException npe) {
                    //System.out.println(i + " " + j);
                    npe.printStackTrace();
                }

            }
        }
        System.out.println("bad-intersected: " +selfIntersectedCounter);
        return banned;
    }

    private void calculatePaths(int n, boolean[][] banned, boolean[][][] mayConnect) {
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
                if (!banned[src][trg]) {
                    limited.addEdge(2 * n + n * src + trg, 2 * n + n * n + n * src + trg, cg.graph.getEdgeWeight(src, trg));
                }
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


        newShortest = new double[n][n];
        paths = new int[n][n][];
        for (int vx = 0; vx < n; ++vx) {
            DijkstraResult shp = shortestPath(limited, vx);
            //System.out.print("From " + vx + ":");
            for (int i = 0; i < n; ++i) {
                newShortest[vx][i] = shp.distance[i + n];
                if (Double.isInfinite(newShortest[vx][i])) {
                    continue;
                }
                if (vx == i) {
                    paths[vx][i] = new int[]{vx};
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
                if (i != vx) {
                    // System.out.printf("   %d:%.02f/%.02f/%.02f  |", i, shp.distance[i + n], cg.graph.getEdgeWeight(vx, i), shortest[vx][i]);
                }
            }
            //System.out.println();
        }

        for (int vx = 0; vx < n; ++vx) {
            for (int i = 0; i < n; ++i) {
                if (Double.isInfinite(newShortest[vx][i])) {
                 //   System.out.println("Infinite between " + vx + " and " + i);
                }
            }
        }
        //printAllPaths(n);
    }

    private void printAllPaths(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               // System.out.println(Arrays.toString(paths[i][j]));
            }

        }
    }

    private void evaluateMayConnectMatrix(int n, boolean[][] banned, boolean[][][] mayConnect, double thresold) throws StructureException {
        int counter = 0;
        for (int vertex = 0; vertex < n; ++vertex) {
            double[][] d = new double[n][];
            int size = -1;
            for (int i = 0; i < n; ++i) {
                if (i != vertex && !banned[vertex][i]) {
                    d[i] = cg.getEdgeSourceTorsionAngleDiff(vertex, i);
                    size = d[i].length;
                }
            }
            if (size == -1) {
              //  System.out.print("Not connected? " + vertex);

                continue;
            }

            d[vertex] = new double[size];
            double[][] sim = new double[n][n];
            for (double[] t : sim) {
                Arrays.fill(t, Double.POSITIVE_INFINITY);
            }
            for (int i = 0; i < n; ++i) {
                if (d[i] == null || vertex == i) {
                    continue;
                }
                for (int j = 0; j < n; ++j) {
                    if (d[j] == null || i == j || vertex == j) {
                        continue;
                    }
                    double max = 0;
                    double lenI = cg.graph.getEdgeWeight(vertex, i);
                    double lenJ = cg.graph.getEdgeWeight(vertex, j);
                    for (int t = 0; t < d[i].length; t += 3) {
                        double dx1 = d[i][t] / lenI;
                        double dy1 = d[i][t + 1] / lenI;
                        double dz1 = d[i][t + 2] / lenI;
                        double dx2 = -d[j][t] / lenJ;
                        double dy2 = -d[j][t + 1] / lenJ;
                        double dz2 = -d[j][t + 2] / lenJ;
                        double dx = dx1 - dx2;
                        double dy = dy1 - dy2;
                        double dz = dz1 - dz2;
                        max = Math.max(max, Math.sqrt(dx * dx + dy * dy + dz * dz));
                    }
                    sim[i][j] = max;
                }
                sim[i][i] = 0.0;
            }
            //System.out.println("Vertex " + vertex + ":");
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    //       System.out.printf("%9f ", sim[i][j]);

                    if (sim[i][j] <= thresold && i != j && i != vertex && j != vertex) {
                        mayConnect[vertex][i][j] = true;
                    } else {
                        counter++;
                    }
                }
                //   System.out.println();
            }
        }
        System.out.println("NUMBEROFBANNED: " + counter);
    }

    public IntPair findMaxShortestPath(int n) throws StructureException, FileNotFoundException {
        int maxI = 0, maxJ = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (paths[i][j] != null && paths[i][j].length > paths[maxI][maxJ].length) {
                    maxI = i;
                    maxJ = j;
                }
            }
        }
        return new IntPair(maxI, maxJ);
    }

    private void printPath(int maxI, int maxJ) throws StructureException, FileNotFoundException {
       // System.out.println("Longest shortest path:");
       // System.out.println(Arrays.toString(paths[maxI][maxJ]));
       // System.out.println("Length: " + newShortest[maxI][maxJ]);

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

    public int[] get(int from, int to) {
        return paths[from][to];
    }

    public double weight(int from, int to) {
        double distance = 0;
        for (int i = 0; i < paths[from][to].length - 1; i++) {
            distance += cg.graph.getEdgeWeight(paths[from][to][i], paths[from][to][i + 1]);
        }
        return distance;
    }


}
