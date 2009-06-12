package dynsim.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSlotList {
	private final List<DataSlot> slots;

	private DataSlot current;

	public DataSlotList() {
		slots = new ArrayList<DataSlot>();
	}

	public void addStorage(final Storage data) {
		DataSlot slot = new DataSlot(data);
		addDataSlot(slot);
	}

	public void addDataSlot(final DataSlot slot) {
		slots.add(slot);
		if (!hasCurrentSlot()) {
			setCurrentSlot(slot);
		}
	}

	public void removeDataSlot(final DataSlot slot) {
		slots.remove(slot);
	}

	public Iterator<DataSlot> dataSlotIterator() {
		return slots.iterator();
	}

	public boolean hasCurrentSlot() {
		return current != null;
	}

	public boolean hasDataSlots() {
		return slots.size() > 1;
	}

	public void setCurrentSlot(final DataSlot slot) {
		current = slot;
	}

	public boolean hasColorings() {
		return current.hasColoring();
	}

	public Storage getCurrentData() {
		return current.getData();
	}

	public int getCurrentDataRowsNum() {
		return current.getRowsNum();
	}

	public int[] getCurrentColoring() {
		return current.getColoring();
	}
}
