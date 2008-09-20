package dynsim.graphics.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import dynsim.data.DataSlot;
import dynsim.data.DataSlotList;
import dynsim.data.Storage;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.simulator.color.ColoringStrategy;

public interface Grapher {

	public abstract void addData(Storage data);

	public abstract void addData(Storage data, ColoringStrategy colors);

	public abstract void addSlot(DataSlot slot);

	public abstract Storage getCurrentData();

	public abstract DataSlotList getDataSlots();

	public abstract GrapherConfig getGrapherConfig();

	public abstract Graphics2D getOffscrGraphics();

	public abstract Iterator<DataSlot> getSlotsIterator();

	public abstract Color getStrokeColor();

	public abstract boolean hasColorings();

	public abstract boolean hasDataSlots();

}