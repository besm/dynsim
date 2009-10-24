package dynsim.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class JAppFrame extends JFrame {
	private static final int DEFAULT_SIZE = 500;

	private static final long serialVersionUID = 5707661587909926364L;

	public JAppFrame() {
		this("Swing Application");
	}

	public JAppFrame(String title) {
		super(title);
		createUI();
	}

	protected void createUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(DEFAULT_SIZE, DEFAULT_SIZE);
		center();
	}

	public void center() {
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension frame = getSize();
		final int x = (screen.width - frame.width) / 2;
		final int y = (screen.height - frame.height) / 2;

		setLocation(x, y);
	}
}
