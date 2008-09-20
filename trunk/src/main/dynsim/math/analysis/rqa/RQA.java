package dynsim.math.analysis.rqa;

public class RQA {

	private int minLineLen;

	private double pctDeterminism;

	private double pctRecurrence;

	private int numRecPoints;

	private int totalPoints;

	private float avgDiagLineLen;

	private int numDiags;

	private double ratio;

	private int maxDiag;

	protected double[] cr, cl;

	private double[] histo;

	private float entropy;

	public RQA(int total, double[] cr, double[] cl, int maxDiag, int minL) {
		this.minLineLen = minL;
		this.cr = cr;
		this.cl = cl;
		this.totalPoints = total;
		this.numRecPoints = (int) cr[0];
		this.pctRecurrence = 100 * (numRecPoints / (float) totalPoints);
		this.numDiags = (int) cl[0];
		this.maxDiag = maxDiag;
		this.histo = getHisto();

		this.pctDeterminism = 100 * (numDiags / (float) numRecPoints);
		this.ratio = pctDeterminism / pctRecurrence;
		this.entropy = getShannonEntropy();

		// float sum = 0;
		// double max = 0;
		// int lp = 0;
		// sum = getLP();
		//
		// System.out.println(lp);

		if (numDiags > 0)
			this.avgDiagLineLen = Math.round(getLP() / numDiags);
	}

	/*
	 * NOTA Shannon's entropy measures the information contained in a message as
	 * opposed to the portion of the message that is determined (or
	 * predictable). Examples of the latter include redundancy in language
	 * structure or statistical properties relating to the occurrence
	 * frequencies of letter or word pairs, triplets etc.
	 */
	private float getShannonEntropy() {
		double H = 0;

		for (int i = 1; i < histo.length; i++) {
			double p = histo[i] / numDiags;
			H += p * Math.log(p);
		}
		return (float) -H;
	}

	private int getLP() {
		int sum = 0;
		for (int i = 1; i < histo.length; i++) {
			sum += i * histo[i];
		}
		return sum;
	}

	private double[] getHisto() {
		double[] h = new double[maxDiag];

		if (maxDiag < 1)
			return h;

		h[maxDiag - 1] = cl[maxDiag - 1];

		for (int i = maxDiag - 2; i > 0; i--) {
			h[i] = (cl[i - 1] - cl[i]);
		}

		return h;
	}

	@Override
	public String toString() {
		String r = "[RQA] (" + minLineLen + ")";
		r += "\n%REC: " + pctRecurrence;
		r += "\n%DET: " + pctDeterminism;
		r += "\n#RP: " + numRecPoints;
		r += "\n#DLINES: " + numDiags;
		r += "\nRATIO: " + ratio;
		r += "\nAVGDIAGLEN: " + avgDiagLineLen;
		r += "\nENTROPY: " + entropy;
		if (maxDiag > 1)
			r += "\nMAXDIAG: " + maxDiag + " #[" + histo[maxDiag - 1] + "]";
		return r;
	}
}
