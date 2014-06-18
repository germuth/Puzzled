package ca.germuth.puzzled;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.leaderboard.LeaderboardRequest;
import ca.germuth.puzzled.leaderboard.Solve;


public class LeaderboardActivity extends Activity{
	//TODO move to settings
	public static final String USERNAME = "Germoose";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
		
		final LinearLayout list = (LinearLayout) this.findViewById(R.id.leaderboard_list);
		Button btn = (Button) this.findViewById(R.id.leaderboard_button);
		btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				new Thread(new Runnable(){
					public void run(){
						PuzzledDatabase db = new PuzzledDatabase(LeaderboardActivity.this);
						SolveDB solDB = db.getLastSolve();
						Solve sol = new Solve(USERNAME, solDB.getmDuration(), solDB.getmDateTime(), 
								solDB.getmScramble(), solDB.getmReplay(), solDB.getmPuzzle().getmId());
						//need to look up the SolveDB puzzle_id and get the puzzle name
						//puzzle names are consistent local -> remote database, but not puzzle_ids
						String puzzleName = db.getPuzzleName(solDB);
						LeaderboardRequest.submitSolve(sol, puzzleName);
					}
				}).start();
			}
		});
		
		new AsyncTask<Void, Void, Void>(){
			private ArrayList<Solve> solves;
			@Override
			protected Void doInBackground(Void... params) {
				PuzzledDatabase db = new PuzzledDatabase(LeaderboardActivity.this);
				
				solves = LeaderboardRequest.getAllSolves(3);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				for(Solve solve: solves){
					TextView v = new TextView(LeaderboardActivity.this);
					v.setText(solve.getUsername() + ": " + solve.getDuration());
					list.addView(v);
				}
			}
		}.execute((Void[])null);
	}
}
