package dynsim.simulator.ifs.system.impl;

import java.util.ArrayList;
import java.util.Iterator;

import dynsim.math.vector.Vector2D;
import dynsim.simulator.ifs.system.FilteredIfs;
import dynsim.simulator.ifs.system.filter.Filter;
import dynsim.simulator.ifs.system.loader.IfsDatum;

public class StochasticFilteredIfs extends StochasticIfs implements FilteredIfs {
	private ArrayList<Filter> filters;

	public StochasticFilteredIfs(IfsDatum data) {
		super(data);
		filters = new ArrayList<Filter>();
	}

	public void add(Filter f) {
		filters.add(f);
	}

	public Vector2D func(double x, double y, int i) {
		Iterator<Filter> itor = filters.iterator();
		Vector2D v = filter(x, y);
		while (itor.hasNext()) {
			Filter f = itor.next();
			v = f.filter(v.getX(), v.getY());
		}
		return super.func(v.getX(), v.getY(), i);
	}

	public Vector2D filter(double x, double y) {
		return new Vector2D(x, y);
	}

}
