package dynsim.graphics.plot.j3d.axis;

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

public class AxisY extends AxisBuilder {
	public AxisY() {
	}

	public AxisY(String label, String[] tickLabels, double[] tickLocations) {
		setLabel(label);
		setTickLabels(tickLabels);
		setTickLocations(tickLocations);
	}

	public Node getNode() {
		Transform3D t3d = new Transform3D();
		t3d.set(1 / scale, new Vector3f(-0.5f, +0.5f, 0));
		Transform3D rot = new Transform3D();
		rot.rotZ(-Math.PI / 2);
		t3d.mul(rot);
		TransformGroup tg = new TransformGroup(t3d);
		tg.addChild(super.getNode());
		return tg;
	}
}
