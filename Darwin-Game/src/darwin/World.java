package darwin;

/**
 * This class includes the functions necessary to keep track of the creatures in
 * a two-dimensional world. 
 */

public class World {
	private Matrix<Creature> creatures;
	
	/**
	 * This function creates a new world consisting of width columns and height
	 * rows, each of which is numbered beginning at 0. A newly created world
	 * contains no objects.
	 */
	public World(int w, int h) {
		creatures = new Matrix<Creature>(h, w);
	}

	/**
	 * Returns the height of the world.
	 */
	public int height() {
		return creatures.numRows();
	}

	/**
	 * Returns the width of the world.
	 */
	public int width() {
		return creatures.numCols();
	}

	/**
	 * Returns whether pos is in the world or not.
	 * 
	 * returns true *if* pos is an (x,y) location within the bounds of the board.
	 */
	public boolean inRange(Position pos) {
		boolean checkTrue = true;
		if (pos.getX() < 0 || pos.getY() < 0) {
			checkTrue = false;
		}
		if (pos.getX() > (creatures.numCols() - 1) || pos.getY() > (creatures.numRows() - 1)) {
			checkTrue = false;
		}
		return checkTrue; 
	}

	/**
	 * Set a position on the board to contain e.
	 * 
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public void set(Position pos, Creature e) {
		if (inRange(pos)) {
			creatures.set(pos.getY(), pos.getX(), e);
		} else {
			throw new IllegalArgumentException("Position is out of range!");
		}
	}

	/**
	 * Return the contents of a position on the board.
	 * 
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public Creature get(Position pos) {
		if (inRange(pos)) {
			return creatures.get(pos.getY(), pos.getX());
		} else {
			throw new IllegalArgumentException("Position is out of range bro!");
		}
	}
}
