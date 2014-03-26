package ru.ifmo.ctd.proteinresearch.ordering.algorithms;

import org.biojava.bio.structure.StructureException;

import java.io.FileNotFoundException;

/**
 * Created by AndreyS on 26.03.14.
 */
public interface Function<A, R> {
    public R apply (A argument);
}
