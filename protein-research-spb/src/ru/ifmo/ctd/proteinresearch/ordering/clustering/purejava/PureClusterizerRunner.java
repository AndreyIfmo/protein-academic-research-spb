package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.SimpleSampling;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.MatrixDistance;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.RMSDDistance;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.TMDistance;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.fuzzy.FuzzyClusterizer;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrey on 25.12.13.
 */
public class PureClusterizerRunner {
    public void run(String name) throws Exception {

        System.out.println("File: " + name);
        System.out.println("MST");
        MatrixDistance distanceMatrix = new MatrixDistance(name);
        for (int i = 2; i < 7; i++) {

            StructuralClusterizer clusterizer3 = new MSTClusterizer(i, distanceMatrix);
            clusterizer3.evaluate(null);
            int counter = 1;
            for (Cluster<Integer> it : clusterizer3.getClusters()) {
                System.out.println(counter++ + ", " + Arrays.toString(it.chainSet.toArray()));
            }

        }
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

        }
        System.out.println("Sampling");
        SimpleSampling simpleSampling = new SimpleSampling(name);
        simpleSampling.run();
    }

    public static void main(String[] args) throws Exception {
        new PureClusterizerRunner().run("matrixes/1BTB.txt");
        new PureClusterizerRunner().run("matrixes/2LJI_optim_costs.txt");
       new PureClusterizerRunner().run("matrixes/2m2y.txt");
    }
}
