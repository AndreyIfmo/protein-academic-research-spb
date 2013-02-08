package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * @author Maxim Buzdalov
 */
public class PathToPDB {
    static class Model {
        public final List<String> lines;

        Model(List<String> lines) {
            this.lines = lines;
        }

        public boolean equals(Object o) {
            return o instanceof Model && lines.equals(((Model) o).lines);
        }
    }

    static class PDB {
        public final List<Model> models;
        public final String title;

        public PDB(String title, PDB[] sequence) {
            this.title = title;
            List<Model> models = new ArrayList<>();
            for (PDB pdb : sequence) {
                if (models.isEmpty()) {
                    models.add(pdb.models.get(0));
                } else {
                    if (!pdb.models.get(0).equals(models.get(models.size() - 1))) {
                        throw new AssertionError("Model gluing failed");
                    }
                }
                models.addAll(pdb.models.subList(1, pdb.models.size()));
            }
            this.models = Collections.unmodifiableList(models);
        }

        public PDB(String title, InputStream stream, boolean revert) throws IOException {
            this.title = title;
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[2048];
            int size;
            while ((size = stream.read(buffer)) > 0) {
                for (int i = 0; i < size; ++i) {
                    sb.append((char) (buffer[i]));
                }
            }
            StringTokenizer st = new StringTokenizer(sb.toString(), "\n");
            List<Model> models = new ArrayList<>();
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.startsWith("MODEL")) {
                    List<String> model = new ArrayList<>();
                    while (!(token = st.nextToken()).startsWith("ENDMDL")) {
                        model.add(token);
                    }
                    models.add(new Model(model));
                }
            }
            if (revert) {
                Collections.reverse(models);
            }
            this.models = Collections.unmodifiableList(models);
        }

        public void writeTo(PrintWriter out) {
            out.println("TITLE " + title);
            for (int i = 0; i < models.size(); ++i) {
                out.println("MODEL " + (i + 1));
                for (String s : models.get(i).lines) {
                    out.println(s);
                }
                out.println("ENDMDL");
            }
            out.println("END");
        }
    }

    public static void buildPDB(String archive, String output, Path path) throws IOException {
        String canonicalName = new File(archive).getName();
        String prefix = canonicalName.substring(0, canonicalName.lastIndexOf('.'));
        PDB[] pdbs = new PDB[path.vertices.length - 1];
        Map<String, Integer> indices = new HashMap<>();
        for (int i = 1; i < path.vertices.length; ++i) {
            int a = path.vertices[i - 1] + 1;
            int b = path.vertices[i] + 1;
            String file = String.format("%s/%02d-%02d/Result.pdb", prefix, Math.min(a, b), Math.max(a, b));
            indices.put(file, a < b ? i : -i);
        }

        try (ZipInputStream input = new ZipInputStream(new FileInputStream(archive))) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                String name = entry.getName();
                Integer index = indices.get(name);
                if (index != null) {
                    int fi = name.indexOf('/');
                    int li = name.lastIndexOf('/');
                    PDB pdb = new PDB((index > 0 ? "+" : "-") + name.substring(fi + 1, li), input, index < 0);
                    pdbs[Math.abs(index) - 1] = pdb;
                }
                input.closeEntry();
            }
        }

        try (PrintWriter out = new PrintWriter(output)) {
            new PDB("TSP Result", pdbs).writeTo(out);
        }
    }

    public static void main(String[] args) throws IOException {
        buildPDB("2LJI.zip", "Result.pdb", new Path(
                new int[] {6, 19, 3, 13, 11, 18, 4, 12, 0, 10, 16, 5, 15, 1, 2, 9, 7, 14, 17, 8},
                24631.028528
        ));

//        int[] two = new int[] {3, 16, 10, 0, 12, 4, 18, 11, 13, 6, 19, 8, 17, 14, 7, 9, 2, 1, 15, 5};
    }
}
