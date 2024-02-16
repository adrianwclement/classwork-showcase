package darwin;

import java.util.*;

/**
 * This class represents one creature on the board. Each creature must remember
 * its species, position, direction, and the world in which it is living.
 * In addition, the Creature must remember the next instruction out of its
 * program to execute.
 * The creature is also responsible for making itself appear in the WorldMap. In
 * fact, you should only update the WorldMap from inside the Creature class.
 */
public class Creature {
	private Species species;
	private World world;
	private Position pos;
	private int dir;
	private int step;
	/**
	 * Create a creature of the given species, with the indicated position and
	 * direction. Note that we also pass in the world-- remember this world, so
	 * that you can check what is in front of the creature and update the board
	 * when the creature moves.
	 * @param species # species of creature
	 * @param world # world to be placed in
	 * @param pos # position in world
	 * @param dir # direction facing
	 */
	public Creature(Species species, World world, Position pos, int dir) {
		this.species = species;
		this.world = world;
		this.pos = pos;
		this.dir = dir;

		// keeps track of the current program step
		this.step = 0;

		// controls backend position code of creature in the world
		world.set(pos, this);

		// update UI so we can see new creature
		WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());
	}

	/**
	 * Return the species of the creature.
	 */
	public Species species() {
		return species;
	}

	/**
	 * Return the current direction of the creature.
	 */
	public int direction() {
		return dir;
	}

	/**
	 * Sets the current direction of the creature to the given value 
	 */
	public void setDirection(int dir){
		this.dir = dir;
	}

	/**
	 * Return the position of the creature.
	 */
	public Position position() {
		return pos;
	}

	/**
	 * Return the current step of the creature's program
	 */
	public int getStep() {
		return step;
	}

	/**
	 * Execute steps from the Creature's program
	 *   starting at step #1
	 *   continue until a hop, left, right, or infect instruction is executed.
	 */
	public void takeOneTurn() {
		// ends loop when a hop, left, right, or infect instruction is executed
		while (true) {

			// to call when when adjacent square in world must be found
			Position posAdj = pos.getAdjacent(dir);

			// add 1 to step count each loop
			step++;

			switch (species.programStep(step).getOpcode()) {
				// hop 
				// move forward 1
				case 1:
					// update position to one in front
					Position posNew = posAdj;

					// check if in world
					if (world.inRange(posNew)) {

						// check if no creature in way
						if (world.get(posAdj) == null) {
							
							// update graphics and position
							world.set(pos, null);
							WorldMap.displaySquare(pos, ' ', dir, species.getColor());
	
							world.set(posNew, this);
							WorldMap.displaySquare(posNew, species.getSpeciesChar(), dir, species.getColor());

							this.pos = posNew;
						}
					}
					return;
				// left 
				// left turn
				case 2:
					// test if direction is cycling back to west or increasing by 1, update depending on result
					int newDir1;
					if (dir == 0) {
						newDir1 = 3;
						dir = newDir1;
					} else {
						newDir1 = dir - 1;
						dir = newDir1;
					}

					// update graphics
					WorldMap.displaySquare(pos, species.getSpeciesChar(), newDir1, species.getColor());
					return;
				// right 
				// right turn
				case 3:
					// test if directino is cycling back to north or increasing by 1, update depending on result
					int newDir2;
					if (dir == 3) {
						newDir2 = 0;
						dir = newDir2;
					} else {
						newDir2 = dir + 1;
						dir = newDir2;
					}

					// update graphics
					WorldMap.displaySquare(pos, species.getSpeciesChar(), newDir2, species.getColor());
					return;
				// infect 
				// infext creature on adjacent square if creature is different species
				case 4:
					// check if square in front is populated and an enemy
					if (world.inRange(posAdj)) {
						if (world.get(posAdj) != null) {

							// creates temp var to keep track of creature on adjacent square
							Creature tempCreature = world.get(posAdj);
							tempCreature.species = this.species;
							world.set(posAdj, null);
							WorldMap.displaySquare(posAdj, ' ', dir, species.getColor());

							// go into displaysqaure and fill in information for newly infected creature
							world.set(posAdj, tempCreature);
							WorldMap.displaySquare(posAdj, tempCreature.species.getSpeciesChar(), tempCreature.direction(), tempCreature.species.getColor());
							tempCreature.step = 1;
						}
					}
					return;
				// ifempty
				// checks to see if adjacent square contains a creature
				case 5:
					// check if world tile exists in front
					if (world.inRange(posAdj)) {

						// move to desired instruction step if no creature is in tile
						if (world.get(posAdj) == null) {
							step = this.species.programStep(step).getAddress();
						}
					}
					break;
				// ifwall
				// checks to see if adjacent square is within the world range
				case 6:
					// check if world tile exists in front
					if (!(world.inRange(posAdj))) {
						// move to desired instruction step if no creature is in tile
						step = this.species.programStep(step).getAddress() - 1;
					}
					break;
				// ifsame
				// checks to see if adjacent square is the same species
				case 7:
					// check if world is in range
					if (world.inRange(posAdj)) {
						
						// check if position in front contains creature
						if (world.get(posAdj) != null) {

							// check if creature is same, execute desired instruction
							if ((world.get(posAdj).species.getSpeciesChar()) == (this.species.getSpeciesChar())) {
								step = this.species.programStep(step).getAddress() - 1;
							}
						}
					}
					break;
				// ifenemy
				// checks to see if adjacent square is an enemy
				case 8:
					// check if world within 2 spots is in range
					if (world.inRange(posAdj)) {

						// check if position in front 2 spots is not empty
						if (world.get(posAdj) != null) {

							// if creature is not same, move to desired instruction step
							if (world.get(posAdj).species.getSpeciesChar() != this.species.getSpeciesChar()) {
								step = this.species.programStep(step).getAddress() - 1;
							}
						}
					}
					break;
				// ifrandom
				// completes opCode instruction or goes to next line at a 50% probability
				case 9:
					// acquire a random num
					Random rand = new Random();
					int randNum = rand.nextInt();

					// if true, move to desired instruction step
					if (randNum % 2 == 0) {
						step = this.species.programStep(step).getAddress() - 1;
					}
					break;
				// go
				// goes back to instruction n
				case 10:
					// go to desired instruction step
					step = this.species.programStep(step).getAddress() - 1;
					break;
				// if2enemy
				// checks if an enemy is two steps in front of a creature
				case 11:
					if (world.inRange(posAdj.getAdjacent(dir))) {

						// check if position 2 spaces in front is not empty
						if (world.get(posAdj.getAdjacent(dir)) != null) {

							// if creature is not same, move to desired instruction step
							if (world.get(posAdj).species.getSpeciesChar() != this.species.getSpeciesChar()) {
							step = this.species.programStep(step).getAddress() - 1;
							}
						}
					}
					break;
				
				// alert when a new case arises
				default:
            		throw new IllegalArgumentException("This type is not yet supported.");
			}
		}
	}


	/**
	 * Return the compass direction the is 90 degrees left of the one passed in.
	 */
	public static int leftFrom(int direction) {
		return (4 + direction - 1) % 4;
	}

	/**
	 * Return the compass direction the is 90 degrees right of the one passed
	 * in.
	 */
	public static int rightFrom(int direction) {
		return (direction + 1) % 4;
	}

// main function test cases located in AutograderCompTest.java
}
