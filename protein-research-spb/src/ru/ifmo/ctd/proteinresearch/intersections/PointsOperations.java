package ru.ifmo.ctd.proteinresearch.intersections;

/**
 * @author Smetannikov
 */
public class PointsOperations {
    public double distance(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
    }
}
