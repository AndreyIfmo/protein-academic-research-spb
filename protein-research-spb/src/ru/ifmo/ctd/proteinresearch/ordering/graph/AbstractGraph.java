package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.*;

/**
 * Date: 11.12.12
 * Time: 18:48
 * @author {@link "mailto:ansokolmail@gmail.com" "Andrey Sokolov"}
 */
public abstract class AbstractGraph implements Graph {
    protected final int n;
    public AbstractGraph(int number) {
        this.n=number;
    }
    @Override
    public int getN() {
        return n;
    }

    @Override
    public abstract void addEdge(int from, int to, double w);

    @Override
    public abstract List<Edge> edges(int x);

    @Override
    public abstract boolean removeEdge(int from, int to);

    @Override
    public abstract double getEdgeWeight(int from, int to);

    @Override
    public abstract boolean hasEdge(int from, int to);
}
