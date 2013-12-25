package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.RMSDDistance;

import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.TMDistance;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 25.12.13.
 */
public class PureClusterizerRunner {
    public void run (String name) throws Exception {
        ConformationGraph cg = new ConformationGraph(name+"_costs.txt", name+".zip", name+"/2LJI_optim%d_%d.pdb");
        int numberOfVertices= cg.graph.getN();
        List<Chain> chains = new ArrayList<>();
        Chain reference = VertexEdgeAnalyzer.getBasicChain(cg, 0);
        chains.add(reference);
        for (int i=1; i<numberOfVertices; i++) {
            chains.add(ConformationChain.align(reference, VertexEdgeAnalyzer.getBasicChain(cg, i)));
        }
        Clusterizer clusterizer = new MSTClusterizer(2, new RMSDDistance());
        clusterizer.evaluate(chains);
        clusterizer.getClusters();
        Clusterizer clusterizer2 = new MSTClusterizer(2, new TMDistance());
        clusterizer2.evaluate(chains);
        clusterizer2.getClusters();
    }
    public static void main(String[] args) throws Exception {
        new PureClusterizerRunner().run("2LJI_optim");
    }
}
