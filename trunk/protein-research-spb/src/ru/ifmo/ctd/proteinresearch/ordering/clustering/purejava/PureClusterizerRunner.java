package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;
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
        ConformationGraph cg = new ConformationGraph(name + "_costs.txt", name + ".zip", name + "/2LJI_optim%d_%d.pdb");
        int numberOfVertices = cg.graph.getN();
        List<Chain> chains = new ArrayList<>();
        Chain reference = VertexEdgeAnalyzer.getBasicChain(cg, 0);
        chains.add(reference);
        for (int i = 1; i < numberOfVertices; i++) {
            chains.add(ConformationChain.align(reference, VertexEdgeAnalyzer.getBasicChain(cg, i)));
        }
        String filename = "2LJI_optim_costs.txt";
        MatrixDistance distanceMatrix = new MatrixDistance(filename);
        for (int i = 2; i < 7; i++) {

            StructuralClusterizer clusterizer3 = new MSTClusterizer(i, distanceMatrix);
            clusterizer3.evaluate(null);
            int counter = 1;
            for (Cluster<Integer> it : clusterizer3.getClusters()) {
                System.out.println(counter++ + ", " + Arrays.toString(it.chainSet.toArray()));
            }

        }
        double[][] matrix = GraphParser.parseGraphMatrix(filename);
        for (int numOfClusters=2; numOfClusters<=3; numOfClusters++) {
            FuzzyClusterizer fc = new FuzzyClusterizer(matrix, numOfClusters);
            double[][] similarity = fc.evaluate();

            System.out.println(Arrays.toString(fc.answerCenters));
            for (int i = 0; i < similarity.length; i++) {
                for (int j = 0; j < numOfClusters; j++) {
                    System.out.print(similarity[i][j] + " ");
                }
                System.out.println();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        new PureClusterizerRunner().run("2LJI_optim");
    }
}
