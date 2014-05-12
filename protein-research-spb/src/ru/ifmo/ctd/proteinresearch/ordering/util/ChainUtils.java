package ru.ifmo.ctd.proteinresearch.ordering.util;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;

import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class ChainUtils {
    public static List<Atom> toAtomList(Chain chain) {
        List<Group> groups = chain.getAtomGroups();
        return null;
    }
}
