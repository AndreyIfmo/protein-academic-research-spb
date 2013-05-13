package ru.ifmo.ctd.proteinresearch.intersections;

public interface Calc {
	public int hasIntersections() throws Exception;
	public boolean movingSegmentsIntersection(Point a1, Point b1, Point a2, Point b2, Point c1, Point c2, Point d1, Point d2) throws Exception;
	// public void Initalize(Object ... objects);
}
