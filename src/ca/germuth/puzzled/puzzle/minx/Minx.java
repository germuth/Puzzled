package ca.germuth.puzzled.puzzle.minx;

import java.util.ArrayList;

import ca.germuth.puzzled.R;
import ca.germuth.puzzled.openGL.shapes.Shape;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.Tile;

public class Minx implements Puzzle{
	
//	private static final int myLayout = R.layout.puzzle_cube;
	
	public static int getLayout() {
//		return myLayout;
		return 0;
	}
	//TODO add megaminx layout
	//private static final int myLayout = R.layout.puzzle_cube;
	
	/**
	 * 1 - normal megaminx
	 * 2 - gigaminx
	 * 3 - teraminx
	 * 4 - petaminb
	 * etc. etc.
	 */
	private int size;
	
	private MinxFace U;
	private MinxFace L;
	private MinxFace R;
	private MinxFace F;
	private MinxFace DL;
	private MinxFace DR;
	private MinxFace UR;
	private MinxFace UL;
	private MinxFace D;
	private MinxFace BL;
	private MinxFace BR;
	private MinxFace B;
	
	public static final String[] constructor_param_titles = {"Size"};
	
	public Minx(int size){
		this.size = size;
	}
	
	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSolved() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ArrayList<PuzzleTurn> getAllMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Shape> createPuzzleModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Tile> getChangedTiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOnPuzzleSolvedListener(OnPuzzleSolvedListener list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OnPuzzleSolvedListener getOnPuzzleSolvedListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkSolved() {
		// TODO Auto-generated method stub
		
	}
}
