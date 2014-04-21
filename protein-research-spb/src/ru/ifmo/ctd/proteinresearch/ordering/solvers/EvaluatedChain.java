package ru.ifmo.ctd.proteinresearch.ordering.solvers;

import org.biojava.bio.structure.*;
import ru.ifmo.ctd.proteinresearch.intersections.Point;
import ru.ifmo.ctd.proteinresearch.ordering.util.Function;
import ru.ifmo.ctd.proteinresearch.ordering.util.FunctionalUtils;
import ru.ifmo.ctd.proteinresearch.ordering.util.SinCos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndreyS on 16.04.2014.
 */
public class EvaluatedChain {
    public final Chain chain;
    public final List<Atom> atoms;
    public final List<Double> lengths;
    public final List<Point> points;
    public final List<SinCos> planarAngles;
    public final List<SinCos> torsionAngles;
    public EvaluatedChain(Chain chain) throws StructureException {
        this.chain = chain;
        this.atoms=getAtoms(chain);
        this.points = toPoints(atoms);
        this.lengths=getLengths(points);
        this.torsionAngles = getTorsionAngles(atoms);
        this.planarAngles = getPlanarAngles(points);
    }

    public static List<Point> toPoints(List<Atom> atoms) {
        return FunctionalUtils.map(atoms, new Function<Atom, Point>() {
            @Override
            public Point apply(Atom argument) {
                return new Point(argument.getX(), argument.getY(), argument.getZ());
            }
        });
    }


    public static List<Atom> getAtoms(Chain from) {
        Chain interpolatedChain = new ChainImpl();
        List<Group> atomGroups1 = from.getAtomGroups();

        List<Atom> atoms = new ArrayList<>();
        for (Group group:atomGroups1) {
            atoms.addAll(group.getAtoms());
        }
        return atoms;
    }

    public static List<SinCos> getTorsionAngles(List<Atom> atoms) throws StructureException {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < atoms.size()-3; i++) {
            list.add(Math.toRadians(Calc.torsionAngle(atoms.get(i), atoms.get(i + 1), atoms.get(i + 2), atoms.get(i + 3))));
        }
        return FunctionalUtils.map(list, new Function<Double, SinCos>() {
            @Override
            public SinCos apply(Double argument) {
                return new SinCos(argument);
            }
        });

    }

    public static List<SinCos> getPlanarAngles(List<Point> points) {
        List<SinCos> list = new ArrayList<>();
        for (int i=0; i<points.size()-2; i++) {
            list.add(new SinCos(points.get(i+1).sub(points.get(i)), points.get(i+2).sub(points.get(i+1))));
        }
        return list;
    }

    public static List<Double> getLengths(List<Point> points) {
        List<Double> lengths = new ArrayList<>();
        for (int i = 1; i < points.size(); i++) {
            lengths.add(points.get(i-1).distance(points.get(i)));
        }
        return lengths;
    }

    public List<Point> restorePoints() {
        List<Point> answer = new ArrayList<>();
        answer.add(0 , new Point(0,0,0));
        answer.add(0 , new Point(lengths.get(0),0,0));
        answer.add(0 , new Point(lengths.get(0)+lengths.get(1) * planarAngles.get(0).cos, lengths.get(1) * planarAngles.get(0).sin,0));
        for (int i = 3; i < atoms.size(); i++) {
            double r = lengths.get(i - 1);
            SinCos p = planarAngles.get(i - 2);
            SinCos t = torsionAngles.get(i - 3);
            answer.add(i, new Point(r * p.cos, r * p.sin * t.cos, r * p.sin * t.sin));
        }
        for (int i = 3; i < atoms.size(); i++) {
            Point bc = answer.get(i - 1).sub(answer.get(i - 2)).normalized();
            Point n = ((answer.get(i - 2).sub(answer.get(i - 3))).vectorMult(bc)).normalized();
            Point nbc = n.vectorMult(bc);
            Point ci = answer.get(i);
            Point addPoint = new Point(ci.x * bc.x + ci.y * nbc.x + ci.z * n.x,
                    ci.x * bc.y + ci.y * nbc.y + ci.z * n.y,
                    ci.x * bc.z + ci.y * nbc.z + ci.z * n.z);
            answer.set(i, answer.get(i - 1).add(addPoint));
        }
        return answer;
    }



}
