package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance;

import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;

import java.io.IOException;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 20.01.14
 *         Time: 0:48
 */
public class MatrixDistance {
    public double[][] matrix;

    public MatrixDistance(String pathName) throws IOException {
        matrix = GraphParser.parseGraphMatrix(pathName);
    }
    public MatrixDistance(String pathName, boolean[][]banned) throws IOException {
        matrix = GraphParser.parseGraphMatrix(pathName);
        for (int i=0; i<banned.length; i++) {
            for (int j=0; j<banned.length; j++) {
                if (banned[i][j]) {
                    matrix[i][j] = Integer.MAX_VALUE/10;
                }
            }
        }
    }


    public double distance(int i, int j) {
        return matrix[i][j];
    }

    public int getN() {
        return matrix.length;
    }
}
