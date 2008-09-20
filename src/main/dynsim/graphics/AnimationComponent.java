package dynsim.graphics;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

//TODO clean!
public abstract class AnimationComponent extends Container implements Runnable, TimingTarget {
	private static final float dt = 0.05125f;

	private static final long serialVersionUID = 5142579557769603972L;

	protected static final GraphicsDevice DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice();

	protected static final GraphicsConfiguration CONFIG = DEVICE.getDefaultConfiguration();

	private boolean mRunning;

	protected BufferedImage offImage;

	private long[] mPreviousTimes;

	private double mFrameRate;

	private Animator mTiming;

	private int fcount;

	private long last;

	protected BufferStrategy bufferStrategy;

	public AnimationComponent() {
		setIgnoreRepaint(true);
		mRunning = true;
		mPreviousTimes = new long[128];
		mPreviousTimes[0] = System.currentTimeMillis();
		mTiming = new Animator(Animator.INFINITE);
		mTiming.setResolution(5);
		mTiming.addTarget(this);
		mRateListener = this;
	}

	public abstract void timeStep(float deltatime);

	public void start() {
		// init();
		mTiming.start();
	}

	public void stop() {
		mTiming.stop();
	}

	public void run() {
		while (mRunning) {
			mainStep();
		}
	}

	// protected abstract void init();

	private void mainStep() {
		render();
		timeStep(dt);
		calcFrameRate();
	}

	private void render() {
		if (hasBufferStrategy()) {
			Graphics g = bufferStrategy.getDrawGraphics();

			if (g != null) {
				drawBackBuffer(g);
				if (!bufferStrategy.contentsLost())
					bufferStrategy.show();
				else
					System.out.println("Contents Lost");
				// Sync the display on some systems.
				// (on Linux, this fixes event queue problems)
				Toolkit.getDefaultToolkit().sync();
			}
		} else {
			Graphics g = getGraphics();

			if (g != null) {
				drawBackBuffer(g);
				g.dispose();
			}
		}
	}

	private void drawBackBuffer(Graphics g) {
		Dimension d = getSize();
		if (checkImage(d)) {
			Graphics imageGraphics = offImage.getGraphics();
			paint(imageGraphics);
			g.drawImage(offImage, 0, 0, this);
			imageGraphics.dispose();
		}
	}

	protected boolean hasBufferStrategy() {
		return bufferStrategy != null;
	}

	private boolean checkImage(Dimension d) {
		if (d.width == 0 || d.height == 0)
			return false;

		if (offImage == null || offImage.getWidth(null) != d.width || offImage.getHeight(null) != d.height) {
			// mImage = new BufferedImage(d.width, d.height,
			// BufferedImage.TYPE_INT_RGB);
			offImage = createCompatibleImage(d.width, d.height);
			// mImage = createImage(d.width, d.height);
		}

		return true;
	}

	protected BufferedImage createCompatibleImage(int w, int h) {
		return CONFIG.createCompatibleImage(w, h, Transparency.OPAQUE);
	}

	public void calcFrameRate() {
		long now = System.currentTimeMillis();
		// int numFrames = mPreviousTimes.length;
		double newRate;
		float f = 0.05F;
		long delta = now - last;
		fcount++;
		if (delta >= 1000) {
			float f1 = (0.0035F * (float) (delta)) / (float) fcount;
			if (f1 - f > 0.03F)
				f1 = f + 0.03F;
			f = f1;
			newRate = (1000D * (double) fcount) / (double) (delta);
			if (f > 0.2F)
				f = 0.2F;
			fcount = 0;

			firePropertyChange("frameRate", mFrameRate, newRate);

			mFrameRate = newRate;
			last = now;
		}

		// if (mPrevFilled) {
		// long delta = now - mPreviousTimes[mPrevIndex];
		// // newRate = (double) numFrames / ((double) delta) * 1000.0;
		// newRate = (double) numFrames / ((double) delta / 1000.0);
		// } else {
		// long delta = now - mPreviousTimes[mPrevIndex];//numFrames - 1];
		// // newRate = 1000.0 / ((double) delta);
		// newRate = (double) numFrames / ((double) delta / 1000.0);
		// }

		// time is in millisec, so multiply by 1000 to get frames/sec
		// long deltatime = now - mPreviousTimes[mPrevIndex];
		// newRate = (double) numFrames / ((double) deltatime / 1000.0);

		// log
		// mPreviousTimes[mPrevIndex] = now;
		// mPrevIndex++;
		//
		// if (mPrevIndex >= numFrames) {
		// mPrevIndex = 0;
		// mPrevFilled = true;
		// }
	}

	public void begin() {
		// TODO Auto-generated method stub
	}

	public void end() {
		// TODO Auto-generated method stub
	}

	public void repeat() {
		// TODO Auto-generated method stub
	}

	public void timingEvent(float time) {
		mainStep();
	}

	public double getFrameRate() {
		return mFrameRate;
	}

	private transient AnimationComponent mRateListener;

	public void firePropertyChange(String s, double oldValue, double newValue) {
		mRateListener.rateChanged(newValue);
	}

	protected void rateChanged(double newValue) {

	}

	public void setBufferStrategy(BufferStrategy bufferStrategy) {
		this.bufferStrategy = bufferStrategy;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
}
