package dynsim.general;

import java.util.Observable;

public abstract class Configuration extends Observable {
	protected int conf;

	public void setConfig(final int mask) {
		conf |= mask;

		setChanged();
	}

	public void unsetConfig(final int mask) {
		conf &= ~mask;

		setChanged();
	}

	public void toggleConfig(final int mask) {
		conf ^= mask;

		setChanged();
	}

	public boolean isEnabled(final int property) {
		return ((conf & property) == property);
	}

	public boolean isDisabled(final int property) {
		return ((conf & property) != property);
	}

}
