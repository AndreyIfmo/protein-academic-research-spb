package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andrey on 24.12.13.
 */
public class Cluster {
    Set<DataChain> chainSet;

    public Cluster() {
        this.chainSet = new HashSet<DataChain>();
    }
    public boolean add(DataChain chain) {
        return chainSet.add(chain);
    }

    public void add (Cluster inputCluster) {
        chainSet.addAll(inputCluster.chainSet);
    }
}
