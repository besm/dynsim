package dynsim.simulator.ifs.system.loader;

import java.util.StringTokenizer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class IfsDOMLoader {
	private static final String TABPLUS = "\t+";

	private static final String WSPLUS = "  +";

	private static final String CRPLUS = "\n+";

	private static final String CR = "\n";

	private static final String ID = "id";

	private static final String ROWS = "rows";

	private static final String WS = " ";

	private static final String IFS = "ifs";

	private static final String SYS = "sys";

	private IfsLoader il;

	private String cId;

	protected double[] prob;

	protected double[][] coeff;

	public IfsDOMLoader() {
	}

	public void load(Document doc, IfsLoader il) {
		this.il = il;
		loop((Node) doc);
	}

	private void loop(Node node) {
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			procElement((Element) node);
			break;
		default:
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++)
				loop(list.item(i));
		}
	}

	private void procElement(Element element) {
		String tag = element.getTagName();
		if (tag.equalsIgnoreCase(SYS)) {
			NamedNodeMap map = element.getAttributes();
			for (int i = 0; i < map.getLength(); i++)
				procAttr((Attr) map.item(i));

			Text txt = (Text) element.getFirstChild();
			loadMatrix(txt.getData());
			il.saveMap(cId, new IfsDatum(coeff, prob));
		} else if (tag.equalsIgnoreCase(IFS)) {
			NodeList list = element.getChildNodes();
			for (int i = 0; i < list.getLength(); i++)
				loop(list.item(i));
		}
	}

	private void procAttr(Attr attr) {
		if (attr.getName().equalsIgnoreCase(ROWS)) {
			int s = Integer.parseInt(attr.getValue());
			coeff = new double[s][6];
			prob = new double[s + 1];
		}

		if (attr.getName().equalsIgnoreCase(ID)) {
			cId = attr.getValue();
		}
	}

	private void loadMatrix(String data) {
		StringTokenizer st = new StringTokenizer(data);
		int rowCount = 0;
		while (st.hasMoreTokens()) {
			String tok = st.nextToken(CR);
			tok = sanitize(tok);

			if (tok == null || tok.equals(""))
				continue;

			String[] values = tok.split(WS);

			for (int colCount = 0; colCount < values.length; colCount++) {
				try {
					String s = values[colCount];

					if (colCount < 6) {
						coeff[rowCount][colCount] = Double.parseDouble(s);
					} else {
						prob[rowCount + 1] = prob[rowCount] + Double.parseDouble(s);
					}
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			} // end for
			rowCount++;
		} // end while
	}

	private String sanitize(String str) {
		str = str.replaceAll(TABPLUS, WS);
		str = str.replaceAll(WSPLUS, WS);
		str = str.replaceAll(CRPLUS, CR);
		return str.trim();
	}

}
