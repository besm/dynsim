package dynsim.simulator.ode.system;

import dynsim.simulator.system.AbstractDynamicalSystem;
import dynsim.simulator.system.DynamicalSystem;

public abstract class AbstractOdeSystem extends AbstractDynamicalSystem implements OdeSystem {
	public AbstractOdeSystem() {
		super();
		setInitialCondition("t", 0.); // time
		setType(DynamicalSystem.ODE_T);
	}

	public double[] eval(double[] x) {
		return eval(x, 0);
	}

	public int getTimeNature() {
		return DynamicalSystem.CONTINOUS;
	}

	@Override
	public int getIndepVarsNumNoTime() {
		// Se excluye el tiempo
		return getIndependentVarsNum() - 1;
	}
}
