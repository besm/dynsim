/**
 * 
 */
package dynsim.graphics.render.util;

/**
 * @author maf83
 * 
 */
public class FloatRange {
	private float min, max;

	public FloatRange(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public String toString() {
		return "From " + min + " to " + max;
	}

}
