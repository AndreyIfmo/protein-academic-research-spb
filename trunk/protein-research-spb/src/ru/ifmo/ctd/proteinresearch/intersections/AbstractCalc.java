package ru.ifmo.ctd.proteinresearch.intersections;

public abstract class AbstractCalc implements Calc{
	private int step = 0;
	private ChainSequence chains;
	
	public AbstractCalc(ChainSequence chains){
		this.chains = chains;
		Initalize();
	}
	
	@Override
	public int hasIntersections(ChainSequence chains) {
		return 0;
	}
	
}
