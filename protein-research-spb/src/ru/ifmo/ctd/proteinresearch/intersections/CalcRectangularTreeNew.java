package ru.ifmo.ctd.proteinresearch.intersections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class CalcRectangularTreeNew extends AbstractCalc {
	public int minimalProportion = 4;
	public int maximumSegments = 20;
	public int minimumSegments = 5;
	public TreeElement tree;
	public double dist = Double.NEGATIVE_INFINITY;
	public int step = 0;
	public int functionCalculations;
	public PointsChain data;

	public CalcRectangularTreeNew(ChainSequence chains) {
		super(chains);
	}

	public void initalize() {
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		dist = Double.NEGATIVE_INFINITY;
		data = new PointsChain(chains.chains[0].chain);

		if ((Omin == null) && (Omax == null)) {
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
						dist = Math.max(dist, chains.chains[k].chain[i - 1].distance(chains.chains[k].chain[i]));
					}
				}
			}
			Omin = new Point(minX, minY, minZ);
			Omax = new Point(maxX, maxY, maxZ);
		} else {
			dist = chains.chains[0].chain[1].distance(chains.chains[0].chain[0]);
		}
		tree = new TreeElement(Omin, Omax, minimumSegments, maximumSegments);
		for (int i = 0; i != data.chain.length; ++i) {
			tree.addElement(data.chain[i]);
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
			LinkedList<Point> current = tree.getAllInLocation(points[i]);
			if (current == null) {
				continue;
			}
			Iterator<Point> iter = current.iterator();
			Point tmpPoint = null;
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

	public int hasIntersections() throws Exception {
		PointsChain first = chains.chains[0];
		PointsChain second = chains.chains[1];
		int chainLength = second.chain.length;
		int intersections = 0;
		int skipped = 0;
		
		for (int j = 1; j != chainLength; ++j) {
			if (!movingTooFar(first.chain[j - 1], first.chain[j], second.chain[j - 1], second.chain[j])) {
				ArrayList<Integer> neighbours = getNeighbors(first.chain[j - 1], first.chain[j]);

				for (int k = 0; k < neighbours.size(); k++) {
					int currentNeighbor = neighbours.get(k);
					if ((currentNeighbor == j - 1) || (currentNeighbor == j + 1) || (currentNeighbor == j) || (currentNeighbor == 0))
						continue;
					++functionCalculations;
					if (segmentsHasIntersections(first.chain[j - 1], first.chain[j], second.chain[j - 1], second.chain[j], first.chain[currentNeighbor - 1], second.chain[currentNeighbor - 1], first.chain[currentNeighbor], second.chain[currentNeighbor]) == true) {
						++intersections;
					}
				}
			}else{
				++skipped;
				intersections += intersectAll(j, first.chain, second.chain);
			}
		}
		return intersections;
	}
}
