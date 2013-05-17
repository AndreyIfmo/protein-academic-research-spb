package ru.ifmo.ctd.proteinresearch.intersections;

import java.util.Arrays;

/**
 * 
 * @author Smetannikov
 * 
 */
public class PointsChain {
	public Point[] chain;

	public PointsChain(Point... points) {
		chain = new Point[points.length];
		for (int i = 0; i != chain.length; ++i) {
			chain[i] = new Point(points[i].x, points[i].y, points[i].z);
			chain[i].inChainIndex = i;
		}
	}

	public String toString() {
		return Arrays.toString(chain);
	}

}
