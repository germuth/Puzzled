package ca.germuth.puzzled.puzzles;

import java.util.ArrayList;

/**
 * Puzzle
 * 
 * @author germuth
 */
public interface Puzzle {

	// public abstract OpenGLFace getModel();

	/**
	 * Checks whether the puzzle is currently in in the 
	 * solved state
	 * @return
	 */
	public abstract boolean isSolved();
	
	/**
	 * Sets the current puzzle to be in the solved position
	 */
	public abstract void setSolved();
	
	/**
	 * Returns a list of the changed tiles that have changed in
	 * the last rotation performed on the cube. This is used 
	 * to know which pieces to animate turning
	 */
	public abstract ArrayList<ChangedTile> getChangedTiles();
	
	/**
	 * Returns an array of strings, denoting the names for each operation that
	 * can be performed on that puzzle. For example, [ R, R', L, L' ] for a 3x3
	 * rubik's cube Primary Moves refers to the 10 most basic moves, making up
	 * the most inner column of buttons in the main activity
	 * Also Refers to the moves used to scramble a cube (for now)
	 * @return
	 */
	public abstract ArrayList<PuzzleTurn> getPrimaryMoves();

	/**
	 * Returns the Extra moves, which will make up the 2nd column (if
	 * applicable) of moves in the main activity. If there are enough moves, it
	 * could also fill up the 3rd and 4th drag-columns
	 * 
	 * @return
	 */
	public abstract ArrayList<PuzzleTurn> getExtraMoves();
	
	/**
	 * Returns a puzzleturn object for each rotation move, such as
	 * x, x', y, and z for a 3x3
	 * @return
	 */
	public abstract ArrayList<PuzzleTurn> getRotationMoves();
}
