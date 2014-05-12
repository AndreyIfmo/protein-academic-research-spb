package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.List;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 13.11.12
 *         Time: 14:05
 */

public interface Graph {
    public int getN();

    public void addEdge(int from, int to, double w);

    public List<Edge> edges(int x);

    public double getEdgeWeight(int from, int to);

    public void setEdgeWeight(int from, int to, double weight);

    public boolean hasEdge(int from, int to);

    public void removeEdge(int from, int to);


}
