package dynsim.math.analysis.global;

import dynsim.simulator.Parameters;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;

//TODO reduce ND to 1D with PoincareSection or ReturnMap...
public class BifurcationDiagram1D extends Walker {
	private int warm;

	private String vname;

	private double vstart;

	private int maxPeriod;

	private int minPeriod;

	private BifurcationPlotDelegate plot;

	private int ncycles;

	private double resolution;

	private int paramItersNum;

	private double epsilon;

	private double vend;

	public BifurcationDiagram1D(BifurcationPlotDelegate plot) {
		this.plot = plot;
		maxPeriod = 2;
		minPeriod = -1;
		warm = 0;
		resolution = 100;
		paramItersNum = 200;
	}

	public void compute() {
		prepareData();

		double a = vstart;
		double b = vend;
		Parameters p = sys.getParameters();

		double curval = start;
		epsilon = 1d / (resolution);

		Simulator sim = SimulatorFactory.createSimulator(sys);
		sim.setItersMax(warm + maxPeriod);
		sim.setSkip(warm);

		double dt = (b - a) / paramItersNum;

		for (int i = 0; i < itersNum; i++) {
			p.put(pname, curval); // assign param's value

			double da = a;

			for (int z = 0; z < paramItersNum; z++) {
				procSolutions(sim, da, curval, 0);
				da += dt;
			}

			curval += step;

		}
	}

	// private int hasCycles(Simulator sim, double a, double b) {
	// sys.setInitialCondition(vname, a); // assign state
	// // variable value
	//
	// sim.compute();
	// double[] fa = sim.getStateSeries().getAll(vname);
	// sim.setSkip(warm);
	//
	// sys.setInitialCondition(vname, b); // assign state
	// // variable value
	//
	// sim.compute();
	// double[] fb = sim.getStateSeries().getAll(vname);
	// sim.setSkip(warm);
	//
	// for (int nn = 0; nn < fa.length; nn++) {
	// double sa = fa[nn] - a;
	// double sb = fb[nn] - b;
	// if (sa > -0.1 && sb < 0.1 || sa < 0.1 && sb > -0.1) {
	// return nn;
	// }
	// }
	// return -1;
	// }

	// TODO pronóstico por la tangente?
	// Repasar tipo cómo utiliza la variable... y pensar en ND->1D
	// Fixed point iteration
	private void procSolutions(Simulator sim, double x, double p, int min) {

		sys.setInitialCondition(vname, x);

		try {
			sim.compute();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}

		double[] fn = sim.getStateSeries().getAll(vname);
		int ln = 0;

		for (int nn = min; nn < fn.length; nn++) {
			double delta = Math.abs(fn[nn] - x);
			//
			if (delta < epsilon && nn > minPeriod) {
				// fixed point N = x for parameter p
				if (Double.isNaN(fn[nn])) {
					sim.reset();
					return;
				}
				plot.putpx(p, fn[nn], nn);
				ln = nn;

				// adiabatic cond.
				sys.setInitialConditions(sim.getStateSeries().getAllForRow(ln));

				sim.reset();
				return;
			}
		}

		sim.reset();
	}

	private void prepareData() {
		String[] labels = new String[2];
		labels[0] = pname;
		labels[1] = vname;
	}

	public void setVarName(String vname) {
		this.vname = vname;
	}

	public String getVariableName() {
		return vname;
	}

	public void setVarRange(double vstart, double vend) {
		this.vstart = vstart;
		this.vend = vend;
	}

	public void setMaxPeriod(int n) {
		maxPeriod = n;
	}

	public void setWarm(int w) {
		warm = w;
	}

	public int getNumCyclesFound() {
		return ncycles;
	}

	public void setMinPeriod(int n) {
		minPeriod = n;
	}

	public int getMaxPeriod() {
		return maxPeriod;
	}

	public int getWarm() {
		return warm;
	}

	public int getParamItersNum() {
		return paramItersNum;
	}

	public void setParamItersNum(int paramItersNum) {
		this.paramItersNum = paramItersNum;
	}

	public double getResolution() {
		return resolution;
	}

	public void setResolution(double resol) {
		this.resolution = resol;
	}

	public int getMinPeriod() {
		return minPeriod;
	}

}
