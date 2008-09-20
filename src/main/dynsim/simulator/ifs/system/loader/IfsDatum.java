package dynsim.simulator.ifs.system.loader;

public class IfsDatum {
	protected double[] probs;
	protected double[][] coefs;

	public IfsDatum(double[][] c, double[] p) {
		this.probs = p;
		this.coefs = c;
	}

	public double[][] getCoefs() {
		return coefs;
	}

	public void setCoefs(double[][] coeff) {
		this.coefs = coeff;
	}

	public double[] getProbs() {
		return probs;
	}

	public void setProbs(double[] prob) {
		this.probs = prob;
	}
}
