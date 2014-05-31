package ru.ifmo.ctd.proteinresearch.ordering.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Date: 11.12.12
 * Time: 18:48
 *
 * @author {@link "mailto:ansokolmail@gmail.com" "Andrey Sokolov"}
 */
public class GraphParser {
    public static Graph parseMatrixGraphFromFile(String fileName) throws IOException {
        double[][] edges = parseGraphMatrix(fileName);
        return new MatrixGraph(edges.length, edges);
    }
    public static void floyd(double[][] matrix) {
        int n= matrix.length;

        for (int k = 1;  k< n; k++) {
        for (int i = 1;  i<n; i++) {
        for (int j = 1;  j < n; j++) {
            matrix[i][j] = Math.min(matrix[i][j], matrix[i][k] + matrix[k][j]);
        }}}

    }
    public static double[][] parseGraphMatrix(String fileName) throws IOException {
        String curString;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        boolean isFirst = true;
        int n = 0;
        double[][] edges = null;
        int yCounter = 0;
        while ((curString = br.readLine()) != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(curString);
            if (isFirst) {
                n = Integer.parseInt(stringTokenizer.nextToken());
                edges = new double[n][n];
                isFirst = false;
            } else {
                int xCounter = 0;
                while (stringTokenizer.hasMoreTokens()) {
                    edges[xCounter++][yCounter] = Double.parseDouble(stringTokenizer.nextToken());
                }
                yCounter++;
            }

        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i][j] = Math.max(edges[i][j], edges[j][i]);
                edges[j][i] = edges[i][j];
            }
        }
        br.close();
        return edges;
    }
}
