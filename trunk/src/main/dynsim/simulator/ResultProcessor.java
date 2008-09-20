/**
 * 
 */
package dynsim.simulator;

import dynsim.exceptions.DynSimException;

/**
 * @author maf83
 * 
 */
public interface ResultProcessor {

	/**
	 * @param holder
	 * @throws DynSimException
	 */
	void procResults(double[] holder) throws DynSimException;

	void setSimulator(Simulator simulator);

	void onSimulatorInit();

}
