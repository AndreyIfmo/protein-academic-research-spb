package ru.ifmo.ctd.proteinresearch.ordering.util;

import org.biojava.bio.structure.*;
import ru.ifmo.ctd.proteinresearch.intersections.Point;
import ru.ifmo.ctd.proteinresearch.intersections.PointsChain;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.DistanceUtils;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AndreyS on 04.04.14.
 */
public class MolecularWeighter {
    private static final Map<String, Double> myMap;

    static {
        myMap = new HashMap<String, Double>();
        myMap.put("ala", 71.0779);
        myMap.put("arg", 156.1857);
        myMap.put("asn", 114.1026);
        myMap.put("asp", 115.0874);
        myMap.put("cys", 103.1429);
        myMap.put("glu", 129.114);
        myMap.put("gln", 128.1292);
        myMap.put("gly", 57.0513);
        myMap.put("his", 137.1393);
        myMap.put("ile", 113.1576);
        myMap.put("leu", 113.1576);
        myMap.put("lys", 128.1723);
        myMap.put("met", 131.1961);
        myMap.put("phe", 147.1739);
        myMap.put("pro", 97.1152);
        myMap.put("ser", 87.0773);
        myMap.put("thr", 101.1039);
        myMap.put("sec", 150.0379);
        myMap.put("trp", 186.2099);
        myMap.put("tyr", 163.1733);
        myMap.put("val", 99.1311);


    }

    public static double energy(ConformationChain conformationChain) throws StructureException {
        Structure structure = conformationChain.getStructure();
        int length = structure.nrModels();
        double w = 0;
        for (int i = 0; i < length - 1; ++i) {
            Chain chain1 = structure.getModel(i).get(0);
            Chain chain2 = structure.getModel(i + 1).get(0);
            w += energy(chain1, chain2);
        }
        return w;
    }

    public static double energy(Chain chain1, Chain chain2) {
        List<Group> atomGroups1 = chain1.getAtomGroups();
        int size = atomGroups1.size();
        List<Group> atomGroups2 = chain2.getAtomGroups();
        assert size == atomGroups2.size();
        boolean isFirst = true;
        boolean isLast = false;
        double totalWeight = 0;
        for (int i = 0; i < atomGroups1.size(); i++) {
            isLast = (i == atomGroups1.size() - 1);
            List<Atom> atoms1 = atomGroups1.get(i).getAtoms();
            List<Atom> atoms2 = atomGroups2.get(i).getAtoms();
            assert atoms1.size() == 3;
            Atom atom10 = atoms1.get(0);
            Atom atom20 = atoms2.get(0);
            Atom atom11 = atoms1.get(1);
            Atom atom21 = atoms2.get(1);
            Atom atom12 = atoms1.get(2);
            Atom atom22 = atoms2.get(2);
            double weight1 = weight(atom10.getName(), atomGroups1.get(i).getPDBName(), isFirst, isLast) * DistanceUtils.distance(atom10, atom20);
            double weight2 = weight(atom11.getName(), atomGroups1.get(i).getPDBName(), isFirst, isLast) * DistanceUtils.distance(atom11, atom21);
            double weight3 = weight(atom12.getName(), atomGroups1.get(i).getPDBName(), isFirst, isLast) * DistanceUtils.distance(atom12, atom22);
            totalWeight += weight1;
            totalWeight += weight2;
            totalWeight += weight3;
            isFirst = false;
        }
        return totalWeight;
    }

    public static double weight(String atomName, String residueName, boolean isFirst, boolean isLast) {
        //see http://spin.niddk.nih.gov/bax/software/TALOSORIG/backbone.gif
        switch (atomName) {
            case "C":
                return 12.0107 + 15.9994 + ((isLast) ? (14.00674 +
                        1.00794) : 0.0);
            case "N":
                return 14.00674 + 1.00794 + ((isFirst) ? 3 * 1.00794 : 0.0);
            case "CA":
                return 2 * 12.0107 + 1.00794 + myMap.get(residueName.toLowerCase());
            default:
                throw new IllegalArgumentException(atomName);
        }
    }
}
