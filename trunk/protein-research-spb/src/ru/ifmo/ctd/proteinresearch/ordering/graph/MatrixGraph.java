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
public class MatrixGraph extends AbstractGraph{
    double [][] edges;
    boolean [][]hasEdge;
     
    public MatrixGraph(int num) {
        super(num);
        edges = new double[num][num];
        hasEdge = new boolean[num][num];
    }

    public MatrixGraph(int num, double[][] edges) {
        super(num);
        this.edges = edges;
        hasEdge = new boolean[num][num];
        for (int i=0; i<num; i++) {
            for (int j=0; j<num; j++) {
                hasEdge[i][j]=true;
            }
        }
    }
    

    @Override
    public void addEdge(int from, int to, double w) {
        edges[from][to]=w;
        hasEdge[from][to]=true;
    }

    @Override
    public List<Edge> edges(int x) {
        List<Edge> ans=new ArrayList<Edge>();
        for (int i=0; i<edges[x].length; i++) {
            if (hasEdge[x][i]) {
                ans.add(new Edge(x,i,edges[x][i]));
            }
        }
        return ans;
    }


    @Override
    public boolean removeEdge(int from, int to) {
        if (hasEdge[from][to]) {
            hasEdge[from][to]=false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public double getEdgeWeight(int from, int to) {
        if (hasEdge[from][to]) {
            return edges[from][to];
        } else {
            throw new IllegalArgumentException(String.format("no edge from vertex %d to vertex %d ", from, to));
        }
    }

    @Override
    public boolean hasEdge(int from, int to) {
        return hasEdge[from][to];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.n);
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(edges[j][i]);
                if (j!=n-1) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
