package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.MatrixDistance;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.RMSDDistance;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.TMDistance;
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

        for (int i = 2; i < 7; i++) {
            System.out.println("============" + i + "============");
            StructuralClusterizer clusterizer = new MSTClusterizer(i, new RMSDDistance());
            clusterizer.evaluate(chains);
            int counter = 1;
            for (Cluster<Integer> it : clusterizer.getClusters()) {
                System.out.println(counter++ + " " + Arrays.toString(it.chainSet.toArray()));
            }
            StructuralClusterizer clusterizer2 = new MSTClusterizer(i, new TMDistance());
            clusterizer2.evaluate(chains);

            System.out.println("----------1------------");
            counter = 1;
            for (Cluster<Integer> it : clusterizer2.getClusters()) {
                System.out.println(counter++ + " " + Arrays.toString(it.chainSet.toArray()));
            }
            StructuralClusterizer clusterizer3 = new MSTClusterizer(i, new MatrixDistance("table-2LJI.txt"));
            clusterizer3.evaluate(chains);
            System.out.println("----------2------------");
            counter = 1;
            for (Cluster<Integer> it : clusterizer3.getClusters()) {
                System.out.println(counter++ + " " + Arrays.toString(it.chainSet.toArray()));
            }

        }
    }

    public static void main(String[] args) throws Exception {
        new PureClusterizerRunner().run("2LJI_optim");
    }
}
