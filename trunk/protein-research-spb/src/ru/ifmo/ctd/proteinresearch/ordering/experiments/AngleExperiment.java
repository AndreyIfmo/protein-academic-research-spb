package ru.ifmo.ctd.proteinresearch.ordering.experiments;

import org.biojava.bio.structure.Structure;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;


/**
 * Created by Andrey Sokolov on 02.06.2014.
 */
public class AngleExperiment {
    public static double[] runEdges(String propertiesFileName, int from, int to) throws Exception {
        EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFileName);
        double thresoldValue = PropertiesParser.getThresoldValue(propertiesFileName);
        esls.run(thresoldValue);
        int[][][] paths = esls.getPaths();
         int[] path = paths[from][to];
        double[] torsionAngleDiff = new double[0];
        if (path != null) {
            ConformationChain edge = esls.cg.forPath(new Path(path, Double.NaN));
            //ConformationChain edge = esls.cg.getChain(9,10);
            double weight = esls.cg.graph.getEdgeWeight(from, to);
            Structure structure = edge.getStructure();
             torsionAngleDiff = VertexEdgeAnalyzer.torsionAngleDiff(edge, weight, structure.nrModels());
        }
        return torsionAngleDiff;
    }
}
