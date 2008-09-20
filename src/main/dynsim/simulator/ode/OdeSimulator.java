package dynsim.simulator.ode;

import dynsim.data.impl.BetterMemStorage;
import dynsim.exceptions.DynSimException;
import dynsim.math.numeric.ode.integration.impl.*;
import dynsim.simulator.ResultProcessor;
import dynsim.simulator.ode.system.OdeSystem;

public class OdeSimulator extends AbstractOdeSimulator {

	/**
	 * @param sys
	 */
	public OdeSimulator(OdeSystem sys) {
		super();
		setSolver(new RK5(sys));
		setSys(sys);
		// this.results = new BerkeleyDBStorage(sys.getIndyVarNames());
		init();
	}

	public OdeSimulator(OdeSystem sys, ResultProcessor rend) {
		super();
		setSolver(new RK5(sys));
		setSys(sys);
		// this.results = new BerkeleyDBStorage(sys.getIndyVarNames());
		setDelegate(rend);
		init();
	}

	protected void init() {
		super.init();
		this.t = 0;
		this.h = 0.015d;
		this.dim = sys.getIndependentVarsNum();
		this.holder = sys.getInitialValues();
		if (!hasRP) {
			// this.results = new MemStorage(new double[dim][itersMax -
			// skip],
			// sys
			// .getIndyVarNames());
			this.state = new BetterMemStorage(dim, itersMax - skip, sys.getIndependentVarNames());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.simulator.Simulator#compute()
	 */
	public void compute() throws DynSimException {
		for (int i = 0; i < itersMax; i++) {
			step(i);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.simulator.Simulator#step(int)
	 */
	public void step(int i) throws DynSimException {
		curIter = i;
		t = solver.timeStep(holder, t, h);
		assignResultsFotIter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.simulator.Simulator#nextStep()
	 */
	public void nextStep() throws DynSimException {
		step(curIter + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enectic.dynamics.simulator.Simulator#setSys(com.enectic.dynamics.
	 * ode.system.ODESystem)
	 */
	public void setSys(OdeSystem sys) {
		this.sys = sys;
		init();
	}
}
