package dynsim.ui.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.Renderer;
import dynsim.graphics.render.impl.CasterRenderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.simulator.Parameters;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.impl.Crispy;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.ui.JAppFrame;
import dynsim.ui.SpringUtilities;

/**
 * 
 * @version $Id$
 * 
 */
public class RenderApp extends JAppFrame implements ActionListener, ItemListener {

	public RenderApp() {
		super("Renderer 0.9.0");
		system = new Crispy();
		renderer = new CasterRenderer();
		createRendererUI();
		try {
			initializeSimulator();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class SimulationWorker implements Runnable {
		public void run() {
			try {
				setStatus("Running simulation...");
				simulate();
				setStatus("Done.");
				// let another thread have some time perhaps to stop this one.
				Thread.yield();
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException("Stopped by ifInterruptedStop()");
				}
			} catch (DynSimException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// interrupted
				setStatus("Thread interrupted.");
			}

			stopsim.setEnabled(false);
			runsim.setEnabled(true);

		}
	}

	private static final String STATUS_PREFIX = "RenderApp: ";
	private static final long serialVersionUID = 8893216423390898264L;

	private static final int DEFAULT_ITERS = 15000;
	private static final int DEFAULT_SKIP = 1000;
	private final static int GAP = 10;

	public static void main(String[] args) {
		RenderApp p = new RenderApp();
		p.pack();
		p.setVisible(true);
	}

	private JMenuItem newsim;
	private JMenuItem saveimg;
	private JMenuItem cleardisplay;

	private DisplayImage display;
	private JLabel status;
	private Renderer renderer;
	private Simulator simulator;
	private DynamicalSystem system;
	private JButton runsim;
	private volatile Thread simulationThread;
	private JButton stopsim;

	private JSpinner maxiters;
	private JSpinner detail;
	private JSpinner skip;
	private JSpinner gamma;
	private JSpinner scale;
	private JSpinner offset;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (newsim.equals(e.getSource())) {
			clearDisplay();
			setStatus("New simulation");
		} else if (runsim.equals(e.getSource())) {
			runsim.setEnabled(false);
			stopsim.setEnabled(true);
			runSimulation();
		} else if (stopsim.equals(e.getSource())) {
			stopWorker();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

	}

	protected void createRendererUI() {
		setLayout(new BorderLayout());

		status = new JLabel(STATUS_PREFIX);
		status.setBorder(new EtchedBorder());
		add(status, BorderLayout.SOUTH);

		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		JMenu filemenu = new JMenu("File");
		menubar.add(filemenu);
		newsim = new JMenuItem("New Simulation");
		newsim.addActionListener(this);

		saveimg = new JMenuItem("Save Image");
		cleardisplay = new JMenuItem("Clear Display");
		filemenu.add(newsim);
		filemenu.add(saveimg);
		filemenu.add(cleardisplay);

		display = new DisplayImage(400, 400);
		clearDisplay();

		final JPanel cmdpanel = new JPanel();
		cmdpanel.setLayout(new GridBagLayout());

		JPanel leftHalf = new JPanel() {
			private static final long serialVersionUID = -5837429055693038642L;

			// Don't allow us to stretch vertically.
			public Dimension getMaximumSize() {
				Dimension pref = getPreferredSize();
				return new Dimension(Integer.MAX_VALUE, pref.height);
			}
		};
		leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.PAGE_AXIS));
		leftHalf.add(createSystemFields());
		leftHalf.add(createRenderFields());
		leftHalf.add(createButtons());

		JPanel boxPane = new JPanel();
		boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.LINE_AXIS));
		boxPane.add(display);
		boxPane.add(leftHalf);

		add(boxPane, BorderLayout.CENTER);
	}

	protected void setStatus(String s) {
		status.setText(STATUS_PREFIX + s);
	}

	protected void simulate() throws DynSimException {
		simulator.compute();

		display.setImage(renderer.getImage());
		display.repaint();
	}

	private void clearDisplay() {
		final BufferedImage img = display.getImg();
		final Graphics g = img.getGraphics();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	private void initializeSimulator() throws DynSimException {
		simulator = SimulatorFactory.createSimulator(system, renderer);

		renderer.setAutoAxisRanges();
		renderer.setDetail(new Float((Double) detail.getValue()));
		renderer.setGamma(new Float((Double) gamma.getValue()));
		renderer.setScale(new Float((Double) scale.getValue()));
		renderer.setOffset(new Float((Double) offset.getValue()));

		simulator.setSkip((Integer) skip.getValue());
		simulator.setItersMax((Integer) maxiters.getValue());
	}

	private void runSimulation() {
		final SimulationWorker worker = new SimulationWorker();
		simulationThread = new Thread(worker);
		simulationThread.start();
	}

	private void stopWorker() {
		Thread tmp = simulationThread;
		simulationThread = null;
		if (tmp != null) {
			tmp.interrupt();
		}
	}

	protected JComponent createSystemFields() {
		JPanel panel = new JPanel(new SpringLayout());
		final Parameters params = system.getParameters();

		String[] labelStrings = new String[params.size()];
		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];

		int fieldNum = 0;
		for (String key : params.getLabels()) {
			labelStrings[fieldNum] = key;
			JComponent field = new JSpinner(new SpinnerNumberModel(params.getValue(key), -100, 100, 0.01));
			fields[fieldNum++] = field;
		}

		composePanel(panel, "System Properties", labelStrings, labels, fields);
		return panel;
	}

	private void composePanel(JPanel panel, String title, String[] labelStrings, JLabel[] labels, JComponent[] fields) {
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));

		// Associate label/field pairs, add everything,
		// and lay it out.
		for (int i = 0; i < labelStrings.length; i++) {
			labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
			labels[i].setLabelFor(fields[i]);
			panel.add(labels[i]);
			panel.add(fields[i]);
		}
		SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2, GAP, GAP, // init
				// x,y
				GAP, GAP / 2);// xpad, ypad
	}

	protected JComponent createRenderFields() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings = { "Skip: ", "Iterations: ", "Grain: ", "Gamma: ", "Scale: ", "Offset: " };

		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		int fieldNum = 0;

		skip = new JSpinner(new SpinnerNumberModel(DEFAULT_SKIP, 1000, 200000, 100));
		fields[fieldNum++] = skip;

		maxiters = new JSpinner(new SpinnerNumberModel(DEFAULT_ITERS, 5000, 999999999, 10000));
		fields[fieldNum++] = maxiters;

		detail = new JSpinner(new SpinnerNumberModel(0.015, 0.0, 2.0, 0.005));
		detail.setEditor(new JSpinner.NumberEditor(detail, "#0.0000"));
		fields[fieldNum++] = detail;

		gamma = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 5.0, 0.1));
		gamma.setEditor(new JSpinner.NumberEditor(gamma, "#0.00"));
		fields[fieldNum++] = gamma;

		scale = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 5.0, 0.1));
		scale.setEditor(new JSpinner.NumberEditor(scale, "#0.00"));
		fields[fieldNum++] = scale;

		offset = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 5.0, 0.01));
		offset.setEditor(new JSpinner.NumberEditor(offset, "#0.00"));
		fields[fieldNum++] = offset;

		composePanel(panel, "Render Properties", labelStrings, labels, fields);
		return panel;
	}

	protected JComponent createButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		runsim = new JButton("Run");
		runsim.setVerticalTextPosition(AbstractButton.CENTER);
		runsim.setHorizontalTextPosition(AbstractButton.LEADING);
		runsim.setMnemonic(KeyEvent.VK_R);
		runsim.setActionCommand("runsim");
		runsim.addActionListener(this);
		panel.add(runsim);

		stopsim = new JButton("Stop");
		stopsim.setEnabled(false);
		stopsim.setVerticalTextPosition(AbstractButton.CENTER);
		stopsim.setHorizontalTextPosition(AbstractButton.LEADING);
		stopsim.setMnemonic(KeyEvent.VK_S);
		stopsim.setActionCommand("stopsim");
		stopsim.addActionListener(this);
		panel.add(stopsim);

		// Match the SpringLayout's gap, subtracting 5 to make
		// up for the default gap FlowLayout provides.
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP - 5, GAP - 5));
		return panel;
	}
}
