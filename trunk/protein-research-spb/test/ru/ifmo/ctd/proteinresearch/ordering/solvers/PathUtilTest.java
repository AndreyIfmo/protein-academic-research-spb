package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * ansokolmail@gmail.com
 */
public class PathUtilTest extends TestCase {
    public void testGetAtoms() throws Exception {
        ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        ConformationChain cc = cg.getChain(0,1);
        int number = PathUtil.getAtoms(cc.getStructure().getChain(0)).size();
        Assert.assertEquals(198, number);
    }
}
