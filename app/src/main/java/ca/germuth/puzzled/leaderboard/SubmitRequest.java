package ca.germuth.puzzled.leaderboard;

import android.app.Activity;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.leaderboard.database.LeaderboardDatabase;

public class SubmitRequest extends LeaderboardRequest {

	public SubmitRequest(Activity act) {
		super(act, "Submitting Solves...");
	}

	@Override
	protected Void doInBackground(Void... params) {
		PuzzledDatabase db = new PuzzledDatabase(mActivity);
		SolveDB solDB = db.getLastSolve();
		SolveDB sol = new SolveDB(USERNAME, solDB.getDuration(),
				solDB.getDateSolved(), solDB.getScramble(), solDB.getReplay(),
				solDB.getPuzzle().getmId());
		// need to look up the SolveDB puzzle_id and get the puzzle name
		// puzzle names are consistent local -> remote database, but not
		// puzzle_ids
		String puzzleName = db.getPuzzleName(solDB);
		LeaderboardDatabase.submitSolve(sol, puzzleName);
		db.close();
		return null;
	}

}
