package dynsim.ui.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.Renderer;
import dynsim.graphics.render.impl.CasterRenderer;
import dynsim.graphics.render.impl.DensityRenderer;
import dynsim.graphics.render.impl.ReflectorRenderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.impl.Chua;
import dynsim.simulator.ode.system.impl.Crispy;
import dynsim.ui.JNumericSpinner;

/**
 * 
 * @version $Id$
 * 
 */
public class RenderApp extends BaseApp {

	private static final String REFRESH_UI_CMD = "refreshUI";

	private static final String STATUS_PREFIX = "RenderApp: ";

	private static final long serialVersionUID = 8893216423390898264L;

	private static final int DEFAULT_ITERS = 15000;
	private static final int DEFAULT_SKIP = 1000;

	public static void main(String[] args) {
		BaseApp p = new RenderApp();
		p.pack();
		p.setVisible(true);
	}

	private DisplayImage display;
	private JLabel status;

	private Renderer renderer;

	private JNumericSpinner maxiters;
	private JNumericSpinner detail;

	private JNumericSpinner skip;
	private JNumericSpinner gamma;
	private JNumericSpinner scale;
	private JNumericSpinner offset;

	private ButtonGroup rendererChoice;

	private JRadioButtonMenuItem casterRadio;

	private JRadioButtonMenuItem densityRadio;

	private ButtonGroup systemChoice;

	private JRadioButtonMenuItem crispyRadio;

	private JRadioButtonMenuItem chuaRadio;

	private JRadioButtonMenuItem reflectorRadio;

	public RenderApp() {
		super("Renderer 0.9.0");
		system = new Crispy();
		createRendererUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		if (REFRESH_UI_CMD.equals(e.getActionCommand())) {
			createSystem();
			leftConfigPanel.removeAll();
			addToLeftConfigPanel();
			leftConfigPanel.repaint();
		}
	}

	public void clearDisplay() {
		final BufferedImage img = display.getImg();
		final Graphics g = img.getGraphics();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	protected void addToLeftConfigPanel() {
		leftConfigPanel.add(createSystemFields());
		leftConfigPanel.add(createRenderFields());
		leftConfigPanel.add(createButtons());
	}

	protected void afterSimulation() {
		display.setImage(renderer.getImage());
		display.repaint();
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menubar = super.createMenuBar();

		JMenu rendermenu = new JMenu("Render");
		menubar.add(rendermenu);

		rendererChoice = new ButtonGroup();
		casterRadio = new JRadioButtonMenuItem("Caster");
		casterRadio.setSelected(true);
		rendererChoice.add(casterRadio);
		rendermenu.add(casterRadio);

		reflectorRadio = new JRadioButtonMenuItem("Reflector");
		rendererChoice.add(reflectorRadio);
		rendermenu.add(reflectorRadio);

		densityRadio = new JRadioButtonMenuItem("Density");
		rendererChoice.add(densityRadio);
		rendermenu.add(densityRadio);

		JMenu sysmenu = new JMenu("System");
		menubar.add(sysmenu);

		systemChoice = new ButtonGroup();
		crispyRadio = new JRadioButtonMenuItem("Crispy");
		crispyRadio.setSelected(true);
		crispyRadio.setActionCommand(REFRESH_UI_CMD);
		crispyRadio.addActionListener(this);
		systemChoice.add(crispyRadio);
		sysmenu.add(crispyRadio);

		chuaRadio = new JRadioButtonMenuItem("Chua");
		chuaRadio.addActionListener(this);
		chuaRadio.setActionCommand(REFRESH_UI_CMD);
		systemChoice.add(chuaRadio);
		sysmenu.add(chuaRadio);

		return menubar;
	}

	protected void createRenderer() {
		if (rendererChoice.isSelected(casterRadio.getModel())) {
			renderer = new CasterRenderer();
		} else if (rendererChoice.isSelected(densityRadio.getModel())) {
			renderer = new DensityRenderer();
		} else if (rendererChoice.isSelected(reflectorRadio.getModel())) {
			renderer = new ReflectorRenderer();
		}
	}

	protected void createRendererUI() {
		setLayout(new BorderLayout());

		status = new JLabel(STATUS_PREFIX);
		status.setBorder(new EtchedBorder());
		add(status, BorderLayout.SOUTH);

		final JMenuBar menubar = createMenuBar();
		setJMenuBar(menubar);

		display = new DisplayImage(400, 400);
		clearDisplay();
		JPanel displayPane = new JPanel();
		displayPane.add(display);

		JSplitPane viewPane = createSplitViewPanel(displayPane);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("View", null, viewPane, "Main view");

		JPanel configPane = new JPanel();
		tabbedPane.addTab("Configuration", configPane);

		add(tabbedPane, BorderLayout.CENTER);
	}

	protected JComponent createRenderFields() {
		JPanel panel = new JPanel(new SpringLayout());

		String[] labelStrings = { "Skip: ", "Iterations: ", "Grain: ", "Gamma: ", "Scale: ", "Offset: " };

		JLabel[] labels = new JLabel[labelStrings.length];
		JComponent[] fields = new JComponent[labelStrings.length];
		int fieldNum = 0;

		skip = new JNumericSpinner(DEFAULT_SKIP, 1000, 200000, 100);
		fields[fieldNum++] = skip;

		maxiters = new JNumericSpinner(DEFAULT_ITERS, 5000, 999999999, 10000);
		fields[fieldNum++] = maxiters;

		detail = new JNumericSpinner(0.015, 0.0, 2.0, 0.005);
		detail.setEditor(new JNumericSpinner.NumberEditor(detail, "#0.0000"));
		fields[fieldNum++] = detail;

		gamma = new JNumericSpinner(1.0, 0.0, 5.0, 0.1);
		gamma.setEditor(new JNumericSpinner.NumberEditor(gamma, "#0.00"));
		fields[fieldNum++] = gamma;

		scale = new JNumericSpinner(1.0, 0.0, 5.0, 0.1);
		scale.setEditor(new JNumericSpinner.NumberEditor(scale, "#0.00"));
		fields[fieldNum++] = scale;

		offset = new JNumericSpinner(0.0, 0.0, 5.0, 0.01);
		offset.setEditor(new JNumericSpinner.NumberEditor(offset, "#0.00"));
		fields[fieldNum++] = offset;

		composePanel(panel, "Render Properties", labelStrings, labels, fields);
		return panel;
	}

	protected void createSystem() {
		if (systemChoice.isSelected(crispyRadio.getModel())) {
			system = new Crispy();
		} else if (systemChoice.isSelected(chuaRadio.getModel())) {
			system = new Chua();
		}
	}

	protected void initializeSimulator() {
		createRenderer();

		simulator = SimulatorFactory.createSimulator(system, renderer);

		try {
			renderer.setAutoAxisRanges();
			renderer.setDetail(detail.getFloat());
			renderer.setGamma(gamma.getFloat());
			renderer.setScale(scale.getFloat());
			renderer.setOffset(offset.getFloat());

			simulator.setSkip(skip.getInt());
			simulator.setItersMax(maxiters.getInt());
		} catch (DynSimException e) {
			e.printStackTrace();
		}
	}

	protected void runSimulation() {
		clearDisplay();

		super.runSimulation();
	}

	protected void setStatus(String s) {
		status.setText(STATUS_PREFIX + s);
	}
}
