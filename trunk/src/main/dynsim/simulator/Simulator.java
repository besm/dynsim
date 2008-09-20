package dynsim.simulator;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.math.numeric.Computable;
import dynsim.simulator.system.DynamicalSystem;

public interface Simulator extends Computable {
	public abstract void nextStep() throws DynSimException;

	public abstract int getCurIter();

	public abstract double[][] getRawResults();

	public abstract Storage getStateSeries();

	public abstract int getItersMax();

	public abstract void setItersMax(int itersMax);

	public abstract void setSkip(int skip);

	public abstract int getSkip();

	public abstract double getMax();

	public abstract void setMax(double max);

	public abstract double getMin();

	public abstract void setMin(double min);

	public abstract void setDelegate(ResultProcessor delegate);

	public abstract ResultProcessor getDelegate();

	public abstract void reset();

	public abstract DynamicalSystem getSystem();
}
