package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import org.biojava.bio.structure.io.FileConvert;
import ru.ifmo.ctd.proteinresearch.intersections.Point;
import ru.ifmo.ctd.proteinresearch.ordering.graph.*;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;
import ru.ifmo.ctd.proteinresearch.ordering.util.SinCos;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Maxim Buzdalov
 */
public class PathUtil {
    public static void buildPDB(String costFile, String archive, String pathExpression,int indexOffset, String output, Path path) throws Exception {
        ConformationGraph cg = new ConformationGraph(costFile, archive, pathExpression, indexOffset);
        try (PrintWriter out = new PrintWriter(output)) {
            out.println(cg.forPath(path));
        }
        for (int i = 0; i < cg.graph.getN(); ++i) {
            for (int j = i + 1; j < cg.graph.getN(); ++j) {
                try {
                    double w0 = cg.graph.getEdgeWeight(i, j);
                    double w1 = cg.getChain(i, j).weight();
                    if (Math.abs(w0 - w1) / Math.max(w0, w1) > 0.1) {
                        System.out.println(i + " -> " + j + ": orig = " + w0 + ", new = " + w1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public static List<Chain> getInterpolatedChains(Chain from, Chain to, int k ) throws Exception {
        EvaluatedChain ec1 = new EvaluatedChain(from);
        EvaluatedChain ec2 = new EvaluatedChain(to);
        List<Chain> chains = new ArrayList<>();

        List<SinCos> torsionAngles1 = ec1.torsionAngles;
        int size = torsionAngles1.size();
        List<SinCos> torsionAngles2 = ec2.torsionAngles;
        assert size == torsionAngles2.size();
        SinCos[][] angles= new SinCos[k][size];
        for (int i=0; i < k; i++) {
            for (int j=0; j<size; j++) {
                double step = Math.abs(torsionAngles2.get(j).angle - torsionAngles1.get(j).angle)/k;
                angles[i][j] = new SinCos(torsionAngles1.get(j).angle+step*i);
            }
        }

        for (int i=0; i<k; i++) {
            ec1.torsionAngles = Arrays.asList(angles[i]);
            List<Point> points = ec1.restorePoints();
            Chain chain = (Chain) from.clone();
            int counter=0;
            for (Group group:chain.getAtomGroups()) {
                for (Atom atom: group.getAtoms()) {
                    Point point = points.get(counter);
                    atom.setX(point.x);
                    atom.setY(point.y);
                    atom.setZ(point.z);
                    counter++;
                    System.out.println(Arrays.toString(atom.getCoords()));
                }
            }
            System.out.println("+++++++++++++++++");
            chains.add(chain);

        }
        return chains;

    }




    /*List<Point> restoreCoords(Structure structure, List<Double> torsionAngles, List<Double> planarAngles)  {
        List<Atom> atoms = EvaluatedChain.getAtoms(structure.getChain(0));

        List<Double> lengths = EvaluatedChain.getLengths(points);

    }*/

    public static void writeChain(Chain chain, String fileName) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(fileName);
        StringBuffer sb=new StringBuffer();
        for (Group group:chain.getAtomGroups()) {
            for (Atom atom: group.getAtoms()) {
                FileConvert.toPDB(atom, sb);
            }
        }
        pw.print(sb.toString());

        pw.close();
    }
    public static void main(String[] args) throws Exception {
       /* buildPDB("table-1BTB.txt", "1BTB.zip", "1BTB/%02d-%02d/Result.pdb", "ResultLong.pdb", new Path(
                new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                           10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
                Double.NaN
        ));*/
        ConformationGraph cg = PropertiesParser.getGraphData("1CFC.properties");
        ConformationChain cc = cg.getChain(0,1);
        System.out.print(Arrays.toString(EvaluatedChain.getTorsionAngles(EvaluatedChain.getAtoms(cc.getStructure().getChain(0))).toArray()));
        buildPDB(cg.matrixFileName, cg.zipArchive, cg.fileNamePattern, cg.indexOffset, "Result_optim.pdb", new Path(
                new int[]{7, 22, 5, 14, 0, 20, 13},
                Double.NaN));/*
        ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        ConformationChain cc = cg.getChain(0,1);
        System.out.print(Arrays.toString(EvaluatedChain.getTorsionAngles(EvaluatedChain.getAtoms(cc.getStructure().getChain(0))).toArray()));

        buildPDB("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb", "Result_optim.pdb", new Path(
                new int[]{0, 9, 15, 17, 8, 2, 17, 10, 11, 7, 14},
                Double.NaN
        ));*/

      /*  ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        ConformationChain cc = cg.getChain(0,9);
        Structure structure = cc.getStructure();
        Chain chain1 = structure.getModel(0).get(0);
        Chain chain2 = structure.getModel(1).get(0);
        List<Chain> chainList = PathUtil.getInterpolatedChains(chain1, chain2, 50);
        align(chainList);
        PathUtil.toPDB("test.pdb", chainList);*/
    }

    public static void align(List<Chain> chains) throws StructureException {
        List<Chain> copy = new ArrayList<>();
        Chain reference = (Chain) chains.get(0).clone();
        for (Chain chain: chains) {
            copy.add(ConformationChain.align(reference, chain));
        }
        for (int i=0; i< copy.size(); i++) {
            int size = chains.get(i).getAtomGroups().size();
            for (int j=0; j< size; j++) {
                chains.get(i).getAtomGroup(j).getAtom(0).setCoords(copy.get(i).getAtomGroup(j).getAtom(0).getCoords());
                chains.get(i).getAtomGroup(j).getAtom(1).setCoords(copy.get(i).getAtomGroup(j).getAtom(1).getCoords());
                chains.get(i).getAtomGroup(j).getAtom(2).setCoords(copy.get(i).getAtomGroup(j).getAtom(2).getCoords());
            }
        }
    }
    public static void toPDB(String fileName, List<Chain> chains) throws FileNotFoundException, StructureException {
        PrintWriter pw = new PrintWriter(fileName);
        int counter = 1;
        for (Chain chain:chains) {
            pw.print("MODEL "+counter);
            pw.print(toPDB(chain));
            pw.println("ENDMDL");
            counter++;
        }
        pw.close();

    }
    public static String toPDB(Chain chain) throws StructureException {
        StringBuffer w = new StringBuffer();
        int nrGroups = chain.getAtomLength();

        for ( int h=0; h<nrGroups;h++){

            Group g= chain.getAtomGroup(h);


            toPDB(g,w);


        }

        return w.toString();
    }

    private static void toPDB(Group g, StringBuffer str) throws StructureException {
        // iterate over all atoms ...
        // format output ...
        int groupsize  = g.size();

        for ( int atompos = 0 ; atompos < groupsize; atompos++) {
            Atom a = null ;

            a = g.getAtom(atompos);
            if ( a == null)
                continue ;

            FileConvert.toPDB(a, str);


            //line = record + serial + " " + fullname +altLoc
            //+ leftResName + " " + chainID + resseq
            //+ "   " + x+y+z
            //+ occupancy + tempfactor;
            //str.append(line + newline);
            //System.out.println(line);
        }
        if ( g.hasAltLoc()){
            for (Group alt : g.getAltLocs() ) {
                toPDB(alt,str);
            }
        }

    }
}
