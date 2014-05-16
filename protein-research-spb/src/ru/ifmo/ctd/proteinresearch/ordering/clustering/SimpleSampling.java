package ru.ifmo.ctd.proteinresearch.ordering.clustering;

import com.sun.istack.internal.Nullable;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;

import java.io.IOException;
import java.util.*;

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
    private List<ClusteringAnswer> evaluate(int numOfClusters) {
        List<ClusteringAnswer> answer = new ArrayList<>();
        int[] centers = null;
        double maxOfDist = Double.MIN_VALUE;
        int[] vector = new int[numOfClusters];
        int[] curCenters;
        while (!ClusteringUtils.isAll1(vector, distanceMatrix.length - 1)) {
            curCenters = vector;
            Set<Integer> setCenters = new HashSet<>();
            for (int it : curCenters) {
                setCenters.add(it);
            }
           // if (setCenters.size() == curCenters.length) {
                for (int i = 0; i < distanceMatrix.length; i++) {
                    double distI = dist(i, curCenters);
                    answer.add(new ClusteringAnswer(distI, curCenters));
                    if (maxOfDist < distI) {
                        maxOfDist = distI;
                        centers = curCenters;
                    }
                }
            //}
            vector = ClusteringUtils.next(vector, distanceMatrix.length);
            //   System.out.println(Arrays.toString(vector));
        }
        Collections.sort(answer);
        return answer;
    }

    public static void main(String[] args) throws IOException {
        Object[] answer =  new SimpleSampling("2LJI_optim_costs.txt").evaluate(3).toArray();
        for (Object it: answer) {
            System.out.println(it);
        }
    }

}
