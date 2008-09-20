package dynsim.simulator;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;

public abstract class AbstractSimulator implements Simulator {
	protected int curIter;

	protected int itersMax;

	protected Storage state;

	protected int dim;

	protected double[] holder;

	protected int skip, curSkip;

	protected double max;

	protected double min;

	protected ResultProcessor delegate;

	protected boolean hasRP;

	public AbstractSimulator() {
		this.itersMax = 15000;
		this.skip = 0;
		this.curSkip = skip;
		this.hasRP = false;
	}

	public void setDelegate(ResultProcessor delegate) {
		this.delegate = delegate;
		this.hasRP = true;
		this.delegate.setSimulator(this);
	}

	public ResultProcessor getDelegate() {
		return this.delegate;
	}

	public int getCurIter() {
		return curIter;
	}

	public int getItersMax() {
		return itersMax;
	}

	public void setItersMax(int itersMax) {
		this.itersMax = itersMax;
		init();
	}

	public double[][] getRawResults() {
		return state.getRawData();
	}

	public Storage getStateSeries() {
		return state;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
		init();
	}

	protected void init() {
		max = Double.MAX_VALUE;
		min = -Double.MAX_VALUE;
		curSkip = skip;

		if (hasRP) {
			delegate.onSimulatorInit();
		}
	}

	protected double trimData(double value) {
		if (value > max)
			return max;
		if (value < min)
			return min;
		return value;
	}

	protected void trimData() {
		for (int n = 0; n < dim; n++) {
			holder[n] = trimData(holder[n]);
		}
	}

	protected void assignResultsFotIter() throws DynSimException {
		if (curSkip > 0) {
			curSkip--;
			return;
		}

		trimData();

		if (hasRP) {
			delegate.procResults(holder);
		} else {
			state.add(holder);
		}
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void reset() {
		curIter = 0;
		init();
	}
}
