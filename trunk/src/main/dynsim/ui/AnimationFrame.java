package dynsim.ui;

import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.ImageCapabilities;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import dynsim.graphics.animation.AnimationComponent;

//TODO refactor!
public class AnimationFrame extends AppFrame {
	private static final int FSEM_HEIGHT = 480;

	private static final int FSEM_WIDTH = 640;

	private static final long serialVersionUID = -1115629898533428423L;

	private GraphicsDevice gd;

	private BufferStrategy bufferStrategy;

	private boolean fsem;

	public AnimationFrame(String title, AnimationComponent ac, boolean fsem) {
		super(title);
		ArrayList<AnimationComponent> acl = new ArrayList<AnimationComponent>();
		acl.add(ac);
		init(acl, fsem);
	}

	public AnimationFrame(String title, ArrayList<AnimationComponent> acl, boolean fsem) {
		super(title);
		init(acl, fsem);
	}

	public AnimationFrame(String title, AnimationComponent ac, int w, int h, boolean fsem) {
		this(title, ac, fsem);
		setSize(w, h);
	}

	private void init(ArrayList<AnimationComponent> acl, boolean fsem) {
		this.fsem = fsem;

		// TODO callback para propiedades color..?�
		setBackground(Color.BLACK);

		initScreen();

		for (AnimationComponent ac : acl) {
			add(ac);

			if (fsem) {
				ac.setBufferStrategy(bufferStrategy);
			}

			// Kick off
			ac.start();
		}

		pack();
		center();
	}

	private void initScreen() {
		setLayout(new GridLayout());
		if (fsem) {
			initFullScreen(); // switch to FSEM
			this.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						System.out.println("closing...");
						restoreScreen();
						System.exit(0);
					}
				}

				public void keyTyped(KeyEvent e) {
				}
			});
		} else {
			setIgnoreRepaint(true);
			setResizable(false);
			setVisible(true);
		}
	}

	private void restoreScreen() {
		Window w = gd.getFullScreenWindow();
		if (w != null)
			w.dispose();
		gd.setFullScreenWindow(null);
	}

	private void initFullScreen() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		dispose();
		setUndecorated(true); // no menu bar, borders, etc.
		setIgnoreRepaint(true);
		// turn off paint events since doing active rendering
		setResizable(false);
		if (!gd.isFullScreenSupported()) {
			System.out.println("Full-screen exclusive mode not supported");
			System.exit(0);
		}
		gd.setFullScreenWindow(this); // switch on FSEM
		// we can now adjust the display modes, if we wish
		showCurrentMode(); // show the current display mode
		setDisplayMode(FSEM_WIDTH, FSEM_HEIGHT, 32); // or try 16 bits
		// setDisplayMode(1280, 1024, 32);
		reportCapabilities();
		setBufferStrategy();
	}

	private void setDisplayMode(int width, int height, int bitDepth) {
		if (!gd.isDisplayChangeSupported()) {
			System.out.println("Display mode changing not supported");
			return;
		}
		if (!isDisplayModeAvailable(width, height, bitDepth)) {
			System.out.println("Display mode (" + width + "," + height + "," + bitDepth + ") not available");
			return;
		}
		DisplayMode dm = new DisplayMode(width, height, bitDepth, DisplayMode.REFRESH_RATE_UNKNOWN); // any
		// refresh
		// rate
		try {
			gd.setDisplayMode(dm);
			System.out.println("Display mode set to: (" + width + "," + height + "," + bitDepth + ")");
		} catch (IllegalArgumentException e) {
			System.out.println("Error setting Display mode (" + width + "," + height + "," + bitDepth + ")");
		}
		try { // sleep to give time for the display to be changed
			Thread.sleep(1000); // 1 sec
		} catch (InterruptedException ex) {
		}
	} // end of setDisplayMode

	private boolean isDisplayModeAvailable(int width, int height, int bitDepth)
	/*
	 * Check that a displayMode with this width, height, and bit depth is
	 * available. We don't care about the refresh rate, which is probably
	 * REFRESH_RATE_UNKNOWN anyway.
	 */
	{
		DisplayMode[] modes = gd.getDisplayModes(); // modes list
		showModes(modes);
		for (int i = 0; i < modes.length; i++) {
			if (width == modes[i].getWidth() && height == modes[i].getHeight() && bitDepth == modes[i].getBitDepth())
				return true;
		}
		return false;
	}

	private void showModes(DisplayMode[] modes) {
		System.out.println("Modes");
		for (int i = 0; i < modes.length; i++) {
			System.out.print("(" + modes[i].getWidth() + "," + modes[i].getHeight() + "," + modes[i].getBitDepth()
					+ "," + modes[i].getRefreshRate() + ") ");
			if ((i + 1) % 4 == 0)
				System.out.println();
		}
		System.out.println();
	}

	private void showCurrentMode() {
		DisplayMode dm = gd.getDisplayMode();
		System.out.println("Current Display Mode: (" + dm.getWidth() + "," + dm.getHeight() + "," + dm.getBitDepth()
				+ "," + dm.getRefreshRate() + ") ");
	}

	private void setBufferStrategy() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				private static final int NUM_BUFFERS = 2;

				public void run() {
					createBufferStrategy(NUM_BUFFERS);
				}
			});
		} catch (Exception e) {
			System.out.println("Error while creating buffer strategy");
			System.exit(0);
		}
		try { // sleep to give time for buffer strategy to be done
			Thread.sleep(500); // 0.5 sec
		} catch (InterruptedException ex) {
		}
		bufferStrategy = getBufferStrategy(); // store for later
	}

	private void reportCapabilities() {
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		// image capabilities
		ImageCapabilities imageCaps = gc.getImageCapabilities();
		System.out.println("Image Caps. isAccelerated: " + imageCaps.isAccelerated());
		System.out.println("Image Caps. isTrueVolatile: " + imageCaps.isTrueVolatile());
		// buffer capabilities
		BufferCapabilities bufferCaps = gc.getBufferCapabilities();
		System.out.println("Buffer Caps. isPageFlipping: " + bufferCaps.isPageFlipping());
		System.out.println("Buffer Caps. Flip Contents: " + getFlipText(bufferCaps.getFlipContents()));
		System.out.println("Buffer Caps. Full-screen Required: " + bufferCaps.isFullScreenRequired());
		System.out.println("Buffer Caps. MultiBuffers: " + bufferCaps.isMultiBufferAvailable());
	} // end of reportCapabilities()

	private String getFlipText(BufferCapabilities.FlipContents flip) {
		if (flip == null)
			return "false";
		else if (flip == BufferCapabilities.FlipContents.UNDEFINED)
			return "Undefined";
		else if (flip == BufferCapabilities.FlipContents.BACKGROUND)
			return "Background";
		else if (flip == BufferCapabilities.FlipContents.PRIOR)
			return "Prior";
		else
			// if (flip == BufferCapabilities.FlipContents.COPIED)
			return "Copied";
	} // end of getFlipTest()
}
