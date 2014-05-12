package ru.ifmo.ctd.proteinresearch.ordering.util;

import org.biojava.bio.structure.StructureException;

import java.io.FileNotFoundException;

/**
 * ansokolmail@gmail.com
 */
public interface Function<A, R> {
    public R apply(A argument) throws Exception;
}
