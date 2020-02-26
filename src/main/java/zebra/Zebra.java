package zebra;

import java.util.ArrayList;
import java.util.List;

import zebra.model.Getraenk;
import zebra.model.House;
import zebra.model.ModelHelper;
import zebra.model.Nation;
import zebra.model.Tier;
import zebra.model.Zigarette;

/*
 * Es gibt fünf Häuser.
Der Engländer wohnt im roten Haus.
Der Spanier hat einen Hund.
Der Ukrainer trinkt Tee.
Kaffee wird im grünen Haus getrunken.
Das grüne Haus ist direkt links vom weißen Haus.
Der Raucher von Old-Gold-Zigaretten hält Schnecken als Haustiere.
Die Zigaretten der Marke Kools werden im gelben Haus geraucht.
Milch wird im mittleren Haus getrunken.
Der Norweger wohnt im ersten Haus.
Der Mann, der Chesterfields raucht, wohnt neben dem Mann mit dem Fuchs.
Die Marke Kools wird geraucht im Haus neben dem Haus mit dem Pferd.
Der Lucky-Strike-Raucher trinkt am liebsten Orangensaft.
Der Japaner raucht Zigaretten der Marke Parliaments.
Der Norweger wohnt neben dem blauen Haus.


 */
public class Zebra {

	private Database db;
	private long solveCount = 0;
	private boolean solvedCompletely = false;


	public Zebra(Database db) {
		this.db = db;
	}

	public void solve() {
		solvedCompletely = false;
		solve(db.getAussagen());
	}

	public long getSolveCount() {
		return this.solveCount;
	}

	public boolean solve(List<Aussage> as) {
		solveCount++;
		if (solveCount % 1_000_000 == 0) {
			System.out.println(solveCount);
		}
		if (solvedCompletely) {
			return true;
		}

		if (as.size() == 0) {
			return true;
		}

		boolean solved = false;
		for (int i=0; i<as.size(); i++) {
			Aussage a = as.get(i);
			Name name = a.getName();

			if (null == name) {
				solved = handleAssignment(as, solved, i, a);
			}
			else {
				switch(name) {
					case LINKS_VON: solved = handleNextTo(as, solved, i, a, true); break;
					case NEBEN: solved = handleNextTo(as, solved, i, a, false) || handleNextTo(as, solved, i, a, true); break;
					default:
				}
			}
		}
		return solved;
	}

	private boolean handleNextTo(List<Aussage> as, boolean solved, int i, Aussage a, boolean left) {
		if (solvedCompletely) {
			return true;
		}
		House inputA = (House)a.getA();
		House inputB = (House)a.getB();

		List<House> potentialMatches = db.findPotentialMatches(inputA);
		for (House house:potentialMatches) {
			House neighbor = left ? db.findRightNeighbor(house) : db.findLeftNeighbor(house);
			if (neighbor != null && ModelHelper.potentiallyMatches(neighbor, inputB)) {

				House backup = house.copy();
				House backupNeighbor = neighbor.copy();

				ModelHelper.mergeSafely(inputA, house);
				ModelHelper.mergeSafely(inputB, neighbor);

				if (db.isCorrect()) {
					List<Aussage> rest = new ArrayList<>(as);
					rest.remove(i);
					if (solve(rest)) {
						solved = true;
					}
				}
				if (solved) {
					if (as.size() == 1) {
//						System.out.println("Fertig");
//						db.dump();
						solvedCompletely = true;
					}
					break;
				}
				else {
					// overwrite and try next potentialMatch
					ModelHelper.overwrite(backup, house);
					ModelHelper.overwrite(backupNeighbor, neighbor);
				}
			}
		}
		return solved;
	}

	private boolean handleAssignment(List<Aussage> as, boolean solved, int i, Aussage a) {
		House inputA = (House)a.getA();
		List<House> potentialMatches = db.findPotentialMatches(inputA);
		if (potentialMatches.size() == 0) {
			return false;
		}
		for (House house:potentialMatches) {
			House backup = house.copy();

			ModelHelper.mergeSafely(inputA, house);
			if (ModelHelper.existsDelta(backup, house) && db.isCorrect()) {
				List<Aussage> rest = new ArrayList<>(as);
				rest.remove(i);
				if (solve(rest)) {
					solved = true;
				}
			}

			if (solved) {
				if (as.size() == 1) {
//						System.out.println("Fertig");
//						db.dump();
					solvedCompletely = true;
				}
				break;
			}
			else {
				// overwrite and try next potentialMatch
				ModelHelper.overwrite(backup, house);
			}
		}
		return solved;
	}

	public static void main(String[] args) {
		List<Aussage> aussagen = new ArrayList<>();

		aussagen.add(new Aussage(new House("rot", null, new Nation("Englaender"), null, null, null)));
		aussagen.add(new Aussage(new House(null, null, new Nation("Spanier"), new Tier("Hund"), null, null)));
		aussagen.add(new Aussage(new House(null, null, new Nation("Ukrainer"), null, new Getraenk("Tee"), null)));
		aussagen.add(new Aussage(new House("grün", null, null, null, new Getraenk("Kaffee"), null)));
		aussagen.add(new Aussage(new House("grün"), Name.LINKS_VON, new House("weiss")));
		aussagen.add(new Aussage(new House(null, null, null, new Tier("Schnecken"), null, new Zigarette("Old-Gold"))));
		aussagen.add(new Aussage(new House("gelb", null, null, null, null, new Zigarette("Kools"))));
		aussagen.add(new Aussage(new House(null, 3, null, null, new Getraenk("Milch"), null)));
		aussagen.add(new Aussage(new House(null, 1, new Nation("Norweger"), null, null, null)));
		aussagen.add(new Aussage(new House(null, null, null, null, null, new Zigarette("Chesterfield")), Name.NEBEN, new House(null, null, null, new Tier("Fuchs"), null, null)));
		aussagen.add(new Aussage(new House(null, null, null, null, null, new Zigarette("Kools")), Name.NEBEN, new House(null, null, null, new Tier("Pferd"), null, null)));
		aussagen.add(new Aussage(new House(null, null, null, null, new Getraenk("Orangensaft"), new Zigarette("Lucky-Strike"))));
		aussagen.add(new Aussage(new House(null, null, new Nation("Japaner"), null, null, new Zigarette("Parliament"))));
		aussagen.add(new Aussage(new House(null, null, new Nation("Norweger"), null, null, null), Name.NEBEN, new House("blau")));

		// Optimiert
//		aussagen.add(new Aussage(new House(null, 1, new Nation("Norweger"), null, null, null)));
//		aussagen.add(new Aussage(new House(null, 3, null, null, new Getraenk("Milch"), null)));
//		aussagen.add(new Aussage(new House(null, null, new Nation("Norweger"), null, null, null), Name.NEBEN, new House("blau")));
//		aussagen.add(new Aussage(new House("rot", null, new Nation("Englaender"), null, null, null)));
//		aussagen.add(new Aussage(new House(null, null, new Nation("Spanier"), new Tier("Hund"), null, null)));
//		aussagen.add(new Aussage(new House(null, null, new Nation("Ukrainer"), null, new Getraenk("Tee"), null)));
//		aussagen.add(new Aussage(new House("grün", null, null, null, new Getraenk("Kaffee"), null)));
//		aussagen.add(new Aussage(new House("grün"), Name.LINKS_VON, new House("weiss")));
//		aussagen.add(new Aussage(new House(null, null, null, new Tier("Schnecken"), null, new Zigarette("Old-Gold"))));
//		aussagen.add(new Aussage(new House("gelb", null, null, null, null, new Zigarette("Kools"))));
//		aussagen.add(new Aussage(new House(null, null, null, null, null, new Zigarette("Chesterfield")), Name.NEBEN, new House(null, null, null, new Tier("Fuchs"), null, null)));
//		aussagen.add(new Aussage(new House(null, null, null, null, null, new Zigarette("Kools")), Name.NEBEN, new House(null, null, null, new Tier("Pferd"), null, null)));
//		aussagen.add(new Aussage(new House(null, null, null, null, new Getraenk("Orangensaft"), new Zigarette("Lucky-Strike"))));
//		aussagen.add(new Aussage(new House(null, null, new Nation("Japaner"), null, null, new Zigarette("Parliament"))));


		Database db = new Database(aussagen);

		Zebra zebra = new Zebra(db);
		zebra.solve();

		db.dump();

		System.out.println();
		System.out.println(zebra.getSolveCount());
	}
}
