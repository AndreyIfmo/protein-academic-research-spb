package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import org.biojava.bio.structure.io.*;
import ru.ifmo.ctd.proteinresearch.ordering.graph.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * A conformation change matrix augmented with edge chains of conformations.
 *
 * @author Maxim Buzdalov
 */
public class ConformationGraph {
    public final Graph graph;
    public final ConformationChain[][] chains;

    public ConformationGraph(String matrixFileName, String zipArchive, String fileNamePattern) throws IOException {
        this.graph = GraphParser.parseMatrixGraphFromFile(matrixFileName);
        int n = graph.getN();
        chains = new ConformationChain[n][n];

        Map<String, Integer> firstIndices = new HashMap<>();
        Map<String, Integer> secondIndices = new HashMap<>();
        for (int from = 0; from < n; ++from) {
            for (int to = from + 1; to < n; ++to) {
                String s = String.format(fileNamePattern, from + 1, to + 1);
                firstIndices.put(s, from);
                secondIndices.put(s, to);
            }
        }

        PDBFileReader in = new PDBFileReader();
        try (ZipInputStream input = new ZipInputStream(new FileInputStream(zipArchive))) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                String name = entry.getName();
                Integer firstIndex = firstIndices.get(name);
                Integer secondIndex = secondIndices.get(name);
                if (firstIndex != null && secondIndex != null) {
                    File file = File.createTempFile("fuck-bio-java-", ".pdb");
                    file.deleteOnExit();
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        byte[] buf = new byte[2048];
                        int size;
                        while ((size = input.read(buf)) > 0) {
                            out.write(buf, 0, size);
                        }
                    }
                    Structure structure = in.getStructure(file);
                    chains[firstIndex][secondIndex] = new ConformationChain(structure);
                    chains[secondIndex][firstIndex] = chains[firstIndex][secondIndex].reverse();
                }
                input.closeEntry();
            }
        }
    }

    public double[] getEdgeSourceTorsionAngleDiff(int source, int target) throws StructureException {
        return chains[source][target].getEdgeSourceTorsionAngleDiff();
    }

    public double[] getEdgeTargetTorsionAngleDiff(int source, int target) throws StructureException {
        double[] rv = getEdgeSourceTorsionAngleDiff(target, source);
        for (int i = 0; i < rv.length; ++i) {
            rv[i] = -rv[i];
        }
        return rv;
    }

    public ConformationChain forPath(Path path) throws StructureException {
        ConformationChain rv = new ConformationChain();
        for (int i = 1; i < path.vertices.length; ++i) {
            rv.append(chains[path.vertices[i - 1]][path.vertices[i]]);
        }
        return rv;
    }
}
