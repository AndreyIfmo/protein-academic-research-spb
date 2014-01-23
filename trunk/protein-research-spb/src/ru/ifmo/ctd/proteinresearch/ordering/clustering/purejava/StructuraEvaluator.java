package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;

import java.util.List;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 23.01.14
 *         Time: 21:41
 */
public interface StructuraEvaluator {
    void evaluate(List<Chain> chains);
}
