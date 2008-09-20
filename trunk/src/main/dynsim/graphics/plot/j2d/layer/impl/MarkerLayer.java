package dynsim.graphics.plot.j2d.layer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.layer.AbstractGrapherLayer;
import dynsim.graphics.plot.j2d.layer.Marker;

public class MarkerLayer extends AbstractGrapherLayer {
	private Collection<Marker> markerPoints;

	public MarkerLayer() {
		markerPoints = new ArrayList<Marker>();
	}

	public boolean isDrawable(GrapherConfig config) {
		// TODO add DRAW_MARKER
		boolean r = (config.isEnabled(GrapherConfig2D.DRAW_BORDER));
		return r;
	}

	public void draw(Grapher2D g) {
		Iterator<Marker> markers = markerPoints.iterator();

		while (markers.hasNext()) {
			Marker marker = markers.next();
			marker.draw(g);
		}

	}

	public void addMarker(Marker fp) {
		markerPoints.add(fp);
	}

	public boolean hasMarkerPoints() {
		return !markerPoints.isEmpty();
	}

	public Iterator<Marker> getMarkersIterator() {
		return markerPoints.iterator();
	}
}
