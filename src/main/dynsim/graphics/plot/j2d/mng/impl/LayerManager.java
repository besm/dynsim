package dynsim.graphics.plot.j2d.mng.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.layer.AbstractGrapherLayer;
import dynsim.graphics.plot.j2d.layer.GrapherLayer;
import dynsim.graphics.plot.j2d.layer.impl.AxesLayer;
import dynsim.graphics.plot.j2d.layer.impl.BorderLayer;
import dynsim.graphics.plot.j2d.layer.impl.DataLayer;
import dynsim.graphics.plot.j2d.layer.impl.GridLayer;
import dynsim.graphics.plot.j2d.mng.GrapherAction;
import dynsim.graphics.plot.j2d.mng.GrapherManager;

public class LayerManager extends GrapherManager implements Observer {
	/**
	 * Actions
	 * 
	 */
	protected class DrawAction implements GrapherAction {

		public void execute(GrapherManager mng) {
			LayerManager lm = (LayerManager) mng;
			Iterator<GrapherLayer> layers = lm.layersIterator();

			while (layers.hasNext()) {
				runLayer(layers.next());
			}
		}

	}

	List<GrapherLayer> layers;

	private AbstractGrapherLayer grid, axes, border, data;

	public LayerManager(Grapher2D g) {
		super(g);
		layers = new ArrayList<GrapherLayer>();

		GrapherConfig config = g.getGrapherConfig();
		config.addObserver(this);

		// Default layers
		grid = new GridLayer();
		axes = new AxesLayer();
		data = new DataLayer();
		border = new BorderLayer();

		addLayer(grid);
		addLayer(axes);
		addLayer(data);
		addLayer(border);
	}

	public void addLayer(GrapherLayer layer) {
		layers.add(layer);
	}

	public void addLayer(int index, GrapherLayer layer) {
		layers.add(index, layer);
	}

	public Iterator<GrapherLayer> layersIterator() {
		return layers.iterator();
	}

	public GrapherAction newDrawAction() {
		return new DrawAction();
	}

	public void update(Observable o, Object arg) {
		// TODO hacer algo útil: log...
		// pasarlo a las capas interesadas?
		System.out.println("obj: " + o + " arg: " + arg);

		if (o.getClass() == GrapherConfig2D.class) {
			System.out.println("is " + o.getClass());
		}
	}

	private void runLayer(GrapherLayer layer) {
		if (layer.isDrawable(g.getGrapherConfig())) {
			layer.init(g);
			try {
				layer.compute();
			} catch (DynSimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			layer.draw(g);
		}
	}

}
