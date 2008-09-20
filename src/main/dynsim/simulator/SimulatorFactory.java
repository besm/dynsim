/**
 * 
 */
package dynsim.simulator;

import dynsim.simulator.ifs.IfsSimulator;
import dynsim.simulator.ifs.system.IteratedFunctionSystem;
import dynsim.simulator.iteratedmap.IteratedMapSimulator;
import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.system.DynamicalSystem;

/**
 * @author maf83
 * 
 */

public abstract class SimulatorFactory {
	public static Simulator createSimulator(DynamicalSystem sys) {
		switch (sys.getType()) {
		case DynamicalSystem.ODE_T:
			return new OdeSimulator((OdeSystem) sys);
		case DynamicalSystem.MAP_T:
			return new IteratedMapSimulator((IteratedMap) sys);
		case DynamicalSystem.IFS_T:
			return new IfsSimulator((IteratedFunctionSystem) sys);
		default:
			return null;
		}
	}

	public static Simulator createSimulator(DynamicalSystem sys, ResultProcessor rp) {
		switch (sys.getType()) {
		case DynamicalSystem.ODE_T:
			return new OdeSimulator((OdeSystem) sys, rp);
		case DynamicalSystem.MAP_T:
			return new IteratedMapSimulator((IteratedMap) sys, rp);
		case DynamicalSystem.IFS_T:
			return new IfsSimulator((IteratedFunctionSystem) sys, rp);
		default:
			return null;
		}
	}
}
