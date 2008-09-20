package dynsim.graphics.plot.j3d;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

public abstract class Base3D extends Canvas3D {
	private static final long serialVersionUID = -1366627770017008417L;

	protected boolean init = false;

	protected boolean parallelProjection = false;

	protected SimpleUniverse universe;

	public Base3D() {
		super(SimpleUniverse.getPreferredConfiguration());
		setSize(500, 500);
	}

	protected void init() {
		Node plot = createPlot();
		BranchGroup scene = defineMouseBehaviour(plot);
		setupLights(scene); // Surface plot wants an extra light
		scene.compile();

		universe = new SimpleUniverse(this);
		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(scene);

		if (parallelProjection) {
			setProjectionPolicy(universe, parallelProjection);
		}

		init = true;
	}

	// addNotify is called when the Canvas3D is added to a container
	public void addNotify() {
		if (!init)
			init();
		super.addNotify(); // must call for Java3D to operate properly when
		// overriding
	}

	public boolean getParallelProjection() {
		return parallelProjection;
	}

	public void setParallelProjection(boolean b) {
		if (parallelProjection != b) {
			parallelProjection = b;
			setProjectionPolicy(universe, parallelProjection);
		}
	}

	/**
	 * Override to provide plot content
	 */
	protected abstract Node createPlot();

	/**
	 * Override to provide different mouse behaviour
	 */
	protected BranchGroup defineMouseBehaviour(Node scene) {
		BranchGroup bg = new BranchGroup();
		Bounds bounds = getDefaultBounds();

		TransformGroup objTransform = new TransformGroup();
		objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTransform.addChild(scene);
		bg.addChild(objTransform);

		MouseRotate mouseRotate = new MouseRotate();
		mouseRotate.setTransformGroup(objTransform);
		mouseRotate.setSchedulingBounds(bounds);
		bg.addChild(mouseRotate);

		MouseTranslate mouseTranslate = new MouseTranslate();
		mouseTranslate.setTransformGroup(objTransform);
		mouseTranslate.setSchedulingBounds(bounds);
		bg.addChild(mouseTranslate);

		MouseZoom mouseZoom = new MouseZoom();
		mouseZoom.setTransformGroup(objTransform);
		mouseZoom.setSchedulingBounds(bounds);
		bg.addChild(mouseZoom);

		MouseWheelZoom mouseWheelZoom = new MouseWheelZoom();
		mouseWheelZoom.setTransformGroup(objTransform);
		mouseWheelZoom.setSchedulingBounds(bounds);
		bg.addChild(mouseWheelZoom);

		// Set initial transformation
		Transform3D trans = createDefaultOrientation();
		objTransform.setTransform(trans);

		Behavior keyBehavior = new PlotKeyNavigatorBehavior(objTransform, .1f, 10f);
		objTransform.addChild(keyBehavior);
		keyBehavior.setSchedulingBounds(bounds);

		return bg;
	}

	protected void setupLights(BranchGroup root) {
		DirectionalLight lightD = new DirectionalLight();
		lightD.setDirection(new Vector3f(0.0f, -0.7f, -0.7f));
		lightD.setInfluencingBounds(getDefaultBounds());
		root.addChild(lightD);

		// This second light is added for the Surface Plot, so you
		// can see the "under" surface
		DirectionalLight lightD1 = new DirectionalLight();
		lightD1.setDirection(new Vector3f(0.0f, 0.7f, 0.7f));
		lightD1.setInfluencingBounds(getDefaultBounds());
		root.addChild(lightD1);

		AmbientLight lightA = new AmbientLight();
		lightA.setInfluencingBounds(getDefaultBounds());
		root.addChild(lightA);
	}

	/**
	 * Override to set a different initial transformation
	 */
	protected Transform3D createDefaultOrientation() {
		Transform3D trans = new Transform3D();
		trans.setIdentity();
		trans.rotX(-Math.PI / 2.);
		trans.setTranslation(new Vector3f(0.f, -.4f, 0.f));
		return trans;
	}

	/**
	 * Set the projection policy for the plot - either perspective or projection
	 */
	protected void setProjectionPolicy(SimpleUniverse universe, boolean parallelProjection) {
		View view = universe.getViewer().getView();
		if (parallelProjection)
			view.setProjectionPolicy(View.PARALLEL_PROJECTION);
		else
			view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
	}

	/**
	 * Returns a bounds object that can be used for most behaviours, lighting
	 * models, etc.
	 */
	protected Bounds getDefaultBounds() {
		if (bounds == null) {
			Point3d center = new Point3d(0, 0, 0);
			bounds = new BoundingSphere(center, 10);
		}
		return bounds;
	}

	private Bounds bounds;

}
