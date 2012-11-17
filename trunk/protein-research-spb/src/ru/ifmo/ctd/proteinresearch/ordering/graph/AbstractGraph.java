package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ansokol
 * Date: 14.11.12
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGraph implements Graph {
    protected final int n;
    public AbstractGraph(int number) {
        this.n=number;
    }
    @Override
    public int getN() {
        return n;  //To change body of implemented methods use File | Settings | File Templates.
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
