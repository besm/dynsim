package dynsim.math.analysis;

import java.util.Iterator;

import dynsim.data.Storage;
import dynsim.data.impl.BetterMemStorage;
import dynsim.math.numeric.function.RealFunction;
import dynsim.math.numeric.ode.integration.Integrator;
import dynsim.math.numeric.ode.integration.impl.RK4;
import dynsim.simulator.ode.system.AbstractOdeSystem;
import dynsim.simulator.ode.system.OdeSystem;

public class OdeReturnMap {
	private OdeSystem sys;

	String vname;

	private int dotsNum;

	private int skipDots;

	public OdeReturnMap() {
		dotsNum = 1500;
		skipDots = 500;
	}

	public Storage getReturnMap() {
		double time = 0;
		double xn = 0, xnl = 0;
		double zdotBefore, zdotAfter, delta;
		double[] state = sys.getInitialValues();
		Integrator intg = new RK4(sys);
		Integrator reduintg = new RK4(newReducSys(sys));

		int i = 0;
		Storage data = new BetterMemStorage(2, dotsNum - skipDots, new String[] { getNameVarX(), getNameVarY() });

		while (i < dotsNum) {
			zdotBefore = -(state[1] + state[2]);
			time = intg.timeStep(state, time, 0.005);
			zdotAfter = -(state[1] + state[2]);

			if ((zdotAfter > 0.0) && ((zdotBefore * zdotAfter) < 0.0)) {
				double[] tmp = state.clone();
				delta = -zdotAfter;

				reduintg.timeStep(tmp, time, delta);

				xnl = xn;
				xn = Math.abs(tmp[1]); // current max

				if (i >= skipDots) {
					data.add(new double[] { xnl, xn });
				}

				i++;
			}
		}

		return data;
	}

	private OdeSystem newReducSys(OdeSystem sys) {
		return new StepBackSystem(sys);
	}

	public void setSystem(OdeSystem sys) {
		this.sys = sys;
	}

	public String getNameVarX() {
		return "x";
	}

	public String getNameVarY() {
		return "xn";
	}

	public int getDotsNum() {
		return dotsNum;
	}

	public void setDotsNum(int dotsNum) {
		this.dotsNum = dotsNum;
	}

	protected class StepBackSystem extends AbstractOdeSystem {

		public StepBackSystem(OdeSystem sys) {
			Iterator<RealFunction> itor = sys.getFunctions().iterator();

			initConditions.putAll(sys.getInitialConditions());
			params.putAll(sys.getParameters());

			while (itor.hasNext()) {
				addFunction(itor.next());
			}
		}

		public double[] eval(double[] state, double t) {
			Iterator<RealFunction> itor = funcs.iterator();
			double[] rhs = new double[state.length];
			rhs[0] = 1;

			int i = 1;
			double[] p = new double[] { state[1], state[2], state[3] };

			while (itor.hasNext()) {
				RealFunction f = itor.next();
				rhs[i] = f.eval(p);
				i++;
			}

			itor = funcs.iterator();
			// skip first
			itor.next();

			// Henon's trick
			double xddot = 0;

			while (itor.hasNext()) {
				RealFunction f = itor.next();
				xddot += f.eval(p);
			}
			xddot = -xddot;
			rhs[0] = 1.0;
			rhs[1] = 1.0 / xddot;
			rhs[2] /= xddot;
			rhs[3] /= xddot;

			return rhs;
		}

	}
}
