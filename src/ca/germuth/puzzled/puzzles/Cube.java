package ca.germuth.puzzled.puzzles;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Cube implements Puzzle{

	private int width;
	private int height;
	private int depth;
	/**
	 * HashMap from each face of the cube to it's colour in
	 * the solved position
	 */
	private HashMap<CubeFace, Colour> myColours;
	
	private CubeFace top;
	private CubeFace front;
	private CubeFace left;
	private CubeFace right;
	private CubeFace bottom;
	private CubeFace back;
	
	public Cube(int width, int height, int depth){
		assert(width > 0 && height > 0 && depth > 0);
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		
		//make 6 cube faces
		//number of row then column
		//TODO grab saved values here rather than always use defaults
		this.top = new CubeFace(depth, width, new Colour(255, 255, 255, 0));
		this.front = new CubeFace(height, width, new Colour(0, 255, 0, 0));
		this.left = new CubeFace(depth, height, new Colour(255, 128, 0, 0));
		this.right = new CubeFace(depth, height, new Colour(255, 0, 0, 0)); 
		this.bottom = new CubeFace(depth, width, new Colour(255, 255, 0, 0)); 
		this.back = new CubeFace(height, width, new Colour(0, 0, 255, 0));
	}
	
	public class CubeFace implements PuzzleFace{
		private Colour[][] mFace;
		private Colour mSolvedColour;
		
		public CubeFace(int rows, int columns, Colour colour){
			mFace = new Colour[rows][columns];
			mSolvedColour = colour;
			
			setSolved();
		}
		
		public boolean isSolved(){
			for(int i = 0; i < mFace.length; i++){
				for(int j = 0; j < mFace[i].length; j++){
					if( ! this.mFace[i][j].equals( this.mFace[0][0] )){
						return false;
					}
				}
			}
			return true;
		}
		
		public void setSolved(){
			for(int i = 0; i < mFace.length; i++){
				for(int j = 0; j < mFace[i].length; j++){
					mFace[i][j] = mSolvedColour;
				}
			}
		}
	}
	
	@Override
	public boolean isSolved() {
		//check if every colour in top is the same
		//check bottom at the same time
		if( this.top.isSolved() &&
				this.front.isSolved() &&
				this.left.isSolved() &&
				this.right.isSolved() &&
				this.back.isSolved() &&
				this.bottom.isSolved()){
			return true;
		}
		return false;
	}

	@Override
	public void setSolved() {
		this.front.setSolved();
		this.top.setSolved();
		this.back.setSolved();
		this.bottom.setSolved();
		this.left.setSolved();
		this.right.setSolved();
	}

	@Override
	public String scramble(int scramble_length) {
		return "";
	}

	@Override
	public String[] getPrimaryMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getExtraMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Colour[] getColours() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Colour[] setColours() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ChangedTile> getChangedTiles() {
		// TODO Auto-generated method stub
		return null;
	}
}
