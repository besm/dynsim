package dynsim.math.analysis.global;

import dynsim.data.Storage;
import dynsim.math.numeric.Computable;
import dynsim.simulator.system.DynamicalSystem;

public abstract class Walker implements Computable {

	protected DynamicalSystem sys;

	protected String pname;

	protected double start;

	protected double end;

	protected double step;

	protected int itersNum;

	protected Storage data;

	public Walker() {
		itersNum = 100;
	}

	public Storage getData() {
		return data;
	}

	public void setSystem(DynamicalSystem sys) {
		this.sys = sys;
	}

	public void setParameterName(String name) {
		this.pname = name;
	}

	public void setParameterRange(double start, double end) {
		this.start = start;
		this.end = end;
		this.step = (end - start) / itersNum;
	}

	public int getItersNum() {
		return itersNum;
	}

	public void setItersNum(int itersNum) {
		this.itersNum = itersNum;
	}

	public String getParameterName() {
		return pname;
	}
}
