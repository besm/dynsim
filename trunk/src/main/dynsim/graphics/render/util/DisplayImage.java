package dynsim.graphics.render.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DisplayImage extends Component {

	/**
         * 
         */
	private static final long serialVersionUID = -8581116647663916616L;

	private BufferedImage img;

	private int pw;

	private int ph;

	public DisplayImage(int width, int height) {
		pw = width;
		ph = height;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImage(BufferedImage img) {
		this.img = img;
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (img != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(img, null, 0, 0);
		}
	}

	/**
	 * The preferred size.
	 */
	public Dimension getPreferredSize() {
		return new Dimension(pw, ph);
	}

	/**
	 * The minimum size.
	 */
	public Dimension getMinimumSize() {
		return new Dimension(100, 100);
	}
}
