package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxim Buzdalov
 */
public class PathToPDB {
    static class Model {
        public final List<String> lines;

        Model(List<String> lines) {
            this.lines = lines;
        }
    }

    static class PDB {
        public final List<Model> models;

        PDB(List<Model> models) {
            this.models = models;
        }

        public void reverse() {
            Collections.reverse(models);
        }
    }

    static PDB read(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        }
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < lines.size(); ++i) {
            if (lines.get(i).startsWith("MODEL")) {
                int j = i + 1;
                while (j < lines.size() && !lines.get(j).startsWith("MODEL")) {
                    ++j;
                }
                models.add(new Model(Collections.unmodifiableList(lines.subList(i + 1, j))));
            }
        }
        return new PDB(models);
    }

    public static void buildPDB(String root, String output, Path path) throws IOException {
        PDB[] pdbs = new PDB[path.vertices.length - 1];
        for (int i = 1; i < path.vertices.length; ++i) {
            int a = path.vertices[i - 1] + 1;
            int b = path.vertices[i] + 1;
            String file = String.format("%s/%02d-%02d/Result.pdb", root, Math.min(a, b), Math.max(a, b));
            pdbs[i - 1] = read(file);
            if (a > b) {
                pdbs[i - 1].reverse();
            }
        }
        List<Model> modelChain = new ArrayList<>();
        for (int i = 0; i < pdbs.length; ++i) {
            List<Model> local = pdbs[i].models;
            modelChain.addAll(local.subList(i == 0 ? 0 : 1, local.size()));
        }
        try (PrintWriter out = new PrintWriter(output)) {
            out.println("TITLE Conformation Chain");
            for (int i = 0; i < modelChain.size(); i++) {
                Model m = modelChain.get(i);
                out.println("MODEL " + (i + 1));
                for (String l : m.lines) {
                    out.println(l);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        buildPDB(".", "Result.pdb", new Path(
                new int[] {6, 19, 3, 13, 11, 18, 4, 12, 0, 10, 16, 5, 15, 1, 2, 9, 7, 14, 17, 8},
                24631.028528
        ));

        int[] two = new int[] {3, 16, 10, 0, 12, 4, 18, 11, 13, 6, 19, 8, 17, 14, 7, 9, 2, 1, 15, 5};
    }
}
