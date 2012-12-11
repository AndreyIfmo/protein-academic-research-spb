package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ansokol
 * Date: 14.11.12
 * Time: 18:18
 * To change this template use File | Settings | File Templates.
 */
public class ListGraph extends AbstractGraph{
    List<Edge>[] edges;
    public ListGraph(int n) {
        super(n);
        edges=new ArrayList[n];
        for (int i=0; i<n; i++) {
            edges[i]=new ArrayList<Edge>();
        }
    }
    public ListGraph(List<Edge>[] edges) {
        super(edges.length);
        this.edges = edges;
    }
    

    @Override
    public void addEdge(int from, int to, double w) {
        edges[from].add(new Edge(from, to, w));   
    }

    @Override
    public List<Edge> edges(int x) {
        return edges[x];
    }

    @Override
    public boolean removeEdge(int from, int to) {
        for (int i=0; i < edges[from].size(); i++)  {
            if (edges[from].get(i).to== to) {
                edges[from].remove(i);
                return true;
            }
        }
        return false;
        
    }

    @Override
    public double getEdgeWeight(int from, int to) {
        for (Edge it: edges[from]) {
            if (it.to==to) {
                return it.weight;
            }
        }
        throw new IllegalArgumentException(String.format("no edge from vertex %d to vertex %d ", from, to));
    }
    @Override
    public boolean hasEdge(int from, int to) {
        for (Edge it: edges[from]) {
            if (it.to==to) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return (edges == null ? null : Arrays.asList(edges)).toString();
    }
}
