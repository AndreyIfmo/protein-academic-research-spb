package ru.ifmo.ctd.proteinresearch.ordering.clustering;

import org.biojava.bio.structure.Atom;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.StructureException;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * @param vector mask
     * @return next mask 1001 -> 1010
     */
    public static boolean next(boolean[] vector) {
        int i = vector.length - 1;
        while (vector[i] && i > 0) {
            vector[i] = false;
            i--;
        }
        if (i == 0 && vector[i]) {
            return false;
        }
        vector[i] = true;
        return true;
    }

    /**
     * return centers
     *
     * @param vector - mask
     * @return center array
     */

    public static int[] getCenters(boolean[] vector, int length) {
        int[] centers = new int[length];
        int counter = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i]) {
                centers[counter] = i;
                counter++;
            }
        }
        return centers;
    }

    /**
     * @param vector - vector
     * @return number Of 1
     */
    public static int numOfElements(boolean[] vector) {
        int counter = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i]) {
                counter++;
            }
        }
        return counter;
    }

    public static int[] next(int[] arg, int base) {
        int[] answer = Arrays.copyOf(arg, arg.length);
        next(answer, base, answer.length - 1);
        return answer;
    }

    public static void next(int[] arg, int base, int curPos) {
        if (curPos != -1) {
            if (arg[curPos] == base - 1) {
                arg[curPos] = 0;
                next(arg, base, curPos - 1);
            } else {
                arg[curPos]++;
            }
        }
    }

    public static boolean isAll1(int[] arg, int base) {

        for (int i : arg) {
            if (i != base) {
                return false;
            }
        }
        return true;
    }
}
