package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.distance.RMSDDistance;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;
import ru.ifmo.ctd.proteinresearch.ordering.util.SinCos;

import java.io.IOException;
import java.util.*;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 14.04.13
 *         Time: 19:46
 */
public class VertexEdgeAnalyzer {
    public static double RMSD(Chain chain1, Chain chain2) throws StructureException {
        return new RMSDDistance().distance(chain1, chain2);
    }
    public static double[] torsionAngleDiff(Chain chain1, Chain chain2, double norm) throws Exception {
        List<Atom> atoms1 = EvaluatedChain.getAtoms(chain1);
        List<SinCos> torsionAngles1 = EvaluatedChain.getTorsionAngles(atoms1);
        List<Atom> atoms2 = EvaluatedChain.getAtoms(chain2);
        List<SinCos> torsionAngles2 = EvaluatedChain.getTorsionAngles(atoms2);
        double[] angles = new double[torsionAngles1.size()];
        for (int i=0; i< torsionAngles1.size(); i++) {
            angles[i]=(torsionAngles2.get(i).sub(torsionAngles1.get(i)).angle)/norm;
        }
        return angles;
    }

    public static double RMSD(Chain chain1, ConformationChain chain2) throws StructureException {
        Structure structure = chain2.getStructure();
        double min = Double.MAX_VALUE;
        for (int i=0; i< structure.nrModels(); i++) {
            min = Math.min(min, RMSD(chain1, structure.getModel(i).get(0)));
        }
        return min;
    }

    public static double[] torsionAngleDiff(ConformationChain chain, double[] norm, int n) throws Exception {
        Structure structure = chain.getStructure();
        if (structure.nrModels()>0) {
            List<Atom> atoms1 = EvaluatedChain.getAtoms(structure.getModel(0).get(0));
            List<SinCos> torsionAngles1 = EvaluatedChain.getTorsionAngles(atoms1);
            int size = torsionAngles1.size();
            double[] maxDiff = new double[size];
            double[] minDiff = new double[size];
            Arrays.fill(maxDiff, Double.MIN_VALUE);
            Arrays.fill(minDiff, Double.MAX_VALUE);
            for (int i = 1; i < structure.nrModels(); i++) {
                double[] temp = torsionAngleDiff(structure.getModel(i - 1).get(0), structure.getModel(i).get(0), norm[(i-1)/n]);

                for (int j = 0; j < temp.length; j++) {
                    maxDiff[j] = Math.max(maxDiff[j], temp[j]);
                    minDiff[j] = Math.min(minDiff[j], temp[j]);
                }
            }
            double[] answer = new double[maxDiff.length];
            for (int i = 0; i < maxDiff.length; i++) {
                answer[i] = maxDiff[i] - minDiff[i];
            }
            return answer;
        }
        return new double[0];
    }



    public static void main(String[] args) throws IOException, StructureException {
        new VertexEdgeAnalyzer().run();
    }

    public void run() throws IOException, StructureException {
        ConformationGraph cg = PropertiesParser.getGraphData("1BTB.properties");
        int n = cg.graph.getN();
        Container result = new Container(n, 1.2);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if ((k != i) && (k != j)) {
                        if (cg.files[i][j]!=null) {
                            Structure chainStructure = cg.getChain(i, j).getStructure();
                            Chain baseChain = getBasicChain(cg, k);
                            int numberOfModelsInChain = chainStructure.nrModels();
                            for (int it = 0; it < numberOfModelsInChain; it++) {
                                List<Chain> chainList = chainStructure.getModel(it);
                                int cou = 0;

                                double rmsd = RMSD(chainList.get(0), baseChain);
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
        double min = Double.MAX_VALUE;
        double mean=0;
        for (int i = 0; i < n; i++) {
            double meanI=0;
            for (int j = 0; j < n; j++) {

                rmsdMatrix[i][j] = RMSD(getBasicChain(cg, i), getBasicChain(cg, j));
                if (rmsdMatrix[i][j] < 1.0e-10) {
                    System.out.print(0 + " ");
                    meanI+=rmsdMatrix[i][j];
                } else {
                    System.out.print(rmsdMatrix[i][j] + " ");
                    min = Math.min(rmsdMatrix[i][j], min);
                    meanI+=rmsdMatrix[i][j];
                }
            }
            mean += meanI/n;
            System.out.println();
        }
        System.out.println(min);
        System.out.println(mean/n);
        for (int i=1; i<rmsdMatrix.length; i++) {
            System.out.println(rmsdMatrix[i-1][i]);
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
