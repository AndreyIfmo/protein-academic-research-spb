package ru.ifmo.ctd.proteinresearch.intersections;

public abstract class AbstractCalc implements Calc {
	public ChainSequence chains;
	public Point Omin = null;
	public Point Omax = null;
	public TernaryShortestDistance distanceCalc = new TernaryShortestDistance();

	public AbstractCalc(ChainSequence chains) {
		this.chains = chains;
	}

	@Override
	public int hasIntersections() throws Exception {
		return 0;
	}

	boolean movingTooFar(Point a1, Point b1, Point a2, Point b2) {
		double length = (b1.x - a1.x) * (b1.x - a1.x) + (b1.y - a1.y) * (b1.y - a1.y) + (b1.z - a1.z) * (b1.z - a1.z);
		double dist1 = (a2.x - a1.x) * (a2.x - a1.x) + (a2.y - a1.y) * (a2.y - a1.y) + (a2.z - a1.z) * (a2.z - a1.z);
		double dist2 = (b1.x - b2.x) * (b1.x - b2.x) + (b1.y - b2.y) * (b1.y - b2.y) + (b1.z - b2.z) * (b1.z - b2.z);
		double dist = Math.max(dist1, dist2);

		return dist > 16 * length; // invariant check
	}

	boolean segmentsHasIntersections(Point a1, Point b1, Point a2, Point b2, Point c1, Point c2, Point d1, Point d2) throws Exception {
		return movingSegmentsIntersection2Spheres(a1, b1, a2, b2, c1, c2, d1, d2);

//		double dist = distanceCalc.computeShortestDistanceForSegments(a1, b1, a2, b2, c1, c2, d1, d2, 0.1);
//		if (dist < 0.1) {
//			return true;
//		}

//		if (movingSegmentsIntersection2Spheres(a1, b1, a2, b2, c1, c2, d1, d2)) {
//			double dist = distanceCalc.computeShortestDistanceForSegments(a1, b1, a2, b2, c1, c2, d1, d2, 0.1);
//			if (dist < 0.1) {
//				return true;
//			}
// }

//		return false;
	}

	int intersectAll(int index, Point[] first, Point[] second) throws Exception {
		// int functionCalculations = 0;
		int intersections = 0;

		Point[] firstPoints = getEdgePoints(first[index - 1], first[index], second[index - 1], second[index]);
		for (int k = 1; k != first.length; ++k) {
			if (k == index) {
				continue;
			}

			TernaryShortestDistance distanceCalc = new TernaryShortestDistance();

			Point[] secondPoints = getEdgePoints(first[k - 1], first[k], second[k - 1], second[k]);
			if (movingSegmentsAreClose(firstPoints, secondPoints) == true) {
				// ++functionCalculations;
				// if (movingSegmentsIntersection2Spheres(first[index - 1],
				// first[index], second[index - 1], second[index], first[k - 1],
				// first[k], second[k - 1], second[k]) == true) {
				// ++intersections;
				// }
				double dist = distanceCalc.computeShortestDistanceForSegments(first[index - 1], first[index], second[index - 1], second[index], first[k - 1], first[k], second[k - 1], second[k], 0.1);
				if (dist < 0.1) {
					++intersections;
				}
			}

		}
		return intersections;
	}

	private boolean movingSegmentsAreClose(Point[] firstCube, Point[] secondCube) {
		if (pointInBetween(firstCube, secondCube[0].x, secondCube[0].y, secondCube[0].z))
			return true;
		if (pointInBetween(firstCube, secondCube[0].x, secondCube[0].y, secondCube[1].z))
			return true;
		if (pointInBetween(firstCube, secondCube[1].x, secondCube[0].y, secondCube[1].z))
			return true;
		if (pointInBetween(firstCube, secondCube[1].x, secondCube[0].y, secondCube[0].z))
			return true;
		if (pointInBetween(firstCube, secondCube[0].x, secondCube[1].y, secondCube[0].z))
			return true;
		if (pointInBetween(firstCube, secondCube[0].x, secondCube[1].y, secondCube[1].z))
			return true;
		if (pointInBetween(firstCube, secondCube[1].x, secondCube[1].y, secondCube[1].z))
			return true;
		if (pointInBetween(firstCube, secondCube[1].x, secondCube[1].y, secondCube[0].z))
			return true;
		return false;
	}

	private boolean pointInBetween(Point[] cube, double x, double y, double z) {
		if ((x < cube[1].x) && (x > cube[0].x)) {
			if ((y < cube[1].y) && (y > cube[0].y)) {
				if ((y < cube[1].y) && (y > cube[0].y)) {
					return true;
				}
			}
		}

		return false;
	}

	private Point[] getEdgePoints(Point point, Point point2, Point point3, Point point4) {
		Point[] result = new Point[2];
		Point[] pointComparison = new Point[3];
		pointComparison[0] = point2;
		pointComparison[1] = point3;
		pointComparison[2] = point4;
		double minX = point.x;
		double maxX = point.x;
		double minY = point.y;
		double maxY = point.y;
		double minZ = point.z;
		double maxZ = point.z;

		for (int i = 0; i != 2; ++i) {
			if (pointComparison[i].x > maxX) {
				maxX = pointComparison[i].x;
			} else if (pointComparison[i].x < minX) {
				minX = pointComparison[i].x;
			} else if (pointComparison[i].y < minY) {
				minY = pointComparison[i].y;
			} else if (pointComparison[i].y > maxY) {
				maxY = pointComparison[i].y;
			} else if (pointComparison[i].z < minZ) {
				minZ = pointComparison[i].z;
			} else if (pointComparison[i].z > maxZ) {
				maxZ = pointComparison[i].z;
			}
		}
		result[0] = new Point(minX, minY, minZ);
		result[1] = new Point(maxX, maxY, maxZ);
		return result;
	}

	/*
	 * a1, b1 - starting position of the first segment; a2, b2 - ending position
	 * of the first segment; c1, c2 - starting position of the second segment;
	 * d1, d2 - ending position of the second segment;
	 */
	public boolean movingSegmentsIntersection2Spheres(Point a1, Point b1, Point a2, Point b2, Point c1, Point c2, Point d1, Point d2) throws Exception {
		// return false;
		Point Oab1 = a1.add(b1);
		Oab1.multHere(0.5);
		Point Oab2 = a2.add(b2);
		Oab2.multHere(0.5);
		Point Ocd1 = c1.add(d1);
		Ocd1.multHere(0.5);
		Point Ocd2 = c2.add(d2);
		Ocd2.multHere(0.5);

		Point Ms = new Point(Oab1.x - Ocd1.x, Oab1.y - Ocd1.y, Oab1.z - Ocd1.z);
		Point Ks = new Point(Oab2.x - Oab1.x - Ocd2.x + Ocd1.x, Oab2.y - Oab1.y - Ocd2.y + Ocd1.y, Oab2.z - Oab1.z - Ocd2.z + Ocd1.z);

		Double top = Ms.x * Ks.x + Ms.y * Ks.y + Ms.z * Ks.z;
		Double bot = Ks.x * Ks.x + Ks.y * Ks.y + Ks.z * Ks.z;

		double t = top / bot;

		if (t > 1) {
			t = 1;
		} else if (t < 0) {
			t = 0;
		}

		double diameter = a1.distance(b1);

		double minDistance = Math.sqrt(Math.pow(Ms.x + Ks.x * t, 2) + Math.pow(Ms.y + Ks.y * t, 2) + Math.pow(Ms.z + Ks.z * t, 2));

		if (minDistance < diameter) {
			return true;
		}
		return false;
	}
}
