package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 14.11.12
 *         Time: 18:18
 */
public class ListGraph extends AbstractGraph {
    final List<List<Edge>> edges;

    public ListGraph(int n) {
        super(n);
        edges = new ArrayList<List<Edge>>(n);
        for (int i = 0; i < n; i++) {
            edges.add(new ArrayList<Edge>());
        }
    }

    public ListGraph(List<List<Edge>> edges) {
        super(edges.size());
        this.edges = edges;
    }


    @Override
    public void addEdge(int from, int to, double w) {
        edges.get(from).add(new Edge(from, to, w));
    }

    @Override
    public List<Edge> edges(int x) {
        return edges.get(x);
    }


    @Override
    public void setEdgeWeight(int from, int to, double weight) {
        for (Edge it : edges.get(from)) {
            if (it.to == to) {
               it.setWeight(weight);
            }
        }
    }

    @Override
    public double getEdgeWeight(int from, int to) {
        for (Edge it : edges.get(from)) {
            if (it.to == to) {
                return it.weight;
            }
        }
        throw new IllegalArgumentException(String.format("no edge from vertex %d to vertex %d ", from, to));
    }

    @Override
    public boolean hasEdge(int from, int to) {
        for (Edge it : edges.get(from)) {
            if (it.to == to) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeEdge(int from, int to) {
        List<Edge> edgeList = edges.get(from);
        for (int i=0; i< edgeList.size(); i++) {
            if (edgeList.get(i).to == to) {
                edgeList.remove(i);
            }
        }
    }

    @Override
    public String toString() {
        return "{" + (edges == null ? null : edges) + '}';
    }
}
