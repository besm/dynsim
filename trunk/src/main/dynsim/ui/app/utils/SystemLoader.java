package dynsim.ui.app.utils;

import java.util.HashSet;
import java.util.Set;

import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.system.interp.InterpretedSystem;

public class SystemLoader {

	@SuppressWarnings("unchecked")
	private static HashSet<Class> included;
	@SuppressWarnings("unchecked")
	private static HashSet<Class> excluded;
	private static HashSet<String> packageFilter;
	private static HashSet<String> jarFilter;

	@SuppressWarnings("unchecked")
	public static Set<Class> getMapSystemClasses() {
		initCriteria("dynsim.simulator.iteratedmap.system.impl", IteratedMap.class);
		return retrieve();
	}
	
	@SuppressWarnings("unchecked")
	public static Set<Class> getOdeSystemClasses() {
		initCriteria("dynsim.simulator.ode.system.impl", OdeSystem.class);
		return retrieve();
	}

	@SuppressWarnings("unchecked")
	private static void initCriteria(String packageName, Class systemClass) {
		included = new HashSet<Class>();
		included.add(systemClass);
		excluded = new HashSet<Class>();
		excluded.add(InterpretedSystem.class);
		packageFilter = new HashSet<String>();
		packageFilter.add(packageName);
		jarFilter = new HashSet<String>();
	}

	@SuppressWarnings("unchecked")
	private static Set<Class> retrieve() {
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
