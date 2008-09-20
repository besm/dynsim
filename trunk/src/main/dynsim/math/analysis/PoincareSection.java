package dynsim.math.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import dynsim.data.Storage;
import dynsim.data.impl.BetterMemStorage;
import dynsim.math.vector.Vector3D;
import dynsim.math.vector.VectorOps;

public class PoincareSection {

	static public Storage getPoincareSection(Storage data) {
		int nrows = data.getRowsNum();
		String[] names = { "x", "y", "z" };
		double[][] map = new double[names.length][nrows];
		Storage section = new BetterMemStorage(map, names);

		// Plane normal
		Vector3D Nv = new Vector3D(0, 1, -1);
		// Point in plane
		Vector3D V0 = new Vector3D(-2, 0, 0);

		int j = 0;

		for (int i = 0; i < data.getRowsNum(); i += 2) {

			Vector3D P0 = new Vector3D(data.get("x", i), data.get("y", i), data.get("z", i));
			Vector3D P1 = new Vector3D(data.get("x", i + 1), data.get("y", i + 1), data.get("z", i + 1));

			Vector3D u = new Vector3D(VectorOps.sub(P1, P0));
			Vector3D w = new Vector3D(VectorOps.sub(P0, V0));

			double D = VectorOps.dot(Nv, u);
			double N = -VectorOps.dot(Nv, w);

			// segment is parallel to plane
			if (Math.abs(D) < 0.000000001) {
				// segment lies in plane
				if (N == 0) {
					continue;
				} else {
					// no intersection
					continue;
				}
			}

			// they are not parallel
			// compute intersect param
			double sI = N / D;
			if (sI < 0 || sI > 1)
				continue; // no intersection

			// compute intersection point
			Vector3D I = new Vector3D(P0.add(u.multiply(sI)));

			j = assignPoint(names, section, j, I);
		}

		double[][] tmp = section.getRawData();

		if (j < 1)
			j = 1;

		double[][] ret = new double[tmp.length][j - 1];

		for (int i = 0; i < ret.length; i++) {
			System.arraycopy(tmp[i], 0, ret[i], 0, ret[i].length);
		}

		return new BetterMemStorage(ret, section.getColumnNames());
	}

	private static int assignPoint(String[] names, Storage section, int j, Vector3D I) {
		for (int n = 0; n < names.length; n++) {
			section.put(n, j, I.get(n));
		}

		j++;
		return j;
	}

	// TODO necesita streaming con resultados
	// el parámetro será la condición de corte
	// el nombre de la v de corte, el nombre de las que devuelve
	static public Storage getSect(Storage data, String varname) {
		int nrows = data.getRowsNum();
		String[] names = { "x", "z" };
		Collection<Double> list = new ArrayList<Double>();

		int j = 0;
		boolean localMax = true;

		for (int i = 0; i < nrows; i++) {
			double y = data.get("y", i);
			double x = data.get("x", i);
			double z = data.get("z", i);

			// if (Math.sin(t) < 0.01 && Math.sin(t) > -0.01
			// && Math.cos(t) > 0.999 && Math.cos(t) < 1.01) {
			if (y < 0.1 && y > -0.1 && x < 0 && z < 1) {
				if (localMax) {

					j++;
				} else {
					list.add(data.get(names[0], i));
					list.add(data.get(names[1], i));
					j++;
				}
			}
		}

		double[][] rmap = new double[1][j];
		Storage retmap = new BetterMemStorage(rmap, names);

		int i = 0;
		for (Iterator<Double> itor = list.iterator(); itor.hasNext();) {
			double x1 = itor.next();
			// double x2 = itor.next();

			retmap.put(0, i, x1);
			// retmap.put(1, i, x2);
			i++;
		}

		return retmap;
	}

	static public Storage getReturnMap(Storage stor, String varname) {
		int nrows = stor.getRowsNum();
		int hrows = nrows / 2;
		String[] names = { "x", "y" };
		double[][] rmap = new double[2][hrows];
		Storage retmap = new BetterMemStorage(rmap, names);

		for (int i = 0, j = 0; i < nrows; i += 2) {

			double z1 = stor.get(varname, i);
			if (i + 1 >= nrows)
				continue;
			double z2 = stor.get(varname, i + 1);

			retmap.put(0, j, z1);
			retmap.put(1, j, z2);
			j++;
		}

		return retmap;
	}

}
