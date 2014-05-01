package ca.germuth.puzzled.puzzle;

public class ChangedTile {
	private Puzzle mPuzzle;
	private PuzzleFace mFace;
	//TODO current implementation assumes
	//that every puzzle face will be a 2D array
	//and therefore have a row and column index
	private int mRow;
	private int mColumn;
	
	public ChangedTile(Puzzle p, PuzzleFace pf, int row, int column){
		this.mPuzzle = p;
		this.mFace = pf;
		this.mRow = row;
		this.mColumn = column;
	}

	public Puzzle getmPuzzle() {
		return mPuzzle;
	}

	public void setmPuzzle(Puzzle mPuzzle) {
		this.mPuzzle = mPuzzle;
	}

	public PuzzleFace getmFace() {
		return mFace;
	}

	public void setmFace(PuzzleFace mFace) {
		this.mFace = mFace;
	}

	public int getmRow() {
		return mRow;
	}

	public void setmRow(int mRow) {
		this.mRow = mRow;
	}

	public int getmColumn() {
		return mColumn;
	}

	public void setmColumn(int mColumn) {
		this.mColumn = mColumn;
	}
}
