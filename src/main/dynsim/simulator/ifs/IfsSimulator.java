package dynsim.simulator.ifs;

import dynsim.data.impl.BetterMemStorage;
import dynsim.exceptions.DynSimException;
import dynsim.simulator.AbstractSimulator;
import dynsim.simulator.ResultProcessor;
import dynsim.simulator.ifs.system.IteratedFunctionSystem;
import dynsim.simulator.system.DynamicalSystem;

public class IfsSimulator extends AbstractSimulator {

	private IteratedFunctionSystem ifs;

	public IfsSimulator(IteratedFunctionSystem ifs) {
		super();
		this.ifs = ifs;
		init();
	}

	public IfsSimulator(IteratedFunctionSystem ifs, ResultProcessor rp) {
		super();
		this.ifs = ifs;
		setDelegate(rp);
		init();
	}

	// public void setItersMax(int iterMax) {
	// this.itersMax = iterMax;
	// init();
	// }

	public void nextStep() throws DynSimException {
		holder = ifs.eval(holder);
		assignResultsFotIter();
		curIter++;
	}

	public void compute() throws DynSimException {
		for (int i = 0; i < itersMax; i++) {
			nextStep();
		}
	}

	protected void init() {
		super.init();
		this.holder = ifs.getInitialValues();
		this.dim = ifs.getIndependentVarsNum();
		if (!hasRP) {
			this.state = new BetterMemStorage(new double[dim][itersMax - skip], ifs.getIndependentVarNames());
		}
	}

	public DynamicalSystem getSystem() {
		return ifs;
	}

}
