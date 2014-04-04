package ru.ifmo.ctd.proteinresearch.ordering.util;

import org.biojava.bio.structure.ChainImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AndreyS on 04.04.14.
 */
public class MolecularWeighter {
    private static final Map<String, Double> myMap;
    static
    {
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
        myMap.put("ser", 101.1039);
        myMap.put("sec", 150.0379);


    }
    public static double energy (ChainImpl chain1, ChainImpl chain2) {

        return 0;
    }

    public double weight( String atomName, String residueName, boolean isFirst, boolean isLast) {
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
