package dynsim.ui.app;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;

import dynsim.exceptions.DynSimException;
import dynsim.simulator.Parameters;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.ui.JAppFrame;
import dynsim.ui.JNumericSpinner;
import dynsim.ui.SpringUtilities;
import dynsim.ui.app.utils.SystemLoader;

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
	protected static final String REFRESH_UI_CMD = "refreshUI";

	private static final int DEFAULT_ITERS = 15000;
	private static final int DEFAULT_SKIP = 1000;
	protected final static int GAP = 10;

	protected volatile Thread simulationThread;
	protected Simulator simulator;
	protected DynamicalSystem system;
	private String[] parameterLabels;
	private JNumericSpinner[] parameterFields;
	protected JButton runsim;
	protected JButton stopsim;
	protected JPanel leftConfigPanel;
	protected ButtonGroup systemChoice;
	private JNumericSpinner skip;
	private JNumericSpinner maxiters;

	protected HashMap<String, Class<DynamicalSystem>> systemClassNames;
	private AbstractButton stopMenuItem;
	private JMenuItem runMenuItem;

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
		} else if (REFRESH_UI_CMD.equals(e.getActionCommand())) {
			refreshConfigUI();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// void
	}

	protected void addToLeftConfigPanel() {
		leftConfigPanel.add(createSystemFields());
		leftConfigPanel.add(CreateSimulatorFields());
		leftConfigPanel.add(createPlayerButtonBar());
	}

	protected Component createPlayerButtonBar() {
		JToolBar toolbar = new JToolBar("Simulation player");
		toolbar.setOrientation(JToolBar.VERTICAL);
		toolbar.add(createPlayerButtons());
		return toolbar;
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

	protected JComponent createPlayerButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		runsim = makeButton("Play24", RUNSIM_CMD, "Run simulation", "Run");
		runsim.setVerticalTextPosition(AbstractButton.CENTER);
		runsim.setHorizontalTextPosition(AbstractButton.LEADING);
		runsim.setMnemonic(KeyEvent.VK_R);
		panel.add(runsim);

		stopsim = makeButton("Stop24", STOPSIM_CMD, "Stop simulation", "Stop");
		stopsim.setEnabled(false);
		stopsim.setVerticalTextPosition(AbstractButton.CENTER);
		stopsim.setHorizontalTextPosition(AbstractButton.LEADING);
		stopsim.setMnemonic(KeyEvent.VK_S);
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

		JMenuItem saveimg = new JMenuItem("Save Image...");
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

	protected JMenu createRunMenu() {
		JMenu runmenu = new JMenu("Run");
		runMenuItem = new JMenuItem("Run simulation");
		runMenuItem.setActionCommand(RUNSIM_CMD);
		runMenuItem.addActionListener(this);
		runmenu.add(runMenuItem);
		stopMenuItem = new JMenuItem("Stop simulation");
		stopMenuItem.setEnabled(false);
		stopMenuItem.setActionCommand(STOPSIM_CMD);
		stopMenuItem.addActionListener(this);
		runmenu.add(stopMenuItem);
		return runmenu;
	}

	protected Component CreateSimulatorFields() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings = { "Skip: ", "Iterations: " };
		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		int fieldNum = 0;
		skip = new JNumericSpinner(DEFAULT_SKIP, 1000, 200000, 100);
		fields[fieldNum++] = skip;

		maxiters = new JNumericSpinner(DEFAULT_ITERS, 5000, 999999999, 10000);
		fields[fieldNum++] = maxiters;

		composePanel(panel, "Simulation Properties", labelStrings, labels, fields);
		return panel;
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

	protected void createSystem() {
		Enumeration<AbstractButton> elements = systemChoice.getElements();
		while (elements.hasMoreElements()) {
			AbstractButton button = elements.nextElement();
			if (button.isSelected()) {
				system = newDynamicalSystem(button.getName());
				break;
			}
		}
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

	protected JMenu createSystemMenu() {
		JMenu sysmenu = new JMenu("System");

		systemChoice = new ButtonGroup();

		createOdeSubmenu(sysmenu);

		return sysmenu;
	}

	protected void exitApplication() {
		System.exit(0);
	}

	protected void initializeSimulator() {
		simulator = SimulatorFactory.createSimulator(system);
		simulator.setSkip(skip.getInt());
		simulator.setItersMax(maxiters.getInt());
	}

	@SuppressWarnings("unchecked")
	protected void loadOdeSystems() {
		systemClassNames = new HashMap<String, Class<DynamicalSystem>>();
		final Set<Class> classes = SystemLoader.getOdeSystemClasses();
		for (Class clazz : classes) {
			systemClassNames.put(clazz.getSimpleName(), clazz);
		}
	}

	protected DynamicalSystem newDynamicalSystem(String name) {
		DynamicalSystem tmp = null;
		try {
			tmp = systemClassNames.get(name).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}

	protected void onSimulationEnd() {
		toogglePlayerComps();
	}

	private void toogglePlayerComps() {
		tooggle(runsim);
		tooggle(runMenuItem);
		tooggle(stopsim);
		tooggle(stopMenuItem);
	}

	protected void onSimulationStart() {
		toogglePlayerComps();
	}

	protected void tooggle(JComponent component) {
		if (component != null) {
			component.setEnabled(!component.isEnabled());
		}
	}

	protected void prepareSystem() {
		for (int i = 0; i < parameterLabels.length; i++) {
			system.setParameter(parameterLabels[i], parameterFields[i].getDouble());
		}
	}

	protected void refreshConfigUI() {
		createSystem();
		leftConfigPanel.removeAll();
		addToLeftConfigPanel();
		leftConfigPanel.updateUI();
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

	private void createOdeSubmenu(JMenu sysmenu) {
		JMenu submenu = new JMenu("ODE");
		submenu.setMnemonic(KeyEvent.VK_O);
		sysmenu.add(submenu);

		loadOdeSystems();

		for (String sysname : systemClassNames.keySet()) {
			JRadioButtonMenuItem radio = new JRadioButtonMenuItem(sysname);
			radio.setActionCommand(REFRESH_UI_CMD);
			radio.addActionListener(this);
			radio.setName(sysname);
			systemChoice.add(radio);
			submenu.add(radio);
		}
	}

	protected JButton makeButton(String imageName, String actionCommand, String toolTipText, String altText) {
		// Look for the image.
		String imgLocation = "images/" + imageName + ".gif";
		URL imageURL = BaseApp.class.getResource(imgLocation);

		// Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if (imageURL != null) { // image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else { // no image found
			button.setText(altText);
			System.err.println("Resource not found: " + imgLocation);
		}

		return button;
	}
}
