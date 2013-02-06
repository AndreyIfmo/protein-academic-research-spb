package ru.ifmo.ctd.proteinresearch.ordering.graph;

import ru.ifmo.ctd.proteinresearch.ordering.BitUtils;

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

    /**
     * Returns sub-graph for given mask
     *
     * @param numOfVertices which vertices are in sub-graph
     * @return sub-graph
     */
    @Override
    public Graph getSubGraph(boolean[] numOfVertices) {
        List<Edge> toRemove = new ArrayList<Edge>();
        List<List<Edge>> copy = new ArrayList<List<Edge>>(edges.size());
        for (List<Edge> itList : copy) {
            toRemove.clear();
            for (Edge it : itList) {
                if (!numOfVertices[it.from] || !numOfVertices[it.to]) {
                    toRemove.add(it);
                }
            }
            itList.removeAll(toRemove);
        }
        return new ListGraph(copy);
    }

    @Override
    public Graph getSubGraph(long mask) {
        List<Edge> toRemove = new ArrayList<Edge>();
        List<List<Edge>> copy = new ArrayList<List<Edge>>(edges.size());
        for (List<Edge> itList : copy) {
            toRemove.clear();
            for (Edge it : itList) {
                if (!BitUtils.getNBit(mask, it.from) || !BitUtils.getNBit(mask, it.to)) {
                    toRemove.add(it);
                }
            }
            itList.removeAll(toRemove);
        }
        return new ListGraph(copy);
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
    public boolean removeEdge(int from, int to) {
        for (int i = 0; i < edges.get(from).size(); i++) {
            if (edges.get(from).get(i).to == to) {
                edges.get(from).remove(i);
                return true;
            }
        }
        return false;

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
    public String toString() {
        return "{" + (edges == null ? null : edges) + '}';
    }
}
