package ca.germuth.puzzled.puzzle;

import java.util.ArrayList;

import ca.germuth.puzzled.openGL.shapes.Shape;

/**
 * PuzzleTurn
 * 
 * TODO: kind of done weird where these objects are 
 * implied to be static and a puzzle has a defined set of 
 * them, but really they are changed all over (mCHangedTiles)
 * at runtime 
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
	 * A list of the changed tiles used to animate the correct
	 * pieces for that turn
	 * 
	 * probably accessed from multiple threads
	 */
	private volatile ArrayList<Shape> mChangedTiles;
	/**
	 * The amount of rotation in radians that the puzzle
	 * turn encompasses. For example, every turn on the 3x3
	 * represents a 90 degree turn.
	 */
	private float mAngle;
	/**
	 * Axis for turn
	 */
	private char axis;
	
	public PuzzleTurn(Puzzle p, String name, ArrayList<Shape> sh,
			float rotation, char axis){
		this.mPuzzle = p;
		if(name.startsWith("1")){
			name = name.substring(1);
		}
		this.mName = name;
		this.mChangedTiles = sh;;
		this.mAngle = rotation;
		this.axis = axis;
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
	
	public ArrayList<Shape> getmChangedTiles() {
		return mChangedTiles;
	}

	public void setmChangedTiles(ArrayList<Shape> tiles) {
		this.mChangedTiles = tiles;
	}

	public float getmAngle() {
		return mAngle;
	}

	public void setmAngle(float mAngle) {
		this.mAngle = mAngle;
	}

	@Override
	public String toString() {
		return "PuzzleTurn: " + this.mName;
	}

	public char getAxis() {
		return axis;
	}

	public void setAxis(char axis) {
		this.axis = axis;
	}
	
	
}
