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
	 * Applies X random moves to the puzzle, scrambling it. X is determined
	 * from user settings
	 */
	public abstract String scramble(int scramble_length);
	
	/**
	 * Returns an array of RGBA Colours used for this puzzle
	 * @return
	 */
	public abstract Colour[] getColours();
	
	/**
	 * Sets the RGBA Colours used for this puzzle
	 * @return
	 */
	public abstract Colour[] setColours();
	
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
	 * 
	 * @return
	 */
	public abstract String[] getPrimaryMoves();

	/**
	 * Returns the Extra moves, which will make up the 2nd column (if
	 * applicable) of moves in the main activity. If there are enough moves, it
	 * could also fill up the 3rd and 4th drag-columns
	 * 
	 * @return
	 */
	public abstract String[] getExtraMoves();
}
