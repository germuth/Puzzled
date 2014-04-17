package ca.germuth.puzzled.puzzles;

public class Cube implements Puzzle{

	private int width;
	private int height;
	private int depth;
	
	public Cube(int width, int height, int depth){
		assert(width > 0 && height > 0 && depth > 0);
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		
		/**
		 * HOW DO YOU WANT TO DO COULOURS?????
		 */
	}
	/**
	 * 
	 */
	
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
	public void scramble() {
		// TODO Auto-generated method stub
		
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

	

}
