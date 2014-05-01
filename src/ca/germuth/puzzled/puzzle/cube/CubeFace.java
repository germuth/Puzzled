package ca.germuth.puzzled.puzzle.cube;

<<<<<<< HEAD
import ca.germuth.puzzled.openGL.shapes.Shape;
import ca.germuth.puzzled.puzzle.PuzzleFace;

=======
import ca.germuth.puzzled.puzzle.Tile;
import ca.germuth.puzzled.puzzle.PuzzleFace;
>>>>>>> parent of 33c3307... Turn animations completed except rotations

public class CubeFace implements PuzzleFace{
	protected Shape[][] mFace;
	
	public CubeFace(int rows, int columns, Shape[][] sh){
		mFace = sh;
		
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
<<<<<<< HEAD
//				mFace[i][j] = new Tile(mSolvedColour.getRed(),
//				mSolvedColour.getGreen(), mSolvedColour.getBlue(),
//				mSolvedColour.getAlpha());
=======
				mFace[i][j] = mSolvedColour;
>>>>>>> parent of 33c3307... Turn animations completed except rotations
			}
		}
	}
}
