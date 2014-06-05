package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Andrey on 24.12.13.
 */
public class Cluster<T> {
    public Set<T> chainSet;

    public Cluster() {
        this.chainSet = new HashSet<T>();
    }


    public Cluster(List<T> data) {
        this.chainSet = new HashSet<T>(data);
    }

    public boolean add(T chain) {
        return chainSet.add(chain);
    }

    public void add(Cluster<T> inputCluster) {
        chainSet.addAll(inputCluster.chainSet);
    }
}
