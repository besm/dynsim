package dynsim.simulator.ode;

import dynsim.exceptions.DynSimException;
import dynsim.math.numeric.ode.integration.Integrator;
import dynsim.simulator.AbstractSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.system.DynamicalSystem;

public abstract class AbstractOdeSimulator extends AbstractSimulator {
	protected double h, t;

	protected Integrator solver;

	protected OdeSystem sys;

	public abstract void step(int i) throws DynSimException;

	public abstract void setSys(OdeSystem sys);

	public AbstractOdeSimulator() {
		super();
	}

	public double getDt() {
		return h;
	}

	public void setDt(double h) {
		this.h = h;
	}

	public OdeSystem getSys() {
		return sys;
	}

	public double getTime() {
		return t;
	}

	public void setTime(double t) {
		this.t = t;
	}

	public Integrator getSolver() {
		return solver;
	}

	public void setSolver(Integrator solver) {
		this.solver = solver;
	}

	public DynamicalSystem getSystem() {
		return sys;
	}
}