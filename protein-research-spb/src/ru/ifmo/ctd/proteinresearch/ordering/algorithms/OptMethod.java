package ru.ifmo.ctd.proteinresearch.ordering.algorithms;

/**
 * Created by AndreyS on 26.03.14.
 */
public class OptMethod {


    public static double triSearch(Function<Double, Double> f, double left, double right, double tolerance) {
        boolean returnRight = false;
        while (right - left > tolerance) {
            double m1 = left + (right - left) / 3,
                    m2 = right - (right - left) / 3;
            if (f.apply(m1) < f.apply(m2)) {
                left = m1;
                returnRight=false;
            } else {
                right = m2;
                returnRight=true;
            }
        }
        return returnRight?right:left;
    }

}
