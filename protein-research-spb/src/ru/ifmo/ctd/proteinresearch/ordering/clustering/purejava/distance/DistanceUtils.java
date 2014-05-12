package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance;

import org.biojava.bio.structure.Atom;

/**
 * Created by Andrey on 24.12.13.
 */
public class DistanceUtils {
    public static double distanceSquared(Atom atom1, Atom atom2) {
        return (atom1.getX() - atom2.getX()) * (atom1.getX() - atom2.getX()) +
                (atom1.getY() - atom2.getY()) * (atom1.getY() - atom2.getY()) +
                (atom1.getZ() - atom2.getZ()) * (atom1.getZ() - atom2.getZ());
    }

    public static double distance(Atom atom1, Atom atom2) {
        return Math.sqrt(distanceSquared(atom1, atom2));
    }

}
