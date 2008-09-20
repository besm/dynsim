package dynsim.data;

import java.awt.Color;

//TODO autovalue para funciones 1D que lo haga
//el propio Storage...
public class DataSlot {
	private String label;

	// Color palette
	private int[] coloring;

	// Color plano
	private Color color;

	private Storage data;

	public DataSlot(Storage data) {
		this.data = data;
	}

	public int[] getColoring() {
		return coloring;
	}

	public void setColoring(int[] coloring) {
		this.coloring = coloring;
	}

	public Storage getData() {
		return data;
	}

	public void setData(Storage data) {
		this.data = data;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean hasData() {
		return data != null;
	}

	public int getRowsNum() {
		return data.getRowsNum();
	}

	public int getColumnsNum() {
		return data.getColumnsNum();
	}

	public boolean hasColoring() {
		return coloring != null;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean hasColor() {
		return color != null;
	}

}
