package ca.germuth.puzzled.puzzle;

import java.util.ArrayList;

import ca.germuth.puzzled.openGL.shapes.Shape;

/**
 * Puzzle
 * 
 * Each Implementing class should also have a static method
 * 
 * @author germuth
 */
public interface Puzzle {

	/**
	 * Checks whether the puzzle is currently in the solved state
	 * 
	 * @return
	 */
	abstract boolean isSolved();

	/**
	 * Sets the current puzzle to be in the solved position
	 */
	abstract void setSolved();

	/**
	 * Returns all of the changed tiles since the last call
	 * to moveFinished(); 
	 * @return
	 */
	abstract ArrayList<Tile> getChangedTiles();

	/**
	 * Tells the cube that the current move is finished and to clear 
	 * its current record of stored changed tiles
	 */
	abstract void moveFinished();
	
	/**
	 * Returns an arraylist of shapes for drawing this puzzle in openGL
	 * @return
	 */
	abstract ArrayList<Shape> createPuzzleModel();
	
	/**
	 * Given a tile of the puzzle, returns the openGL shape for 
	 * that tile.
	 * This method can't be used until createPuzzleModel() is called
	 * @param colour
	 * @return
	 */
	abstract Shape getTileFor(Tile colour);

	/**
	 * Returns an array of possible puzzle turn objects. The GUI can use this to
	 * show the correct buttons. OpenGL can also use this to figure out what
	 * pieces to animate turning. For a 3x3, It would consist of a puzzleturn
	 * object for R, R', L, L', D, D', r, r' etc Also Refers to the moves used
	 * to scramble a cube (for now)
	 * 
	 * @return
	 */
	abstract ArrayList<PuzzleTurn> getAllMoves();

	/**
	 * Returns a puzzleturn object for each rotation move, such as x, x', y, and
	 * z for a 3x3
	 * 
	 * @return
	 */
	abstract ArrayList<PuzzleTurn> getAllRotationMoves();
}
