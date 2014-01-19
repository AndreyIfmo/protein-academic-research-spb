package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance;

import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;

import java.io.IOException;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 20.01.14
 *         Time: 0:48
 */
public class MatrixDistance {
    double[][] matrix;

    public MatrixDistance(String filename) throws IOException {
        matrix = GraphParser.parseGraphMatrix(filename);
    }


    public double distance(int i, int j) {
        return matrix[i][j];
    }
}
