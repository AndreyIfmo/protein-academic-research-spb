package ru.ifmo.ctd.proteinresearch.ordering.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class FunctionalUtils {
    public static <E, R> List<R> map (Collection<E> collection, Function<E, R> function) {
        List<R> list = new ArrayList<>();
        for (E iterator:collection) {
            list.add(function.apply(iterator));
        }
        return list;
    }
}
