package ru.ifmo.ctd.proteinresearch.intersections;

import java.util.LinkedList;

public class CalcRectangularTree extends AbstractCalc {
	public int minimalProportion = 4;
	public int maximumSegments = 20;

	public CalcRectangularTree(ChainSequence chains) {
		super(chains);
	}

}
