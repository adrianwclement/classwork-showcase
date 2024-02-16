package darwin;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * This class controls the simulation. The design is entirely up to you. You
 * should include a main method that takes the array of species file names
 * passed in and populates a world with species of each type. You class should
 * be able to support anywhere between 1 to 4 species.
 * 
 * Be sure to call the WorldMap.pause() method every time through the main
 * simulation loop or else the simulation will be too fast. For example:
 * 
 * 
 * public void simulate() { 
 * 	for (int rounds = 0; rounds < numRounds; rounds++) {
 * 		giveEachCreatureOneTurn(); 
 * 		WorldMap.pause(500); 
 * 	} 
 * }
 * 
 */
class Darwin {
	// initialize different var and objects
	private World world;
	private int numSpecies;
	private int numCreatures;
	private ArrayList<Species> speciesArray;
	private ArrayList<Creature> creatureList;

	/**
	 * creates new darwin world based on given size, populating world with creatures based on
	 * number of species and creatures hard coded in and species filenames added,
	 * runs game for a given number of turns
	 * @param speciesFilenames # list of species filenames for Buffered Reader to analyze
	 * 
	 * @throws Exception # when buffered reader reads incorrectly formatted files
	 */
	public Darwin(String[] speciesFilenames) {
		// instantiate new world of creatures and assign number of species and number of creatures
		this.world = new World(10,10);
		this.numSpecies = 4;
		this.numCreatures = 5;

		// create species array with unique species
		this.speciesArray = new ArrayList<>(numSpecies);

		// create creature array with desired num of creatures per species
		this.creatureList = new ArrayList<>(numCreatures);
		
		// input all 4 species into the ArrayList
		for (int i = 0; i < speciesFilenames.length; i++) {
			try {
				// initializes all desired species for Darwin game
				speciesArray.add(new Species(new BufferedReader(new FileReader("./Creatures/" + speciesFilenames[i]))));
			} catch (Exception e) {
				System.err.println("Exception while reading " + speciesFilenames[i]);
			}
		}
	}

	/**
	 * The array passed into main will include the arguments that appeared on the
	 * command line. For example, running "java Darwin Hop.txt Rover.txt" will call
	 * the main method with s being an array of two strings: "Hop.txt" and
	 * "Rover.txt".
	 * 
	 * The autograder will always call the full path to the creature files, for
	 * example "java Darwin /home/user/Desktop/Assignment02/Creatures/Hop.txt" So
	 * please keep all your creates in the Creatures in the supplied
	 * Darwin/Creatures folder.
	 *
	 * To run your code you can either: supply command line arguments through
	 * VS code (add a launch.json file in the folder .vscode according to instructions and add/edit the following attribute
     * `"args": ["whateverArgumentsYouWantSeparatedByComma"]`) or by creating a temporary array
	 * with the filenames and passing it to the Darwin constructor. If you choose
	 * the latter options, make sure to change the code back to: Darwin d = new
	 * Darwin(s); before submitting. If you want to use relative filenames for the
	 * creatures they should be of the form "./Creatures/Hop.txt".
	 */
	/**
	 * 
	 * @param numCreatures # of creatures to initialize
	 * @param numSpecies # number of species to instantiate
	 * @param world # world bounds and all necessary components to track creatures on board
	 */
	public ArrayList<Creature> initializeCreatures(int numCreatures, int numSpecies, World world) {
		// allocates a given number of each creature to each species to initialize
		for (int s = 0; s < numSpecies; s++) {
			for (int i = 0; i < numCreatures; i++) {

				// checks if position on board is already taken by a creature
				boolean posFound = false;
				while (!posFound) {
					
					// assign new random position
					Random random = new Random();
					int randomX = random.nextInt(10);
					int randomY = random.nextInt(10);
					Position newPos = new Position(randomY, randomX);
					int randomDir = random.nextInt(4);

					// add new anonymous creature data to backend arraylist
					if (world.get(newPos) == null) {
						creatureList.add(new Creature(speciesArray.get(s), world, newPos, randomDir));
						posFound = true;
					}
				}
			}
		}
		return creatureList;
	}
	/**
	 * simulates each turn in darwin and initializes all creates
	 */
	public void simulate() {

		ArrayList<Creature> creatureList = new ArrayList<>(initializeCreatures(numCreatures, numSpecies, world));

		// tells each creature to take turns (change for desired num of turns)
		for (int i = 0; i < 400; i++) {
			WorldMap.pause(500);

			// for each itme in the creatureList, give each a turn
			for (int j = 0; j < creatureList.size(); j++) {
				creatureList.get(j).takeOneTurn();
				}
		}
	}

	public static void main(String s[]) {
		WorldMap.createWorldMap(10, 10);
		Darwin d = new Darwin(s);
		d.simulate();
	}
}

