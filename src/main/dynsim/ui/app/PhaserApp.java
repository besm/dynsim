package dynsim.ui.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dynsim.data.Storage;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.layer.field.VectField;
import dynsim.graphics.plot.j2d.layer.field.impl.OdeVectField;
import dynsim.math.analysis.local.CPoint;
import dynsim.math.analysis.local.LocalStabilityAnalyser;
import dynsim.simulator.color.ColoringStrategy;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.Rossler;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.ui.JNumericSpinner;
import dynsim.ui.ShowColorChooserAction;
import dynsim.ui.data.Pair;

public class PhaserApp extends BaseApp {
	private class ColoringStrategyListener implements ActionListener {

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox) e.getSource();
			Pair<ColoringStrategy> strategy = (Pair<ColoringStrategy>) cb.getSelectedItem();
			if (strategy.getName().equals("Basic")) {
				basicColoringPane.setVisible(true);
				multirampColoringPane.setVisible(false);
			} else if (strategy.getName().equals("Multiramp")) {
				basicColoringPane.setVisible(false);
				multirampColoringPane.setVisible(true);
			}
		}

	}

	private class ConfigurationListListener implements ListSelectionListener {

		@SuppressWarnings("unchecked")
		@Override
		public void valueChanged(ListSelectionEvent e) {
			final Pair<JComponent> selectedValue = (Pair<JComponent>) ((JList) e.getSource()).getSelectedValue();
			changeConfigOption(selectedValue.getValue());
		}

		private void changeConfigOption(JComponent component) {
			configOption.removeAll();
			configOption.add(component);
			configOption.updateUI();
		}

	}

	private static final int DEFAULT_TICK_STEP = 2;
	private static final String STATUS_PREFIX = "Phaser: ";
	private JLabel status;
	private JPanel graphPane;
	private JNumericSpinner tickStepX;
	private JCheckBox axisTextX;
	private JCheckBox axisTextY;
	private JCheckBox axisX;
	private JCheckBox axisY;
	private JCheckBox borderLayer;
	private JNumericSpinner tickStepY;
	private JColorChooser axesForeColorChooser;
	private JColorChooser axesBackColorChooser;
	private JPanel configOption;
	private JComponent propsFields;
	private JComponent layerFields;
	private JCheckBox gridLayer;
	private JCheckBox vectorLayer;
	private JComboBox graphPrimitive;

	private JNumericSpinner curveControlPoints;

	private JComponent coloringFields;
	private JComboBox coloringStrategy;
	private JPanel basicColoringPane;
	private JPanel multirampColoringPane;
	private JColorChooser basicColorChooser;

	private JCheckBox criticalAnalysis;
	private JEditorPane console;
	private JCheckBox fixedAnalysis;
	private JComboBox varX;
	private JComboBox varY;

	private static final long serialVersionUID = -1284363571798838954L;

	public static void main(String[] args) {
		PhaserApp p = new PhaserApp();
		p.pack();
		p.setExtendedState(JFrame.MAXIMIZED_BOTH);
		p.setVisible(true);
	}

	public PhaserApp() {
		super("Phaser 0.9.0");
		system = new Rossler();
		createPhaserUI();
	}

	@Override
	protected void addToLeftConfigPanel() {
		leftConfigPanel.add(createSystemFields());
		leftConfigPanel.add(createSimulatorFields());
		leftConfigPanel.add(createVarsFields());
		leftConfigPanel.add(createAnalysisFields());
		leftConfigPanel.add(createPlayerButtonBar());
	}

	protected Component createVarsFields() {
		JPanel panel = new JPanel(new SpringLayout());

		int fieldNum = 0;

		String[] labelStrings = new String[] { "X", "Y" };
		String[] names = system.getIndependentVarNames();
		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];

		varX = new JComboBox(names);
		varX.setSelectedItem("x");
		fields[fieldNum++] = varX;

		varY = new JComboBox(names);
		varY.setSelectedItem("y");
		fields[fieldNum++] = varY;

		composePanel(panel, "Simulation Properties", labelStrings, labels, fields);
		return panel;
	}

	@Override
	protected void afterSimulation() {
		final Storage data = simulator.getStateSeries();
		final Grapher2D graph = initializeGrapher(data);

		graphPane.removeAll();
		graphPane.add(graph);
	}

	protected JComponent createPropsFields() {
		JPanel panel = makePropertiesPanel();

		String showlabels[] = { "X axis", "Y axis", "X axis text", "Y axis text" };
		JPanel showPane = new JPanel(new GridLayout(-1, 3));
		showPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Show options"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		int fieldNum = 0;

		axisX = new JCheckBox(showlabels[fieldNum++], true);
		showPane.add(axisX);

		axisY = new JCheckBox(showlabels[fieldNum++], true);
		showPane.add(axisY);

		axisTextX = new JCheckBox(showlabels[fieldNum++], true);
		showPane.add(axisTextX);

		axisTextY = new JCheckBox(showlabels[fieldNum++], true);
		showPane.add(axisTextY);

		JPanel composite = new JPanel();
		composite.setLayout(new BoxLayout(composite, BoxLayout.Y_AXIS));
		composite.add(panel);
		composite.add(showPane);

		return composite;
	}

	@Override
	protected void setStatus(String s) {
		status.setText(STATUS_PREFIX + s);
	}

	private void appendPointDescription(final StringBuffer report, CPoint fp) {
		double[] coords = fp.getCoords();
		report.append("Point(");
		for (int i = 0; i < coords.length; i++) {
			report.append(coords[i]);
			if (i < coords.length - 1) {
				report.append(',');
			}
		}
		report.append(") ");
		report.append(fp.getDescription());
		report.append('\n');
	}

	private JComponent createAnalysisFields() {
		String labels[] = { "Critical Points", "Fixed Points" };
		JPanel pane = new JPanel(new GridLayout(-1, 3));
		pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Analysis"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));

		int fieldNum = 0;

		criticalAnalysis = new JCheckBox(labels[fieldNum++], false);
		pane.add(criticalAnalysis);

		fixedAnalysis = new JCheckBox(labels[fieldNum++], false);
		pane.add(fixedAnalysis);

		return pane;
	}

	private JComponent createColoringFields() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings = { "Coloring", "Configuration" };

		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		int fieldNum = 0;

		coloringStrategy = new JComboBox();
		coloringStrategy.addItem(new Pair<ColoringStrategy>("Basic", null));
		coloringStrategy.addItem(new Pair<ColoringStrategy>("Multiramp", new MultirampColors()));
		coloringStrategy.addActionListener(new ColoringStrategyListener());
		fields[fieldNum++] = coloringStrategy;

		basicColoringPane = new JPanel();
		basicColorChooser = new JColorChooser(Color.RED);
		JButton basicColorButton = new JButton(new ShowColorChooserAction("Color", this, basicColorChooser));
		basicColoringPane.add(basicColorButton);

		multirampColoringPane = new JPanel();
		multirampColoringPane.add(new JLabel("multi..."));
		multirampColoringPane.setVisible(false);

		JPanel coloringConfig = new JPanel();
		coloringConfig.add(basicColoringPane);
		coloringConfig.add(multirampColoringPane);
		fields[fieldNum++] = coloringConfig;

		composePanel(panel, "Colors", labelStrings, labels, fields);
		return panel;
	}

	private JSplitPane createConfigurationView() {
		layerFields = createLayerFields();
		propsFields = createPropsFields();
		coloringFields = createColoringFields();

		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(new Pair<JComponent>("Properties", propsFields));
		listModel.addElement(new Pair<JComponent>("Colors", coloringFields));
		listModel.addElement(new Pair<JComponent>("Layers", layerFields));

		JList list = new JList(listModel);
		list.setSelectedIndex(0);
		list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		list.setBackground(Color.WHITE);
		list.addListSelectionListener(new ConfigurationListListener());
		JPanel menuPane = new JPanel();
		menuPane.setBackground(Color.WHITE);
		menuPane.add(list);

		configOption = new JPanel();
		configOption.add(propsFields);

		final JSplitPane configPane = createSplitViewPanel(menuPane, configOption);
		configPane.setResizeWeight(0);
		return configPane;
	}

	private JComponent createLayerFields() {
		String labels[] = { "Border", "Grid", "Vector field" };
		JPanel pane = new JPanel(new GridLayout(-1, 3));
		pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Show options"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		int fieldNum = 0;

		borderLayer = new JCheckBox(labels[fieldNum++], true);
		pane.add(borderLayer);

		gridLayer = new JCheckBox(labels[fieldNum++], false);
		pane.add(gridLayer);

		vectorLayer = new JCheckBox(labels[fieldNum++], false);
		pane.add(vectorLayer);

		return pane;
	}

	private void createPhaserUI() {
		setLayout(new BorderLayout());

		status = new JLabel(STATUS_PREFIX);
		status.setBorder(new EtchedBorder());
		add(status, BorderLayout.SOUTH);

		final JMenuBar menubar = createMenuBar();
		menubar.add(createSystemMenu());
		menubar.add(createRunMenu());
		setJMenuBar(menubar);

		graphPane = new JPanel(new BorderLayout());
		graphPane.setBackground(Color.BLACK);
		graphPane.add(new Grapher2D(600, 600));

		final JSplitPane view = createSplitViewPanel(graphPane);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("View", null, view, "Main view");

		final JSplitPane configPane = createConfigurationView();

		tabbedPane.addTab("Configuration", configPane);

		console = new JEditorPane();
		console.setEditable(false);
		JScrollPane consolePane = new JScrollPane(console);
		tabbedPane.addTab("Console", consolePane);

		add(tabbedPane, BorderLayout.CENTER);
	}

	private void initAnalysis(final Grapher2D graph) {

		final boolean ca = criticalAnalysis.isSelected();
		final boolean fa = fixedAnalysis.isSelected();
		if (ca || fa) {
			final StringBuffer report = new StringBuffer();

			if (fa) {
				graph.getGrapherConfig().unsetDrawConfig(GrapherConfig2D.DRAW_AXES_TEXT);
			}

			if (ca && fa) {
				runAnalysis("All points", LocalStabilityAnalyser.ALL, graph, report);
			} else {
				if (ca) {
					runAnalysis("Critial points", LocalStabilityAnalyser.EQUILIBRIUM, graph, report);
				}

				if (fa) {
					runAnalysis("Fixed points", LocalStabilityAnalyser.FIXED, graph, report);
				}
			}

			console.setText(report.toString());
		}
	}

	private void runAnalysis(String title, int mode, final Grapher2D graph, final StringBuffer report) {
		LocalStabilityAnalyser lsa = new LocalStabilityAnalyser(system);
		lsa.setUseHomotopy(false);
		lsa.setMode(mode);

		try {
			lsa.compute();

			report.append('=');
			report.append(title.toUpperCase());
			report.append("=\n");

			Iterator<CPoint> itor = lsa.getFixedPoints().iterator();

			while (itor.hasNext()) {
				CPoint fp = itor.next();
				appendPointDescription(report, fp);
				graph.addMarker(fp);
			}
		} catch (Exception e) {
			setStatus(e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void initColoring(final Storage data, GrapherConfig conf, final Grapher2D graph) {
		final Pair<ColoringStrategy> selectedColoring = (Pair<ColoringStrategy>) coloringStrategy.getSelectedItem();
		final ColoringStrategy coloringStrategy = selectedColoring.getValue();
		if (coloringStrategy == null) {
			conf.setColor(basicColorChooser.getColor(), 0);
			graph.addData(data);
		} else {
			graph.addData(data, coloringStrategy);
		}
	}

	private void initDrawConfiguration(GrapherConfig conf, final Grapher2D graph) {
		if (borderLayer.isSelected()) {
			conf.setDrawConfig(GrapherConfig2D.DRAW_BORDER);
		}

		if (gridLayer.isSelected()) {
			conf.setDrawConfig(GrapherConfig2D.DRAW_GRID);
		}

		if (vectorLayer.isSelected()) {
			if (system.getType() == DynamicalSystem.ODE_T) {
				conf.setDrawConfig(GrapherConfig2D.DRAW_BORDER);
				conf.setDrawConfig(GrapherConfig2D.DRAW_VECTFIELD);
				OdeVectField vf = new OdeVectField((OdeSystem) system);
				vf.setGradient(new GradientPaint(0, 0, new Color(0, 255, 0, 0), 255, 0, new Color(0, 255, 0, 255),
						false));
				vf.setStyle(VectField.GRAD_LINE);
				graph.setVectField(vf);
			}
		}

		if (axisX.isSelected()) {
			conf.setDrawConfig(GrapherConfig2D.DRAW_AXIS_X);
		}

		if (axisY.isSelected()) {
			conf.setDrawConfig(GrapherConfig2D.DRAW_AXIS_Y);
		}

		if (axisTextX.isSelected()) {
			conf.setDrawConfig(GrapherConfig2D.DRAW_AXIS_TEXT_X);
		}

		if (axisTextY.isSelected()) {
			conf.setDrawConfig(GrapherConfig2D.DRAW_AXIS_TEXT_Y);
		}
	}

	@SuppressWarnings("unchecked")
	private void initGrapherConf(GrapherConfig conf, final Grapher2D graph) {
		final Pair<Integer> selectedItem = (Pair<Integer>) graphPrimitive.getSelectedItem();
		conf.setPlotStyle(selectedItem.getValue());
		conf.setCurveControlPointsNum(curveControlPoints.getInt());
		conf.setPlotVarX((String) varX.getSelectedItem());
		conf.setPlotVarY((String) varY.getSelectedItem());
		graph.setForeground(axesForeColorChooser.getColor());
		graph.setBackground(axesBackColorChooser.getColor());
		graph.setTickStep(tickStepX.getFloat(), tickStepY.getFloat());
	}

	private Grapher2D initializeGrapher(final Storage data) {
		GrapherConfig conf = new GrapherConfig2D();
		final Grapher2D graph = new Grapher2D(500, 500, conf);

		initDrawConfiguration(conf, graph);

		initGrapherConf(conf, graph);
		
		initAnalysis(graph);

		initColoring(data, conf, graph);

		return graph;
	}

	private JPanel makePropertiesPanel() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings = { "Tick step X", "Tick step Y", "Foreground", "Background", "Primitive", "Curve points" };

		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		int fieldNum = 0;

		tickStepX = new JNumericSpinner(DEFAULT_TICK_STEP, 0.1, 200000, 0.5);
		fields[fieldNum++] = tickStepX;

		tickStepY = new JNumericSpinner(DEFAULT_TICK_STEP, 0.1, 200000, 0.5);
		fields[fieldNum++] = tickStepY;

		axesForeColorChooser = new JColorChooser(Color.DARK_GRAY);
		JButton axesForeColorButton = new JButton(new ShowColorChooserAction("Foreground color", this,
				axesForeColorChooser));
		fields[fieldNum++] = axesForeColorButton;

		axesBackColorChooser = new JColorChooser(Color.BLACK);
		JButton axesBackColorButton = new JButton(new ShowColorChooserAction("Backgound color", this,
				axesBackColorChooser));
		fields[fieldNum++] = axesBackColorButton;

		graphPrimitive = new JComboBox();
		graphPrimitive.addItem(new Pair<Integer>("Points", GrapherConfig2D.POINTS));
		graphPrimitive.addItem(new Pair<Integer>("Lines", GrapherConfig2D.LINES));
		graphPrimitive.addItem(new Pair<Integer>("Curves", GrapherConfig2D.CURVES));
		fields[fieldNum++] = graphPrimitive;

		curveControlPoints = new JNumericSpinner(3, 3, 1000, 3);
		fields[fieldNum++] = curveControlPoints;

		composePanel(panel, "Properties", labelStrings, labels, fields);
		return panel;
	}
}
