package ru.ifmo.ctd.proteinresearch.ordering.clustering;

import com.sun.istack.internal.Nullable;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.Cluster;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;

import java.io.IOException;
import java.util.*;

/**
 * Created by AndreyS on 02.05.2014.
 */
public class SimpleSampling {
    double[][] distanceMatrix;
    String fileName;

    public double d(int x, int y) {
        return distanceMatrix[x][y];
    }

    public double dist(int x, int[] centers) {
        double min = Double.MAX_VALUE;
        for (int i : centers) {
            if (min > d(x, i)) {
                min = d(x, i);
            }
        }
        return min;
    }

    public SimpleSampling(String fileName) throws IOException {
        this.fileName = fileName;
        this.distanceMatrix = GraphParser.parseGraphMatrix(fileName);
        GraphParser.floyd(distanceMatrix);
    }


    /**
     * @return clusterCenters
     */
    @Nullable
    private List<ClusteringAnswer> evaluate(int numOfClusters) {
        List<ClusteringAnswer> answer = new ArrayList<>();
        int[] centers = null;

        int[] vector = new int[numOfClusters];
        int[] curCenters;
        while (!ClusteringUtils.isAll1(vector, distanceMatrix.length - 1)) {
            double maxOfDist = Double.MIN_VALUE;
            curCenters = vector;
            Set<Integer> setCenters = new HashSet<>();
            for (int it : curCenters) {
                setCenters.add(it);
            }
            // if (setCenters.size() == curCenters.length) {
            for (int i = 0; i < distanceMatrix.length; i++) {
                double distI = dist(i, curCenters);

                if (maxOfDist < distI) {
                    maxOfDist = distI;
                    centers = curCenters;
                }
            }
            answer.add(new ClusteringAnswer(maxOfDist, centers));
            //}
            vector = ClusteringUtils.next(vector, distanceMatrix.length);
            //   System.out.println(Arrays.toString(vector));
        }
        Collections.sort(answer);
        return answer;
    }

    public static void main(String[] args) throws IOException {
        SimpleSampling simpleSampling = new SimpleSampling("matrixes\\1BTB.txt");
        simpleSampling.run(4);
    }

    public List<Cluster> run(int number) {
        List<Cluster> clusters = new ArrayList<>();
        System.out.println("File: " + fileName);
        List<ClusteringAnswer> evaluate = evaluate(number);
        Object[] answer = evaluate.toArray();
        int counter = 0;
        ClusteringAnswer clusteringAnswer = evaluate.get(0);
        System.out.println(Arrays.toString(clusteringAnswer.centers));
        for (List<Integer> it : ClusteringAnswer.getClusters(distanceMatrix, clusteringAnswer.centers)) {
            for (Integer it1 : it) {
                System.out.print(it1 + " ");
            }
            System.out.println();
        }
        return clusteringAnswer.toClusters(distanceMatrix);
        /*
        System.out.print("===============================");
        for (Object it : answer) {

            if (counter < 100) {
                System.out.println(it);
            }
            counter++;
        }*/
    }

    public static int closes(double[][] weights, int i, int[] ans) {
        double min = Double.MAX_VALUE;
        int minI = -1;
        for (int it = 0; it < ans.length; it++) {
            double weight = weights[i][ans[it]];
            if (min > weight) {
                min = weight;
                minI = it;
            }
        }
        return minI;
    }

}
