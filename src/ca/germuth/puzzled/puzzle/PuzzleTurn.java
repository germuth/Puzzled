package ca.germuth.puzzled.puzzle;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
	 * A reference to the actual method of puzzle that implements that turn
	 */
	private Method[] mMethods;
	/**
	 * Argument for each method. Assumes each method
	 * can only take one parameter. 
	 */
	private Object[] mArguments;
	/**
	 * A list of the changed tiles used to animate the correct
	 * pieces for that turn
	 */
	private ArrayList<Tile> mChangedTiles;
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
	
	public static Method[] concatenate(Method[] input, int times){
		Method[] triple = new Method[input.length * 3];
		for(int i = 0; i < triple.length; i++){
			triple[i] = input[i % input.length];
		}
		return triple;
	}
	
	public static Object[] concatenate(Object[] input, int times){
		Object[] triple = new Object[input.length * 3];
		for(int i = 0; i < triple.length; i++){
			triple[i] = input[i % input.length];
		}
		return triple;
	}
	
	public PuzzleTurn(Puzzle p, String name, Method[] methods, Object[] args,
			float rotation, char axis){
		this.mPuzzle = p;
		if(name.startsWith("1")){
			name = name.substring(1);
		}
		this.mName = name;
		this.mMethods = methods;
		this.mArguments = args;
		this.mChangedTiles = new ArrayList<Tile>();
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
	
	public ArrayList<Tile> getmChangedTiles() {
		return mChangedTiles;
	}

	public void setmChangedTiles(ArrayList<Tile> tiles) {
		for(int i = 0; i < tiles.size(); i++){
			this.mChangedTiles.add(tiles.get(i));
		}
	}

	public Method[] getMethods() {
		return mMethods;
	}

	public void setMethod(Method[] mMethod) {
		this.mMethods = mMethod;
	}

	public Object[] getmArguments() {
		return mArguments;
	}

	public void setmArguments(Object[] mArguments) {
		this.mArguments = mArguments;
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
