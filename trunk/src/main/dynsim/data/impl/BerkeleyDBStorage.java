package dynsim.data.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import dynsim.data.AbstractStorage;

public class BerkeleyDBStorage extends AbstractStorage {
	private static final String DB_NAME = "db1";

	private Environment env = null;

	private Database db = null;

	private Logger log = Logger.getLogger(BerkeleyDBStorage.class.getName());

	public BerkeleyDBStorage(String[] names) {
		this(DB_NAME, names);
	}

	// Tener en cuenta si se llama dos veces para la misma
	// db .--- tema de init!
	public BerkeleyDBStorage(String dbName, String[] names) {
		super(names);
		EnvironmentConfig econf = new EnvironmentConfig();
		econf.setAllowCreate(true);
		econf.setLocking(false);
		// TODO rowsnum
		try {
			env = new Environment(new File("data/test/dbEnv"), econf);
			DatabaseConfig dbConfig = new DatabaseConfig();
			// dbConfig.setSortedDuplicates(true);
			dbConfig.setAllowCreate(true);

			db = env.openDatabase(null, dbName, dbConfig);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public void add(double[] holder) {
		try {
			String key;
			DatabaseEntry k;

			for (int i = 0; i < holder.length; i++) {
				key = "K" + i + currentRowIndex;
				k = new DatabaseEntry(key.getBytes("UTF-8"));
				DatabaseEntry v = new DatabaseEntry();

				EntryBinding dbind = TupleBinding.getPrimitiveBinding(Double.class);
				dbind.objectToEntry(new Double(holder[i]), v);

				db.put(null, k, v);
			}
			currentRowIndex++;
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public double[] getAll(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public double get(int c, int r) {
		try {
			String key = "K" + c + r;
			DatabaseEntry k = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry v = new DatabaseEntry();

			if ((db.get(null, k, v, LockMode.DEFAULT) == OperationStatus.SUCCESS)) {
				byte[] retData = v.getData();
				double foundData = Double.longBitsToDouble(new BigInteger(retData).longValue());

				return foundData;
			} else {
				log.info("No record found for key '" + key + "'.");
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return 0;
	}

	public void remove() {
		try {
			String name = db.getDatabaseName();
			db.close();
			env.removeDatabase(null, name);
			env.close();
		} catch (DatabaseException e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public void put(int c, int r, double value) {
		String key = "K" + c + r;
		DatabaseEntry k;
		try {
			k = new DatabaseEntry(key.getBytes("UTF-8"));

			DatabaseEntry v = new DatabaseEntry();

			EntryBinding dbind = TupleBinding.getPrimitiveBinding(Double.class);
			dbind.objectToEntry(new Double(value), v);

			db.put(null, k, v);
		} catch (UnsupportedEncodingException e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (DatabaseException e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
