package ru.ifmo.ctd.proteinresearch.intersections;
/**
 * 
 * @author Smetannikov
 *
 */
public class PointsChain {
	public Point[] chain;
	
	public PointsChain(Point ... points){
		this.chain=points;
		for (int i=0;i!=chain.length;++i){
			chain[i].inChainIndex = i;
		}
	}
	
}
