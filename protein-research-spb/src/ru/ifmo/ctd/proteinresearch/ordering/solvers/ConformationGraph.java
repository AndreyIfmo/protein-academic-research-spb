package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.biojava.bio.structure.io.PDBFileReader;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A conformation change matrix augmented with edge chains of conformations.
 *
 * @author Maxim Buzdalov
 */
public class ConformationGraph {
    public final Graph graph;
    private final ConformationChain[][] chains;
    public final File[][] files;
    public final ConformationChain[] roots;
    private final PDBFileReader fileReader = new PDBFileReader();
    private final Queue<Integer> openChains = new ArrayDeque<>();
    public final String matrixFileName;
    public final String zipArchive;
    public final String fileNamePattern;
    public final int indexOffset;

    public ConformationChain getChain(int source, int target) {
        if (source == target) {
            throw new IllegalArgumentException("source == target");
        }
        if (chains[source][target] == null) {
            try {
                int firstIndex = Math.min(source, target);
                int secondIndex = Math.max(source, target);
                if (files[firstIndex][secondIndex] == null) {
                    throw new IllegalArgumentException("Path is not computed" + firstIndex + " " + secondIndex);
                }
                Structure structure = fileReader.getStructure(files[firstIndex][secondIndex]);
                chains[firstIndex][secondIndex] = new ConformationChain(structure);
                if (roots[firstIndex] == null) {
                    roots[firstIndex] = chains[firstIndex][secondIndex];
                } else {
                    chains[firstIndex][secondIndex] = chains[firstIndex][secondIndex].alignStarts(roots[firstIndex]);
                }
                chains[secondIndex][firstIndex] = chains[firstIndex][secondIndex].reverse();
                if (roots[secondIndex] == null) {
                    roots[secondIndex] = chains[secondIndex][firstIndex];
                } else {
                    chains[secondIndex][firstIndex] = chains[secondIndex][firstIndex].alignStarts(roots[secondIndex]);
                }
                int chainID = firstIndex * chains.length + secondIndex;
                openChains.add(chainID);
                if (openChains.size() > chains.length) {
                    int removedID = openChains.remove();
                    int firstRemovedID = removedID / chains.length;
                    int secondRemovedID = removedID % chains.length;
                    chains[firstRemovedID][secondRemovedID] = null;
                    chains[secondRemovedID][firstRemovedID] = null;
                }
            } catch (Exception ex) {
                System.out.println("INDEXES: " + source + " " + target);
                throw new RuntimeException(ex);
            }
        }
        return chains[source][target];
    }
///

    public ConformationGraph(String matrixFileName, String zipArchive, String fileNamePattern, int indexOffset, String... bannedEdges) throws IOException {
        this.matrixFileName = matrixFileName;
        this.zipArchive = zipArchive;
        this.fileNamePattern = fileNamePattern;
        this.indexOffset = indexOffset;
        List<String> bannedFileNames = Arrays.asList(bannedEdges);
        this.graph = GraphParser.parseMatrixGraphFromFile(matrixFileName);
        int n = graph.getN();
        chains = new ConformationChain[n][n];

        Map<String, Integer> firstIndices = new HashMap<>();
        Map<String, Integer> secondIndices = new HashMap<>();
        for (int from = 0; from < n; ++from) {
            for (int to = from + 1; to < n; ++to) {
                String s = String.format(fileNamePattern, from + indexOffset, to + indexOffset);
                firstIndices.put(s, from);
                secondIndices.put(s, to);
            }
        }

        roots = new ConformationChain[n];

        files = new File[n][n];

        File dir = new File("temp");
        if (!dir.exists()) {
            boolean result = dir.mkdir();
            if (result) {
                System.out.print("temp directory is created");
            }
        }


        try (ZipInputStream input = new ZipInputStream(new FileInputStream(zipArchive))) {
            ZipEntry entry;
            int counter = 0;
            while ((entry = input.getNextEntry()) != null) {
                String name = entry.getName();
                Integer firstIndex = firstIndices.get(name);
                Integer secondIndex = secondIndices.get(name);
                if (firstIndex != null && secondIndex != null) {
                    File file = new File("temp/temp-" + counter++);
                    file.deleteOnExit();
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        byte[] buf = new byte[2048];
                        int size;
                        while ((size = input.read(buf)) > 0) {
                            out.write(buf, 0, size);
                        }
                    }
                    files[firstIndex][secondIndex] = file;
                    files[secondIndex][firstIndex] = file;
                    if (bannedFileNames.contains(name)) {
                        System.out.print("deleted" + name + " " + "first index:" + " " + firstIndex + "second index: " + secondIndex);
                        graph.removeEdge(firstIndex, secondIndex);
                        graph.removeEdge(secondIndex, firstIndex);
                    }
                }
                input.closeEntry();
            }
        }
    }

    public double[] getEdgeSourceTorsionAngleDiff(int source, int target) throws StructureException {
        return getChain(source, target).getEdgeSourceTorsionAngleDiff();
    }

    public ConformationChain forPath(Path path) throws StructureException {
        ConformationChain rv = new ConformationChain();
        for (int i = 1; i < path.vertices.length; ++i) {
            rv.append(getChain(path.vertices[i - 1], path.vertices[i]));
        }
        return rv;
    }

    public double [] getAngleWeights(Path path) throws StructureException {
        double [] weight = new double[path.vertices.length-1];

        for (int i = 1; i < path.vertices.length; ++i) {
            int from = path.vertices[i - 1];
            int to = path.vertices[i];
            weight[i-1] = graph.getEdgeWeight(from, to);
            int numberOfModels = chains[from][to].getStructure().nrModels();
            weight[i-1]/=numberOfModels;
        }
        return weight;
    }



    public void cleanUp() {
        for (File[] filesIt : files) {
            for (File it : filesIt) {
                it.delete();
            }
        }
    }
}
