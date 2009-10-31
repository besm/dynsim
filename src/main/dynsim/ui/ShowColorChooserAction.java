package dynsim.ui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ShowColorChooserAction extends AbstractAction {
	private static final long serialVersionUID = -7345708033144147617L;
	JColorChooser chooser;
	JDialog dialog;

	BufferedImage img;

	// Define listener for ok events
	ActionListener okListener = new ActionListener() {
		// Called when user clicks ok
		public void actionPerformed(ActionEvent evt) {
			setEnabled(true);
			updateIco();
		}
	};

	// Define listener for cancel events
	ActionListener cancelListener = new ActionListener() {
		// Called when user clicks cancel
		public void actionPerformed(ActionEvent evt) {
			setEnabled(true);
			updateIco();
		}
	};

	public ShowColorChooserAction(String title, JFrame frame, JColorChooser chooser) {
		super("Choose...");

		this.chooser = chooser;

		createIcon();
		
		// Choose whether dialog is modal or modeless
		boolean modal = false;

		// Create the dialog that contains the chooser
		dialog = JColorChooser.createDialog(frame, title, modal, chooser, okListener, cancelListener);
	}

	private void createIcon() {
		this.img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		ImageIcon icon = new ImageIcon(img);
		putValue(Action.SMALL_ICON, icon);
		updateIco();
	}

	public void actionPerformed(ActionEvent evt) {
		// Show dialog
		dialog.setVisible(true);

		// Disable the action; to enable the action when the dialog is closed,
		setEnabled(false);
	}

	private void updateIco() {
		Graphics g = img.getGraphics();
		g.setColor(chooser.getColor());
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

}
