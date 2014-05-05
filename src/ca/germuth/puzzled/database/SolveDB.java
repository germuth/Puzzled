package ca.germuth.puzzled.database;

public class SolveDB {
	
	private int mId;
	private int mDuration;
	private String mReplay;
	private PuzzleDB mPuzzle;
	private int mDateTime;
	
	public SolveDB(int id, int duration, String replay, PuzzleDB puz, int dateTime){
		mId = id;
		mDuration = duration;
		mReplay = replay;
		mPuzzle = puz;
		mDateTime = dateTime;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public int getmDuration() {
		return mDuration;
	}

	public void setmDuration(int mDuration) {
		this.mDuration = mDuration;
	}

	public String getmReplay() {
		return mReplay;
	}

	public void setmReplay(String mReplay) {
		this.mReplay = mReplay;
	}

	public PuzzleDB getmPuzzle() {
		return mPuzzle;
	}

	public void setmPuzzle(PuzzleDB mPuzzle) {
		this.mPuzzle = mPuzzle;
	}

	public int getmDateTime() {
		return mDateTime;
	}

	public void setmDateTime(int mDateTime) {
		this.mDateTime = mDateTime;
	}
	
}
