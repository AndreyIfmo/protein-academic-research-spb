package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 14.11.12
 *         Time: 18:18
 *         To change this template use File | Settings | File Templates.
 */
public class MatrixGraph extends AbstractGraph {
    final double[][] edges;
    final boolean[][] hasEdge;

    public MatrixGraph(int num) {
        super(num);
        edges = new double[num][num];
        hasEdge = new boolean[num][num];
    }

    public MatrixGraph(int num, double[][] edges) {
        super(num);
        this.edges = edges;
        hasEdge = new boolean[num][num];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                hasEdge[i][j] = true;
            }
        }
    }

    public MatrixGraph(double[][] edges, boolean[][] hasEdge) {
        super(edges.length);
        this.edges = edges;
        this.hasEdge = hasEdge;
    }

    @Override
    public void addEdge(int from, int to, double w) {
        edges[from][to] = w;
        hasEdge[from][to] = true;
    }

    @Override
    public List<Edge> edges(int x) {
        List<Edge> ans = new ArrayList<Edge>();
        for (int i = 0; i < edges[x].length; i++) {
            if (hasEdge[x][i]) {
                ans.add(new Edge(x, i, edges[x][i]));
            }
        }
        return ans;
    }

    @Override
    public void setEdgeWeight(int from, int to, double weight) {
        hasEdge[from][to] = false;
        hasEdge[to][from] = false;
    }

    @Override
    public double getEdgeWeight(int from, int to) {
        return edges[from][to];
    }

    @Override
    public void removeEdge(int from, int to) {
        hasEdge[from][to] = false;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        return hasEdge[from][to];
    }

    public MatrixGraph getSubgraph(Set<Integer> numOfVertices) {
        double[][] edgesCopy = new double[this.edges.length][this.edges.length];
        boolean[][] hasEdgeCopy = new boolean[this.hasEdge.length][this.hasEdge.length];
        cloneMatrix(edgesCopy, edges);
        cloneMatrix(hasEdgeCopy, hasEdge);
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                if (numOfVertices.contains(i)) {

                }
            }
        }
        return null;
    }

    public void cloneMatrix(double[][] newMatrix, double[][] oldMatrix) {
        for (int i = 0; i < oldMatrix.length; i++) {
            for (int j = 0; j < oldMatrix.length; j++) {
                newMatrix[i][j] = oldMatrix[i][j];
            }
        }
    }

    public void cloneMatrix(boolean[][] newMatrix, boolean[][] oldMatrix) {
        for (int i = 0; i < oldMatrix.length; i++) {
            for (int j = 0; j < oldMatrix.length; j++) {
                newMatrix[i][j] = oldMatrix[i][j];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.n);
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(edges[j][i]);
                if (j != n - 1) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
