package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;

import java.util.List;

/**
 * Created by Andrey on 24.12.13.
 */
public interface Clusterizer {
    public void evaluate(List<Chain> chains);
    public List<Cluster> getClusters();
}
