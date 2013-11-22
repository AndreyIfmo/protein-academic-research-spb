package ru.ifmo.ctd.proteinresearch.ordering.clustering;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.StructureException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 22.11.13
 *         Time: 3:09
 */
public class ClusteringUtils {
    public static double[] toArray(Chain chain1) throws StructureException {
        List<Group> groupList1 = chain1.getAtomGroups();
        List<Atom> allAtoms1 = new ArrayList<>();
        for (Group groupIt : groupList1) {
            allAtoms1.addAll(groupIt.getAtoms());
        }
        int n = allAtoms1.size();
        double[] array = new double[3 * n];
        for (int i = 0; i < n; i++) {
            array[3 * i] = allAtoms1.get(i).getX();
            array[3 * i + 1] = allAtoms1.get(i).getY();
            array[3 * i + 2] = allAtoms1.get(i).getZ();
        }
        return array;
    }
}
