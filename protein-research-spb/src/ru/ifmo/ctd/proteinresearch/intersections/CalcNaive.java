package ru.ifmo.ctd.proteinresearch.intersections;

public class CalcNaive extends AbstractCalc{

	public CalcNaive(ChainSequence chains) {
		super(chains);
		Initalize();
	}
	
	
	@Override
	public int hasIntersections(ChainSequence chains) {
		PointsChain first;
		PointsChain second = chains.chains[0];
		PointsChain[] sequence = chains.chains;
		for (int i=1; i!=sequence.length;++i){
			first = second;
			second = sequence[i];
			
		}
		return -1;
	}

	
	@Override
	public void Initalize() {}
	
	
}
