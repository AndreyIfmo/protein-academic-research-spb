package ru.ifmo.ctd.proteinresearch.ordering.graph;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 07.02.13
 *         Time: 22:02
 */
public class DisjointSets {
    private int[] parent;
    private int[] rank;

    public DisjointSets(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        if (parent[x] == x) {
            return x;
        } else {
            parent[x] = find(parent[x]);
            return parent[x];
        }
    }

    public boolean union(int root1, int root2) {
        root1 = find(root1);
        root2 = find(root2);
        if (root1 == root2) {
            return false;
        }
        if (rank[root1] == rank[root2]) {
            ++rank[root1];

        }
        if (rank[root1] < rank[root2]) {
            parent[root1] = root2;
        } else {
            parent[root2] = root1;
        }
        return true;
    }


}
