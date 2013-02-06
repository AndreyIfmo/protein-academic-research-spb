package ru.ifmo.ctd.proteinresearch.ordering;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 06.02.13
 *         Time: 16:13
 */
public class BitUtils {
    public static boolean getNBit(long a, int n) {
        a = a >> n;
        a = a & 1;
        return a > 0;
    }

    public static int setNBit(int a, int pos, boolean value) {
        if (value) {
            return (a | (1 << pos));
        } else {
            return (a & (-(1 << pos)));
        }
    }


}
