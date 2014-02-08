package ru.ifmo.ctd.proteinresearch.intersections;

public class CalcMax  extends AbstractCalc{

	public CalcMax(ChainSequence chains) {
		super(chains);
	}

	public int functionCalculations = 0;

	@Override
	public int hasIntersections() throws Exception {
		PointsChain first;
		PointsChain second = chains.chains[0];
		PointsChain[] sequence = chains.chains;
		int chainLength = second.chain.length;

		for (int i = 1; i != sequence.length; ++i) {
			first = second;
			second = sequence[i];
			for (int j = 1; j != chainLength; ++j) {
				Point[] firstPoints = getEdgePoints(first.chain[j - 1], first.chain[j], second.chain[j - 1], second.chain[j]);
				for (int k = 1; k != chainLength; ++k) {
					Point[] secondPoints = getEdgePoints(first.chain[k - 1], first.chain[k], second.chain[k - 1], second.chain[k]);
					if (movingSegmentsAreClose(firstPoints, secondPoints) == true){
						++functionCalculations;
						if (movingSegmentsIntersection2Spheres(first.chain[j - 1], first.chain[j], second.chain[j - 1], second.chain[j], first.chain[k - 1], first.chain[k], second.chain[k - 1], second.chain[k]) == true) {
		//					return i;
						}
					}
				}
			}
		}
		return -1;
	}

	private Point[] getEdgePoints(Point point, Point point2, Point point3, Point point4)
	{
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
		
		for (int i=0;i!=2;++i)
		{
			if (pointComparison[i].x>maxX){
				maxX = pointComparison[i].x; 
			}else if (pointComparison[i].x<minX){
				minX = pointComparison[i].x; 
			}else if (pointComparison[i].y<minY){
				minY = pointComparison[i].y; 
			}else if (pointComparison[i].y>maxY){
				maxY = pointComparison[i].y; 
			}else if (pointComparison[i].z<minZ){
				minZ = pointComparison[i].z; 
			}else if (pointComparison[i].z>maxZ){
				maxZ = pointComparison[i].z; 
			}  
		}
		result[0] = new Point(minX,minY,minZ);
		result[1] = new Point(maxX,maxY,maxZ);
		return result;
	}
	
	private boolean pointInBetween(Point[] cube, double x, double y, double z)
	{
		if ((x<cube[1].x)&&(x>cube[0].x)){
			if ((y<cube[1].y)&&(y>cube[0].y)){
				if ((y<cube[1].y)&&(y>cube[0].y)){
					return true;
				}
			}
		}
			
		return false;
	}
	
	private boolean movingSegmentsAreClose(Point[] firstCube, Point[] secondCube) {
		if (pointInBetween(firstCube,secondCube[0].x,secondCube[0].y,secondCube[0].z))
			return true;
		if (pointInBetween(firstCube,secondCube[0].x,secondCube[0].y,secondCube[1].z))
			return true;
		if (pointInBetween(firstCube,secondCube[1].x,secondCube[0].y,secondCube[1].z))
			return true;
		if (pointInBetween(firstCube,secondCube[1].x,secondCube[0].y,secondCube[0].z))
			return true;
		if (pointInBetween(firstCube,secondCube[0].x,secondCube[1].y,secondCube[0].z))
			return true;
		if (pointInBetween(firstCube,secondCube[0].x,secondCube[1].y,secondCube[1].z))
			return true;
		if (pointInBetween(firstCube,secondCube[1].x,secondCube[1].y,secondCube[1].z))
			return true;
		if (pointInBetween(firstCube,secondCube[1].x,secondCube[1].y,secondCube[0].z))
			return true;
		return false;
	}
}
