package ru.ifmo.ctd.proteinresearch.ordering.graph;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 07.02.13
 *         Time: 22:02
 */
public class DisjointSets {
    private int[] parent;

    public DisjointSets(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        if (parent[x] < 0) {
            return x;
        } else {
            parent[x] = find(parent[x]);
            return parent[x];
        }
    }

    public void union(int root1, int root2) {
        if (parent[root2] < parent[root1]) {
            parent[root1] = root2;
        } else {
            if (parent[root1] == parent[root2]) {
                parent[root1]--;
            }
            parent[root2] = root1;
        }
    }
}
