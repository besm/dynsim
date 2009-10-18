package dynsim.simulator.system;

import java.util.Collection;

import Jama.Matrix;
import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.Parameters;

public interface DynamicalSystem {

	public static int DISCRETE = 0;

	public static int CONTINOUS = 1;

	public static int MAP_T = 0;
	public static int ODE_T = 1;
	public static int IFS_T = 2;

	public static int UNKNOWN_T = -1;

	public void addFunction(RealFunction f);

	public double[] eval(double[] state);

	public Collection<RealFunction> getFunctions();

	public double[] getGradient(double[] point);

	public String[] getIndependentVarNames();

	public int getIndependentVarsNum();

	public int getIndepVarsNumNoTime();

	public Parameters getInitialConditions();

	public double[] getInitialValues();

	public Matrix getJacobianMatrix(double[] point);

	public double getParameter(String name);

	public Parameters getParameters();

	public int getTimeNature();

	public int getType();

	public int getVarPosition(String pname);

	public void resetInitialConditions();

	public void resetParameters();

	public void setInitialCondition(String label, double value);

	public void setInitialConditions(double[] conds);

	public void setParameter(String label, double value);

	public void setParameters(Parameters params);

	public void setType(int type);
}
