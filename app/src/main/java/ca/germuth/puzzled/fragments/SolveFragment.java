package ca.germuth.puzzled.fragments;

import net.peterkuterna.android.apps.swipeytabs.SwipeyTabFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import ca.germuth.puzzled.GameActivity;
import ca.germuth.puzzled.GameActivityType;
import ca.germuth.puzzled.PuzzledApplication;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.StatisticsPanel;
import ca.germuth.puzzled.leaderboard.database.LeaderboardDatabase;
import ca.germuth.puzzled.puzzle.cube.Cube;
import ca.germuth.puzzled.statistics.graph.GraphStatisticsTask;
import ca.germuth.puzzled.statistics.graph.MoveDistribution;
import ca.germuth.puzzled.statistics.graph.TimeDistribution;
import ca.germuth.puzzled.statistics.graph.TurnsPerSecond;
import ca.germuth.puzzled.statistics.text.MoveCount;
import ca.germuth.puzzled.statistics.text.Percentile;
import ca.germuth.puzzled.statistics.text.SolveNumber;
import ca.germuth.puzzled.statistics.text.TextStatisticsTask;
import ca.germuth.puzzled.util.Utils;
//add guage for turns per second
//graph for turns per second overtime

//global turn per second gauge
//ETM meaning rotations are included
public class SolveFragment extends SwipeyTabFragment{
	public interface OnResetListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onReset();
    }
	private static final String name = "SOLVE";
	
	private SolveDB mSolve;
	private SolveType mType;
	private OnResetListener mResetListener;
	
	public static final SolveFragment newInstance(SolveDB solve, SolveType type)
	{
	    SolveFragment fragment = new SolveFragment();
	    Bundle bundle = new Bundle(1);
	    bundle.putParcelable("solve", solve);
	    bundle.putParcelable("type", type);
	    fragment.setArguments(bundle);
	    return fragment ;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (activity instanceof OnResetListener){
			mResetListener = (OnResetListener) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.statistic_panel, null);
		ViewGroup list = (ViewGroup) root.getChildAt(0);
		
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			// if fragment was shut down and restarted, it will bring up same solve
			mSolve = savedInstanceState.getParcelable("solve");
			mType = savedInstanceState.getParcelable("type");
		}else{
			// otherwise fragment has just been started and get solve from bundle
			mSolve = this.getArguments().getParcelable("solve");
			mType = this.getArguments().getParcelable("type");
		}

		//create header panel which left half is last solve, and right half is averages
		StatisticsPanel st = new StatisticsPanel(this.getActivity());
		View.inflate(this.getActivity(), R.layout.statistic_solve_header, st);

		
		list.addView(st, 0);
		Button delSolve = (Button)list.findViewById(R.id.stats_puzzle_delete_button);
		//if fragment is globally used, it will not have listener
		//must hide button, can't delete remote solve
		if( mResetListener == null){
			delSolve.setVisibility(View.GONE);
		}
		delSolve.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//TODO asynctask?
				PuzzledDatabase db = new PuzzledDatabase(getActivity());
				db.deleteSolve(mSolve);
				//reset fragment to load the last solve
				mResetListener.onReset();
			}
		});
		Button replay = (Button)root.findViewById(R.id.stats_solve_replay_button);
		replay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getActivity(), GameActivity.class);
            	myIntent.putExtra("activity_type", (Parcelable)GameActivityType.REPLAY);
            	myIntent.putExtra("solve", mSolve);
            	//TODO
            	((PuzzledApplication)getActivity().getApplication()).setPuzzle( new Cube(3) );
        		startActivity(myIntent);
			}	
		});
		Button resolve = (Button)root.findViewById(R.id.stats_solve_again_button);
		resolve.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getActivity(), GameActivity.class);
				myIntent.putExtra("activity_type", (Parcelable)GameActivityType.PLAY);
				//TODO
            	((PuzzledApplication)getActivity().getApplication()).setPuzzle( new Cube(3) );
        		startActivity(myIntent);
			}
		});
		setHeader(root);
		
		//TODO proper compatibility detection
//		if( mSolve.getmPuzzle().getmName().contains("3") && mSolve.getmPuzzle().getmName().contains("Cube")){
		new GraphStatisticsTask(this.getActivity(), mSolve, TimeDistribution.class, (ScrollView)root, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
		new GraphStatisticsTask(this.getActivity(), mSolve, MoveDistribution.class, (ScrollView)root, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
		new GraphStatisticsTask(this.getActivity(), mSolve, TurnsPerSecond.class, (ScrollView)root, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
//		}
		
		return root;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("solve", mSolve);
		outState.putParcelable("type", mType);
	}

	
	private void setHeader(ViewGroup root){
		TextView time = (TextView) root.findViewById(R.id.stats_solve_duration);
		time.setText( Utils.solveDurationToString( mSolve.getDuration() ) );
		final TextView puzName = (TextView) root.findViewById(R.id.stats_solve_puzzle);
		
		TextView solDate = (TextView) root.findViewById(R.id.stats_solve_date);
		solDate.setText( Utils.solveDateToString( mSolve.getDateSolved() ) );
		TextView scramble = (TextView) root.findViewById(R.id.stats_solve_scramble);
		scramble.setText("Scramble: " + mSolve.getScramble());
		
		TextView moveCount = (TextView) root.findViewById(R.id.stats_solve_move_count);
		TextView solveNum = (TextView) root.findViewById(R.id.stats_solve_number);
		TextView percentile = (TextView) root.findViewById(R.id.stats_solve_percentile);
		
		if (mType == SolveType.LOCAL) {	
			puzName.setText( mSolve.getPuzzle().getmName() );
			TextStatisticsTask.runAll(new Class[]{MoveCount.class, SolveNumber.class, Percentile.class},
					new TextView[]{moveCount, solveNum, percentile}, new int[3], mSolve, getActivity());
		}else if (mType == SolveType.GLOBAL){
			//need to get puzzle name from remote database
			new AsyncTask<Void, Void, Void>(){
				private String mPuzName;
				@Override
				protected Void doInBackground(Void... params) {
					mPuzName = LeaderboardDatabase.getPuzzleName(mSolve.getRemotePuzzleId());
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					puzName.setText(mPuzName);
				}
			}.execute((Void[])null);

			//change to USERname
			((TextView)root.findViewById(R.id.activity_statistics_solve_number_label)).setText("Username:");
			solveNum.setText(mSolve.getUsername());
			
			//change to place (ranking)
			((TextView)root.findViewById(R.id.activity_statistics_percentile_label)).setText("Leaderboard Ranking");
			percentile.setText("3");
			
			TextStatisticsTask.runAll(new Class[]{MoveCount.class},
					new TextView[]{moveCount}, new int[1], mSolve, getActivity());
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
}
