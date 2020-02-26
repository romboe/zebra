package zebra.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {

	private String farbe;
	private Integer pos;
	private Nation nation;
	private Tier tier;
	private Getraenk getraenk;
	private Zigarette zigarette;

	public House(String farbe) {
		this.farbe = farbe;
	}

	public House(int pos) {
		this.pos = pos;
	}

	public boolean isInitialized() {
		return farbe != null || pos != null || nation != null || tier != null || getraenk != null || zigarette != null;
	}

	public House copy() {
		House c = new House();
		c.setFarbe(this.getFarbe());
		c.setPos(this.getPos() == null ? null : new Integer(this.getPos()));
		c.setNation(this.getNation() == null ? null : new Nation(this.getNation().getName()));
		c.setTier(this.getTier() == null ? null : new Tier(this.getTier().getName()));
		c.setGetraenk(this.getGetraenk() == null ? null : new Getraenk(this.getGetraenk().getName()));
		c.setZigarette(this.getZigarette() == null ? null : new Zigarette(this.getZigarette().getName()));
		return c;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(print(pos)).append(", ");
		sb.append(print(farbe)).append(", ");
		sb.append(print(nation)).append(", ");
		sb.append(print(tier)).append(", ");
		sb.append(print(getraenk)).append(", ");
		sb.append(print(zigarette));
		sb.append("]");
		return sb.toString();
	}

	private String print(NamedObject n) {
		return n == null ? "_" : n.getName();
	}

	private String print(String s) {
		return s == null ? "_" : s;
	}

	private String print(Integer s) {
		return s == null ? "_" : s.toString();
	}
}
