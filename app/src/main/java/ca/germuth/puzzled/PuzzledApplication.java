package ca.germuth.puzzled;

import android.app.Application;
import ca.germuth.puzzled.puzzle.Puzzle;

public class PuzzledApplication extends Application{
	private Puzzle mPuzzle;

	public Puzzle getPuzzle() {
		return mPuzzle;
	}

	public void setPuzzle(Puzzle mPuzzle) {
		this.mPuzzle = mPuzzle;
	}
}
