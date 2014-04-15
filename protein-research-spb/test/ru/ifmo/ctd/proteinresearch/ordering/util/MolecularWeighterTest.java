package ru.ifmo.ctd.proteinresearch.ordering.util;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * ansokolmail@gmail.com
 */
public class MolecularWeighterTest extends TestCase {
    public void testSection() throws Exception {
        double[] array = {0, 1, 2, 3, 4, 5, 5.5, 7};
        Assert.assertEquals(2, MolecularWeighter.section(array, 2.1));
    }
}
