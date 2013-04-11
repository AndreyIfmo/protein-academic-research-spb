package ru.ifmo.ctd.proteinresearch.intersections;

public class CalcNaive extends AbstractCalc {

	public CalcNaive(ChainSequence chains) {
		super(chains);
	}

	@Override
	public int hasIntersections() {
		PointsChain first;
		PointsChain second = chains.chains[0];
		PointsChain[] sequence = chains.chains;
		int chainLength = second.chain.length;

		for (int i = 1; i != sequence.length; ++i) {
			first = second;
			second = sequence[i];
			for (int j = 1; j != chainLength; ++j) {
				for (int k = 1; k != chainLength; ++k) {
					if (movingSegmentsIntersection(first.chain[j - 1],
							first.chain[j], second.chain[j - 1],
							second.chain[j], first.chain[k - 1],
							first.chain[k], second.chain[k - 1],
							second.chain[k])==true){
						return i;
					}
				}
			}
		}
		return -1;
	}

}
