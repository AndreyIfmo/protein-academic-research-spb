package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.RMSDDistance;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 14.04.13
 *         Time: 19:46
 */
public class VertexEdgeAnalyzer {
    public static double RMSD(Chain chain1, Chain chain2) throws StructureException {
        return new RMSDDistance().distance(chain1, chain2);
    }

    public static double RMSD(Chain chain1, ConformationChain chain2) throws StructureException {
        Structure structure = chain2.getStructure();
        double min = Double.MAX_VALUE;
        for (int i=0; i< structure.nrModels(); i++) {
            min = Math.min(min, RMSD(chain1, structure.getModel(i).get(0)));
        }
        return min;
    }



    public static void main(String[] args) throws IOException, StructureException {
        new VertexEdgeAnalyzer().run();
    }

    public void run() throws IOException, StructureException {
        ConformationGraph cg = PropertiesParser.getGraphData("2LJI.properties");
        int n = cg.graph.getN();
        Container result = new Container(n, 1.2);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if ((k != i) && (k != j)) {
                        Structure chainStructure = cg.getChain(i, j).getStructure();
                        Chain baseChain = getBasicChain(cg, k);
                        int numberOfModelsInChain = chainStructure.nrModels();
                        for (int it = 0; it < numberOfModelsInChain; it++) {
                            List<Chain> chainList = chainStructure.getModel(it);
                            int cou = 0;
                            for (Chain chainIt : chainList) {
                                double rmsd = RMSD(chainIt, baseChain);
                                result.addResult(i, j, k, rmsd);
                                cou++;
                                if (cou > 1) {
                                    throw new AssertionError("More than one chain");
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (result.minimalRmsdTable[i][j].size() > 0) {
                    System.out.print(i + " " + j + " " + Collections.min(result.minimalRmsdTable[i][j], new Comparator<ContainerEntry>() {
                        @Override
                        public int compare(ContainerEntry o1, ContainerEntry o2) {
                            if (o1.rmsd < o2.rmsd) {
                                return -1;
                            }
                            if (o1.rmsd > o2.rmsd) {
                                return 1;
                            }
                            return 0;
                        }

                    }) + " ");
                } else {
                    System.out.print(i + " " + j + " null ");
                }
            }
            System.out.println();
        }
        System.out.println("baseMatrix");
        double[][] rmsdMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rmsdMatrix[i][j] = RMSD(getBasicChain(cg, i), getBasicChain(cg, j));
                System.out.print((rmsdMatrix[i][j] < 1.0e-10 ? 0 : rmsdMatrix[i][j]) + " ");
            }
            System.out.println();
        }

    }

    public static Chain getBasicChain(ConformationGraph cg, int k) {
        Structure baseStructure;
        Chain baseChain;
        if (k != 0) {
            baseStructure = cg.getChain(k - 1, k).getStructure();
            baseChain = baseStructure.getModel(baseStructure.nrModels() - 1).get(0);
        } else {
            baseStructure = cg.getChain(0, 1).getStructure();
            baseChain = baseStructure.getModel(0).get(0);
        }
        return baseChain;
    }

    class ContainerEntry {
        public final double rmsd;
        public final int numberOfClosestConformation;

        ContainerEntry(double rmsd, int numberOfClosestConformation) {
            this.rmsd = rmsd;
            this.numberOfClosestConformation = numberOfClosestConformation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ContainerEntry)) {
                return false;
            }

            ContainerEntry that = (ContainerEntry) o;

            if (numberOfClosestConformation != that.numberOfClosestConformation) {
                return false;
            }
            return Double.compare(that.rmsd, rmsd) == 0;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(rmsd);
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + numberOfClosestConformation;
            return result;
        }

        @Override
        public String toString() {
            return "{" + rmsd +
                    ", " + numberOfClosestConformation +
                    '}';
        }
    }

    class Container {
        public final List<ContainerEntry>[][] minimalRmsdTable;
        public final double FILTER;

        Container(int n, double filter) {
            FILTER = filter;
            minimalRmsdTable = new ArrayList[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    minimalRmsdTable[i][j] = new ArrayList<>();
                }
            }
        }

        public void addResult(int from, int to, int numberOfBaseConformation, double rmsd) {
            if (rmsd <= FILTER) {
                minimalRmsdTable[from][to].add(new ContainerEntry(rmsd, numberOfBaseConformation));
            }
        }
    }

}
