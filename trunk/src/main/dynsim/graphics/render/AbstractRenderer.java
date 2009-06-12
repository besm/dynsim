/**
 * 
 */
package dynsim.graphics.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.ScreenMap;
import dynsim.graphics.render.bones3D.Camera;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.helper.BoundsFinder;
import dynsim.simulator.system.DynamicalSystem;

/**
 * @author maf83
 * 
 */
public abstract class AbstractRenderer implements Renderer {
	protected int w, h, d;

	protected BufferedImage img;

	protected Graphics g;

	protected Camera cam;

	protected FloatRange ax;

	protected FloatRange ay;

	protected FloatRange az;

	protected int[] varpos;

	protected RenderConfig conf;

	protected Simulator sim;

	protected float scale;

	protected float gamma;

	protected float offset;

	protected Color backgroundColor;

	protected float detail;
	
	protected String filename;
	
	protected String directory;

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void flush() throws DynSimException {
		// TODO Auto-generated method stub
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Camera getCamera() {
		return cam;
	}

	public RenderConfig getConfig() {
		return conf;
	}

	public float getDetail() {
		return detail;
	}

	public float getGamma() {
		return gamma;
	}

	public BufferedImage getImage() {
		if (!hasImage()) {
			rasterize();
		}
		return img;
	}

	public float getOffset() {
		return offset;
	}

	public float getScale() {
		return scale;
	}

	public boolean hasImage() {
		return (img != null);
	}

	public void onSimulatorInit() {
		// null impl
	}

	public void procResults(final double[] holder) throws DynSimException {
		final double x = holder[getPosX()]; // x
		final double y = holder[getPosY()]; // y

		if (has3D(holder, sim.getSystem())) {
			final double z = holder[getPosZ()]; // z

			if (conf.isEnabled(RenderConfig.MODE_3D)) {
				put3D(x, y, z);
			} else {
				put2D(x, y);
			}

		} else {
			put2D(x, y);
		}
	}

	public abstract void rasterize();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dynsim.graphics.render.Render#setAllAxisRanges(dynsim.graphics.render
	 * .FloatRange)
	 */
	public void setAllAxisRanges(final FloatRange ra) {
		setAxisRanges(ra, ra, ra);
	}

	public void setAutoAxisRanges() throws DynSimException {
		final FloatRange[] frs;

		final Simulator tmp = SimulatorFactory.createSimulator(sim.getSystem());
		frs = BoundsFinder.getBounds(tmp);
		ax = frs[getPosX()];
		ay = frs[getPosY()];
		if (frs.length > getPosZ()) {
			az = frs[getPosZ()];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dynsim.graphics.render.Render#setAxisRanges(dynsim.graphics.render.FloatRange
	 * , dynsim.graphics.render.FloatRange)
	 */
	public void setAxisRanges(final FloatRange ax, final FloatRange ay) {
		this.ax = ax;
		this.ay = ay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dynsim.graphics.render.Render#setAxisRanges(dynsim.graphics.render.FloatRange
	 * , dynsim.graphics.render.FloatRange, dynsim.graphics.render.FloatRange)
	 */
	public void setAxisRanges(final FloatRange ax, final FloatRange ay, final FloatRange az) {
		setAxisRanges(ax, ay);
		this.az = az;
	}

	public void setBackgroundColor(final Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setCamera(final Camera cam) {
		this.cam = cam;
	}

	public void setConfig(final RenderConfig conf) {
		this.conf = conf;
	}

	public void setDefaultCorrection() {
		scale = 1f; // contrast
		gamma = 1f;//
		offset = 0f; // bright
	}

	public void reset() {
		initImage();
	}
	
	public void setDetail(float detail) {
		this.detail = detail;
	}

	public void setDimensions(final int w, final int h, final int d) {
		this.w = w;
		this.h = h;
		this.d = d;
	}

	public void setEye(final double x, final double y, final double z) {
		cam.setEye(x, y, z);
	}

	public void setGamma(final float gamma) {
		this.gamma = gamma;
	}

	public void setOffset(final float offset) {
		this.offset = offset;
	}

	public void setRotation(final double yaw, final double pitch, final double roll) {
		cam.setRotation(yaw, pitch, roll);
	}

	public void setScale(final float scale) {
		this.scale = scale;
	}

	public void setSimulator(final Simulator sim) {
		this.sim = sim;

		if (varPosIsNull()) {
			varpos = getVarPosFromSim();
		}
	}

	// TODO revisar
	// si ode 1,2,3 otherwise 0,1,2
	public void setVarpos(final int[] varpos) {
		this.varpos = varpos;
	}

	protected float clamp(final float v) {
		return (v > 1) ? 1 : (v < 0) ? 0 : v;
	}

	protected void drawAxes() {
		drawAxis("X", Color.YELLOW, 100, 0, 0);
		drawAxis("Y", Color.GREEN, 0, 100, 0);
		drawAxis("Z", Color.BLUE, 0, 0, 100);
	}

	protected void drawBox() {
		final Graphics g = img.getGraphics();
		g.setColor(new Color(255, 255, 255, 126));
		int[] xy1, xy2, xy3, xy4;
		xy1 = cam.worldToScreen(ax.getMin() * (w / (ax.getMax() - ax.getMin())), -ay.getMin()
				* (w / (ay.getMax() - ay.getMin())), az.getMax() * (d / (az.getMax() - az.getMin())));
		xy2 = cam.worldToScreen(ax.getMax() * (w / (ax.getMax() - ax.getMin())), -ay.getMax()
				* (w / (ay.getMax() - ay.getMin())), az.getMax() * (d / (az.getMax() - az.getMin())));
		g.drawRect(xy1[0], xy1[1], xy2[0] - xy1[0], xy2[1] - xy1[1]);

		xy3 = cam.worldToScreen(ax.getMin() * (w / (ax.getMax() - ax.getMin())), -ay.getMin()
				* (w / (ay.getMax() - ay.getMin())), az.getMin() * (d / (az.getMax() - az.getMin())));
		xy4 = cam.worldToScreen(ax.getMax() * (w / (ax.getMax() - ax.getMin())), -ay.getMax()
				* (w / (ay.getMax() - ay.getMin())), az.getMin() * (d / (az.getMax() - az.getMin())));
		g.setColor(new Color(255, 255, 0, 126));
		g.drawLine(xy1[0], xy1[1], xy3[0], xy3[1]);
		g.drawLine(xy2[0], xy2[1], xy4[0], xy4[1]);
		g.drawLine(xy2[0], xy1[1], xy4[0], xy3[1]);
		g.drawLine(xy1[0], xy2[1], xy3[0], xy4[1]);

		g.setColor(new Color(255, 0, 0, 126));
		g.drawRect(xy3[0], xy3[1], xy4[0] - xy3[0], xy4[1] - xy3[1]);

	}

	protected float expTransfer(final float c, final float factor) {
		return (float) (1 - Math.exp(-factor * c));
	}

	protected int getPosX() {
		return varpos[0];
	}

	protected int getPosY() {
		return varpos[1];
	}

	protected int getPosZ() {
		return varpos[2];
	}

	protected int[] getVarPosFromSim() {
		int[] vp = new int[3];
		final DynamicalSystem sys = sim.getSystem();

		if (sys != null && sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
			vp[0] = 1;
			vp[1] = 2;
			vp[2] = 3;
		} else {
			vp[0] = 0;
			vp[1] = 1;
			vp[2] = 2;
		}

		return vp;
	}

	protected void initImage() {
		img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		g = img.getGraphics();

		if (backgroundColor == null) {
			backgroundColor = Color.DARK_GRAY.darker();
		}

		g.setColor(backgroundColor);
		g.fillRect(0, 0, w, h);
	}

	protected float logTransfer(final float c, final float factor) {
		return (float) Math.log(factor * c + 1);
	}

	protected boolean outOfBounds(final int x, final int y) {
		if (x >= w || x < 0)
			return true;
		if (y >= h || y < 0)
			return true;

		return false;
	}

	protected void procPixel(final int x, final int y, final double dx, final double dy, final double dz) throws DynSimException {
		// null imp.
	}

	protected void put2D(final double dx, final double dy) throws DynSimException {
		final int x = ScreenMap.toScreenInt(dx, w, ax.getMax(), ax.getMin());
		// int y = ScreenMap.toScr(dy, h, ay.getMax(), ay.getMin());
		final int y = ScreenMap.toScreenInt(dy, h, ay.getMin(), ay.getMax());

		if (isOverflow(x, y, 0))
			return; // skip

		procPixel(x, y, dx, dy, 0);
	}

	protected void put3D(final double dx, final double dy, final double dz) throws DynSimException {
		int x, y;

		final int[] xy = worldToScreen(dx, dy, dz);

		x = xy[0];
		y = xy[1];

		if (outOfBounds(x, y))
			return;

		procPixel(x, y, dx, dy, dz);
	}

	protected void putPx(final int x, final int y, float r, float g, float b) {
		if (notIdentityGamma()) {
			r = (float) (scale * Math.pow(r, 1f / gamma) + offset);
			g = (float) (scale * Math.pow(g, 1f / gamma) + offset);
			b = (float) (scale * Math.pow(b, 1f / gamma) + offset);
		}

		r = clamp(r);
		g = clamp(g);
		b = clamp(b);

		final Color c = new Color(r, g, b);

		img.setRGB(x, y, c.getRGB());
	}

	protected int[] worldToScreen(double x, double y, double z) {
		// double tx, ty, tz;
		// tx = ScreenMap.toScrD(dx, w, ax.getMax(), ax.getMin());
		// ty = ScreenMap.toScrD(dy, h, ay.getMin(), ay.getMax());
		// tz = ScreenMap.toScrD(dz, d, az.getMax(), az.getMin());
		// // //
		// if (isOverflow(tx, ty, tz))
		// return; // skip

		x = x * (w / (ax.getMax() - ax.getMin()));
		y = y * (h / (ay.getMax() - ay.getMin()));
		z = z * (d / (az.getMax() - az.getMin()));

		return cam.worldToScreen(x, y, z);
	}

	protected int[] worldToScreen(final double[] xyz) {
		return worldToScreen(xyz[0], xyz[1], xyz[2]);
	}

	protected void writeToDisk(final String fname, final String ext, final String dir) throws IOException {
//		final File f = File.createTempFile(fname, "." + ext, new File(dir));
		final File f = new File(dir+"/"+fname+"."+ext);
		ImageIO.write(img, ext, f);
	}

	private void drawAxis(final String label, final Color color, final int x, final int y, final int z) {
		int[] xy = this.cam.worldToScreen(0, 0, 0);
		int x1 = xy[0];
		int y1 = xy[1];
		xy = this.cam.worldToScreen(x, y, z);
		int x2 = xy[0];
		int y2 = xy[1];

		g.setColor(color);

		g.drawLine(x1, y1, x2, y2);

		xy = this.cam.worldToScreen(x / 2, y / 2, z / 2);
		x2 = xy[0];
		y2 = xy[1];
		g.drawString(label, x2, y2);

		xy = this.cam.worldToScreen(-x, -y, -z);
		x2 = xy[0];
		y2 = xy[1];

		g.setColor(color.darker().darker());
		g.drawLine(x1, y1, x2, y2);
	}

	private boolean has3D(final double[] holder, final DynamicalSystem sys) {
		return ((sys.getTimeNature() == DynamicalSystem.CONTINOUS && holder.length > 3) || (sys.getTimeNature() == DynamicalSystem.DISCRETE && holder.length > 2));
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private boolean isOverflow(final double x, final double y, final double z) {
		if (x >= w || x < 0)
			return true;
		if (y >= h || y < 0)
			return true;
		if (z >= d || z < 0)
			return true;

		return false;
	}

	private boolean notIdentityGamma() {
		return !(gamma == 1 && scale == 1 && offset == 0);
	}

	private boolean varPosIsNull() {
		return (varpos == null) ? true : false;
	}
}
