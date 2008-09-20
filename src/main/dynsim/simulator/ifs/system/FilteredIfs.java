package dynsim.simulator.ifs.system;

import dynsim.simulator.ifs.system.filter.Filter;

public interface FilteredIfs extends Filter, IteratedFunctionSystem {
	void add(Filter sinusoidal);
}
