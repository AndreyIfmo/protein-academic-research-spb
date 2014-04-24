package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class PathUtilTest extends TestCase {
    public void testGetAtoms() throws Exception {
        ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        ConformationChain cc = cg.getChain(0,1);
        int number = EvaluatedChain.getAtoms(cc.getStructure().getChain(0)).size();
        Assert.assertEquals(198, number);
    }
    public void testWriteChain() throws Exception {
        ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        ConformationChain cc = cg.getChain(0,9);
        Structure structure = cc.getStructure();
        Chain chain1 = structure.getModel(0).get(0);
        Chain chain2 = structure.getModel(1).get(0);
        List<Chain> chainList = PathUtil.getInterpolatedChains(chain1, chain2, 50);
        
    }
}
