package ru.ifmo.ctd.proteinresearch.ordering.experiments;

import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;

import java.util.ArrayList;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class Experiment1 {
    public static class Answer {
        public final double[] rangesBetweenReferenceAndEdges;
        public final double[] rangesBetweenReferenceAndVertices;
        public final double[] rangesBetweenReferenceAndBannedEdges;

        Answer(double[] rangesBetweenReferenceAndEdges, double[] rangesBetweenReferenceAndVertices, double[] rangesBetweenReferenceAndBannedEdges) {
            this.rangesBetweenReferenceAndEdges = rangesBetweenReferenceAndEdges;
            this.rangesBetweenReferenceAndVertices = rangesBetweenReferenceAndVertices;
            this.rangesBetweenReferenceAndBannedEdges = rangesBetweenReferenceAndBannedEdges;
        }
    }

    public static Answer runEdges(String propertiesFileName) throws Exception {

        EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFileName);
        double thresoldValue = PropertiesParser.getThresoldValue(propertiesFileName);
        List<Integer> bannedVertices = new ArrayList<>();
        for (int i = 0; i < esls.getN(); i++) {
            bannedVertices.add(i);
        }

        Chain reference = VertexEdgeAnalyzer.getBasicChain(esls.cg, esls.getN() - 1);
        double[] answerEdges = new double[esls.getN()];
        double[] answerBannedEdges = new double[esls.getN()];
        double[] answerVertices = new double[esls.getN()];


        for (int i = 0; i < esls.getN(); i++) {
            bannedVertices.remove(0);
            esls.run(thresoldValue, bannedVertices);
            int[][][] paths = esls.getPaths();
            double minEdge = Double.MAX_VALUE;
            double minVertex = Double.MAX_VALUE;
            double minBannedEdge = Double.MAX_VALUE;
            for (int j = 0; j < i + 1; j++) {
                for (int k = j; k < i + 1; k++) {
                    int[] path = paths[j][k];
                    if (path != null) {
                        ConformationChain edge = esls.cg.forPath(new Path(path, Double.NaN));
                        minEdge = Math.min(minEdge, VertexEdgeAnalyzer.RMSD(reference, edge));
                        minVertex = Math.min(minVertex, VertexEdgeAnalyzer.RMSD(reference, VertexEdgeAnalyzer.getBasicChain(esls.cg, j)));
                        minVertex = Math.min(minVertex, VertexEdgeAnalyzer.RMSD(reference, VertexEdgeAnalyzer.getBasicChain(esls.cg, k)));

                        if (esls.isBanned(j, k)) {
                            Structure structure = edge.getStructure();
                            minBannedEdge = Math.min(minBannedEdge, VertexEdgeAnalyzer.RMSD(reference, structure.getModel(structure.nrModels() / 2).get(0)));
                        }
                    }
                }
            }
            answerEdges[i] = minEdge;

            answerBannedEdges[i] = minBannedEdge;

            answerVertices[i] = minVertex;
        }
        return new Answer(answerEdges, answerVertices, answerBannedEdges);
    }

    public static double[] runVerticess(String propertiesFileName) throws Exception {
        double[] answer;
        EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFileName);
        double thresoldValue = PropertiesParser.getThresoldValue(propertiesFileName);
        List<Integer> bannedVertices = new ArrayList<>();
        for (int i = 0; i < esls.getN(); i++) {
            bannedVertices.add(i);
        }

        Chain reference = VertexEdgeAnalyzer.getBasicChain(esls.cg, esls.getN() - 1);
        answer = new double[esls.getN()];
        for (int i = 0; i < esls.getN(); i++) {
            bannedVertices.remove(0);
            esls.run(thresoldValue, bannedVertices);
            int[][][] paths = esls.getPaths();
            double min = Double.MAX_VALUE;
            for (int j = 0; j < i + 1; j++) {
                for (int k = j; k < i + 1; k++) {
                    int[] path = paths[j][k];
                    if (path != null) {
                        ConformationChain edge = esls.cg.forPath(new Path(path, Double.NaN));
                        Structure structure = edge.getStructure();
                        int numberOfModels = structure.nrModels();
                        min = Math.min(min, VertexEdgeAnalyzer.RMSD(reference, VertexEdgeAnalyzer.getBasicChain(esls.cg, j)));
                        min = Math.min(min, VertexEdgeAnalyzer.RMSD(reference, VertexEdgeAnalyzer.getBasicChain(esls.cg, k)));
                    }
                }
            }
            answer[i] = min;

        }
        return answer;
    }

    public static void main(String[] args) throws Exception {
        new Experiment1().runEdges("1BTB.properties");
    }


}
