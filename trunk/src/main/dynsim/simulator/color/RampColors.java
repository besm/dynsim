package dynsim.simulator.color;

import java.awt.Color;

import dynsim.data.Storage;
import dynsim.graphics.color.LinearRamp;

public class RampColors implements ColoringStrategy {
	// TODO algo va mal comparar con MRampColors que está OK
	// (el resultado!) Tiene que ver con la vertical
	public int[] getColors(Storage data) {
		int len = data.getRowsNum();
		int[] colors = new int[len];
		float[] dxy = new float[len];

		float max = 0;

		for (int i = 1; i < len; i++) {
			double x = data.get(0, i - 1);
			double y = data.get(1, i - 1);
			double xx = data.get(0, i);
			double yy = data.get(1, i);

			float tmp = (float) Math.abs((xx - x) + (yy - y));
			if (tmp > max)
				max = tmp;
			dxy[i - 1] = tmp;
		}

		LinearRamp r = new LinearRamp();
		int[] ramp = r.getColors(500, new Color(255, 255, 128), new Color(200, 0, 0));
		float ratio = (ramp.length - 1) / max;

		for (int i = 0; i < len - 1; i++) {
			int color = (int) (ratio * dxy[i]);
			colors[i] = ramp[color];
		}

		return (int[]) colors.clone();
	}

}
