package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import org.biojava.bio.structure.jama.*;

import java.util.*;

/**
 * This is a chain of conformations, aligned by RMSD.
 *
 * @author Maxim Buzdalov
 */
public class ConformationChain {
    private Structure storage = new StructureImpl();

    public ConformationChain() {}

    public ConformationChain(Structure inputStructure) {
        consumeStructure(inputStructure);
    }

    public ConformationChain(ConformationChain source) {
        this(source.storage);
    }

    public ConformationChain reverse() {
        Structure temp = new StructureImpl();
        for (int i = storage.nrModels() - 1; i >= 0; --i) {
            temp.addModel(storage.getModel(i));
        }
        return new ConformationChain(temp);
    }

    public double[] getEdgeSourceTorsionAngleDiff() throws StructureException {
        if (storage.nrModels() < 2) {
            throw new IllegalStateException();
        }
        List<Group> first = storage.getModel(0).get(0).getAtomGroups();
        List<Group> second = storage.getModel(1).get(0).getAtomGroups();

        if (first.size() != second.size()) {
            throw new AssertionError();
        }

        List<Atom> a1 = new ArrayList<>(first.size());
        for (Group g : first) {
            a1.addAll(g.getAtoms());
        }
        List<Atom> a2 = new ArrayList<>(first.size());
        for (Group g : second) {
            a2.addAll(g.getAtoms());
        }

        double[] diff = new double[a1.size() - 3];
        double sumSq = 0;
        for (int i = 0; i + 3 < a1.size(); ++i) {
            double t1 = Calc.torsionAngle(a1.get(i), a1.get(i + 1), a1.get(i + 2), a1.get(i + 3));
            double t2 = Calc.torsionAngle(a2.get(i), a2.get(i + 1), a2.get(i + 2), a2.get(i + 3));
            double d = t1 - t2;
            if (d > 180) {
                d -= 360;
            } else if (d <= -180) {
                d += 360;
            }
            diff[i] = d;
            sumSq += d * d;
        }
        sumSq = Math.sqrt(sumSq);
        for (int i = 0; i < diff.length; ++i) {
            diff[i] /= sumSq;
        }
        return diff;
    }

    public void append(ConformationChain otherChain) throws StructureException {
        if (storage.nrModels() == 0) {
            consumeStructure(otherChain.storage);
        } else {
            Chain lastChain = storage.getModel(storage.nrModels() - 1).get(0);
            Structure aug = otherChain.storage;
            for (int i = 0; i < aug.nrModels(); ++i) {
                List<Chain> model = aug.getModel(i);
                if (model.size() != 1) {
                    throw new AssertionError();
                }
                Chain aligned = align(lastChain, model.get(0));
                if (i == 0) {
                    for (int a = 0; a < aligned.getAtomLength(); ++a) {
                        Group gSrc = lastChain.getAtomGroup(a);
                        Group gTrg = aligned.getAtomGroup(a);
                        for (int j = 0; j < gSrc.size(); ++j) {
                            Atom aSrc = gSrc.getAtom(j);
                            Atom aTrg = gTrg.getAtom(j);
                            double[] sCoords = aSrc.getCoords();
                            double[] tCoords = aTrg.getCoords();
                            for (int k = 0; k < sCoords.length; ++k) {
                                if (Math.abs(sCoords[k] - tCoords[k]) > 1e-2) {
                                    throw new AssertionError("Unaligned. " +
                                            "src = " + Arrays.toString(sCoords) +
                                            ", trg = " + Arrays.toString(tCoords));
                                }
                            }
                        }
                    }
                } else {
                    storage.addModel(Collections.singletonList(aligned));
                    lastChain = aligned;
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TITLE PATH\n");
        for (int i = 0; i < storage.nrModels(); ++i) {
            List<Chain> model = storage.getModel(i);
            StructureImpl s = new StructureImpl();
            s.addModel(model);
            sb.append("MODEL ").append(i).append("\n");
            sb.append(s.toPDB());
            sb.append("TER\n");
            sb.append("ENDMDL\n");
        }
        sb.append("END\n");
        return sb.toString();
    }

    protected void consumeStructure(Structure structure) {
        for (int i = 0; i < structure.nrModels(); ++i) {
            List<Chain> model = structure.getModel(i);
            if (model.size() != 1) {
                throw new AssertionError();
            }
            storage.addModel(model);
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
}
