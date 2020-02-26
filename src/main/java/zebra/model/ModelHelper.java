package zebra.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ModelHelper {

	public static House mergeSafely(House src, House target) {
		if ((src.getFarbe() != null) && (target.getFarbe() == null)) {
			target.setFarbe(src.getFarbe());
		}
		if ((src.getPos() != null) && (target.getPos() == null)) {
			target.setPos(src.getPos());
		}
		if ((src.getNation() != null) && (target.getNation() == null)) {
			target.setNation(src.getNation());
		}
		if ((src.getTier() != null) && (target.getTier() == null)) {
			target.setTier(src.getTier());
		}
		if ((src.getGetraenk() != null) && (target.getGetraenk() == null)) {
			target.setGetraenk(src.getGetraenk());
		}
		if ((src.getZigarette() != null) && (target.getZigarette() == null)) {
			target.setZigarette(src.getZigarette());
		}
		return target;
	}

	public static House overwrite(House src, House target) {
		target.setFarbe(src.getFarbe());
		target.setPos(src.getPos());
		target.setNation(src.getNation());
		target.setTier(src.getTier());
		target.setGetraenk(src.getGetraenk());
		target.setZigarette(src.getZigarette());
		return target;
	}

	public static boolean potentiallyMatches(House a, House b) {
		return potentiallyMatches(a.getFarbe(), b.getFarbe())
			&& potentiallyMatches(a.getPos(), b.getPos())
			&& potentiallyMatches(a.getNation(), b.getNation())
			&& potentiallyMatches(a.getTier(), b.getTier())
			&& potentiallyMatches(a.getGetraenk(), b.getGetraenk())
			&& potentiallyMatches(a.getZigarette(), b.getZigarette());
	}

	public static boolean potentiallyMatches(NamedObject a, NamedObject b) {
		return (a == null && b == null) ||
			(a != null && b == null) ||
			(a == null && b != null) ||
			potentiallyMatches(a.getName(), b.getName());
	}

	public static boolean potentiallyMatches(String a, String b) {
		return (a == null && b == null) ||
			(a != null && b == null) ||
			(a == null && b != null) ||
			a.equals(b);
	}

	public static boolean potentiallyMatches(Integer a, Integer b) {
		return (a == null && b == null) ||
			(a != null && b == null) ||
			(a == null && b != null) ||
			a.equals(b);
	}

	public static boolean equals(NamedObject a, NamedObject b) {
		return a != null && b != null && a.getName() != null && equals(a.getName(), b.getName());
	}

	public static boolean equals(String a, String b) {
		return a != null && b != null && a.equals(b);
	}

	public static boolean equals(Integer a, Integer b) {
		return a != null && b != null && a.equals(b);
	}

	public static boolean isDistinct(List<House> houses) {
		for (int i=0; i<houses.size(); i++) {
			for (int n=i+1; n<houses.size(); n++) {
				if (!areDistinct(houses.get(i), houses.get(n))) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areDistinct(House h1, House h2) {
		return !equals(h1.getNation(), h2.getNation())
			&& !equals(h1.getFarbe(), h2.getFarbe())
			&& !equals(h1.getPos(), h2.getPos())
			&& !equals(h1.getTier(), h2.getTier())
			&& !equals(h1.getZigarette(), h2.getZigarette())
			&& !equals(h1.getGetraenk(), h2.getGetraenk());
	}

	public static boolean existsDelta(House h1, House h2) {
		return existsDelta(h1.getPos(), h2.getPos())
			|| !StringUtils.equals(h1.getFarbe(), h2.getFarbe())
			|| existsDelta(h1.getNation(), h2.getNation())
			|| existsDelta(h1.getTier(), h2.getTier())
			|| existsDelta(h1.getGetraenk(),  h2.getGetraenk())
			|| existsDelta(h1.getZigarette(), h2.getZigarette());
	}

	public static boolean existsDelta(Integer a, Integer b) {
		if (null == a && null == b) {
			return false;
		}
		if ((null == a && null != b) || (null != a && null == b)) {
			return true;
		}
		return !equals(a, b);
	}

	public static boolean existsDelta(NamedObject a, NamedObject b) {
		if (null == a && null == b) {
			return false;
		}
		if ((null == a && null != b) || (null != a && null == b)) {
			return true;
		}
		return !equals(a, b);
	}
}
