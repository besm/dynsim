package dynsim.ui.app;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import dynsim.data.Storage;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.system.impl.Rossler;
import dynsim.ui.JNumericSpinner;
import dynsim.ui.ShowColorChooserAction;

public class PhaserApp extends BaseApp {

	private static final int DEFAULT_TICK_STEP = 2;
	private static final String STATUS_PREFIX = "Phaser: ";
	private JLabel status;
	private JPanel graphPane;
	private JNumericSpinner tickStepX;
	private JCheckBox axisTextX;
	private JCheckBox axisTextY;
	private JCheckBox axisX;
	private JCheckBox axisY;
	private JCheckBox border;
	private JNumericSpinner tickStepY;
	private JColorChooser axesForeColorChooser;
	private JColorChooser axesBackColorChooser;

	private static final long serialVersionUID = -1284363571798838954L;

	public static void main(String[] args) {
		PhaserApp p = new PhaserApp();
		p.pack();
		p.center();
		p.setVisible(true);
	}

	public PhaserApp() {
		super("Phaser 0.9.0");
		system = new Rossler();
		createPhaserUI();
	}

	protected JComponent createGrapherFields() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings = { "Tick step X", "Tick step Y", "Foreground", "Background", "", "", "", "", "" };

		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		int fieldNum = 0;

		tickStepX = new JNumericSpinner(DEFAULT_TICK_STEP, 0.1, 200000, 0.5);
		fields[fieldNum++] = tickStepX;

		tickStepY = new JNumericSpinner(DEFAULT_TICK_STEP, 0.1, 200000, 0.5);
		fields[fieldNum++] = tickStepY;
		

		axesForeColorChooser = new JColorChooser(Color.DARK_GRAY);
		JButton axesForeColorButton = new JButton(new ShowColorChooserAction("Foreground color", this, axesForeColorChooser));
		fields[fieldNum++] = axesForeColorButton;
		
		axesBackColorChooser = new JColorChooser(Color.BLACK);
		JButton axesBackColorButton = new JButton(new ShowColorChooserAction("Backgound color", this, axesBackColorChooser));
		fields[fieldNum++] = axesBackColorButton;

		border = new JCheckBox("Border", true);
		fields[fieldNum++] = border;

		axisX = new JCheckBox("X axis", true);
		fields[fieldNum++] = axisX;

		axisY = new JCheckBox("Y axis", true);
		fields[fieldNum++] = axisY;

		axisTextX = new JCheckBox("X axis text", true);
		fields[fieldNum++] = axisTextX;

		axisTextY = new JCheckBox("Y axis text", true);
		fields[fieldNum++] = axisTextY;

		composePanel(panel, "Axes Properties", labelStrings, labels, fields);
		return panel;
	}

	@Override
	protected void afterSimulation() {
		final Storage data = simulator.getStateSeries();
		final Grapher2D graph = initializeGrapher(data);

		graphPane.removeAll();
		graphPane.add(graph);
	}

	private Grapher2D initializeGrapher(final Storage data) {
		GrapherConfig conf = new GrapherConfig2D();

		if (border.isSelected()) {
			conf.setDrawConfig(GrapherConfig2D.DRAW_BORDER);
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

		conf.setDrawConfig(GrapherConfig2D.DRAW_GRID);
		conf.setMainColor(Color.BLUE);

		final Grapher2D graph = new Grapher2D(500, 500, conf);
		graph.setForeground(axesForeColorChooser.getColor());
		graph.setBackground(axesBackColorChooser.getColor());
		graph.setTickStep(tickStepX.getFloat(), tickStepY.getFloat());
		graph.addData(data, new MultirampColors());
		return graph;
	}

	@Override
	protected void setStatus(String s) {
		status.setText(STATUS_PREFIX + s);
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
		graphPane.add(new Grapher2D(500, 500));

		final JSplitPane view = createSplitViewPanel(graphPane);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("View", null, view, "Main view");

		JPanel configPane = new JPanel();
		configPane.add(createGrapherFields());
		tabbedPane.addTab("Configuration", configPane);


		add(tabbedPane, BorderLayout.CENTER);
	}

}
