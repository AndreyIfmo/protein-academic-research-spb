package ru.ifmo.ctd.proteinresearch.ordering.util;

import org.biojava.bio.structure.StructureException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class FunctionalUtils {
    public static <E, R> List<R> map(Collection<E> collection, Function<E, R> function) throws Exception {
        List<R> list = new ArrayList<>();
        for (E iterator : collection) {
            list.add(function.apply(iterator));
        }
        return list;
    }

    public static double sum(double [] arg) {
        double sum = 0;
        for (int i=0; i<arg.length; i++) {
            sum +=arg[i];
        }
        return sum;
    }
}
