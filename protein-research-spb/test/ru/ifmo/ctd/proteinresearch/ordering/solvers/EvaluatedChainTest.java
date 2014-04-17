package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.FileConvert;
import ru.ifmo.ctd.proteinresearch.intersections.Point;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndreyS on 17.04.2014.
 */
public class EvaluatedChainTest extends TestCase {
    public void testRestorePoints() throws Exception {
        ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        ConformationChain cc = cg.getChain(0,1);
        Chain chain = cc.getStructure().getChain(0);
        EvaluatedChain ec = new  EvaluatedChain(chain);
        List<Point> points = ec.restorePoints();
        Chain temp = (Chain) chain.clone();
        int counter=0;
        for (Group group: temp.getAtomGroups()) {
            for (Atom atom: group.getAtoms()) {
                atom.setX(points.get(counter).x);
                atom.setY(points.get(counter).y);
                atom.setZ(points.get(counter).z);
                counter++;
            }
        }
        temp=ConformationChain.align(chain, temp);
        points=EvaluatedChain.toPoints(EvaluatedChain.getAtoms(temp));
        counter=0;
        for (Group group: chain.getAtomGroups()) {
            for (Atom atom: group.getAtoms()) {
                atom.setX(points.get(counter).x);
                atom.setY(points.get(counter).y);
                atom.setZ(points.get(counter).z);
                counter++;
            }

        }
        //temp.setParent(chain.getParent());
        temp=ConformationChain.align(temp, chain);
        List<Atom> newAtoms = EvaluatedChain.getAtoms(temp);
        int cou=0;
        for (Group group: chain.getAtomGroups()) {
            for (Atom atom : group.getAtoms()) {
                atom.setCoords(newAtoms.get(cou).getCoords());
                cou++;
            }
        }
        PrintWriter pw = new PrintWriter("aaa.pdb");
        StringBuffer sb=new StringBuffer();
        for (Group group:chain.getAtomGroups()) {
            for (Atom atom: group.getAtoms()) {
                FileConvert.toPDB(atom, sb);
            }
        }
        pw.print(sb.toString());

        pw.close();
        Assert.assertEquals(chain.getAtomGroups().get(8).getAtom(0).getX(), temp.getAtomGroups().get(8).getAtom(0).getX());
    }
}
