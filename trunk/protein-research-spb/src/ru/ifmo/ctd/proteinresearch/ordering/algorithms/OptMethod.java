package ru.ifmo.ctd.proteinresearch.ordering.algorithms;

import ru.ifmo.ctd.proteinresearch.ordering.util.Function;

/**
 * ansokolmail@gmail.com
 */
public class OptMethod {

    public static double upgradedTriSearch(Function<Double, Double> f, double left, double right, double tolerance, int n) throws Exception {
        double max = 0;
        double answer = right;
        double step = ((left + right) / n)-left;
        for (double i = left; i<right;i+=step) {
            double curArg = triSearch(f, i, i + step, tolerance);
            double curVal = f.apply(curArg);
            if (max < curVal) {
                max = curVal;
                answer = curArg;
            }
        }
        return answer;
    }

    public static double triSearch(Function<Double, Double> f, double left, double right, double tolerance) throws Exception {
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
