package ru.ifmo.ctd.proteinresearch.ordering.graph;

/**
 * Created by IntelliJ IDEA.
 * User: Ansokol
 * Date: 15.11.12
 * Time: 3:04
 * To change this template use File | Settings | File Templates.
 */
public class Edge {
    public final int from;
    public final int to;
    public double weight;

    public Edge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return from == edge.from && to == edge.to && Double.compare(edge.weight, weight) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = from;
        result = 31 * result + to;
        temp = weight != +0.0d ? Double.doubleToLongBits(weight) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
