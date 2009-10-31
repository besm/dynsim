package dynsim.graphics.plot.j2d.layer;

import java.awt.Color;

import dynsim.graphics.plot.j2d.Grapher2D;

public interface Marker {
	public Color getColor();

	public void setColor(Color color);

	public String[] getCoordNames();

	public void setCoordNames(String[] coordNames);

	public double[] getCoords();

	public void setCoords(double[] coords);

	public int getType();

	public void setType(int type);

	public double getCoord(String key);

	public void draw(Grapher2D g);
	
	public String getDescription();
	
	public void setDescription(String description);
}
