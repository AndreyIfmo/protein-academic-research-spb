package ru.ifmo.ctd.proteinresearch.ordering.util;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class FunctionalUtilsTest extends TestCase {
    public void testMap() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);
        List<Integer> list2=FunctionalUtils.map(list, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer argument) {
                return 6-argument;
            }
        });
        Assert.assertEquals(new Integer(1), list2.get(0));
    }

    public void testListFunctions() {
        FunctionalUtils.list(1,5,3);
        Assert.assertEquals(FunctionalUtils.list(0,2,4),FunctionalUtils.complList(FunctionalUtils.list(1,5,3),5));
    }

}
