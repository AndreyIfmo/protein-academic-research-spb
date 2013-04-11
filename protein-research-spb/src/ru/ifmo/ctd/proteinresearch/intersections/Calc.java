package ru.ifmo.ctd.proteinresearch.intersections;

public interface Calc {
	public int hasIntersections();
	public boolean movingSegmentsIntersection(Point a1, Point b1, Point a2, Point b2, Point c1, Point c2, Point d1, Point d2);
	//public void Initalize(Object ... objects);
}
