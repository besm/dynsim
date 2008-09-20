package dynsim.graphics.plot.j2d.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.graphics.AppFrame;
import dynsim.graphics.DrawingSurface;

public class SurfaceProbe extends AppFrame {
	private static final long serialVersionUID = 8646311124969347700L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SurfaceProbe p = new SurfaceProbe();
		p.setLayout(new BorderLayout());
		DrawingSurface ds = new DrawingSurface(p.getWidth(), p.getHeight());
		ds.setBackground(Color.GRAY);

		ds.drawPoint(200.60f, 255.5f);
		ds.drawLine(0.5f, 0.5f, 200.5f, 200.5f);
		ds.fillCircle(200, 200, 4, Color.DARK_GRAY);
		p.add(ds, BorderLayout.CENTER);
		p.pack();
		p.setVisible(true);
	}

}
