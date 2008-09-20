package dynsim.graphics.plot.j2d.layer.field.impl;

import dynsim.graphics.plot.j2d.layer.field.AbstractVectField;
import dynsim.math.vector.Vector2D;
import dynsim.simulator.ode.system.OdeSystem;

public class OdeVectField extends AbstractVectField {
	private double[] tempRes;

	private OdeSystem sys;

	protected Vector2D func(Vector2D v) {
		tempRes[1] = v.getX();
		tempRes[2] = v.getY();
		if (tempRes.length > 3)
			tempRes[3] = 0;

		tempRes = sys.eval(tempRes);

		return new Vector2D(tempRes[1], tempRes[2]);
	}

	public OdeVectField(OdeSystem sys) {
		super();
		this.sys = sys;
		tempRes = sys.getInitialValues();
	}
}
