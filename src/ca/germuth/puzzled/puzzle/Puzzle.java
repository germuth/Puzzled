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
	
	public interface OnPuzzleSolvedListener{
		abstract void onPuzzleSolved();
	}
	
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

	abstract void setOnPuzzleSolvedListener(OnPuzzleSolvedListener list);
	abstract OnPuzzleSolvedListener getOnPuzzleSolvedListener();
	
	abstract String getName();
	
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
	 * Returns an array of possible puzzle turn objects. The GUI can use this to
	 * show the correct buttons. OpenGL can also use this to figure out what
	 * pieces to animate turning. For a 3x3, It would consist of a puzzleturn
	 * object for R, R', L, L', D, D', r, r' etc Also Refers to the moves used
	 * to scramble a cube (for now)
	 * 
	 * @return
	 */
	abstract ArrayList<PuzzleTurn> getAllMoves();
}