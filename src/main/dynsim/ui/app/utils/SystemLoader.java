package dynsim.ui.app.utils;

import java.util.HashSet;
import java.util.Set;

import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.system.interp.InterpretedSystem;

public class SystemLoader {

	private static HashSet<Class> included;
	private static HashSet<Class> excluded;
	private static HashSet<String> packageFilter;
	private static HashSet<String> jarFilter;

	static {
		included = new HashSet<Class>();
		included.add(OdeSystem.class);
		excluded = new HashSet<Class>();
		excluded.add(InterpretedSystem.class);
		packageFilter = new HashSet<String>();
		packageFilter.add("dynsim.simulator.ode.system.impl");
		jarFilter = new HashSet<String>();
	}

	@SuppressWarnings("unchecked")
	public static Set<Class> getOdeSystemClasses() {

		final Set<Class> classes = new HashSet<Class>();

		try {
			classes.addAll(ClassList.findClasses(Thread.currentThread().getContextClassLoader(), included, excluded,
					packageFilter, jarFilter));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return classes;
	}
}
