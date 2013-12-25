package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance;

import org.biojava.bio.structure.Chain;

/**
 * Created by Andrey on 24.12.13.
 */
public interface Distance {
    public double distance(Chain chain1, Chain chain2);
}
