package ru.ifmo.ctd.proteinresearch.intersections;

public abstract class AbstractCalc implements Calc {
	public ChainSequence chains;
	public Point Omin = null;
	public Point Omax = null;
	private double eps = 0.0000000001;

	public AbstractCalc(ChainSequence chains) {
		this.chains = chains;
	}

	@Override
	public int hasIntersections() throws Exception {
		return 0;
	}

	/*
	 * a1, b1 - starting position of the first segment; a2, b2 - ending position
	 * of the first segment; c1, c2 - starting position of the second segment;
	 * d1, d2 - ending position of the second segment;
	 */
	public boolean movingSegmentsIntersection(Point a1, Point b1, Point a2,
			Point b2, Point c1, Point c2, Point d1, Point d2) throws Exception {
		double v1 = (a1.x - d1.x) * (c1.y - d1.y) * (b1.z - d1.z)
				+ (a1.y - d1.y) * (c1.z - d1.z) * (b1.x - d1.x) + (a1.z - d1.z)
				* (c1.x - d1.x) * (b1.y - d1.y) - (a1.x - d1.x) * (c1.z - d1.z)
				* (b1.y - d1.y) - (a1.y - d1.y) * (c1.x - d1.x) * (b1.z - d1.z)
				- (a1.z - d1.z) * (c1.y - d1.y) * (b1.x - d1.x);
		double v2 = (a2.x - d2.x) * (c2.y - d2.y) * (b2.z - d2.z)
				+ (a2.y - d2.y) * (c2.z - d2.z) * (b2.x - d2.x) + (a2.z - d2.z)
				* (c2.x - d2.x) * (b2.y - d2.y) - (a2.x - d2.x) * (c2.z - d2.z)
				* (b2.y - d2.y) - (a2.y - d2.y) * (c2.x - d2.x) * (b2.z - d2.z)
				- (a2.z - d2.z) * (c2.y - d2.y) * (b2.x - d2.x);

		if ((v1 == 0) && (v2 == 0)) {
			// отрезки движутся в одной плоскости
			double s1 = (a1.y - b1.y) * (a1.z - b1.z) + (a1.x - b1.x)
					* (c1.y - b1.y) + (a1.z - b1.z) * (c1.x - b1.x)
					- (a1.y - b1.y) * (c1.x - b1.x) - (a1.z - b1.z)
					* (c1.y - b1.y) - (a1.x - b1.x) * (c1.z - b1.z);
			double s2 = (a2.y - b2.y) * (a2.z - b2.z) + (a2.x - b2.x)
					* (c2.y - b2.y) + (a2.z - b2.z) * (c2.x - b2.x)
					- (a2.y - b2.y) * (c2.x - b2.x) - (a2.z - b2.z)
					* (c2.y - b2.y) - (a2.x - b2.x) * (c2.z - b2.z);
			double s3 = (a1.y - b1.y) * (a1.z - b1.z) + (a1.x - b1.x)
					* (d1.y - b1.y) + (a1.z - b1.z) * (d1.x - b1.x)
					- (a1.y - b1.y) * (d1.x - b1.x) - (a1.z - b1.z)
					* (d1.y - b1.y) - (a1.x - b1.x) * (d1.z - b1.z);
			double s4 = (a2.y - b2.y) * (a2.z - b2.z) + (a2.x - b2.x)
					* (d2.y - b2.y) + (a2.z - b2.z) * (d2.x - b2.x)
					- (a2.y - b2.y) * (d2.x - b2.x) - (a2.z - b2.z)
					* (d2.y - b2.y) - (a2.x - b2.x) * (d2.z - b2.z);
			Point piuc1 = a1.sub(b1).vectorMult(c1.sub(b1));
			Point piud1 = a1.sub(b1).vectorMult(d1.sub(b1));
			Point piuc2 = a2.sub(b2).vectorMult(c2.sub(b2));
			Point piud2 = a2.sub(b2).vectorMult(d2.sub(b2));

			if ((s1 == 0) && (s2 == 0) && (s3 == 0) && (s4 == 0)) {
				// отрезки движутся по одной прямой
				if (c1.sub(b1).add(c2.sub(b1)).distance() < c1.sub(b1)
						.distance() + c2.sub(b1).distance()) {
					return true;
				}
				if (d1.sub(b1).add(d2.sub(b1)).distance() < d1.sub(b1)
						.distance() + d2.sub(b1).distance()) {
					return true;
				}
				if (c1.sub(a1).add(c1.sub(b1)).distance() < c1.sub(a1)
						.distance() + c1.sub(b1).distance()) {
					return true;
				}
				if (d1.sub(a1).add(d1.sub(b1)).distance() < d1.sub(a1)
						.distance() + d1.sub(b1).distance()) {
					return true;
				}
				if (c2.sub(a2).add(c2.sub(b2)).distance() < c2.sub(a2)
						.distance() + c2.sub(b2).distance()) {
					return true;
				}
				if (d2.sub(a2).add(d1.sub(b2)).distance() < d2.sub(a1)
						.distance() + d2.sub(b2).distance()) {
					return true;
				}
			}
			if ((s1 * s2 == 0) || (s3 * s4 == 0)) {
				throw (new Exception(
						"Something crashed at flat area calculation"));
			}

			return ((s1 * s2 < 0) || (s3 * s4 < 0));
		}
		if (v1 == 0) {
			// отрезки стартуют в одной плоскости
			throw (new Exception("Segments starts at one plane"));
		}
		if (v2 == 0) {
			// отрезки финишируют в одной плоскости
			throw (new Exception("Segments finish at one plane"));
		}
		return v1 * v2 < 0;
	}

	/*
	 * a1, b1 - starting position of the first segment; a2, b2 - ending position
	 * of the first segment; c1, c2 - starting position of the second segment;
	 * d1, d2 - ending position of the second segment;
	 */
	public boolean movingSegmentsIntersection2Spheres(Point a1, Point b1,
			Point a2, Point b2, Point c1, Point c2, Point d1, Point d2)
			throws Exception {
		Point Oab1 = a1.add(b1);
		Oab1.multHere(0.5);
		Point Oab2 = a2.add(b2);
		Oab2.multHere(0.5);
		Point Ocd1 = c1.add(d1);
		Ocd1.multHere(0.5);
		Point Ocd2 = c2.add(d2);
		Ocd2.multHere(0.5);

		double diameter = a1.distance(b1);
		//double diameter2 = c1.distance(d1);

		//double diameter3 = a2.distance(b2);
		//double diameter4 = c2.distance(d2);

		
		double abDist = Oab1.distance(Oab2);
		double cdDist = Ocd1.distance(Ocd2);
		int maxStep = (int) (Math.max(abDist, cdDist) / diameter) + 1;

		Point abDelta = Oab2.sub(Oab1);
		abDelta.divideHere(maxStep);
		Point cdDelta = Ocd2.sub(Ocd1);
		cdDelta.divideHere(maxStep);

		diameter/=3;
		
		for (int i = 0; i != maxStep; ++i) {
			if (Oab1.distance(Ocd1)<diameter){
				return true;
			}
			Oab1.addHere(abDelta);
			Ocd1.addHere(cdDelta);
		}
		if (Oab2.distance(Ocd2)<diameter){
			int a = 1;
			return true;
		}
		return false;
	}
}
