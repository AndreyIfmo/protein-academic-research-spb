package ru.ifmo.ctd.proteinresearch.ordering.clustering;

import com.sun.istack.internal.Nullable;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by AndreyS on 02.05.2014.
 */
public class SimpleSampling {
    double[][] distanceMatrix;

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
        this.distanceMatrix = GraphParser.parseGraphMatrix(fileName);
    }

    /**
     * @return clusterCenters
     */
    @Nullable
    private int[] evaluate(int numOfClusters) {
        int[] centers = null;
        double maxOfDist = Double.MIN_VALUE;
        int[] vector = new int[numOfClusters];
        int[] curCenters;
        while (!ClusteringUtils.isAll1(vector, distanceMatrix.length - 1)) {
            curCenters = vector;
            for (int i = 0; i < distanceMatrix.length; i++) {
                double distI = dist(i, curCenters);
                if (maxOfDist < distI) {
                    maxOfDist = distI;
                    centers = curCenters;
                }
            }
            vector = ClusteringUtils.next(vector, distanceMatrix.length);
            System.out.println(Arrays.toString(vector));
        }
        return centers;
    }

    public static void main(String[] args) throws IOException {
        System.out.print(Arrays.toString(new SimpleSampling("2m3m.txt").evaluate(3)));
    }

}
