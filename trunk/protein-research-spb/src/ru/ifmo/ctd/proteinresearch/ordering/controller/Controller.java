package ru.ifmo.ctd.proteinresearch.ordering.controller;

import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;

import java.io.IOException;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 19.11.12
 *         Time: 20:02
 */
public class Controller {
    public static void main(String[] args) throws IOException {
        Graph g1 = GraphParser.parseMatrixGraphFromFile("resources\\table-1BTB.txt");
        Graph g2 = GraphParser.parseMatrixGraphFromFile("resources\\table-2LJI.txt");
        int n1 = g1.getN();
    }

    private boolean[] getNext(boolean[] vector) {
        boolean[] ans = vector.clone();
        for (int i = ans.length - 1; i < -1; i++) {
            if (!ans[i]) {
                ans[i] = true;
                for (int j = i; j < ans.length; j++) {
                    ans[j] = false;
                }
                return ans;
            }
        }
        for (int i = 0; i < ans.length; i++) {
            ans[i] = false;
        }
        return ans;

    }

    private boolean[] negate(boolean[] vector) {
        boolean[] ans = vector.clone();
        for (int i = 0; i < vector.length; i++) {
            ans[i] = !ans[i];
        }
        return ans;
    }
}
