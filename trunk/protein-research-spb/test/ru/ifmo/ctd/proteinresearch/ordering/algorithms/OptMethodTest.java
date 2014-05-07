package ru.ifmo.ctd.proteinresearch.ordering.algorithms;

import junit.framework.Assert;
import junit.framework.TestCase;
import ru.ifmo.ctd.proteinresearch.ordering.util.Function;

public class OptMethodTest extends TestCase {

    public void testUpgradedTriSearch() throws Exception {
        double answer=OptMethod.upgradedTriSearch(new Function<Double, Double>() {
            @Override
            public Double apply(Double argument) throws Exception {
                return argument;
            }
        }, 0, 3, 0.0000001, 5);
        Assert.assertEquals(3, answer);
    }

    public void testTriSearch() throws Exception {
        double answer=OptMethod.triSearch(new Function<Double, Double>() {
            @Override
            public Double apply(Double argument) throws Exception {
                return argument;
            }
        }, 0, 3, 0.0000001);
        Assert.assertEquals(3, answer);
    }
}