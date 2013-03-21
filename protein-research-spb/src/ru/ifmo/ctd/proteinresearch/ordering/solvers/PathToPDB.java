package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import org.biojava.bio.structure.io.*;
import org.biojava.bio.structure.jama.*;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * @author Maxim Buzdalov
 */
public class PathToPDB {
    public static void buildPDB(String archive, String pathExpression, String output, Path path) throws Exception {
        Chain[][] pdbs = new Chain[path.vertices.length - 1][];
        Map<String, Integer> indices = new HashMap<>();
        for (int i = 1; i < path.vertices.length; ++i) {
            int a = path.vertices[i - 1] + 1;
            int b = path.vertices[i] + 1;
            String file = String.format(pathExpression, Math.min(a, b), Math.max(a, b));
            indices.put(file, a < b ? i : -i);
        }

        PDBFileReader in = new PDBFileReader();

        try (ZipInputStream input = new ZipInputStream(new FileInputStream(archive))) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                String name = entry.getName();
                Integer index = indices.get(name);
                if (index != null) {
                    String fileName = ("tmp-" + name + ".pdb").replace('/', '-');
                    try (FileOutputStream out = new FileOutputStream(fileName)) {
                        byte[] buf = new byte[2048];
                        int size;
                        while ((size = input.read(buf)) > 0) {
                            out.write(buf, 0, size);
                        }
                    }
                    Structure structure = in.getStructure(fileName);
                    Chain[] models = new Chain[structure.nrModels()];
                    System.out.println(name + ": " + models.length + " models");

                    for (int i = 0; i < models.length; ++i) {
                        List<Chain> model = structure.getModel(i);
                        if (model.size() != 1) {
                            throw new AssertionError("Teapot!!1");
                        }
                        models[i] = model.get(0);
                    }

                    if (index < 0) {
                        Collections.reverse(Arrays.asList(models));
                    }

                    pdbs[Math.abs(index) - 1] = models;
                }
                input.closeEntry();
            }
        }

        Structure result = new StructureImpl();

        Chain last = null;

        int index = 0;
        for (Chain[] models : pdbs) {
            if (last == null) {
                last = models[0];
                result.addModel(Collections.singletonList(last));
            } else {
                Chain aligned = align(last, models[0]);
                for (int i = 0; i < aligned.getAtomLength(); ++i) {
                    Group gSrc = last.getAtomGroup(i);
                    Group gTrg = aligned.getAtomGroup(i);
                    for (int j = 0; j < gSrc.size(); ++j) {
                        Atom aSrc = gSrc.getAtom(j);
                        Atom aTrg = gTrg.getAtom(j);
                        double[] sCoords = aSrc.getCoords();
                        double[] tCoords = aTrg.getCoords();
                        for (int k = 0; k < sCoords.length; ++k) {
                            if (Math.abs(sCoords[k] - tCoords[k]) > 1e-2) {
                                throw new AssertionError("Unaligned. Index = " + index +
                                        ", src = " + Arrays.toString(sCoords) +
                                        ", trg = " + Arrays.toString(tCoords));
                            }
                        }
                    }
                }
            }
            for (int i = 1; i < models.length; ++i) {
                Chain aligned = align(last, models[i]);
                result.addModel(Collections.singletonList(aligned));
                last = aligned;
            }
            ++index;
        }

        try (PrintWriter out = new PrintWriter(output)) {
            out.println("TITLE PATH");
            for (int i = 0; i < result.nrModels(); ++i) {
                List<Chain> mdl = result.getModel(i);
                StructureImpl s = new StructureImpl();
                s.addModel(mdl);
                out.println("MODEL " + i);
                out.print(s.toPDB());
                out.println("TER");
                out.println("ENDMDL");
            }
            out.println("END");
        }
    }

    private static Chain align(Chain reference, Chain argument) throws StructureException {
        Atom[] prev = StructureTools.getAtomCAArray(reference);
        Atom[] curr = StructureTools.getAtomCAArray(argument);

        SVDSuperimposer poser = new SVDSuperimposer(prev, curr);
        Matrix rotation = poser.getRotation();
        Atom translation = poser.getTranslation();

        Chain newLast = new ChainImpl();
        for (Group g : argument.getAtomGroups()) {
            Group z = (Group) g.clone();
            Calc.rotate(z, rotation);
            Calc.shift(z, translation);
            newLast.addGroup(z);
        }
        return newLast;
    }

    public static void main(String[] args) throws Exception {
//        buildPDB("2LJI.zip", "2LJI/%02d-%02d/Result.pdb", "Result.pdb", new Path(
//                new int[] {6, 19, 3, 13, 11, 18, 4, 12, 0, 10, 16, 5, 15, 1, 2, 9, 7, 14, 17, 8},
//                24631.028528
//        ));
//        buildPDB("2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb", "Result_optim.pdb", new Path(
//                new int[] {8, 19, 6, 13, 11, 2, 1, 9, 7, 14, 17, 0, 10, 15, 5, 3, 16, 12, 4, 18},
//                20552.23
//        ));
        buildPDB("2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb", "Result_optim.pdb", new Path(
                new int[] {8, 19, 13, 18},
                Double.NaN
        ));
    }
}
