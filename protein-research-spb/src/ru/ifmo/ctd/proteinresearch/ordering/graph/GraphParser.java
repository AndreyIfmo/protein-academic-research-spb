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
        br.close();
        return new MatrixGraph(n, edges);
    }
}
