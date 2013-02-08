package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import ru.ifmo.ctd.proteinresearch.ordering.graph.Edge;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;
import ru.ifmo.ctd.proteinresearch.ordering.graph.MinimalSpanTreeFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Maxim Buzdalov
 */
public class MSTSolver {
    public static void main(String[] args) throws IOException {
        Graph g = GraphParser.parseMatrixGraphFromFile("resources/table-2LJI.txt");
        Graph mst = new MinimalSpanTreeFinder().getMST(g);
        double weight = 0;
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < mst.getN(); ++i) {
            for (int j = 0; j < i; ++j) {
                if (mst.hasEdge(i, j)) {
                    double w = mst.getEdgeWeight(i, j);
                    edges.add(new Edge(i, j, w));
                    edges.add(new Edge(j, i, w));
                    weight += w;
                }
                if (mst.hasEdge(j, i)) {
                    double w = mst.getEdgeWeight(j, i);
                    edges.add(new Edge(i, j, w));
                    edges.add(new Edge(j, i, w));
                    weight += w;
                }
            }
        }
        Collections.sort(edges, new Comparator<Edge>() {
            @Override
            public int compare(Edge edge, Edge edge2) {
                if (edge.from != edge2.from) {
                    return edge.from - edge2.from;
                } else {
                    return edge.to - edge2.to;
                }
            }
        });
        System.out.println("Weight = " + weight);
        for (Edge e : edges) {
            System.out.println(e.from + " " + e.to + ": " + e.weight);
        }
    }
}
