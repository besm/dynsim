package dynsim.simulator.system.interp;

public interface InterpretedSystem {
	public abstract ParserWrapper getWrapper();

	public abstract void setWrapper(ParserWrapper wrapper);
}
