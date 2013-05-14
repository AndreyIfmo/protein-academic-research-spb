package ru.ifmo.ctd.proteinresearch.intersections;

public class PointsListElement {
	public Point point;
	public PointsListElement next;
	public PointsListElement prev;
	public PointsList position;
	
	public PointsListElement(Point p){
		this.point = p;
	}

	public void deleteMyself() {
		if (this.prev!=null){
			prev.next = this.next;
		}
		if (this.next!=null){
			next.prev = this.prev;
		}
	}
	
}
