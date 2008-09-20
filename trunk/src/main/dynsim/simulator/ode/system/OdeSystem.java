package dynsim.simulator.ode.system;

import dynsim.simulator.system.DynamicalSystem;

public interface OdeSystem extends DynamicalSystem {
	public double[] eval(double[] state, double t);
}
