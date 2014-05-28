package ru.ifmo.ctd.proteinresearch.ordering.experiments;

import org.biojava.bio.structure.Chain;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.PathUtil;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;
import ru.ifmo.ctd.proteinresearch.ordering.util.IntPair;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class Experiment1 {
    public static double[] run(String propertiesFileName) throws Exception {
        double[] answer;
        EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFileName);
        double thresoldValue = PropertiesParser.getThresoldValue(propertiesFileName);
        List<Integer> bannedVertices = new ArrayList<>();
        for (int i=0; i<esls.getN(); i++) {
            bannedVertices.add(i);
        }

        Chain reference = VertexEdgeAnalyzer.getBasicChain(esls.cg, esls.getN()-1);
        answer = new double[esls.getN()];
        for (int i = 0; i < esls.getN(); i++) {
            bannedVertices.remove(0);
            esls.run(thresoldValue, bannedVertices);
            int [][][] paths = esls.getPaths();
            double min = Double.MAX_VALUE;
            for (int j= 0; j< i+1; j++) {
                for (int k= j; k< i+1; k++) {
                    int [] path = paths[j][k];
                    if (path!=null) {
                        ConformationChain edge = esls.cg.forPath(new Path(path, Double.NaN));
                        min = Math.min(min, VertexEdgeAnalyzer.RMSD(reference, edge));
                    }
                }
            }
            answer[i]=min;

        }
        return answer;
    }
    public static void main(String[] args) throws Exception {
        new Experiment1().run("1BTB.properties");
    }


}
