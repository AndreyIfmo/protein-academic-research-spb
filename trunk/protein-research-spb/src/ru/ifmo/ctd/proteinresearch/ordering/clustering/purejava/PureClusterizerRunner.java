package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;


import ru.ifmo.ctd.proteinresearch.ordering.clustering.SimpleSampling;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.MatrixDistance;

import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;



import java.util.Arrays;


/**
 * Created by Andrey on 25.12.13.
 */
public class PureClusterizerRunner {
    public void run(String name, String propertiesFile) throws Exception {

        System.out.println("File: " + name);
        System.out.println("MST");
        EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFile);
        boolean[][] banned = esls.getBadFiles(esls.getN(), new boolean[esls.getN()][esls.getN()]);

        MatrixDistance distanceMatrix = new MatrixDistance(name, banned);
        for (int i = 2; i < 7; i++) {

            StructuralClusterizer clusterizer3 = new MSTClusterizer(i, distanceMatrix);
            clusterizer3.evaluate(null);
            int counter = 1;
            for (Cluster<Integer> it : clusterizer3.getClusters()) {
                System.out.println(counter++ + ", " + Arrays.toString(it.chainSet.toArray()));
            }

        }/*
        System.out.println("FUZZY: ");
        double[][] matrix = GraphParser.parseGraphMatrix(name);
        for (int numOfClusters = 2; numOfClusters <= 3; numOfClusters++) {
            FuzzyClusterizer fc = new FuzzyClusterizer(matrix, numOfClusters);
            double[][] similarity = fc.evaluate();

            System.out.println(Arrays.toString(fc.answerCenters));
            for (int i = 0; i < similarity.length; i++) {
                System.out.print("Cluster " + i + ": ");
                for (int j = 0; j < numOfClusters; j++) {
                    System.out.print(similarity[i][j] + " ");
                }
                System.out.println();
            }

        }*/
        System.out.println("Sampling");
        SimpleSampling simpleSampling = new SimpleSampling(name);
        simpleSampling.run();
    }

    public static void main(String[] args) throws Exception {
    //    new PureClusterizerRunner().run("matrixes/1BTB.txt");
        new PureClusterizerRunner().run("matrixes/2LJI_optim_costs.txt","2LJI.properties");
      // new PureClusterizerRunner().run("matrixes/2m2y.txt");
    }
}
