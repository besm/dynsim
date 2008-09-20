package dynsim.math.numeric.ode.integration;

public interface Integrator {
	public int getDimension();

	/**
	 * Integra un paso de longitud h con los valores x[] para el tiempo t.
	 * 
	 * @param x
	 *            tiempo + variables dependientes
	 * @param t
	 *            tiempo actual
	 * @param h
	 *            incremento de tiempo
	 * @return tiempo incrementado
	 * @throws ArithmeticException
	 */
	public double timeStep(double[] x, double t, double h) throws ArithmeticException;

}
