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
    public Graph getMST(Graph g) {
        DisjointSets sets = new DisjointSets(g.getN());
        return getPartMST(g, 1, sets);
    }

    public Graph getPartMST(Graph g, int components, DisjointSets sets) {
        if (components <= 0 || components > g.getN()) {
            throw new IllegalArgumentException();
        }
        List<List<Edge>> answer = new ArrayList<List<Edge>>(g.getN());

        List<Edge> edgeList = new ArrayList<Edge>();
        for (int i = 0; i < g.getN(); i++) {
            edgeList.addAll(g.edges(i));
            answer.add(new ArrayList<Edge>());
        }
        Collections.sort(edgeList, new EdgeComparator());
        int haveComponents = g.getN();
        for (Edge it : edgeList) {
            if (haveComponents == components) {
                break;
            }
            if (sets.find(it.from) != sets.find(it.to)) {
                answer.get(it.from).add(it);
                boolean union = sets.union(it.from, it.to);
                if (union) {
                    --haveComponents;
                }
            }
        }
        return new ListGraph(answer);
    }

    class EdgeComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge o1, Edge o2) {
            return Double.compare(o1.weight, o2.weight);
        }
    }
}
