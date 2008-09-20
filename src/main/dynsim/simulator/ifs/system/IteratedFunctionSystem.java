package dynsim.simulator.ifs.system;

import dynsim.simulator.ifs.system.loader.IfsDatum;
import dynsim.simulator.system.DynamicalSystem;

public interface IteratedFunctionSystem extends DynamicalSystem {
	public abstract void setDatum(IfsDatum data);
}