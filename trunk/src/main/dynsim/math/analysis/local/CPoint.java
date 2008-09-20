package dynsim.math.analysis.local;

import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.layer.AbstractMarker;

public class CPoint extends AbstractMarker {

	public CPoint(double[] coords) {
		super(coords);
	}

	@Override
	protected void draw(float x, float y, Grapher2D g) {
		if (g.checkBounds(x, y)) {
			if (getType() == LocalStabilityAnalyser.FIXED) {
				g.drawCircle((int) x, (int) y, 2, getColor());
			} else {
				g.fillCircle((int) x, (int) y, g.getWidth() / 100, getColor());
			}
		}
	}
}
