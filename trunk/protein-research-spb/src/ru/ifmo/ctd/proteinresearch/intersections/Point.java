package ru.ifmo.ctd.proteinresearch.intersections;
/**
 * 
 * @author Smetannikov
 *
 */
public class Point {
	public double x;
	public double y;
	public double z;
	public int inChainIndex;
	
	Point(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
		inChainIndex=-1;
	}
	
}
