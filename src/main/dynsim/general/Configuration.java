package dynsim.general;

import java.util.Observable;

public abstract class Configuration extends Observable {
	protected int conf;

	public void setConfig(int mask) {
		conf |= mask;

		setChanged();
	}

	public void unsetConfig(int mask) {
		conf &= ~mask;

		setChanged();
	}

	public void toggleConfig(int mask) {
		conf ^= mask;

		setChanged();
	}

	public boolean isEnabled(int property) {
		return ((conf & property) == property);
	}

	public boolean isDisabled(int property) {
		return ((conf & property) != property);
	}

}
