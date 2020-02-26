package zebra;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import zebra.model.House;
import zebra.model.ModelHelper;
import zebra.model.NamedObject;
import zebra.model.PosComparator;

public class Database {

	private static final int HOUSES = 5;
	private static char[] ID = new char[] {'A','B','C','D','E'};
	private List<House> houses = new ArrayList<>();

	private List<Aussage> aussagen;

	public Database(List<Aussage> aussagen) {
		this.aussagen = aussagen;
		for (int i=0; i<HOUSES; i++) {
			houses.add(new House(i+1));
		}
	}

	private House findPotentialMatch(House inputHaus) {
		List<House> matches = houses.stream().filter(x -> ModelHelper.potentiallyMatches(x, inputHaus)).collect(Collectors.toList());
		// nicht eindeutig, aber alles freie Häuser
		if (matches.size() > 1 && areAllHousesUninitialized(matches)) {
			return ModelHelper.mergeSafely(inputHaus, matches.get(0));
		}
		// eindeutig
		else if (matches.size() == 1) {
			return ModelHelper.mergeSafely(inputHaus, matches.get(0));
		}
		// nicht eindeutig
		return null;
	}

	public List<House> findPotentialMatches(House input) {
		return houses.stream().filter(x -> ModelHelper.potentiallyMatches(x, input)).collect(Collectors.toList());
	}

	public House findLeftNeighbor(House input) {
		House left = null;
		if (input.getPos() != null) {
			int pos = input.getPos();
			if (pos > 1) {
				left = findPotentialMatch(new House(pos-1));
			}
		}
		return left;
	}

	public House findRightNeighbor(House input) {
		House right = null;
		if (input.getPos() != null) {
			int pos = input.getPos();
			if (pos < HOUSES) {
				right = findPotentialMatch(new House(pos+1));
			}
		}
		return right;
	}

	private boolean areAllHousesUninitialized(List<House> houses) {
		for (House h:houses) {
			if (h.isInitialized()) {
				return false;
			}
		}
		return true;
	}

	public void dump() {
		StringBuilder sb = new StringBuilder();

		houses.sort(new PosComparator());
		for (int i=0; i<houses.size(); i++) {
			String str = String.format("%-20s", "Haus " + ID[i]);
			sb.append(str);
		}
		sb.append("\n");
		// Farbe
		for (House h:houses) {
			sb.append(String.format("%-20s", print(h.getPos())));
		}
		sb.append("\n");
		// Farbe
		for (House h:houses) {
			sb.append(String.format("%-20s", print(h.getFarbe())));
		}
		sb.append("\n");
		// Nation
		for (House h:houses) {
			sb.append(String.format("%-20s", print(h.getNation())));
		}
		sb.append("\n");
		// Tiere
		for (House h:houses) {
			sb.append(String.format("%-20s", print(h.getTier())));
		}
		sb.append("\n");
		// Getraenk
		for (House h:houses) {
			sb.append(String.format("%-20s", print(h.getGetraenk())));
		}
		sb.append("\n");
		// Zigarette
		for (House h:houses) {
			sb.append(String.format("%-20s", print(h.getZigarette())));
		}

		System.out.println(sb.toString());
	}

	private static String print(String s) {
		return s == null ? "-" : s;
	}

	private static String print(NamedObject o) {
		return o == null ? "-" : o.getName();
	}

	private static String print(Integer i) {
		return i == null ? "-" : i.toString();
	}

	public List<Aussage> getAussagen() {
		return this.aussagen;
	}

	public boolean isCorrect() {
		return ModelHelper.isDistinct(houses);
	}

	public List<House> getHouses() {
		return this.houses;
	}
}
