package ru.ifmo.ctd.proteinresearch.ordering.controller;

import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphUtils;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;

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
        Path[] paths = GraphUtils.getPaths(g2, 2);
        for (Path it : paths) {
            System.out.print(it.cost);
            System.out.print(it);
        }
    }


}
