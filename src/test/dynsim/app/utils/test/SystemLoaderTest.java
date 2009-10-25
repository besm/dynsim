package dynsim.app.utils.test;

import java.util.Set;

import junit.framework.TestCase;
import dynsim.ui.app.utils.SystemLoader;

public class SystemLoaderTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testLoadClasses() {
		Set<Class> classes = SystemLoader.getOdeSystemClasses();
		assertFalse(classes.isEmpty());
	}

}
