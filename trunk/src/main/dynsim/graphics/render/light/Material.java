package dynsim.graphics.render.light;

public class Material {
	public float ks, kd, kr;

	public float roughness;

	public float[] Id;

	public float[] Is;

	public Material() {
		Id = new float[3];
		Is = new float[3];

		ks = 0.35f;
		kd = 0.45f;
		kr = 0.55f; // reflection coefficient
		roughness = 12;
	}

	public Material(Material mat) {
		Id = mat.Id.clone();
		Is = mat.Is.clone();

		ks = mat.ks;
		kd = mat.kr;
		kr = mat.kr;
		roughness = mat.roughness;
	}

	public void setSpecularColor(float r, float g, float b) {
		Is[0] = r;
		Is[1] = g;
		Is[2] = b;
	}

	public void setDiffuseColor(float r, float g, float b) {
		Id[0] = r;
		Id[1] = g;
		Id[2] = b;
	}

	public void setRoughness(float f) {
		this.roughness = f;
	}
}
