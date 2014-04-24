package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.biojava.bio.structure.*;
import org.biojava.bio.structure.io.FileConvert;

import java.io.PrintWriter;
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


}
