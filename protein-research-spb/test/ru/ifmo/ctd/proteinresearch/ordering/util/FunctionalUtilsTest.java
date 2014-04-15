package ru.ifmo.ctd.proteinresearch.ordering.util;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ansokolmail@gmail.com
 */
public class FunctionalUtilsTest extends TestCase {
    public void testMap() throws Exception {
        List<Integer> list = new ArrayList<Integer>();
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);
        List<Integer> list2=new FunctionalUtils<Integer,Integer>().map(list, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer argument) {
                return 6-argument;
            }
        });
        Assert.assertEquals(new Integer(1), list2.get(0));
    }

    public void testJDK8() throws Exception {
        List<Integer> list = new ArrayList<Integer>();
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);
        Stream<Integer> list2 = list.stream()
                .map(person -> 6 - person);
        Assert.assertEquals(new Integer(1), list2.collect(Collectors.toList()).get(0));
    }


}
