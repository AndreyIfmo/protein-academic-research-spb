package ru.ifmo.ctd.proteinresearch.ordering.experiments;

import org.biojava.bio.structure.Structure;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;
import ru.ifmo.ctd.proteinresearch.ordering.util.FunctionalUtils;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;


/**
 * Created by Andrey Sokolov on 02.06.2014.
 */
public class AngleExperiment {
    public static double[] runEdges(String propertiesFileName, int from, int to) throws Exception {
        double[] torsionAngleDiff = new double[0];
        if (from != to) {
            EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFileName);
            double thresoldValue = PropertiesParser.getThresoldValue(propertiesFileName);
            esls.run(thresoldValue);
            int[][][] paths = esls.getPaths();
            int[] pathIndexes = paths[from][to];
            if (pathIndexes != null) {
                Path path = new Path(pathIndexes, Double.NaN);
                ConformationChain chain = esls.cg.forPath(path);
                double[] weights = esls.cg.getAngleWeights(path);
                int numberOfModels = esls.cg.getChain(from, to).getStructure().nrModels();
                //ConformationChain chain = esls.cg.getChain(9,10);
                //double weight = esls.cg.graph.getEdgeWeight(from, to);
                Structure structure = chain.getStructure();
                torsionAngleDiff = VertexEdgeAnalyzer.torsionAngleDiff(chain, weights, numberOfModels);
            }
        }
        return torsionAngleDiff;
    }

    public static double [] runMaxAmplitude(String propertiesFileName, int from, int n) throws Exception {
        double [] answer = new double[n];
        for (int i=0; i<n; i++) {
            answer[i] = FunctionalUtils.sum(runEdges(propertiesFileName, from, i));
        }
        return answer;
    }
}
