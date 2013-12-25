package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.Distance;
import ru.ifmo.ctd.proteinresearch.ordering.graph.DisjointSets;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.MatrixGraph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.MinimalSpanTreeFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 24.12.13.
 */
public class MSTClusterizer implements Clusterizer {
    double[][] distanceMatrix;
    int numberOfClusters;
    Graph mst;
    DisjointSets sets;
    Distance distance;

    public MSTClusterizer(int numberOfClusters, Distance operator) {
        this.numberOfClusters = numberOfClusters;
        this.distance = operator;
    }

    @Override
    public void evaluate(List<Chain> allignedChains) {
        distanceMatrix = new double[allignedChains.size()][allignedChains.size()];
        for (int i = 0; i< allignedChains.size(); i++) {
            for(int j=0; j<allignedChains.size(); j++) {
                distanceMatrix[i][j]=distance.distance(allignedChains.get(i),allignedChains.get(j));
            }
        }
        Graph g= new MatrixGraph(distanceMatrix.length,distanceMatrix);
        sets=new DisjointSets(g.getN());
        mst = new MinimalSpanTreeFinder().getPartMST(g, numberOfClusters, sets);
    }

    @Override
    public List<Cluster> getClusters() {
        List<Cluster> clusters = new ArrayList<>(numberOfClusters);
        for (int i=0; i<mst.getN(); i++) {
            int numberOfCluster = sets.find(i);
            System.out.println(i+" "+numberOfCluster);

        }
        return null;
    }
}
