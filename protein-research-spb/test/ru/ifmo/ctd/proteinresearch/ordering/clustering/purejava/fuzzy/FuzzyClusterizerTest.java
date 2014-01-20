package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.fuzzy;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 19.01.14
 *         Time: 19:49
 */
public class FuzzyClusterizerTest {
    public double[][] matrix;

    @Before
    public void constructGraph() throws IOException {
        matrix = GraphParser.parseGraphMatrix("resources/table-2LJI.txt");
    }

    @Test
    public void testEvaluate() throws Exception {
        int numOfClusters = 2;
        FuzzyClusterizer clusterizer = new FuzzyClusterizer(matrix, numOfClusters);
        clusterizer.exp = 1;
        double[][] similarity = clusterizer.evaluate();
        for (int i = 0; i < similarity.length; i++) {
            double controlSum = 0;
            for (int j = 0; j < numOfClusters; j++) {
                Assert.assertTrue(similarity[i][j] <= 1);
                controlSum += similarity[i][j];
            }
            Assert.assertEquals(1.0, controlSum, 0.01);

        }
        System.out.print("Cluster centers:" + Arrays.toString(clusterizer.answerCenters));
        for (int i = 0; i < similarity.length; i++) {
            for (int j = 0; j < numOfClusters; j++) {
                System.out.print((similarity[i][j] > 0.1 ? similarity[i][j] : 0) + " ");
            }
            System.out.println();
        }


    }
}
