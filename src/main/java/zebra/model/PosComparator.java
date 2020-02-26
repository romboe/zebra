package zebra.model;

import java.util.Comparator;

public class PosComparator implements Comparator<House> {

	@Override
	public int compare(House o1, House o2) {
		if (o1.getPos() != null && o2.getPos() != null) {
			if (o1.getPos() > o2.getPos()) {
				return 1;
			}
			else if (o1.getPos() < o2.getPos()) {
				return -1;
			}
		}
		return 0;
	}

}
