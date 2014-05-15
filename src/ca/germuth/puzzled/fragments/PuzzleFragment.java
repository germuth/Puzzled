package ca.germuth.puzzled.fragments;

import java.util.ArrayList;

import net.peterkuterna.android.apps.swipeytabs.SwipeyTabFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.StatisticActivity;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.StatisticsPanel;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.cube.Cube;
import ca.germuth.puzzled.statistics.graph.GraphStatisticsTask;
import ca.germuth.puzzled.statistics.graph.SolveHistory;
import ca.germuth.puzzled.statistics.text.Average;
import ca.germuth.puzzled.statistics.text.BestAverage;
import ca.germuth.puzzled.statistics.text.FewestMoves;
import ca.germuth.puzzled.statistics.text.Highest;
import ca.germuth.puzzled.statistics.text.Lowest;
import ca.germuth.puzzled.statistics.text.TimesSolved;

public class PuzzleFragment extends SwipeyTabFragment {

	private static final String name = "PUZZLE";
	private Puzzle mPuzzle;
	private ScrollView mRoot;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mPuzzle = new Cube(3, 3, 3);
		mRoot = (ScrollView) inflater.inflate(R.layout.statistic_panel,
				null);
		final ViewGroup list = (ViewGroup) mRoot.getChildAt(0);

		// create header panel which left half is last solve, and right half is
		// averages
		StatisticsPanel st = new StatisticsPanel(this.getActivity());
		View.inflate(this.getActivity(), R.layout.statistic_puzzle_header, st);
		list.addView(st, 0);
		
		((Button)list.findViewById(R.id.stats_puzzle_delete_button)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				PuzzledDatabase db = new PuzzledDatabase(getActivity());
				db.deleteAllSolves( db.convert(mPuzzle) );
				db.close();
				
				Intent mainIntent = new Intent(getActivity(), StatisticActivity.class);
				getActivity().startActivity(mainIntent);
				getActivity().finish();
			}
		});
		//TODO these two methods share 99% code
		setUpHeaderStats(st);
		setAllTimeAverages(st);
		setCurrentAverages(st);
		
//		Class<?>[] myStats = TextStatisticsMeasure.myMeasures;
//		for (int i = 0; i < myStats.length; i++) {
//			final Class<?> current = myStats[i];
//
//			final StatisticsPanel sPanel = new StatisticsPanel(
//					this.getActivity());
//			
//			new TextStatisticsTask(this.getActivity(), current, list, sPanel).execute((Void[]) null);
//		}

		Class<?> test = SolveHistory.class;
		new GraphStatisticsTask(this.getActivity(), test, mRoot, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
		
		return mRoot;
	}
	
	private void setUpHeaderStats(StatisticsPanel st){
		View[] averages = new View[]{ st.findViewById(R.id.stats_puzzle_times_solved),
				st.findViewById(R.id.stats_puzzle_fastest_solve), st.findViewById(R.id.stats_puzzle_slowest_solve),
				st.findViewById(R.id.stats_puzzle_fewest_moves)};
		
		
		for(int i = 0; i < averages.length; i++){
			final TextView cur = (TextView) averages[i];
			final int index = i;
			final PuzzledDatabase db = new PuzzledDatabase(this.getActivity());
			new AsyncTask<Void, Void, Void>(){
				private String value;
				@Override
				protected Void doInBackground(Void... params) {
					ArrayList<SolveDB> solves = db.getAllSolves( db.convert(mPuzzle) );
					
					try{
						if(index == 0){
							double highest = TimesSolved.getValues(solves);
							value = (int)highest + "";							
						}if(index == 1){
							double highest = Highest.getValues(solves);
							value = SolveHistory.solveDurationToString((int)highest);
						}if(index == 2){
							double highest = Lowest.getValues(solves);
							value = SolveHistory.solveDurationToString((int)highest);
						}if(index == 3){
							double highest = FewestMoves.getValues(solves);
							value = (int)highest + "";
						}
					}catch(IllegalArgumentException e){
						value = "N/A";
					}
					
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					
					cur.setText(value);
				}
			}.execute((Void[])null);
		}
	}
	private void setCurrentAverages(StatisticsPanel st){
		View[] averages = new View[]{ st.findViewById(R.id.stats_puzzle_current_avg_5),
				st.findViewById(R.id.stats_puzzle_current_avg_12), st.findViewById(R.id.stats_puzzle_current_avg_50),
				st.findViewById(R.id.stats_puzzle_current_avg_100)};
		int[] average = new int[]{5, 12, 50, 100};
		
		for(int i = 0; i < averages.length; i++){
			final TextView cur = (TextView) averages[i];
			final int avg = average[i];
			
			final PuzzledDatabase db = new PuzzledDatabase(this.getActivity());
			new AsyncTask<Void, Void, Void>(){
				private String value;
				@Override
				protected Void doInBackground(Void... params) {
					ArrayList<SolveDB> solves = db.getAllSolves( db.convert(mPuzzle) );
					
					try{
						double average = Average.getValues(solves, avg);
						value = SolveHistory.solveDurationToString((int)average);
					}catch(IllegalArgumentException e){
						value = "N/A";
					}
					
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					
					cur.setText(value);
				}
			}.execute((Void[])null);
		}
	}

	private void setAllTimeAverages(StatisticsPanel st){
		View[] averages = new View[]{ st.findViewById(R.id.puzzle_panel_avg_5),
				st.findViewById(R.id.puzzle_panel_avg_12), st.findViewById(R.id.puzzle_panel_avg_50),
				st.findViewById(R.id.puzzle_panel_avg_100), st.findViewById(R.id.puzzle_panel_avg_all_time)};
		int[] average = new int[]{5, 12, 50, 100, Integer.MAX_VALUE};
		
		for(int i = 0; i < averages.length; i++){
			final TextView cur = (TextView) averages[i];
			final int avg = average[i];
			
			final PuzzledDatabase db = new PuzzledDatabase(this.getActivity());
			new AsyncTask<Void, Void, Void>(){
				private String value;
				@Override
				protected Void doInBackground(Void... params) {
					ArrayList<SolveDB> solves = db.getAllSolves( db.convert(mPuzzle) );
					
					try{
						double average = BestAverage.getValues(solves, avg);
						value = SolveHistory.solveDurationToString((int)average);
					}catch(IllegalArgumentException e){
						value = "N/A";
					}
					
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					
					cur.setText(value);
				}
			}.execute((Void[])null);
		}
	}
	@Override
	public String getName() {
		return name;
	}
}
