package darwin;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This is a very basic test suite for the early stages of a Darwin
 * implementation.
 */
public class AutograderCompTest {

	public AutograderCompTest() {
		System.out.println("Please note that this only tests compatibility, not correctness.");
	}

	/**
	 * We create a world, place a string in a known place, and see if it reads back
	 * as we set it.
	 */
	public void testWorld() {
		try {
			World world = new World(5, 3);
			if (world.height() != 3) {
				System.out.println("World compatability test failed - incorrect height");
				return;
			}
			if (world.width() != 5) {
				System.out.println("World compatability test failed - incorrect width");
				return;
			}
			// test different positions
			Position posNew0 = new Position(4, 2);
			System.out.println(world.get(posNew0));
			System.out.println(world.inRange(posNew0));

			Position posNew1 = new Position(0, 0);
			System.out.println(world.get(posNew1));
			System.out.println(world.inRange(posNew1));
			
			Position posNew2 = new Position(3, 1);
			System.out.println(world.get(posNew2));
			System.out.println(world.inRange(posNew2));

			System.out.println("World compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("World compatibility test failed." + e);
		}
	}

	// these must agree with the data in Hop.txt
	private static final String CREATURE_NAME = "Hop";
	private static final String CREATURE_COLOR = "blue";
	private static final int CREATURE_PGM_SIZE = 2;
	private static final int CREATURE_FIRST_INST = Instruction.HOP;
	private static final int CREATURE_DIR = Position.EAST;

	/**
	 * We test the Species class by reading in a known description and see if it is
	 * correctly instantiated.
	 */
	public void testSpecies() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./Creatures/" + CREATURE_NAME + ".txt"));
			Species s = new Species(in);
			char c = s.getSpeciesChar();

			// test program array (instructions)
			System.out.println(s.programStep(1));
			System.out.println(s.programStep(2));
			System.out.println(s.programToString());
			System.out.println(s.getStep());
			
			
			if (c != CREATURE_NAME.charAt(0)) {
				System.out.println("Species compatibility test failed: incorrect species character");
				return;
			}
			String name = s.getName();
			if (!name.equals(CREATURE_NAME)) {
				System.out.println("Species compatibility test failed: incorrect name readback");
				return;
			}
			String color = s.getColor();
			if (!color.equals(CREATURE_COLOR)) {
				System.out.println("Species compatibility test failed: incorrect color readback");
				return;
			}
			int size = s.programSize();
			if (size != CREATURE_PGM_SIZE) {
				System.out.println("Species compatibility test failed: incorrect program size");
				return;
			}
			Instruction step = s.programStep(1);
			if (step.getOpcode() != CREATURE_FIRST_INST) {
				System.out.println("Species compatibility test failed: incorrect first instruction");
				return;
			}

			System.out.println("Species compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("Species compatibility test failed." + e);
		}
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("./Creatures/" + "Rover" + ".txt"));
			Species s = new Species(in);
			char c = s.getSpeciesChar();

			// test program array (instructions)
			System.out.println(s.programStep(1));
			System.out.println(s.programStep(2));
			System.out.println(s.programToString());
			System.out.println(s.getStep());
			
			
			if (c != "Rover".charAt(0)) {
				System.out.println("Species compatibility test failed: incorrect species character");
				return;
			}
			String name = s.getName();
			if (!name.equals("Rover")) {
				System.out.println("Species compatibility test failed: incorrect name readback");
				return;
			}
			String color = s.getColor();
			if (!color.equals("red")) {
				System.out.println("Species compatibility test failed: incorrect color readback");
				return;
			}
			int size = s.programSize();
			if (size != 12) {
				System.out.println("Species compatibility test failed: incorrect program size");
				return;
			}
			Instruction step = s.programStep(1);
			if (step.getOpcode() != Instruction.IFENEMY) {
				System.out.println("Species compatibility test failed: incorrect first instruction");
				return;
			}

			System.out.println("Extra species compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("Extra species test failed");
		}
	}

	public void testCreature() {
		try {
			// create a world<Creature>
			World world = new World(10, 10);
			WorldMap.createWorldMap(10, 10);

			// read in a Species description
			BufferedReader in = new BufferedReader(new FileReader("./Creatures/" + CREATURE_NAME + ".txt"));
			Species s = new Species(in);

			// create a creature of that species
			Position pos = new Position(1, 1);
			Creature c = new Creature(s, world, pos, CREATURE_DIR);

			// does the Creature.species return the expected value
			Species s1 = c.species();
			if (s1 != s) {
				System.out.println("Creature compatibility test failed: wrong species returned");
				return;
			}

			// does Creature.position return the expected value
			Position pos1 = c.position();
			if (pos1.getX() != pos.getX() || pos1.getY() != pos.getY()) {
				System.out.println("Creature compatibility test failed: wrong pos returned");
				return;
			}

			// does Creature.direction return the expected value
			int dir = c.direction();
			if (dir != CREATURE_DIR) {
				System.out.println("Creature compatibility test failed: wrong direction returned");
				return;
			}

			// manually test get and set world methods of newly instantiated creature
			Position newPos = new Position(3,3);
			world.set(newPos, c);
			WorldMap.displaySquare(newPos, s.getSpeciesChar(), CREATURE_DIR, s.getColor());
			if (!world.get(newPos).equals(c)) {
				System.out.println("World compatibility test failed: wrong creature not returned");
				return;
			}

			// manually go through takeOneTurn of new HOP creature
			// should return opCode of current program step
			for (int i = 0; i < 5; i++) {
				System.out.println(c.getStep());
				c.takeOneTurn();
			}
			System.out.println("Creature compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("Creature compatibility test failed." + e);
			e.printStackTrace();
		}

		try {
			// test separate species to see if creature still works properly
			World world = new World(10, 10);
			WorldMap.createWorldMap(10, 10);
			BufferedReader in = new BufferedReader(new FileReader("./Creatures/" + "Rover" + ".txt"));
			Species s = new Species(in);
			Position pos = new Position(1, 1);
			Creature c = new Creature(s, world, pos, 1);

			// does the Creature.species return the expected value
			Species s1 = c.species();
			if (s1 != s) {
				System.out.println("Creature compatibility test failed: wrong species returned");
				return;
			}

			// does Creature.position return the expected value
			Position pos1 = c.position();
			if (pos1.getX() != pos.getX() || pos1.getY() != pos.getY()) {
				System.out.println("Creature compatibility test failed: wrong pos returned");
				return;
			}

			// does Creature.direction return the expected value
			int dir = c.direction();
			if (dir != CREATURE_DIR) {
				System.out.println("Creature compatibility test failed: wrong direction returned");
				return;
			}

			// manually test get and set world methods of newly instantiated creature
			Position newPos = new Position(3,3);
			world.set(newPos, c);
			WorldMap.displaySquare(newPos, s.getSpeciesChar(), CREATURE_DIR, s.getColor());
			if (!world.get(newPos).equals(c)) {
				System.out.println("World compatibility test failed: wrong creature not returned");
				return;
			}

			// manually go through takeOneTurn of new Rover creature
			// should return opCode of current program step
			for (int i = 0; i < 20; i++) {
				System.out.println(c.getStep());
				c.takeOneTurn();
			}

			System.out.println("Extra creature compatibility test passed.");
		} catch (Exception | Error e) {
			System.out.println("Extra creature test failed");
		}
		
	}

	/**
	 * to test the full program, we invoke the main module with three files, and
	 * expect to see an appropriately populated world.
	 */
	public void testDarwin() {
		try {
			String[] s = new String[4];
			s[1] = "Hop.txt";
			s[0] = "Rover.txt";
			s[2] = "Flytrap.txt";
			s[3] = "Clement_OConnor.txt";

			System.out.println("Darwin compatibility test passed if a well populated board appears w/no errors.");

			Darwin.main(s);
		} catch (Exception | Error e) {
			System.out.println("Darwin compatibility test failed." + e);
		}
	}

	/**
	 * if this module is invoked as an application it runs a set of (very) basic
	 * unit tests.
	 * 
	 * As you add additional tests, it us up to you whether you want to add them
	 * here, or create real JUnit test modules.
	 */
	public static void main(String[] args) {

		// extra test cases added in autograder 
		AutograderCompTest a = new AutograderCompTest();
		a.testWorld();
		a.testSpecies();
		a.testCreature();
		a.testDarwin();
	}
}
