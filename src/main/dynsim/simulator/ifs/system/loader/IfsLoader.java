package dynsim.simulator.ifs.system.loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class IfsLoader {
	private HashMap<String, IfsDatum> smap;

	private DocumentBuilderFactory dbf;

	public IfsLoader() {
		super();
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		smap = new HashMap<String, IfsDatum>();
	}

	public void loadFromFile(String fname) {
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new IfsErrorHandler());
			InputSource is = new InputSource(fname);
			doc = db.parse(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		;

		IfsDOMLoader itl = new IfsDOMLoader();
		itl.load(doc, this);
	}

	protected void saveMap(String key, IfsDatum value) {
		smap.put(key, value);
	}

	public Map getIfsMap() {
		return smap;
	}

	public IfsDatum getIfsById(String id) {
		if (smap.containsKey(id))
			return smap.get(id);

		return null;
	}
}
