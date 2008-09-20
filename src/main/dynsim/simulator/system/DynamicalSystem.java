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

	public int getIndependentVarsNum();

	public int getIndepVarsNumNoTime();

	public double[] getInitialValues();

	public Parameters getInitialConditions();

	public Parameters getParameters();

	public void setParameters(Parameters params);

	public void setInitialCondition(String label, double value);

	public void setParameter(String label, double value);

	public double getParameter(String name);

	public String[] getIndependentVarNames();

	public int getTimeNature();

	public double[] eval(double[] state);

	public void addFunction(RealFunction f);

	public Collection<RealFunction> getFunctions();

	public Matrix getJacobianMatrix(double[] point);

	public double[] getGradient(double[] point);

	public int getVarPosition(String pname);

	public void setInitialConditions(double[] conds);

	public int getType();

	public void setType(int type);
}
