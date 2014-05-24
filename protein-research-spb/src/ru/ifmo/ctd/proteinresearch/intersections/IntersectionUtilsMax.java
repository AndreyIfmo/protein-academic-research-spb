package ru.ifmo.ctd.proteinresearch.intersections;

public class IntersectionUtilsMax {
	static double eps = 1e-9;

	public static double distanceBetweenSegments(Point p1, Point p2, Point q1, Point q2) {
		Point u = p2.sub(p1);
		Point v = q2.sub(q1);
		Point w = p1.sub(q1);

		Point a = u.mult(u);
		Point b = u.mult(v);
		Point c = v.mult(v);
		Point d = u.mult(w);
		Point e = v.mult(w);

		Double det = a.scalarMult(c) - b.scalarMult(b);

		if (det < eps) {
			Point q1p1 = q1.sub(p1);
			Point q1p2 = q1.sub(p2);
			Point q2p1 = q2.sub(p1);
			Point q2p2 = q2.sub(p2);

			double qwe1 = ((q1p1.mult(v)).mult((q2p1.mult(v))).distance() >= 0) ? Double.POSITIVE_INFINITY : (q1p1.vectorMult(q2p1)).coordSqared() / v.coordSqared();
			double qwe2 = ((q1p2.mult(v)).mult((q2p2.mult(v))).distance() >= 0) ? Double.POSITIVE_INFINITY : (q1p2.vectorMult(q2p2)).coordSqared() / v.coordSqared();
			double qwe3 = ((q1p1.mult(v)).mult((q1p2.mult(v))).distance() >= 0) ? Double.POSITIVE_INFINITY : (q1p1.vectorMult(q1p2)).coordSqared() / v.coordSqared();
			double qwe4 = ((q2p1.mult(v)).mult((q2p2.mult(v))).distance() >= 0) ? Double.POSITIVE_INFINITY : (q2p2.vectorMult(q2p2)).coordSqared() / v.coordSqared();
			double min1 = Math.min(Math.min(q1p1.coordSqared(), q2p1.coordSqared()), Math.min(q1p2.coordSqared(), q2p2.coordSqared()));
			double min2 = Math.min(Math.min(Math.min(qwe1, qwe2), Math.min(qwe3, qwe4)), min1);

			return Math.sqrt(min2);
		} else {
			Double sc = (((b.mult(e)).sub(c.mult(d))).divide(det)).distance();
			Double tc = (((a.mult(e)).sub(b.mult(d))).divide(det)).distance();

			Point ps = sc < eps ? p1 : (sc >= 1 - eps ? p2 : p1.add((u.mult(sc))));
			Point pt = tc < eps ? p1 : (tc >= 1 - eps ? p2 : p1.add((u.mult(tc))));

			return (ps.sub(pt)).distance();

		}
	}
}
