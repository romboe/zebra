package zebra;

import lombok.Data;

@Data
public class Aussage {

	private Name name;
	private Object a;
	private Object b;

	public Aussage(Object a) {
		this(a, null, null);
	}

	public Aussage(Object a, Name name, Object b) {
		this.name = name;
		this.a = a;
		this.b = b;
	}
}
