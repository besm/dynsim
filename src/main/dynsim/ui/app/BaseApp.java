package dynsim.ui.app;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

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
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;

import dynsim.exceptions.DynSimException;
import dynsim.simulator.Parameters;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.ui.JAppFrame;
import dynsim.ui.JNumericSpinner;
import dynsim.ui.SpringUtilities;

public abstract class BaseApp extends JAppFrame implements ActionListener, ItemListener {
	private class SimulationWorker implements Runnable {
		public void run() {
			onSimulationStart();

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

			onSimulationEnd();

		}
	}

	protected static final String RUNSIM_CMD = "runsim";

	protected static final String STOPSIM_CMD = "stopsim";
	protected static final String EXIT_CMD = "exit";

	protected final static int GAP = 10;
	protected volatile Thread simulationThread;
	protected Simulator simulator;
	protected DynamicalSystem system;
	private String[] parameterLabels;
	private JNumericSpinner[] parameterFields;

	protected JButton runsim;
	protected JButton stopsim;

	protected JPanel leftConfigPanel;

	private static final long serialVersionUID = -8767220695127049878L;

	public BaseApp(String title) {
		super(title);
	}

	public void actionPerformed(ActionEvent e) {
		if (RUNSIM_CMD.equals(e.getActionCommand())) {
			runSimulation();
		} else if (STOPSIM_CMD.equals(e.getActionCommand())) {
			stopWorker();
		} else if (EXIT_CMD.equals(e.getActionCommand())) {
			exitApplication();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// void
	}

	protected void addToLeftConfigPanel() {
		leftConfigPanel.add(createSystemFields());
		leftConfigPanel.add(createButtons());
	}

	protected abstract void afterSimulation();

	protected void composePanel(JPanel panel, String title, String[] labelStrings, JLabel[] labels, JComponent[] fields) {
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

	protected JComponent createButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		runsim = new JButton("Run");
		runsim.setVerticalTextPosition(AbstractButton.CENTER);
		runsim.setHorizontalTextPosition(AbstractButton.LEADING);
		runsim.setMnemonic(KeyEvent.VK_R);
		runsim.setActionCommand(RUNSIM_CMD);
		runsim.addActionListener(this);
		panel.add(runsim);

		stopsim = new JButton("Stop");
		stopsim.setEnabled(false);
		stopsim.setVerticalTextPosition(AbstractButton.CENTER);
		stopsim.setHorizontalTextPosition(AbstractButton.LEADING);
		stopsim.setMnemonic(KeyEvent.VK_S);
		stopsim.setActionCommand(STOPSIM_CMD);
		stopsim.addActionListener(this);
		panel.add(stopsim);

		// Match the SpringLayout's gap, subtracting 5 to make
		// up for the default gap FlowLayout provides.
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP - 5, GAP - 5));
		return panel;
	}

	protected JPanel createLefBoxPanel() {
		JPanel boxPane = new JPanel();
		boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.Y_AXIS));
		boxPane.add(createLeftConfigPanel());
		return boxPane;
	}

	protected JComponent createLeftConfigPanel() {
		leftConfigPanel = new JPanel() {
			private static final long serialVersionUID = -5837429055693038642L;

			// Don't allow us to stretch vertically.
			public Dimension getMaximumSize() {
				Dimension pref = getPreferredSize();
				return new Dimension(Integer.MAX_VALUE, pref.height);
			}
		};
		leftConfigPanel.setLayout(new BoxLayout(leftConfigPanel, BoxLayout.Y_AXIS));
		addToLeftConfigPanel();

		return leftConfigPanel;
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		menubar.add(filemenu);

		JMenuItem saveimg = new JMenuItem("Save Image");
		filemenu.add(saveimg);
		filemenu.addSeparator();
		JMenuItem cleardisplay = new JMenuItem("Clear Display");
		filemenu.add(cleardisplay);
		filemenu.addSeparator();
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		exit.setActionCommand(EXIT_CMD);
		filemenu.add(exit);

		return menubar;
	}

	protected JSplitPane createSplitViewPanel(JPanel pane) {
		return createSplitViewPanel(pane, createLefBoxPanel());
	}

	protected JSplitPane createSplitViewPanel(JPanel pane, JPanel leftPane) {
		JSplitPane viewPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pane, leftPane);
		viewPane.setResizeWeight(0.5);
		viewPane.setOneTouchExpandable(true);
		viewPane.setContinuousLayout(true);
		return viewPane;
	}

	protected JComponent createSystemFields() {
		JPanel panel = new JPanel(new SpringLayout());
		final Parameters params = system.getParameters();

		parameterLabels = new String[params.size()];
		JLabel[] labels = new JLabel[parameterLabels.length];
		parameterFields = new JNumericSpinner[parameterLabels.length];

		int fieldNum = 0;
		for (String key : params.getLabels()) {
			parameterLabels[fieldNum] = key;
			JNumericSpinner field = new JNumericSpinner(params.getValue(key), -100, 100, 0.01);
			parameterFields[fieldNum++] = field;
		}

		composePanel(panel, system.toString() + " Properties", parameterLabels, labels, parameterFields);
		return panel;
	}

	protected void exitApplication() {
		System.exit(0);
	}

	protected void initializeSimulator() {
		simulator = SimulatorFactory.createSimulator(system);
	}

	protected void onSimulationEnd() {
		stopsim.setEnabled(false);
		runsim.setEnabled(true);
	}

	protected void onSimulationStart() {
		runsim.setEnabled(false);
		stopsim.setEnabled(true);
	}

	protected void prepareSystem() {
		for (int i = 0; i < parameterLabels.length; i++) {
			system.setParameter(parameterLabels[i], parameterFields[i].getDouble());
		}
	}

	protected void runSimulation() {
		prepareSystem();

		initializeSimulator();

		final SimulationWorker worker = new SimulationWorker();
		simulationThread = new Thread(worker);
		simulationThread.start();
	}

	protected abstract void setStatus(String s);

	protected void simulate() throws DynSimException {
		simulator.compute();
		afterSimulation();
	}

	protected void stopWorker() {
		Thread tmp = simulationThread;
		simulationThread = null;
		if (tmp != null) {
			tmp.interrupt();
		}
	}
}
