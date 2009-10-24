package dynsim.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppFrame extends Frame {
	private static final int DEFAULT_SIZE = 500;

	private static final long serialVersionUID = 5707661587909926364L;

	public AppFrame() {
		this("Application");
	}

	public AppFrame(String title) {
		super(title);
		createUI();
	}

	protected void createUI() {
		setSize(DEFAULT_SIZE, DEFAULT_SIZE);
		center();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	public void center() {
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension frame = getSize();
		final int x = (screen.width - frame.width) / 2;
		final int y = (screen.height - frame.height) / 2;

		setLocation(x, y);
	}
}
