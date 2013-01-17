package ru.ifmo.ctd.proteinresearch.ordering.graph;

/**
 * Created by IntelliJ IDEA.
 * User: Ansokol
 * Date: 15.11.12
 * Time: 4:03
 * To change this template use File | Settings | File Templates.
 */
public class Path {
    final int[] vertices;
    final double cost;

    public Path(int[] vertices, double cost) {
        this.vertices = vertices;
        this.cost = cost;
    }
}
