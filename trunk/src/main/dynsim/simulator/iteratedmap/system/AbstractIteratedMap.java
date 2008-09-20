package dynsim.simulator.iteratedmap.system;

import dynsim.simulator.system.AbstractDynamicalSystem;
import dynsim.simulator.system.DynamicalSystem;

public abstract class AbstractIteratedMap extends AbstractDynamicalSystem implements IteratedMap {

	public AbstractIteratedMap() {
		super();
		setType(DynamicalSystem.MAP_T);
	}

	public int getTimeNature() {
		return DynamicalSystem.DISCRETE;
	}
}
