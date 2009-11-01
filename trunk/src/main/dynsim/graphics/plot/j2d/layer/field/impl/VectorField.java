package dynsim.graphics.plot.j2d.layer.field.impl;

import dynsim.graphics.plot.j2d.layer.field.AbstractVectField;
import dynsim.math.vector.Vector2D;
import dynsim.simulator.system.DynamicalSystem;

public class VectorField extends AbstractVectField {
	private double[] tempRes;

	private final DynamicalSystem sys;

	private final int pivotIndex;

	protected Vector2D func(Vector2D v) {
		tempRes[pivotIndex] = v.getX();
		tempRes[pivotIndex + 1] = v.getY();
		if (tempRes.length > pivotIndex + 2)
			tempRes[pivotIndex + 2] = 0;

		tempRes = sys.eval(tempRes);

		return new Vector2D(tempRes[pivotIndex], tempRes[pivotIndex + 1]);
	}

	public VectorField(DynamicalSystem sys) {
		super();
		this.sys = sys;
		tempRes = sys.getInitialValues();
		if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
			pivotIndex = 1;
		} else {
			pivotIndex = 0;
		}
	}
}
