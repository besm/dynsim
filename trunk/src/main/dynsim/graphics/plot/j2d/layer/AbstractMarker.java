package dynsim.graphics.plot.j2d.layer;

import java.awt.Color;

import dynsim.graphics.ScreenMap;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.Axis;

public abstract class AbstractMarker implements Marker {
	private double[] coords;

	private String[] coordNames;

	private int type;

	private Color color;

	private String description;

	public AbstractMarker(double[] coords) {
		this.coords = coords;
		this.coordNames = new String[] { "x", "y", "z" };
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String[] getCoordNames() {
		return coordNames;
	}

	public void setCoordNames(String[] coordNames) {
		this.coordNames = coordNames;
	}

	public double[] getCoords() {
		return coords;
	}

	public void setCoords(double[] coords) {
		this.coords = coords;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getCoord(String key) {
		return coords[getPos(key)];
	}

	private int getPos(String key) {
		for (int i = 0; i < coordNames.length; i++) {
			if (coordNames[i].equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public void draw(Grapher2D g) {
		GrapherConfig config = g.getGrapherConfig();
		Axis xAxis = g.getAxisX();
		Axis yAxis = g.getAxisY();

		float x = (float) getCoord(config.getPlotVarX());
		float y = (float) getCoord(config.getPlotVarY());

		if (!g.notInAxisRange(x, y)) {

			x = ScreenMap.toScreenFloat(x, g.getWidth() - g.getMarginW(), xAxis.getStart(), xAxis.getEnd());

			// Nota: max. Y es 0 en coordenadas de pantalla => necesario
			// invertirlo
			y = ScreenMap.toScreenFloat(y, g.getHeight() - g.getMarginH(), yAxis.getEnd(), yAxis.getStart());

			x += g.getHalfMW();
			y += g.getHalfMH();

			draw(x, y, g); // subclass draw callback
		}

	}

	protected abstract void draw(float x, float y, Grapher2D g);

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

}
