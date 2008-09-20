package dynsim.graphics.plot.config;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;

import dynsim.general.Configuration;

public abstract class GrapherConfig extends Configuration {
	private static final String PVARX = "pvx";

	private static final String PVARY = "pvy";

	private static final String PVARZ = "pvz";

	private static final long serialVersionUID = -2380992430697758823L;

	private Map<String, String> map;

	private float lineWeight;

	private float borderWeight;

	private float pointWeight;

	private int marginW;

	private int marginH;

	private float marginWPct;

	private float marginHPct;

	public static final int POINTS = 0;

	public static final int LINES = 1;

	public static final int CURVES = 2;

	public static final int DRAW_OFF = 0;

	private int style;

	private boolean autoAdjust;

	// Default colors
	protected Color[] colors;

	private int ticklen;

	private int curveCtrlPointsNum;

	private int splineParts;

	public GrapherConfig() {
		this(true);
	}

	public GrapherConfig(boolean init) {
		map = new Hashtable<String, String>();
		setPlotVarX("x");
		setPlotVarY("y");
		setPlotVarZ("z");
		setPlotStyle(POINTS);
		setColors(new Color[] { Color.RED, Color.GREEN, Color.ORANGE, Color.CYAN, Color.YELLOW, Color.BLUE });
		setConfig(DRAW_OFF);

		if (init) {
			initDefaultConfig();
		}
	}

	public void clear() {
		map.clear();
	}

	public String getPlotVarX() {
		return map.get(PVARX);
	}

	public void setPlotVarX(String varname) {
		map.put(PVARX, varname);
	}

	public String getPlotVarY() {
		return map.get(PVARY);
	}

	public void setPlotVarY(String varname) {
		map.put(PVARY, varname);
	}

	public String getPlotVarZ() {
		return map.get(PVARZ);
	}

	public void setPlotVarZ(String varname) {
		map.put(PVARZ, varname);
	}

	public int getPlotStyle() {
		return style;
	}

	public void setPlotStyle(int style) {
		this.style = style;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public int size() {
		return map.size();
	}

	public float getBorderWeight() {
		return borderWeight;
	}

	public void setBorderWeight(float borderWeight) {
		this.borderWeight = borderWeight;
	}

	public float getLineWeight() {
		return lineWeight;
	}

	public void setLineWeight(float lineWeight) {
		this.lineWeight = lineWeight;
	}

	public float getPointWeight() {
		return pointWeight;
	}

	public void setPointWeight(float pointWeight) {
		this.pointWeight = pointWeight;
	}

	public int getMarginH() {
		return marginH;
	}

	public void setMarginH(int marginH) {
		this.marginH = marginH;
	}

	public float getMarginHPct() {
		return marginHPct;
	}

	public void setMarginHPct(float marginHPct) {
		this.marginHPct = marginHPct;
	}

	public int getMarginW() {
		return marginW;
	}

	public void setMarginW(int marginW) {
		this.marginW = marginW;
	}

	public float getMarginWPct() {
		return marginWPct;
	}

	public void setMarginWPct(float marginWPct) {
		this.marginWPct = marginWPct;
	}

	public void setDrawConfig(int mask) {
		super.setConfig(mask);

		notifyObservers(super.conf);
	}

	public void unsetDrawConfig(int mask) {
		super.unsetConfig(mask);

		notifyObservers(super.conf);
	}

	public void toggleDrawConfig(int mask) {
		super.toggleConfig(mask);

		notifyObservers(super.conf);
	}

	public boolean isEnabled(int property) {
		return super.isEnabled(property);
	}

	public boolean isDisabled(int property) {
		return super.isDisabled(property);
	}

	public void setAutoAdjust(boolean autoAdjust) {
		this.autoAdjust = autoAdjust;
	}

	public boolean isAutoAdjust() {
		return autoAdjust;
	}

	public void setMainColor(Color color) {
		colors[0] = color;
	}

	public Color getMainColor() {
		return colors[0];
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	public Color[] getColors() {
		return colors;
	}

	public Color getColor(int i) {
		return colors[i];
	}

	public void setColor(Color color, int i) {
		colors[i] = color;
	}

	public int getColorsNum() {
		return colors.length;
	}

	public void setTickLen(int l) {
		this.ticklen = l;
	}

	public int getTickLen() {
		return ticklen;
	}

	public int getCurveControlPointsNum() {
		return curveCtrlPointsNum;
	}

	public void setCurveControlPointsNum(int n) {
		this.curveCtrlPointsNum = n;
	}

	public void setSplineParts(int parts) {
		this.splineParts = parts;
	}

	public int getSplineParts() {
		return splineParts;
	}

	public void initDefaultConfig() {
		setAutoAdjust(true);
		setLineWeight(0.1f);
		setPointWeight(0.1f);
		setBorderWeight(1f);
		setMarginWPct(0.1f);
		setMarginHPct(0.1f);
		setPlotVarX("x");
		setPlotVarY("y");
		setTickLen(4); // px
		setCurveControlPointsNum(200);
		setSplineParts(25);
	}
	//
	// public void notifyChange() {
	// if (hasListeners()) {
	// Iterator<GrapherConfigListener> it = listeners.iterator();
	// while (it.hasNext()) {
	// GrapherConfigListener listener = it.next();
	// listener.onConfigurationChange(this);
	// }
	// }
	// }
}
