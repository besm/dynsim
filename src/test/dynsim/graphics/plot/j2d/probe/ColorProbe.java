/**
 * 
 */
package dynsim.graphics.plot.j2d.probe;

import java.awt.Color;
import java.awt.Graphics;

import dynsim.graphics.color.scheme.impl.Rainbow;
import dynsim.graphics.color.scheme.impl.Warm;
import dynsim.ui.AppFrame;

/**
 * @author maf83
 * 
 */
public class ColorProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private int[][] pal1, pal2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ColorProbe p = new ColorProbe();
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		pal1 = new Warm().loadPals();
		pal2 = new Rainbow().loadPals();
		setVisible(true);
	}

	public void paint(Graphics g) {
		for (int n = 0; n < pal1[0].length; n++) {
			g.setColor(new Color(pal1[0][n]));
			g.drawLine(0, n, getWidth() / 2, n);
		}
		for (int n = 0; n < pal2[0].length; n++) {
			g.setColor(new Color(pal2[0][n]));
			g.drawLine(getWidth() / 2, n, getWidth(), n);
		}
	}

}
