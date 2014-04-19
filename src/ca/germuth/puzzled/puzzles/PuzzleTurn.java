package ca.germuth.puzzled.puzzles;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * PuzzleTurn
 * 
 * This class represents one turn of a twisty puzzle. 
 * It has the name of the method, a reference to the 
 * method, the list of changed tiles by that method,
 * and the amount of rotation needed with that turn. 
 * @author Germuth
 *
 */
public class PuzzleTurn {
	private Puzzle mPuzzle;
	/**
	 * The name of the turn. For example, on a 3x3, this
	 * would be R', L, or F
	 */
	private String mName;
	/**
	 * A reference to the actual method of puzzle. This could be
	 * puzzle.RTurn(), puzzle.FTurn(), etc
	 */
	private Method mMethod;
	/**
	 * A list of the changed tiles used to animate the correct
	 * pieces for that turn
	 */
	private ArrayList<ChangedTile> mChangedTiles;
	/**
	 * The amount of rotation in radians that the puzzle
	 * turn encompasses. For example, every turn on the 3x3
	 * represents a 90 degree turn.
	 */
	private float mRotation;
	
	public PuzzleTurn(Puzzle p, String name, Method method, ArrayList<ChangedTile> changed,
			float rotation){
		this.mPuzzle = p;
		this.mName = name;
		this.mMethod = method;
		this.mChangedTiles = changed;
		this.mRotation = rotation;
	}

	public Puzzle getmPuzzle() {
		return mPuzzle;
	}

	public void setmPuzzle(Puzzle mPuzzle) {
		this.mPuzzle = mPuzzle;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Method getmMethod() {
		return mMethod;
	}

	public void setmMethod(Method mMethod) {
		this.mMethod = mMethod;
	}

	public ArrayList<ChangedTile> getmChangedTiles() {
		return mChangedTiles;
	}

	public void setmChangedTiles(ArrayList<ChangedTile> mChangedTiles) {
		this.mChangedTiles = mChangedTiles;
	}

	public float getmRotation() {
		return mRotation;
	}

	public void setmRotation(float mRotation) {
		this.mRotation = mRotation;
	}
}
