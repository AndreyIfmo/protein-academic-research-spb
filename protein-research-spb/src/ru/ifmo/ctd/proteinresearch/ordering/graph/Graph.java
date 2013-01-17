package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.List;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 13.11.12
 *         Time: 14:05
 */

public interface Graph {
    public int getN();

    /**
     * Returns sub-graph for given mask
     *
     * @param numOfVertices which vertices are in sub-graph
     * @return sub-graph
     */
    public Graph getSubGraph(boolean[] numOfVertices);

    public void addEdge(int from, int to, double w);

    public List<Edge> edges(int x);

    public boolean removeEdge(int from, int to);

    public double getEdgeWeight(int from, int to);

    public boolean hasEdge(int from, int to);
}
