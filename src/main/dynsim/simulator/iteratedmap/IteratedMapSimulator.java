package dynsim.simulator.iteratedmap;

import dynsim.data.impl.MemStorage;
import dynsim.exceptions.DynSimException;
import dynsim.simulator.AbstractSimulator;
import dynsim.simulator.ResultProcessor;
import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.system.DynamicalSystem;

public class IteratedMapSimulator extends AbstractSimulator {
	private IteratedMap map;

	public IteratedMapSimulator(IteratedMap map) {
		super();
		this.map = map;
		init();
	}

	/**
	 * @param map
	 * @param delegate
	 */
	public IteratedMapSimulator(IteratedMap map, ResultProcessor delegate) {
		super();
		this.map = map;
		setDelegate(delegate);
		init();
	}

	// public void setItersMax(int iterMax) {
	// this.itersMax = iterMax;
	// init();
	// }

	protected void init() {
		super.init();

		this.holder = map.getInitialValues();
		this.dim = map.getIndependentVarsNum();

		if (!hasRP) {
			this.state = new MemStorage(new double[dim][itersMax - skip], map.getIndependentVarNames());
		}
	}

	public void compute() throws DynSimException {
		for (int i = 0; i < itersMax; i++) {
			nextStep();
		}
	}

	public void nextStep() throws DynSimException {
		holder = map.eval(holder);
		assignResultsFotIter();
		curIter++;
	}

	// public void setSkip(int skip) {
	// super.setSkip(skip);
	// init();
	// }

	public DynamicalSystem getSystem() {
		return map;
	}
}
