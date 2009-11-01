package dynsim.graphics.render.impl;

import dynsim.graphics.render.AbstractRenderer;
import dynsim.graphics.render.RenderConfig;
import dynsim.graphics.render.bones3D.Camera;
import dynsim.graphics.render.util.FloatRange;

/**
 * @author maf83
 * 
 */
public abstract class DirectRenderer extends AbstractRenderer {

	public DirectRenderer() {
		super();
	}

	public void rasterize() {
		// directo.
	}

	@Override
	public void initialize() {
		initImage();

		conf = new RenderConfig(RenderConfig.MODE_2D);

		cam = new Camera();
		cam.setEye(w >> 1, h >> 1, d >> 1);
		cam.setZoom(0.95f);
		// cam.setRotation(.25, .65, .35);

		setAllAxisRanges(new FloatRange(-50, 50));
	}

}
