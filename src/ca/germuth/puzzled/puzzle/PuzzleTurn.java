package ca.germuth.puzzled.puzzle;

import java.util.ArrayList;

import ca.germuth.puzzled.openGL.shapes.Shape;

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
<<<<<<< HEAD
=======
	 * A reference to the actual method of puzzle that implements that turn
	 */
	private Method[] mMethod;
	/**
	 * Argument for each method. Assumes each method
	 * can only take one parameter. 
	 */
	private Object[] mArguments;
	/**
>>>>>>> parent of 33c3307... Turn animations completed except rotations
	 * A list of the changed tiles used to animate the correct
	 * pieces for that turn
	 * 
	 * probably accessed from multiple threads
	 */
<<<<<<< HEAD
	private volatile ArrayList<Shape> mChangedTiles;
=======
	private ArrayList<ChangedTile> mChangedTiles;
>>>>>>> parent of 33c3307... Turn animations completed except rotations
	/**
	 * The amount of rotation in radians that the puzzle
	 * turn encompasses. For example, every turn on the 3x3
	 * represents a 90 degree turn.
	 */
	private float mRotation;
	
<<<<<<< HEAD
	public PuzzleTurn(Puzzle p, String name, ArrayList<Shape> sh,
			float rotation, char axis){
=======
	public static Method[] concatenate(Method[] input, int times){
		Method[] triple = new Method[input.length * 3];
		for(int i = 0; i < triple.length; i++){
			triple[i] = input[i % input.length];
		}
		return triple;
	}
	
	public PuzzleTurn(Puzzle p, String name, Method[] method, Object[] args, ArrayList<ChangedTile> changed,
			float rotation){
>>>>>>> parent of 33c3307... Turn animations completed except rotations
		this.mPuzzle = p;
		if(name.startsWith("1")){
			name = name.substring(1);
		}
		this.mName = name;
<<<<<<< HEAD
		this.mChangedTiles = sh;;
		this.mAngle = rotation;
		this.axis = axis;
=======
		this.mMethod = method;
		this.mArguments = args;
		this.mChangedTiles = changed;
		this.mRotation = rotation;
>>>>>>> parent of 33c3307... Turn animations completed except rotations
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
	
<<<<<<< HEAD
	public ArrayList<Shape> getmChangedTiles() {
		return mChangedTiles;
	}

	public void setmChangedTiles(ArrayList<Shape> tiles) {
		this.mChangedTiles = tiles;
=======
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

	public Method[] getmMethod() {
		return mMethod;
	}

	public void setmMethod(Method[] mMethod) {
		this.mMethod = mMethod;
>>>>>>> parent of 33c3307... Turn animations completed except rotations
	}

	public Object[] getmArguments() {
		return mArguments;
	}

	public void setmArguments(Object[] mArguments) {
		this.mArguments = mArguments;
	}

	@Override
	public String toString() {
		return "PuzzleTurn: " + this.mName;
	}
}
