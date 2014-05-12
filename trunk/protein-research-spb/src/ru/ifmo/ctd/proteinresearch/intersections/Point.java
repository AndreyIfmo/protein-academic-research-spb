package ru.ifmo.ctd.proteinresearch.intersections;


import org.biojava.bio.structure.Atom;

/**
 * @author Smetannikov
 */
public class Point {
    public double x;
    public double y;
    public double z;
    public int inChainIndex;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        inChainIndex = -1;
    }

    public Point(Atom atom) {
        this(atom.getX(), atom.getY(), atom.getZ());
    }

    public Point add(Point b) {
        return new Point(this.x + b.x, this.y + b.y, this.z + b.z);
    }

    public void addHere(Point b) {
        x += b.x;
        y += b.y;
        z += b.z;
    }

    public Point sub(Point p) {
        return new Point(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    public void subHere(Point p) {
        this.x -= p.x;
        this.y -= p.y;
        this.z -= p.z;
    }

    public void multHere(double d) {
        x *= d;
        y *= d;
        z *= d;
    }

    public Point mult(double d) {
        return new Point(x * d, y * d, z * d);
    }

    public Point divide(double d) {
        return new Point(this.x / d, this.y / d, this.z / d);
    }

    public Point copy() {
        return new Point(x, y, z);
    }

    public void divideHere(double d) {
        x /= d;
        y /= d;
        z /= d;
    }

    public double distance(Point b) {
        return Math.sqrt((this.x - b.x) * (this.x - b.x) + (this.y - b.y) * (this.y - b.y) + (this.z - b.z) * (this.z - b.z));
    }

    public double distance() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Point vectorMult(Point b) {
        return new Point(y * b.z - b.y * z, z * b.x - b.z * x, x * b.y - b.x * y);
    }

    public double scalarMult(Point b) {
        return x * b.x + y * b.y + z * b.z;
    }

    public Point normalized() {
        return new Point(this.x / distance(), this.y / distance(), this.z / distance());
    }

    public String toString() {
        return ("(" + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + ")");
    }
}
