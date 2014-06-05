package ru.ifmo.ctd.proteinresearch.ordering.clustering;

import org.biojava.bio.structure.Chain;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.Cluster;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.MSTClusterizer;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.StructuralClusterizer;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.MatrixDistance;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.RMSDDistance;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrey Sokolov on 05.06.2014.
 */
public class ClusteringMarksUtils {
    public static double dannIndex(List<Cluster> clusters, MatrixDistance distance) {
        double maxDiam = Double.MIN_VALUE;
        for (Cluster<Integer> it: clusters) {
            if (maxDiam<diam(it, distance)) {
                maxDiam=diam(it,distance);
            }

        }
        double min = Double.MAX_VALUE;
        for (int i=0; i<clusters.size(); i++) {
            for (int j=0; j<clusters.size(); j++) {
                if (i!=j) {
                    if(min>clusterDist(clusters.get(i), clusters.get(j), distance)/maxDiam) {
                        min=clusterDist(clusters.get(i),clusters.get(j), distance)/maxDiam;
                    }
                }
            }
        }
        return min;
    }

    public static double diam(Cluster<Integer> cluster, MatrixDistance distance) {
        if (cluster.chainSet.size() <= 1) {
            return 0;
        } else {
            double maxValue = Double.MIN_VALUE;
            for (Integer it : cluster.chainSet) {
                for (Integer it2: cluster.chainSet) {
                    double distance1 = distance.distance(it, it2);
                    if (maxValue < distance1) {
                        maxValue = distance1;
                    }
                }
            }
            return maxValue;
        }
    }

    public static double clusterDist(Cluster<Integer> cluster1, Cluster<Integer> cluster2, MatrixDistance matrix) {
       double min= Double.MAX_VALUE;
        for (Integer it1:cluster1.chainSet) {
            for (Integer it2:cluster2.chainSet) {
                double distance = matrix.distance(it1, it2);
                if (distance<min) {
                    min = distance;
                }
            }
        }
        return min;
    }

    public static void main (String[] args) throws Exception {
        String propertiesFile = "2M2Y.properties";
        String name = "matrixes/2m2y.txt";
        EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFile);
        boolean[][] banned = esls.getBadFiles(esls.getN(), new boolean[esls.getN()][esls.getN()]);

        MatrixDistance distanceMatrix = new MatrixDistance(name, banned);

        for (int i=2; i<20; i++) {
            List<Chain> chains = new ArrayList<>();
            RMSDDistance distanceMatrix2 = new RMSDDistance();
            StructuralClusterizer clusterizer3 = new MSTClusterizer(i, distanceMatrix2);
            Chain reference = VertexEdgeAnalyzer.getBasicChain(esls.cg, 0);
            chains.add(reference);
            for (int j=1; j<esls.getN(); j++) {
                chains.add(ConformationChain.align(reference, VertexEdgeAnalyzer.getBasicChain(esls.cg, j)));
            }
            clusterizer3.evaluate(chains);
            int counter = 1;
            List<Cluster> clusters = clusterizer3.getClusters();
            for (Cluster<Integer> it : clusters) {
                System.out.println(counter++ + ", " + Arrays.toString(it.chainSet.toArray()));
            }

            System.out.println("Dann index :" + dannIndex(clusters, distanceMatrix));
        }
    }
}
