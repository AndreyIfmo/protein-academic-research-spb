package ru.ifmo.ctd.proteinresearch.ordering.util;

import ru.ifmo.ctd.proteinresearch.intersections.Point;

/**
 * Created by AndreyS on 16.04.2014.
 */
public class SinCos {
    public final double cos;
    public final double sin;
    public final double angle;
    public SinCos(Point a, Point b) {
        cos = (a.scalarMult(b)/(a.distance()*b.distance()));
        sin = 1-cos*cos;
        angle = Math.acos(cos);
    }

    public SinCos(double angle) {
        this.angle=angle;
        sin = Math.sin(angle);
        cos = Math.cos(angle);
    }
}
