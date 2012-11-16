package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ansokol
 * Date: 13.11.12
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */
public  interface Graph {
    public int getN();
    public void addEdge(int from, int to, double w);
    public List<Edge> edges(int x);
    public boolean removeEdge(int from, int to);
    public double getEdgeWeight(int from, int to);
    public boolean hasEdge(int from, int to);
}
