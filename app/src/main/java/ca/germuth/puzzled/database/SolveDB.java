/**
 * SolveDB
 * 
 * This class has a shared respositibilty
 * It is the Solve Object for the local database table, but also the solve object
 * for the remote database. 
 */
package ca.germuth.puzzled.database;

import android.os.Parcel;
import android.os.Parcelable;

public class SolveDB extends ObjectDB implements Parcelable{
	/**
	 * Primary key for this solve in local database
	 */
	private int mLocalId;
	/**
	 * Primary key for this solve in remote database
	 */
	private int mRemoteId;
	/**
	 * Username of user who performed this solve
	 * May not be used in local cases
	 */
	private String mUsername;
	/**
	 * Length of solve in milliseconds
	 */
	private int mDuration;
	/**
	 * Replay in string format
	 */
	private String mReplay;
	/**
	 * Scramble in string format
	 */
	private String mScramble;
	/**
	 * Date and time of solve in milliseconds since 1970
	 */
	private long mDateSolved;
	/**
	 * Whether the solve has been finished, or is still in progress
	 */
	private boolean mIsFinished;
	/**
	 * Reference to PuzzleDB object for this solve if local
	 */
	private PuzzleDB mPuzzle;
	/**
	 * Primary Key in remote database for the puzzle this solve was performed on
	 */
	private int mRemotePuzzleId;
	
	//default constructor
	public SolveDB(){
		this.mLocalId = -1;
		this.mDateSolved = -1;
		this.mDuration = -1;
		this.mIsFinished = true;
		this.mPuzzle = null;
		this.mRemoteId = -1;
		this.mRemotePuzzleId = -1;
		this.mReplay = null;
		this.mScramble = null;
		this.mUsername = null;
	}
	
	/**
	 * Constructor for local solve use
	 * @param duration
	 * @param replay
	 * @param scramble
	 * @param puz
	 * @param dateTime
	 */
	public SolveDB(int duration, String replay, String scramble, PuzzleDB puz, long dateTime, boolean isFinished){
		this.mDuration = duration;
		this.mIsFinished = isFinished;
		this.mReplay = replay;
		this.mScramble = scramble;
		this.mPuzzle = puz;
		this.mDateSolved = dateTime;
		
		this.mLocalId = -1;
		this.mRemoteId = -1;
		this.mRemotePuzzleId = -1;
		this.mUsername = null;
	}
	
	/**
	 * Constructor for remote solve use
	 */
	public SolveDB(String username, int duration, long dateSolved, String scramble, String replay, boolean isFinished, int puz_id){
		this.mUsername = username;
		this.mDateSolved = dateSolved;
		this.mIsFinished = isFinished;
		this.mDuration = duration;
		this.mScramble = scramble;
		this.mReplay = replay;
		this.mRemotePuzzleId = puz_id;
		
		this.mLocalId = -1;
		this.mPuzzle = null;
		this.mRemoteId = -1;

	}
	
	/**
	 * Parcelable constructor
	 * @param in
	 */
	public SolveDB(Parcel in){
		this.mLocalId = in.readInt();
		this.mRemoteId = in.readInt();
		this.mUsername = in.readString();
		this.mDuration = in.readInt();
		this.mIsFinished = in.readInt() == 1 ? true : false;
		this.mReplay = in.readString();
		this.mScramble = in.readString();
		this.mDateSolved = in.readLong();
		this.mPuzzle = in.readParcelable(PuzzleDB.class.getClassLoader());
		this.mRemotePuzzleId = in.readInt();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(mLocalId);
		dest.writeInt(mRemoteId);
		dest.writeString(mUsername);
		dest.writeInt(mDuration);
		dest.writeInt(mIsFinished ? 1 : 0);
		dest.writeString(mReplay);
		dest.writeString(mScramble);
		dest.writeLong(mDateSolved);
		dest.writeParcelable(this.mPuzzle, 0);
		dest.writeInt(mRemotePuzzleId);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SolveDB createFromParcel(Parcel in) {
            return new SolveDB(in); 
        }

        public SolveDB[] newArray(int size) {
            return new SolveDB[size];
        }
    };

	@Override
	public boolean equals(Object o) {
		return mLocalId == ((SolveDB)o).mLocalId;
	}

	public int getLocalId() {
		return mLocalId;
	}

	public void setLocalId(int mLocalId) {
		this.mLocalId = mLocalId;
	}

	public int getRemoteId() {
		return mRemoteId;
	}

	public void setRemoteId(int mRemoteId) {
		this.mRemoteId = mRemoteId;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String mUsername) {
		this.mUsername = mUsername;
	}

	public int getDuration() {
		return mDuration;
	}

	public void setDuration(int mDuration) {
		this.mDuration = mDuration;
	}

	public boolean isFinished() {
		return mIsFinished;
	}

	public void setFinished(boolean mIsFinished) {
		this.mIsFinished = mIsFinished;
	}

	public String getReplay() {
		return mReplay;
	}

	public void setReplay(String mReplay) {
		this.mReplay = mReplay;
	}

	public String getScramble() {
		return mScramble;
	}

	public void setScramble(String mScramble) {
		this.mScramble = mScramble;
	}

	public long getDateSolved() {
		return mDateSolved;
	}

	public void setDateSolved(long mDateSolved) {
		this.mDateSolved = mDateSolved;
	}

	public PuzzleDB getPuzzle() {
		return mPuzzle;
	}

	public void setPuzzle(PuzzleDB mPuzzle) {
		this.mPuzzle = mPuzzle;
	}

	public int getRemotePuzzleId() {
		return mRemotePuzzleId;
	}

	public void setRemotePuzzleId(int mRemotePuzzleId) {
		this.mRemotePuzzleId = mRemotePuzzleId;
	}
}
