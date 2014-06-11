package ru.ifmo.ctd.proteinresearch.ordering.util;

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

    public static List<Integer> list(Integer... arg) {
        List<Integer> answer = new ArrayList<Integer>();
        for (int i:arg) {
            answer.add(i);
        }
        return answer;
    }


    public static List<Integer> complList(List<Integer> arg, int maxN) {
        List<Integer> answer = new ArrayList<>();
        for (int i=0;i<maxN; i++) {
            if (!arg.contains(i)) {
                answer.add(i);
            }
        }
        return answer;
    }

}
