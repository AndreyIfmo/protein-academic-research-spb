package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 07.02.13
 *         Time: 22:01
 */
public class MinimalSpanTreeFinder {
    Graph getMST(Graph g) {
        List<List<Edge>> answer = new ArrayList<List<Edge>>(g.getN());
        DisjointSets sets = new DisjointSets(g.getN());
        List<Edge> edgeList = new ArrayList<Edge>();
        for (int i = 0; i < g.getN(); i++) {
            edgeList.addAll(g.edges(i));
        }
        Collections.sort(edgeList, new EdgeComparator());
        for (Edge it : edgeList) {
            if (sets.find(it.from) != sets.find(it.to)) {
                answer.get(it.from).add(it);
                sets.union(it.from, it.to);
            }
        }
        return new ListGraph(answer);

    }

    class EdgeComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge o1, Edge o2) {
            return (o1.weight > o2.weight ? 1 : (o1.weight == o2.weight ? 0 : -1));
        }
    }
}
