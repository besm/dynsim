package dynsim.simulator;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Parameters {
	private Map<String, Double> params;

	public Parameters() {
		params = new LinkedHashMap<String, Double>();
	}

	public double getValue(String key) {
		double r = 0;
		r = params.get(key);
		return r;
	}

	public double getValue(int index) {
		double r = 0;
		r = params.get(index);
		return r;
	}

	public Collection<String> getLabels() {
		return params.keySet();
	}

	public Map<String, Double> getParameters() {
		return params;
	}

	public void put(String label, double value) {
		params.put(label, value);
	}

	public void putAll(Parameters p) {
		params.putAll(p.getParameters());
	}

	public Collection<Double> getValues() {
		return params.values();
	}

	public double[] getValuesAsArray() {
		double[] r = new double[params.size()];
		Iterator it = params.values().iterator();
		int i = 0;
		while (it.hasNext()) {
			r[i] = ((Double) it.next()).doubleValue();
			i++;
		}
		return r;
	}

	public int size() {
		return params.size();
	}

	public String toString() {
		return params.toString();
	}

	public void putAll(double[] conds) {
		Iterator<String> it = params.keySet().iterator();
		int cn = 0;

		while (it.hasNext()) {
			String key = it.next();
			params.put(key, conds[cn++]);
		}
	}
}
