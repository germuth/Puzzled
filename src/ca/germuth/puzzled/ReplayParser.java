package ca.germuth.puzzled;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.util.Log;
import ca.germuth.puzzled.ReplayParser.ReplayMove;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.cube.Cube;

/**
 * Replay Parser
 * 
 * replays are a long string stored in the database used to reconstruct a replay for
 * any given solve. Replays are stored in the following format:
 * move  time
 * negative time represents moves made during inspection
 * time is measured in ms
 * for example
 *  y  -14000 
 *  x  -11000 
 *  z  -1000   
 *  R  1235   
 *  U  1600   
 * etc. 
 * 
 */
public class ReplayParser implements Iterable<ReplayMove>{
	private Puzzle mPuzzle;
	private String mString;
	private String mScramble;
	private ArrayList<String> mMoves;
	private ArrayList<Integer> mTimes;
	private ArrayList<PuzzleTurn> mPuzzleMoves;
	
	public ReplayParser(String replay){
		mString = replay;
		mMoves = new ArrayList<String>();
		mTimes = new ArrayList<Integer>();
		
		mPuzzle = new Cube(3, 3, 3);
		mPuzzleMoves = mPuzzle.getAllMoves();
		
		loadReplay();
	}
	
	public void scramble(String scramble){
		this.mScramble = scramble;
		StringTokenizer s = new StringTokenizer(mScramble);
		while(s.hasMoreTokens()){
			String move = s.nextToken();
			boolean found = false;
			for(PuzzleTurn pt: mPuzzleMoves){
				if(pt.getmName().equals(move)){
					PuzzleMoveListener.executePuzzleTurn(mPuzzle, pt);
					found = true;
				}
			}
			
			if(found == false){
				Log.wtf("DEBUG", move + " was not found!");
			}
		}
	}
	
	private void loadReplay(){
		StringTokenizer s = new StringTokenizer(mString);
		while(s.hasMoreTokens()){
			mMoves.add(s.nextToken());
			mTimes.add(Integer.parseInt(s.nextToken()));
		}
	}
	public int getMoveCount(){
		return mMoves.size();
	}

	@Override
	public Iterator<ReplayMove> iterator() {
		return new Iterator<ReplayMove>(){
			private int currentIndex = 0;
			@Override
			public boolean hasNext() {
				return ( currentIndex < mMoves.size());
			}
			@Override
			public ReplayMove next() {
				ReplayMove rm = new ReplayMove(mMoves.get(currentIndex), mTimes.get(currentIndex));
				currentIndex++;
				return rm;
			}
			@Override
			public void remove() {
				// TODO Auto-generated method stub
			}
		};
	}
	
	public Puzzle getmPuzzle() {
		return mPuzzle;
	}

	public void setmPuzzle(Puzzle mPuzzle) {
		this.mPuzzle = mPuzzle;
	}

	public String getmString() {
		return mString;
	}

	public void setmString(String mString) {
		this.mString = mString;
	}

	public String getmScramble() {
		return mScramble;
	}

	public void setmScramble(String mScramble) {
		this.mScramble = mScramble;
	}

	public ArrayList<String> getmMoves() {
		return mMoves;
	}

	public void setmMoves(ArrayList<String> mMoves) {
		this.mMoves = mMoves;
	}

	public ArrayList<Integer> getmTimes() {
		return mTimes;
	}

	public void setmTimes(ArrayList<Integer> mTimes) {
		this.mTimes = mTimes;
	}

	public ArrayList<PuzzleTurn> getmPuzzleMoves() {
		return mPuzzleMoves;
	}

	public void setmPuzzleMoves(ArrayList<PuzzleTurn> mPuzzleMoves) {
		this.mPuzzleMoves = mPuzzleMoves;
	}



	public class ReplayMove{
		private String mMove;
		private int mTime;
		
		public ReplayMove(String m, int t){
			this.mMove = m;
			this.mTime = t;
		}

		public String getMove() {
			return mMove;
		}

		public void setMove(String mMove) {
			this.mMove = mMove;
		}

		public int getTime() {
			return mTime;
		}

		public void setTime(int mTime) {
			this.mTime = mTime;
		}
	}
}
