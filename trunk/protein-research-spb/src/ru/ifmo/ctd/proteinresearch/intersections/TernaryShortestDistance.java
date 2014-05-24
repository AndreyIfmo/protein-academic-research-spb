package ru.ifmo.ctd.proteinresearch.intersections;

public class TernaryShortestDistance {
	private Point p31;
	private Point p42;
	private Point q31;
	private Point q42;
	private Point pStart1;
	private Point pStart2;
	private Point pFinish1;
	private Point pFinish2;
	private Point qStart1;
	private Point qStart2;
	private Point qFinish1;
	private Point qFinish2;
	private double seekDistancesBelow;

	private double fun(Double t) {
		return IntersectionUtilsMax.distanceBetweenSegments(pStart1.add(p31.mult(t)), pStart2.add(p42.mult(t)), qStart1.add(q31.mult(t)), qStart2.add(q42.mult(t)));
	}

	private double ternary(Double left, Double right, Integer remains) {
		if (remains == null)
			remains = 10;
		if (remains == 0) {
			return fun((left + right) / 2);
		} else {
			double midL = (left * 2 + right) / 3;
			double midR = (left + 2 * right) / 3;
			if (fun(midL) < fun(midR)) {
				return ternary(left, midR, remains - 1);
			} else {
				return ternary(midL, right, remains - 1);
			}
		}
	}

	public double computeShortestDistanceForSegments(Point pStart1, Point pStart2, Point pFinish1, Point pFinish2, Point qStart1, Point qStart2, Point qFinish1, Point qFinish2, Double seekDistancesBelow) {
		this.pStart1 = pStart1;
		this.pStart2 = pStart2;
		this.pFinish1 = pFinish1;
		this.pFinish2 = pFinish2;
		this.qStart1 = qStart1;
		this.qStart2 = qStart2;
		this.qFinish1 = qFinish1;
		this.qFinish2 = qFinish2;
		this.seekDistancesBelow = seekDistancesBelow;

		p31 = pFinish1.sub(pStart1);
		p42 = pFinish2.sub(pStart2);
		q31 = qFinish1.sub(qStart1);
		q42 = qFinish2.sub(qStart2);

		int intervals = 10;

		double minDist = Double.POSITIVE_INFINITY;
		double minLoc = 0;

		for (int i = 0; i <= 10; i++) {
			double k = 0.0;
			double tmpDist = fun(k);
			if (tmpDist < minDist) {
				minDist = tmpDist;
				minLoc = i;
			}
		}

		if (minDist > 1.5 * seekDistancesBelow) {
			return minDist;
		} else {
			return Math.min((minLoc == 0 ? Double.POSITIVE_INFINITY : ternary((minLoc - 1.0) / intervals, (minLoc - 0.0) / intervals, null)), (minLoc == intervals ? Double.POSITIVE_INFINITY : ternary((minLoc + 0.0) / intervals, (minLoc + 1.0) / intervals, null)));
		}
	}
}
