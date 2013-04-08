package ru.ifmo.ctd.proteinresearch.ordering.intersections;

public class SegmentsOperations {
	/*
	 * a1, b1 - starting position of the first segment;
	 * a2, b2 - ending position of the first segment;
	 * c1, c2 - starting position of the first segment;
	 * d1, d2 - ending position of the first segment;
	 */
	private boolean movingSegmentsIntersection(Point a1, Point b1, Point a2, Point b2, Point c1, Point c2, Point d1, Point d2){
		double v1 = (a1.x-d1.x)*(c1.y-d1.y)*(b1.z-d1.z)+
				(a1.y-d1.y)*(c1.z-d1.z)*(b1.x-d1.x)+
				(a1.z-d1.z)*(c1.x-d1.x)*(b1.y-d1.y)-
				(a1.x-d1.x)*(c1.z-d1.z)*(b1.y-d1.y)-
				(a1.y-d1.y)*(c1.x-d1.x)*(b1.z-d1.z)-
				(a1.z-d1.z)*(c1.y-d1.y)*(b1.x-d1.x);
		double v2 = (a2.x-d2.x)*(c2.y-d2.y)*(b2.z-d2.z)+
				(a2.y-d2.y)*(c2.z-d2.z)*(b2.x-d2.x)+
				(a2.z-d2.z)*(c2.x-d2.x)*(b2.y-d2.y)-
				(a2.x-d2.x)*(c2.z-d2.z)*(b2.y-d2.y)-
				(a2.y-d2.y)*(c2.x-d2.x)*(b2.z-d2.z)-
				(a2.z-d2.z)*(c2.y-d2.y)*(b2.x-d2.x);
		return v1*v2<0;
	}
}
