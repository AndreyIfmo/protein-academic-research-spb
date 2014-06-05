package ru.ifmo.ctd.proteinresearch.ordering.clustering;


import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.Cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrey Sokolov on 16.05.2014.
 */
public class ClusteringAnswer implements Comparable<ClusteringAnswer> {
    final double value;
    final int[] centers;

    public ClusteringAnswer(double value, int[] centers) {
        this.value = value;
        this.centers = centers;
    }


    public List<Integer>[] getClusters(double[][] weights) {
        List<Integer>[] clusters = new List[centers.length];
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = new ArrayList<>();
        }
        for (int i = 0; i < weights.length; i++) {
            clusters[SimpleSampling.closes(weights, i, centers)].add(i);
        }
        return clusters;
    }
    public static List<Integer>[] getClusters(double[][] weights, int[] ans) {
        List<Integer>[] clusters = new List[ans.length];
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = new ArrayList<>();
        }
        for (int i = 0; i < weights.length; i++) {
            clusters[SimpleSampling.closes(weights, i, ans)].add(i);
        }
        return clusters;
    }


    @Override
    public int compareTo(ClusteringAnswer o) {
        return Double.compare(value, o.value);
    }

    @Override
    public String toString() {
        return value + " " + Arrays.toString(centers);
    }

    public List<Cluster> toClusters (double[][] weights) {
        List<Cluster> answer = new ArrayList<>();
        List<Integer>[] clusters = getClusters(weights);
        for (int i=0; i<clusters.length; i++) {
            answer.add(new Cluster<>(clusters[i]));
        }
        return answer;
    }
}
