package ru.ifmo.ctd.proteinresearch.ordering.algorithms;

import ru.ifmo.ctd.proteinresearch.ordering.util.Function;

/**
 * ansokolmail@gmail.com
 */
public class OptMethod {

    public static double upgradedTriSearch(Function<Double, Double> f, double left, double right, double tolerance, int n) throws Exception {
        double max = 0;
        double answer = right;
        double step = ((left + right) / n) - left;
        for (double i = left; i < right; i += step) {
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
        double minArg = Double.MAX_VALUE;
        double minValue = Double.MAX_VALUE;
        while (right - left > tolerance) {
            double m1 = left + (right - left) / 3,
                    m2 = right - (right - left) / 3;
            Double fm1 = f.apply(m1);
            Double fm2 = f.apply(m2);
            if (fm1 < fm2) {
                left = m1;
//                returnRight = false;
                if (minValue> fm1) {
                    minValue=fm1;
                    minArg = m1;
                }
            } else {
                right = m2;
               // returnRight = true;
                if (minValue> fm2) {
                    minValue=fm2;
                    minArg = m2;
                }
            }
        }
        return minArg;
    }

    public static double PHI = (1+Math.sqrt(5)+1)/2;

    public static double gold(Function<Double, Double> f, double a, double b, double eps) throws Exception {

        while (Math.abs(b-a)>eps) {
            double x1 = b - (b - a) / PHI;
            double x2 = a + (b - a) / PHI;
            double y1 = f.apply(x1);
            double y2 = f.apply(x2);
            if (y1 <= y2) {
                a = x1;
            }
        }
        return (a+b)/2;
    }
}
