package dynsim.graphics.render.probe;

import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.ScreenMap;
import dynsim.graphics.color.ColorConverter;
import dynsim.graphics.render.impl.DensityRenderer;
import dynsim.math.vector.VectorN;
import dynsim.math.vector.VectorOps;
import dynsim.simulator.system.DynamicalSystem;

public class DirtyHax extends DensityRenderer {

	// private DynamicalSystem sys;

	private VectorN[][] normals;

	private float[][] zbuffer;

	private float zmax, zmin;

	public DirtyHax(DynamicalSystem sys) {
		normals = new VectorN[w][h];
		zbuffer = new float[w][h];

		for (int n = 0; n < zbuffer.length; n++)
			for (int k = 0; k < zbuffer.length; k++)
				zbuffer[n][k] = Float.NaN;
	}

	public void procResults(double[] holder) throws DynSimException {

		super.procResults(holder);

		double x = holder[getPosX()]; // x
		double y = holder[getPosY()]; // y

		if (holder.length > 3) {
			double z = holder[getPosZ()]; // z

			int[] xy = worldToScreen(x, y, z);

			int px = xy[0];
			int py = xy[1];

			if (outOfBounds(px, py))
				return;

			normals[px][py] = new VectorN(new double[] { 0, 0, 0, ScreenMap.toScreenDouble(x, w, -2.3, 2.3),
					ScreenMap.toScreenDouble(x, h, -0.4, 0.4), ScreenMap.toScreenDouble(x, w, -3.7, 3.7) });

			// ZB
			float cz = (float) (z);

			// Section
			// if(z>0) {
			// return;
			// }
			// End Sect...

			if (cz > zmax) {
				zmax = cz;
			}

			if (cz < zmin) {
				zmin = cz;
			}

			if (!Float.isNaN(zbuffer[px][py])) {
				float vz = zbuffer[px][py];

				if (vz <= cz) {
					zbuffer[px][py] = cz;
				}
			} else {
				zbuffer[px][py] = cz;
			}
		}
	}

	public void rasterize() {
		initImage();

		float Ia = 1f; // intensity of the ambient light
		float Ka = 0.1f; // ambient reflection coefficient

		float Ip = 1f;
		float Kd = 0.75f;
		float Ks = 0.7f;

		// chua new dirli double[] { 0, 0.1, 2 }
		// chua V 0, -1, -2
		VectorN dirLi = new VectorN(new double[] { 100, 300, 500 });
		int nspec = 6;
		VectorN V = new VectorN(new double[] { 200, 200, 200 });
		V.normalize();

		float amb = Ia * Ka; // ambient

		// initialize(0.5f, 0.5f, 0.5f);

		// float scale = 1f / maxBin;
		float den;
		float zscale = (1f / (zmax - zmin));

		float mintran = 0f;
		float maxtran = 1f;
		float ktrans = mintran + (maxtran - mintran);

		// Each pixel
		// scan line
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				// Normals
				aproxNormals(x, y);

				VectorN All = normals[x][y];

				if (All == null) {
					continue;
					// All = new VectorN(new double[] { 0, 0, 0, 0, 0, 0 });
				}

				VectorN N = new VectorN(new double[] { All.get(0), All.get(1), All.get(2) });

				N.normalize();

				// Location
				VectorN loc = new VectorN(new double[] { All.get(3), All.get(4), All.get(5) });

				VectorN L = (VectorN) VectorOps.sub(dirLi, loc);

				L.normalize();

				float dL = (float) L.normMax();
				float att = (float) Math.min(1f / (0.1f + (0.45f * dL) + 0.25 * (dL * dL)), 1);

				float NL = (float) VectorOps.dot(N, L);
				float diff = (float) (Kd * NL); // diffusion

				// Reflection
				VectorN R = (VectorN) N.multiply(2).multiply(NL).substract(L);
				R.normalize();

				float RV = (float) VectorOps.dot(R, V);

				for (int n = 0; n < nspec; n++) {
					RV *= RV;
				}

				float I = amb + /* att */Ip * (diff + Ks * RV);

				// float kt = (float) (ktrans * (1 - (1 - N.get(2))));

				// ZB
				// float rz = zbuffer[x][y];
				// if (!Float.isNaN(rz)) {
				// rz = zscale * rz;
				// rz = (1f + rz) / 2f;
				// } else {
				// rz = 0;
				// }

				// Density
				den = bins[x][y];
				// den = scale * den;

				// I = (1f - kt) * den + kt * I;

				float h, s, v;

				// Solid
				h = 0.74f;
				s = I;
				v = s;
				// ---

				// Plasma
				// h = 0.9f + (rz + den); // dominant wave length
				// s = rz;
				// v = (0.85f * den) + (0.15f * rz);
				// ---

				// v=0.5f*(rz+den);
				// h = 0.f + (rz * rz);

				// s = 4f * (tmp / rz);
				// s = 1f / (ts + tmp);
				// s = tmp / ts;
				// h = 6f * (tmp/ts);
				// v = s * tmp;
				// h = tmp/(ts*rz);
				// v = tmp*ts;
				// v = 3f * (ts * s);
				// h = 1f + tmp + ts;

				// h = 0.75f * (tmp + ts);
				// s = 0.25f + ts;
				// v = 1.75f * (tmp / s);

				// v = trim(v);
				// s = trim(s);

				float rgb[] = ColorConverter.HSVtoRGB(h, s, v);
				float r, g, b;
				r = rgb[0];
				g = rgb[1];
				b = rgb[2];

				// tronic
				// r = logTransfer(r, 1.5f);
				// g = logTransfer(g, 1f);
				// b = logTransfer(r, 2f);

				r = trim(r);
				g = trim(g);
				b = trim(b);

				int col = new Color(r, g, b).getRGB();

				img.setRGB(x, y, col);
			}
		}
	}

	private float dNan(float v) {
		if (Float.isNaN(v))
			return 0;

		return v;
	}

	private void aproxNormals(int x, int y) {
		// if (x > w - 2 || y > h - 2)
		// return;
		//
		// VectorN X, Y, O;
		//
		// X = new VectorN(new double[] { 1, 0,
		// dNan(zbuffer[x + 1][y]) - dNan(zbuffer[x][y]) });
		// Y = new VectorN(new double[] { 0, 1,
		// dNan(zbuffer[x][y + 1]) - dNan(zbuffer[x][y]) });
		// O = new VectorN(new double[] { 0, 0,
		// dNan(zbuffer[x][y]) - dNan(zbuffer[x][y]) });
		//
		// VectorN OX = (VectorN) O.substract2(X);
		// VectorN OY = (VectorN) O.substract2(Y);
		//
		// VectorN N = OX.cross(OY);
		//
		// if (normals[x][y] != null) {
		// normals[x][y].set(0, N.get(0));
		// normals[x][y].set(1, N.get(1));
		// normals[x][y].set(2, N.get(2));
		// }
	}

	public int gamma(int rgb) {
		int rgbo;

		int a = (rgb >> 24) & 0xff;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		r = rTable[r];
		g = gTable[g];
		b = bTable[b];
		rgbo = (a << 24) | (r << 16) | (g << 8) | b;

		return rgbo;
	}

	private int[] rTable, gTable, bTable;

	private void initialize(float rValue, float gValue, float bValue) {
		rTable = buildTable(rValue);

		if (gValue == rValue)
			gTable = rTable;
		else
			gTable = buildTable(gValue);

		if (bValue == rValue)
			bTable = rTable;
		else if (bValue == gValue)
			bTable = gTable;
		else
			bTable = buildTable(bValue);
	}

	private int[] buildTable(float gamma) {
		int[] table = new int[256];
		float oneOverGamma = 1.0f / gamma;
		for (int i = 0; i < 256; ++i) {
			int v = (int) ((255.0D * Math.pow(i / 255.0D, oneOverGamma)) + 0.5D);
			if (v > 255)
				v = 255;
			table[i] = v;
		}
		return table;
	}

	public void rasterize3() {
		initImage();
		// drawBox();

		System.out.println("Raster begin " + System.currentTimeMillis());

		float exposure = 150f; // relativo al num. de iters
		float gamma = 0.40f;
		// float scale = 1f / maxBin;
		float tmp;
		float r = 0, g = 0, b = 0;

		float ambient = 0.05f;

		// Each pixel
		// scan line
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				// tmp = denBins[x][y]* scale;
				// tmp = (float) Math.pow(tmp, gamma);
				tmp = bins[x][y];

				// Crypton --------
				// tmp = scale * tmp;

				r = logTransfer(tmp, 25f);
				g = ambient + logTransfer(tmp, 5f);
				b = logTransfer(tmp, 2.5f);

				// BlackWhite -----------

				// r = g = b = inte + tmp;

				// Set Color (trim) ------------
				r = trim(r);
				g = trim(g);
				b = trim(b);

				int col = new Color(r, g, b).getRGB();

				img.setRGB(x, y, col);
			}
		}

		System.out.println("Raster end " + System.currentTimeMillis());
	}

	private float trim(float v) {
		if (v < 0)
			return 0;
		if (v > 1)
			return 1;

		return v;
	}

}
