package dynsim.simulator.ifs.system.impl;

import java.util.Random;

import dynsim.math.vector.Vector2D;
import dynsim.simulator.ifs.system.IteratedFunctionSystem;
import dynsim.simulator.ifs.system.loader.IfsDatum;
import dynsim.simulator.system.AbstractDynamicalSystem;
import dynsim.simulator.system.DynamicalSystem;

public class StochasticIfs extends AbstractDynamicalSystem implements IteratedFunctionSystem {
	protected double[] probs;

	protected double[][] coefs;

	private Random rand;

	private int row;

	public StochasticIfs(IfsDatum data) {
		rand = new Random();
		setDatum(data);
		row = coefs.length;
		setInitialCondition("x", 0);
		setInitialCondition("y", 0);
		setType(DynamicalSystem.IFS_T);
	}

	public void setDatum(IfsDatum data) {
		this.coefs = data.getCoefs();
		this.probs = data.getProbs();
	}

	public int getTimeNature() {
		return DISCRETE;
	}

	public double[] eval(double[] state) {
		double random_num = rand.nextDouble();
		double[] r = new double[state.length];

		for (int i = 0; i < row; i++) {
			if (betweeen(random_num, probs[i], probs[i + 1])) {
				// f(x,y)
				Vector2D v = func(state[0], state[1], i);
				// p2[0] = coefs[i][0] * p1[0] + coefs[i][1] * p1[1]
				// + coefs[i][4];
				// p2[1] = coefs[i][2] * p1[0] + coefs[i][3] * p1[1]
				// + coefs[i][5];
				r[0] = v.getX();
				r[1] = v.getY();
				break;
			}
		}

		return r;
	}

	private boolean betweeen(double a, double b, double c) {
		return (((a >= b) && (a <= c)) ? true : false);
	}

	protected Vector2D func(double x, double y, int i) {
		double xi = coefs[i][0] * x + coefs[i][1] * y + coefs[i][4];
		double yi = coefs[i][2] * x + coefs[i][3] * y + coefs[i][5];
		Vector2D v = new Vector2D(xi, yi);
		return v;
	}

}
