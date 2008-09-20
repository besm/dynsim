package dynsim.math.numeric.ode.integration;

public abstract class AbstractIntegrator implements Integrator {

	abstract public int getDimension();

	abstract public double timeStep(double[] x, double t, double h) throws ArithmeticException;

	protected void checkBounds(double n) throws ArithmeticException {
		if (n == Double.POSITIVE_INFINITY)
			throw new ArithmeticException("+Infinity");

		if (n == Double.NEGATIVE_INFINITY)
			throw new ArithmeticException("-Infinity");

		if (n == Double.NaN)
			throw new ArithmeticException("NaN");
	}

}
