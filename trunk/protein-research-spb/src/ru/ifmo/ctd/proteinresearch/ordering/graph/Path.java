package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.Arrays;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 14.11.12
 *         Time: 18:18
 *         To change this template use File | Settings | File Templates.
 */
public class Path {
    public final int[] vertices;
    public final double cost;

    public Path(int[] vertices, double cost) {
        this.vertices = vertices;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "{" + Arrays.toString(vertices) + " " + cost +
                '}';
    }
}
