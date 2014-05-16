package ru.ifmo.ctd.proteinresearch.ordering.clustering;


import java.util.Arrays;

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


    @Override
    public int compareTo(ClusteringAnswer o) {
        return Double.compare(value, o.value);
    }

    @Override
    public String toString() {
        return value + " " + Arrays.toString(centers);
    }
}
