/**
 * 
 */
package dynsim.graphics.render;

import java.awt.Color;
import java.awt.image.BufferedImage;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.bones3D.Camera;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.ResultProcessor;

/**
 * @author maf83
 * 
 */
public interface Renderer extends ResultProcessor {

	public abstract void rasterize();

	public abstract void flush() throws DynSimException;

	public BufferedImage getImage();

	public void setConfig(RenderConfig conf);

	public RenderConfig getConfig();

	public abstract void setAutoAxisRanges() throws DynSimException;

	public abstract void setAxisRanges(FloatRange ax, FloatRange ay, FloatRange az);

	public abstract void setAxisRanges(FloatRange ax, FloatRange ay);

	public abstract void setAllAxisRanges(FloatRange ra);

	public abstract void setDimensions(int w, int h, int d);

	public abstract void setEye(double x, double y, double z);

	public abstract void setRotation(double yaw, double pitch, double roll);

	public abstract Camera getCamera();

	public abstract void setCamera(Camera cam);

	public abstract void setVarpos(int[] is);

	public abstract void setDetail(float detail);

	public abstract float getDetail();

	public abstract void setScale(float scale);

	public abstract float getScale();

	public abstract void setOffset(float offset);

	public abstract float getOffset();

	public abstract void setGamma(float gamma);

	public abstract float getGamma();

	public abstract void setBackgroundColor(Color bg);

	public abstract Color getBackgroundColor();
}