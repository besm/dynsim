package dynsim.math.analysis.rqa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import dynsim.data.Storage;
import dynsim.graphics.color.Palette;
import dynsim.math.numeric.Computable;

public class RecurrencePlotToImg implements RecurrencePlotDelegate, Computable {

	private static int DEFAULT_IMG_SIZ = 1000;

	private BufferedImage img;

	private String imgDir;

	private String ftype;

	private RecurrencePlot rp;

	private int type;

	private Palette palette;

	private int numColors;

	private float sharpen;

	public RecurrencePlotToImg(Storage state, String[] names) {
		this(state, names, DEFAULT_IMG_SIZ, 1024, 1f);
	}

	public RecurrencePlotToImg(Storage state, String[] names, int siz, int nc, float shp) {
		super();
		imgDir = "data/images/";
		ftype = "png";
		rp = new RecurrencePlot(state, names, this);
		rp.unsetMode(RecurrencePlot.QANALYSIS);
		type = COLOR;
		initImg(siz);
		setNumColors(nc);
		updateColors();
		sharpen = shp;
	}

	private void updateColors() {
		setPalette(new Palette(0, 1f, numColors));
	}

	private void initImg(int siz) {
		if (rp.rowsNum > siz) {
			img = new BufferedImage(siz, siz, BufferedImage.TYPE_INT_RGB);
		} else {
			img = new BufferedImage(rp.rowsNum, rp.rowsNum, BufferedImage.TYPE_INT_RGB);
		}
	}

	public void compute() {
		rp.compute();
	}

	public void end() {
		writeImg(img);
	}

	public void put(int i, int j, double d) {
		if (type == MONOCHROME) {
			if (d > 0) {
				putPx(i, j, Color.GREEN.getRGB());
			}
		} else if (type == COLOR) {
			int c = 0;

			if (sharpen > 0) {
				c = palette.getColor((sharpen * numColors) / d);
			} else {
				c = palette.getColor(numColors / d);
			}
			putPx(i, j, c);
		}
	}

	private void putPx(int i, int j, int c) {
		float ni = rp.rowsNum / img.getWidth();
		i /= ni;

		float nj = rp.rowsNum / img.getHeight();
		j /= nj;

		if (j >= img.getHeight())
			return;
		if (i >= img.getWidth())
			return;

		img.setRGB(i, (img.getHeight() - 1 - j), c);
	}

	private void writeImg(BufferedImage img) {
		try {
			File f = File.createTempFile("rqa", "." + ftype, new File(imgDir));
			ImageIO.write(img, ftype, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getType() {
		return type;
	}

	public boolean isColored() {
		return type == COLOR;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFileType() {
		return ftype;
	}

	public void setFileType(String ftype) {
		this.ftype = ftype;
	}

	public String getImgDir() {
		return imgDir;
	}

	public void setImgDir(String imgDir) {
		this.imgDir = imgDir;
	}

	public int getNumColors() {
		return numColors;
	}

	public void setNumColors(int numColors) {
		this.numColors = numColors;
	}

	public Palette getPalette() {
		return palette;
	}

	public void setPalette(Palette palette) {
		this.palette = palette;
	}

	public float getSharpen() {
		return sharpen;
	}

	public void setSharpen(float sharpen) {
		this.sharpen = sharpen;
	}
}
