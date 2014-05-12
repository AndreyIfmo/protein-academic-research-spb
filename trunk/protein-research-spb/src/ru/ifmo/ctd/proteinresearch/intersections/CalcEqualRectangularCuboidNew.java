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
            if (posX == xParts) {
                --posX;
            }
            if (posY == yParts) {
                --posY;
            }
            if (posZ == zParts) {
                --posZ;
            }
            PointsListElement neighborPoints = cuboids[posX][posY][posZ].first;

            Point tmpPoint = null;
            while (neighborPoints.next != null) {
                tmpPoint = neighborPoints.next.point;
                if (tmpPoint != null) {
                    if (result.contains(tmpPoint.inChainIndex)) {
                        break;
                    } else {
                        result.add(tmpPoint.inChainIndex);
                    }
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
                    if ((currentNeighbor == j - 1) || (currentNeighbor == j + 1) || (currentNeighbor == j) || (currentNeighbor == 0)) {
                        continue;
                    }
                    ++functionCalculations;
                    if (movingSegmentsIntersection2Spheres(first.chain[j - 1], first.chain[j], second.chain[j - 1], second.chain[j], first.chain[currentNeighbor - 1], second.chain[currentNeighbor - 1], first.chain[currentNeighbor], second.chain[currentNeighbor]) == true) {
                        ++intersections;
                    }
                }
            } else {
                intersections += intersectAll(j, first.chain, second.chain);
            }
        }
        return intersections;
    }

}
