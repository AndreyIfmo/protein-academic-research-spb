package ru.ifmo.ctd.proteinresearch.intersections;

import java.util.ArrayList;

public class CalcEqualRectangularCuboidNew extends AbstractCalc {
	public int minimalProportion = 4;
	public int functionCalculations = 0;
	private int xParts = 1;
	private int yParts = 1;
	private int zParts = 1;
	private double minX;
	private double minY;
	private double minZ;
	private double widthX;
	private double widthY;
	private double widthZ;

	PointsList[][][] cuboids;
	PointsListElement[] points;

	public CalcEqualRectangularCuboidNew(ChainSequence chains) {
		super(chains);
	}
	
	public void Initalize(int xParts, int yParts, int zParts) throws Exception {
		cuboids = new PointsList[xParts][yParts][zParts];
		points = new PointsListElement[chains.chains[0].chain.length];

		this.xParts = xParts;
		this.yParts = yParts;
		this.zParts = zParts;

		for (int i = 0; i != xParts; ++i) {
			for (int j = 0; j != yParts; ++j) {
				for (int k = 0; k != zParts; ++k) {
					cuboids[i][j][k] = new PointsList();
				}
			}
		}

		double dist = Double.NEGATIVE_INFINITY;

		minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		minZ = Double.POSITIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		if ((Omin == null) && (Omax == null)) {
			for (int k = 0; k != chains.chains.length; ++k) {
				for (int i = 0; i != chains.chains[k].chain.length; ++i) {
					if (k == 0) {
						points[i] = new PointsListElement(chains.chains[k].chain[i]);
					}

					if (chains.chains[k].chain[i].x < minX) {
						minX = chains.chains[k].chain[i].x;
					} else if (chains.chains[k].chain[i].x > maxX) {
						maxX = chains.chains[k].chain[i].x;
					}

					if (chains.chains[k].chain[i].y < minY) {
						minY = chains.chains[k].chain[i].y;
					} else if (chains.chains[k].chain[i].y > maxY) {
						maxY = chains.chains[k].chain[i].y;
					}

					if (chains.chains[k].chain[i].z < minZ) {
						minZ = chains.chains[k].chain[i].z;
					} else if (chains.chains[k].chain[i].z > maxZ) {
						maxZ = chains.chains[k].chain[i].z;
					}

					if (i != 0) {
						dist = Math.max(dist, chains.chains[k].chain[i - 1].distance(chains.chains[k].chain[i]));
					}
				}
			}
		} else {
			minX = Omin.x;
			minY = Omin.y;
			minZ = Omin.z;
			maxX = Omax.x;
			maxY = Omax.y;
			maxZ = Omax.z;
			dist = chains.chains[0].chain[1].distance(chains.chains[0].chain[0]);
		}

		dist *= minimalProportion;
		widthX = Math.max((maxX - minX) / xParts, dist);
		widthY = Math.max((maxY - minY) / yParts, dist);
		widthZ = Math.max((maxZ - minZ) / zParts, dist);

		for (int i = 0; i != chains.chains[0].chain.length; ++i) {
			int posX = (int) ((chains.chains[0].chain[i].x - minX) / widthX);
			int posY = (int) ((chains.chains[0].chain[i].y - minY) / widthY);
			int posZ = (int) ((chains.chains[0].chain[i].z - minZ) / widthZ);
			if (posX == xParts)
				--posX;
			if (posY == yParts)
				--posY;
			if (posZ == zParts)
				--posZ;
			if (cuboids[posX][posY][posZ].isEmpty()) {
				points[i].position = cuboids[posX][posY][posZ];
			}
			cuboids[posX][posY][posZ].add(points[i]);
		}
	}

	
	

	private ArrayList<Integer> getNeighbors(Point a, Point b) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		Point[] points = new Point[8];
		points[0] = a;
		points[1] = b;
		points[2] = new Point(b.x, a.y, a.z);
		points[3] = new Point(b.x, b.y, a.z);
		points[4] = new Point(a.x, b.y, a.z);
		points[5] = new Point(b.x, a.y, b.z);
		points[6] = new Point(a.x, a.y, b.z);
		points[7] = new Point(a.x, b.y, b.z);

		for (int i = 0; i != 8; ++i) {
			int posX = (int) ((points[i].x - minX) / widthX);
			int posY = (int) ((points[i].y - minY) / widthY);
			int posZ = (int) ((points[i].z - minZ) / widthZ);
			if (posX == xParts)
				--posX;
			if (posY == yParts)
				--posY;
			if (posZ == zParts)
				--posZ;
			PointsListElement neighborPoints = cuboids[posX][posY][posZ].first;

			Point tmpPoint = null;
			while (neighborPoints.next != null) {
				tmpPoint = neighborPoints.next.point;
				if (tmpPoint != null)
					if (result.contains(tmpPoint.inChainIndex)) {
						break;
					} else {
						result.add(tmpPoint.inChainIndex);
					}
				neighborPoints = neighborPoints.next;
			}
		}
		return result;
	}

	public int hasIntersections() throws Exception {
		PointsChain first = chains.chains[0];
		PointsChain second = chains.chains[1];

		int chainLength = second.chain.length;
		int intersections = 0;

		for (int j = 1; j != chainLength; ++j) {
			if (!movingTooFar(first.chain[j - 1], first.chain[j], second.chain[j - 1], second.chain[j])) {
				ArrayList<Integer> neighbours = getNeighbors(first.chain[j - 1], first.chain[j]);
				int currentNeighbor = 1;
				for (int k = 0; k < neighbours.size(); k++) {
					currentNeighbor = neighbours.get(k);
					if ((currentNeighbor == j - 1) || (currentNeighbor == j + 1) || (currentNeighbor == j) || (currentNeighbor == 0))
						continue;
					++functionCalculations;
					if (segmentsHasIntersections(first.chain[j - 1], first.chain[j], second.chain[j - 1], second.chain[j], first.chain[currentNeighbor - 1], second.chain[currentNeighbor - 1], first.chain[currentNeighbor], second.chain[currentNeighbor]) == true) {
						++intersections;
					}
				}
			}else{
				intersections += intersectAll(j, first.chain, second.chain);
			}
		}
		return intersections;
	}

}
