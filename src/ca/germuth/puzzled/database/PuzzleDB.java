package ca.germuth.puzzled.database;

public class PuzzleDB {
	private int mId;
	private String mName;
	
	public PuzzleDB(int id, String name){
		this.mId = id;
		this.mName = name;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}
	
}
