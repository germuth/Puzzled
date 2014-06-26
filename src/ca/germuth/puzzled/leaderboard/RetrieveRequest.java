package ca.germuth.puzzled.leaderboard;

import java.util.ArrayList;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.fragments.LeaderboardListFragment;
import ca.germuth.puzzled.leaderboard.database.LeaderboardDatabase;

public class RetrieveRequest extends LeaderboardRequest {

	private ArrayList<SolveDB> mSolves;
	private LeaderboardListFragment mFrag;
	private RequestType mType;

	public RetrieveRequest(Activity act,
			LeaderboardListFragment frag, RequestType type) {
		super(act, "Retrieve Solves...");
		this.mFrag = frag;
		this.mType = type;
	}

	@Override
	protected Void doInBackground(Void... params) {
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		//TODO make leaderboard support more than 3x3
		mSolves = LeaderboardDatabase.getAllSolves(3, mType);
		db.close();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		this.mFrag.assignSolves(mSolves);
	}

	public void setType(RequestType mType) {
		this.mType = mType;
	}
	
}
