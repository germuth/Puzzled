package ca.germuth.puzzled.puzzle.cube;

import ca.germuth.puzzled.puzzle.Colour;
import ca.germuth.puzzled.puzzle.PuzzleFace;

public class CubeFace implements PuzzleFace{
	protected Colour[][] mFace;
	protected Colour mSolvedColour;
	
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
