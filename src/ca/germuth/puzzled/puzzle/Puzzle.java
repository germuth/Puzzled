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

	abstract ArrayList<Shape> drawPuzzle();

	// abstract int getLayout();
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
	 * Returns a list of the changed tiles that have changed in the last
	 * rotation performed on the cube. This is used to know which pieces to
	 * animate turning
	 */
	abstract ArrayList<ChangedTile> getChangedTiles();

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
