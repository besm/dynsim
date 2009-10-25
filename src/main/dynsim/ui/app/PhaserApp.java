package dynsim.ui.app;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import dynsim.data.Storage;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.system.impl.Rossler;

public class PhaserApp extends BaseApp {

	private static final String STATUS_PREFIX = "Phaser: ";
	private JLabel status;
	private JPanel graphPane;

	public PhaserApp() {
		super("Phaser 0.9.0");
		system = new Rossler();
		createPhaserUI();
	}

	private void createPhaserUI() {
		setLayout(new BorderLayout());

		status = new JLabel(STATUS_PREFIX);
		status.setBorder(new EtchedBorder());
		add(status, BorderLayout.SOUTH);

		final JMenuBar menubar = createMenuBar();
		setJMenuBar(menubar);

		createLeftConfigPanel();

		graphPane = new JPanel();
		graphPane.setBackground(Color.BLACK);
		graphPane.add(new Grapher2D(500, 500));
		JPanel boxPane = new JPanel();
		boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.LINE_AXIS));
		boxPane.add(graphPane);
		boxPane.add(leftConfigPanel);

		add(boxPane, BorderLayout.CENTER);
	}

	protected void addToLeftConfigPanel() {
		leftConfigPanel.add(createSystemFields());
		// leftHalf.add(createGrapherConfigFields());
		leftConfigPanel.add(createButtons());
	}

	public static void main(String[] args) {
		PhaserApp p = new PhaserApp();
		p.pack();
		p.setVisible(true);
	}

	private static final long serialVersionUID = -1284363571798838954L;

	@Override
	protected void afterSimulation() {
		final Storage data = simulator.getStateSeries();
		GrapherConfig conf = new GrapherConfig2D();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);
		conf.setDrawConfig(GrapherConfig2D.DRAW_GRID);
		final Grapher2D graph = new Grapher2D(500, 500, conf);
		graph.setTickStep(2);
		graph.addData(data, new MultirampColors());

		graphPane.removeAll();
		graphPane.add(graph);
	}

	@Override
	protected void setStatus(String s) {
		status.setText(STATUS_PREFIX + s);
	}

}
