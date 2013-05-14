package ru.ifmo.ctd.proteinresearch.intersections;

import java.util.LinkedList;

public class TreeElement {
	public TreeElement[] leaves = null;
	public LinkedList<Point> points = null;
	public boolean isLeave = true;
	public TreeElement parent = null;
	public Point Omin = null;
	public Point Omax = null;
	public int maximumSegments = 20;
	public int minimumSegments = 5;
	public int fullSize = 0;
	public boolean canBeDivided = true;
	public double minimumSize = 0;

	public TreeElement(boolean isLeave, TreeElement parent, LinkedList<Point> points, int maximumSegments, double minimumSize) {
		this.isLeave = isLeave;
		this.points = points;
		this.parent = parent;
		this.minimumSize = minimumSize;
		this.maximumSegments = maximumSegments;
		this.minimumSegments = Math.max(1, maximumSegments/4);
		Omin = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		Omax = new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		for (Point p : points) {
			Math.max(p.x, Omax.x);
			Math.max(p.y, Omax.y);
			Math.max(p.z, Omax.z);
			Math.min(p.x, Omin.x);
			Math.min(p.y, Omin.y);
			Math.min(p.z, Omin.z);
		}
	}

	public TreeElement(Point Omin, Point Omax, int maximumSegments) {
		this.Omax = Omax;
		this.Omin = Omin;
		this.maximumSegments = maximumSegments;
	}
	
	public void removeElement(Point p){
		if (leaves == null) {
			points.remove(p);
			--fullSize;
		} else {
			for (TreeElement t : leaves) {
				if ((p.x <= t.Omax.x) && (p.y <= t.Omax.y) && (p.z <= t.Omax.z) && (p.x >= t.Omin.x) && (p.y >= t.Omin.y) && (p.z >= t.Omin.z)) {
					t.removeElement(p);
					--fullSize;
					if (fullSize<minimumSegments){
						joinKids();
					}
					break;
				}
			}
		}
	}
	
	public void joinKids(){
		points = new LinkedList<Point>();
		for (int i=0;i!=8;++i){
			points.addAll(leaves[i].points);
		}
		leaves = null;
		isLeave = true;
	}

	public void addElement(Point p) {
		if (leaves == null) {
			if (points == null) {
				points = new LinkedList<Point>();
			}
			if ((canBeDivided)&&(points.size() >= maximumSegments)) {
				leaves = new TreeElement[8];
				double midX = (Omax.x + Omin.x) / 2;
				double midY = (Omax.y + Omin.y) / 2;
				double midZ = (Omax.z + Omin.z) / 2;

				if (Math.min(Math.min(midX - Omin.x, midY - Omin.y), midZ - Omin.z) < minimumSize) {
					canBeDivided = false;
					points.add(p);
					++fullSize;
				} else {

					leaves[0] = new TreeElement(Omin, Omax.add(Omin).divide(2), maximumSegments);
					leaves[1] = new TreeElement(new Point(midX, Omin.y, Omin.z), new Point(Omax.x, midY, Omin.z), maximumSegments);
					leaves[2] = new TreeElement(new Point(Omin.x, Omin.y, midZ), new Point(midX, midY, Omax.z), maximumSegments);
					leaves[3] = new TreeElement(new Point(midX, Omin.y, midZ), new Point(Omax.x, midY, Omax.z), maximumSegments);
					leaves[4] = new TreeElement(Omax.add(Omin).divide(2), Omax, maximumSegments);
					leaves[5] = new TreeElement(new Point(midX, midY, Omin.z), new Point(Omax.x, Omax.y, midZ), maximumSegments);
					leaves[6] = new TreeElement(new Point(Omin.x, midY, midZ), new Point(midX, Omax.y, midZ), maximumSegments);
					leaves[7] = new TreeElement(new Point(Omin.x, midY, Omin.z), new Point(midX, Omax.y, midZ), maximumSegments);

					this.fullSize -= points.size();
					for (int i = 0; i != 8; ++i) {
						leaves[i].parent = this;
						leaves[i].minimumSize = this.minimumSize;
					}
					for (Point p2 : points) {
						this.addElement(p2);
					}
					this.isLeave = false;
				}
			} else {
				points.add(p);
				++fullSize;
			}
		} else {
			for (TreeElement t : leaves) {
				if ((p.x <= t.Omax.x) && (p.y <= t.Omax.y) && (p.z <= t.Omax.z) && (p.x >= t.Omin.x) && (p.y >= t.Omin.y) && (p.z >= t.Omin.z)) {
					t.addElement(p);
					++fullSize;
					break;
				}
			}
		}
	}
}
