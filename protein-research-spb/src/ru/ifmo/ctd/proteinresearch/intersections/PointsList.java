package ru.ifmo.ctd.proteinresearch.intersections;

public class PointsList {
    PointsListElement first = new PointsListElement(null);

    public void add(PointsListElement p) {
        PointsListElement tmp = first.next;
        first.next = p;
        p.prev = first;
        p.next = tmp;
        if (tmp != null) {
            tmp.prev = p;
        }
        p.position = this;
    }

    public boolean isEmpty() {
        return first.next == null;
    }
}
