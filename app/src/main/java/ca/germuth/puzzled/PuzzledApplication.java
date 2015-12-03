package ca.germuth.puzzled;

import android.app.Application;

import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.puzzle.Puzzle;

public class PuzzledApplication extends Application{
	private Puzzle mPuzzle;
	private SolveDB mSolve; //used for resuming previous solve

	public Puzzle getPuzzle() {
		return mPuzzle;
	}

	public void setPuzzle(Puzzle mPuzzle) {
		this.mPuzzle = mPuzzle;
	}

	public SolveDB getSolve() {
		return mSolve;
	}

	public void setSolve(SolveDB mSolve) {
		this.mSolve = mSolve;
	}
}
