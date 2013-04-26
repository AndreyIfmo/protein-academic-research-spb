package ru.ifmo.ctd.proteinresearch.intersections;

import java.util.Iterator;
import java.util.LinkedList;

public class CalcEqualRectangularCuboid extends AbstractCalc {
	private class TripleInt {
		public int a;
		public int b;
		public int c;

		public TripleInt(int a, int b, int c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}

	private static int minimalProportion = 4;
	private int xParts = 1;
	private int yParts = 1;
	private int zParts = 1;
	private double minX;
	private double minY;
	private double minZ;
	private double widthX;
	private double widthY;
	private double widthZ;

	private int step = 0;
	private LinkedList<TripleInt> containsSmth;

	LinkedList<Point>[][][] cuboids;

	public CalcEqualRectangularCuboid(ChainSequence chains) {
		super(chains);
	}

	public void Initalize(int xParts, int yParts, int zParts) throws Exception {
		cuboids = new LinkedList[xParts][yParts][zParts];
		containsSmth = new LinkedList<TripleInt>();
		this.xParts = xParts;
		this.yParts = yParts;
		this.zParts = zParts;

		for (int i = 0; i != xParts; ++i) {
			for (int j = 0; j != yParts; ++j) {
				for (int k = 0; k != zParts; ++k) {
					cuboids[i][j][k] = new LinkedList<Point>();
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

		for (int k = 0; k != chains.chains.length; ++k) {
			for (int i = 0; i != chains.chains[k].chain.length; ++i) {
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
					dist = Math.max(dist, chains.chains[k].chain[i - 1]
							.distance(chains.chains[k].chain[i]));
				}
			}
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
				containsSmth.add(new TripleInt(posX, posY, posZ));
			}
			cuboids[posX][posY][posZ].add(chains.chains[0].chain[i]);
		}
	}

	private void stepForward() {
		TripleInt curr = new TripleInt(0, 0, 0);
		for (Iterator<TripleInt> it = containsSmth.iterator(); it.hasNext(); curr = it
				.next()) {
			cuboids[curr.a][curr.b][curr.c].clear();
		}

		containsSmth.clear();

		for (int i = 0; i != chains.chains[step].chain.length; ++i) {
			int posX = (int) ((chains.chains[step].chain[i].x - minX) / widthX);
			int posY = (int) ((chains.chains[step].chain[i].y - minY) / widthY);
			int posZ = (int) ((chains.chains[step].chain[i].z - minZ) / widthZ);
			if (posX == xParts)
				--posX;
			if (posY == yParts)
				--posY;
			if (posZ == zParts)
				--posZ;
			if (cuboids[posX][posY][posZ].isEmpty()) {
				containsSmth.add(new TripleInt(posX, posY, posZ));
			}
			cuboids[posX][posY][posZ].add(chains.chains[step].chain[i]);
		}
		++step;
	}

	private LinkedList<Integer> getNeighbors(Point a, Point b) {
		LinkedList<Integer> result = new LinkedList<Integer>();

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
			LinkedList<Point> neighborPoints = cuboids[posX][posY][posZ];
			Point tmpPoint = null;
			Iterator<Point> iter = neighborPoints.iterator();
			while (iter.hasNext()) {
				tmpPoint = iter.next();
				if (tmpPoint != null)
					if (result.contains(tmpPoint.inChainIndex)) {
						break;
					} else {
						result.add(tmpPoint.inChainIndex);
					}
			}
		}
		return result;
	}

	@Override
	public int hasIntersections() throws Exception {
		PointsChain first;
		PointsChain second = chains.chains[0];
		PointsChain[] sequence = chains.chains;
		int chainLength = second.chain.length;

		for (step = 1; step != sequence.length; stepForward()) {
			first = second;
			second = sequence[step];
			for (int j = 1; j != chainLength; ++j) {
				LinkedList<Integer> neighbours = getNeighbors(
						first.chain[j - 1], first.chain[j]);

				int currentNeighbor = 1;
				for (Iterator<Integer> iter = neighbours.iterator(); iter
						.hasNext(); currentNeighbor = iter.next()) {
					if ((currentNeighbor == j - 1)
							|| (currentNeighbor == j + 1)
							|| (currentNeighbor == j) || (currentNeighbor == 0))
						continue;
					if (movingSegmentsIntersection2Spheres(first.chain[j - 1],
							first.chain[j], second.chain[j - 1],
							second.chain[j], first.chain[currentNeighbor - 1],
							second.chain[currentNeighbor - 1],
							first.chain[currentNeighbor],
							second.chain[currentNeighbor]) == true) {
						return step;
					}
				}
			}
		}
		return -1;
	}
}
