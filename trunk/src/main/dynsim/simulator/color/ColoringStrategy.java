package dynsim.simulator.color;

import dynsim.data.Storage;

public interface ColoringStrategy {
	public int[] getColors(Storage data);
}
