package dynsim.ui.data;

public class Pair<V> {
	private String name;
	private V value;

	public Pair(String name, V value) {
		this.name = name;
		this.value = value;
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public V getValue() {
		return value;
	}
}
