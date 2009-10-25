package dynsim.ui.app;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
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

	private static final long serialVersionUID = -1284363571798838954L;

	public static void main(String[] args) {
		PhaserApp p = new PhaserApp();
		p.pack();
		p.setVisible(true);
	}

	public PhaserApp() {
		super("Phaser 0.9.0");
		system = new Rossler();
		createPhaserUI();
	}

	protected void addToLeftConfigPanel() {
		leftConfigPanel.add(createSystemFields());
		// leftConfigPanel.add(new GraphConfig());
		leftConfigPanel.add(createButtons());
	}

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

	private void createPhaserUI() {
		setLayout(new BorderLayout());

		status = new JLabel(STATUS_PREFIX);
		status.setBorder(new EtchedBorder());
		add(status, BorderLayout.SOUTH);

		final JMenuBar menubar = createMenuBar();
		menubar.add(createSystemMenu());
		setJMenuBar(menubar);

		graphPane = new JPanel(new BorderLayout());
		graphPane.setBackground(Color.BLACK);
		graphPane.add(new Grapher2D(500, 500));

		final JSplitPane view = createSplitViewPanel(graphPane);

		add(view, BorderLayout.CENTER);
	}

}
