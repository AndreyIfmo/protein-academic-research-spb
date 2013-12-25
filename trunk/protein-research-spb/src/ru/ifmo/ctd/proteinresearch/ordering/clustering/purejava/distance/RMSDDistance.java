package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.StructureException;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 24.12.13.
 */
public class RMSDDistance implements Distance {
    @Override
    public double distance(Chain chain1, Chain chain2) {
        try {
            chain2 = ConformationChain.align(chain1, chain2);
        } catch (StructureException e) {

            e.printStackTrace();
        }
        List<Group> groupList1 = chain1.getAtomGroups();
        List<Group> groupList2 = chain2.getAtomGroups();
        List<Atom> allAtoms1 = new ArrayList<>();
        List<Atom> allAtoms2 = new ArrayList<>();
        if (groupList1.size() != groupList2.size()) {
            throw new AssertionError("arg1.size()!=arg2.size()");
        }
        for (Group groupIt : groupList1) {
            allAtoms1.addAll(groupIt.getAtoms());
        }
        for (Group groupIt : groupList2) {
            allAtoms2.addAll(groupIt.getAtoms());
        }
        int n = allAtoms1.size();
        if (n != allAtoms2.size()) {
            throw new IllegalArgumentException("List sizes should be equals. List 1 size: " + groupList1.size() + "list 2 size:" + groupList2.size());
        }
        double result = 0.0;
        for (int i = 0; i < n; i++) {
            result += DistanceUtils.distanceSquared(allAtoms1.get(i), allAtoms2.get(i));
        }
        return Math.sqrt(result / n);
    }
}
