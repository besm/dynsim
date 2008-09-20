package dynsim.math.analysis.global;

import dynsim.data.impl.BetterMemStorage;
import dynsim.math.analysis.local.LyapunovExponent;
import dynsim.simulator.Parameters;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.system.DynamicalSystem;

public class LyapunovPlot extends Walker {
	private static final long serialVersionUID = -8817959870887629516L;

	private int lyaItersNum;

	public LyapunovPlot() {
		lyaItersNum = 2000;
	}

	public void compute() {
		Parameters p = sys.getParameters();
		double curval = start;
		LyapunovExponent le;

		prepareData();

		for (int i = 0; i < itersNum; i++) {
			le = newLyapunovExponentCalc();
			p.put(pname, curval);
			le.compute(lyaItersNum);
			double[] exps = le.getLyapunovExponents();
			assignData(curval, exps);
			curval += step;
		}
	}

	private void assignData(double curval, double[] exps) {
		double[] hold = new double[data.getColumnsNum()];
		hold[0] = curval;

		for (int i = 1; i < data.getColumnsNum(); i++) {
			hold[i] = exps[i - 1];
		}

		data.add(hold);
	}

	private void prepareData() {
		String[] labels = new String[getNumExps() + 1];
		labels[0] = pname;
		if (labels.length > 2) {
			for (int i = 1; i < getNumExps(); i++) {
				labels[i] = "l" + i;
			}
		} else {
			labels[1] = "l1";
		}
		data = new BetterMemStorage(labels.length, itersNum, labels);
	}

	private int getNumExps() {
		return sys.getIndepVarsNumNoTime();
	}

	private LyapunovExponent newLyapunovExponentCalc() {
		if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
			return new LyapunovExponent((OdeSystem) sys);
		}
		return new LyapunovExponent(sys);
	}

	public int getLyaItersNum() {
		return lyaItersNum;
	}

	public void setLyaItersNum(int lyaItersNum) {
		this.lyaItersNum = lyaItersNum;
	}
}
