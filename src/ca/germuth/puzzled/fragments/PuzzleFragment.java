package ca.germuth.puzzled.fragments;

import net.peterkuterna.android.apps.swipeytabs.SwipeyTabFragment;
import android.content.Intent;
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
import ca.germuth.puzzled.database.PuzzleDB;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.gui.StatisticsPanel;
import ca.germuth.puzzled.statistics.graph.GraphStatisticsTask;
import ca.germuth.puzzled.statistics.graph.Histogram;
import ca.germuth.puzzled.statistics.graph.SolveHistory;
import ca.germuth.puzzled.statistics.text.Average;
import ca.germuth.puzzled.statistics.text.BestAverage;
import ca.germuth.puzzled.statistics.text.FewestMoves;
import ca.germuth.puzzled.statistics.text.Highest;
import ca.germuth.puzzled.statistics.text.Lowest;
import ca.germuth.puzzled.statistics.text.TextStatisticsTask;
import ca.germuth.puzzled.statistics.text.TimesSolved;
//add histogram
public class PuzzleFragment extends SwipeyTabFragment {

	private static final String name = "PUZZLE";
	private PuzzleDB mPuzzle;
	private ScrollView mRoot;
	
	public static final PuzzleFragment newInstance(PuzzleDB puz)
	{
	    PuzzleFragment fragment = new PuzzleFragment();
	    Bundle bundle = new Bundle(1);
	    bundle.putParcelable("puzzle", puz);
	    fragment.setArguments(bundle);
	    return fragment ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mPuzzle = this.getArguments().getParcelable("puzzle");
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
				db.deleteAllSolves( mPuzzle );
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

		new GraphStatisticsTask(this.getActivity(), mPuzzle, Histogram.class, mRoot, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
		new GraphStatisticsTask(this.getActivity(), mPuzzle, SolveHistory.class, mRoot, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
		
		return mRoot;
	}
	
	private void setUpHeaderStats(StatisticsPanel st){
		TextView[] averages = new TextView[]{ (TextView) st.findViewById(R.id.stats_puzzle_times_solved),
				(TextView) st.findViewById(R.id.stats_puzzle_fastest_solve), (TextView) st.findViewById(R.id.stats_puzzle_slowest_solve),
				(TextView) st.findViewById(R.id.stats_puzzle_fewest_moves)};
		Class<?>[] operations = new Class[]{ TimesSolved.class, Highest.class, Lowest.class, FewestMoves.class };
		
		TextStatisticsTask.runAll(operations, averages, new int[4], mPuzzle, getActivity());
	}
	private void setCurrentAverages(StatisticsPanel st){
		TextView[] averages = new TextView[]{ (TextView) st.findViewById(R.id.stats_puzzle_current_avg_5),
				(TextView) st.findViewById(R.id.stats_puzzle_current_avg_12), (TextView) st.findViewById(R.id.stats_puzzle_current_avg_50),
				(TextView) st.findViewById(R.id.stats_puzzle_current_avg_100)};
		int[] average = new int[]{5, 12, 50, 100};
		Class<?>[] operations = new Class[]{ Average.class, Average.class, Average.class, Average.class};
		
		TextStatisticsTask.runAll(operations, averages, average, mPuzzle, getActivity());
	}

	private void setAllTimeAverages(StatisticsPanel st){
		TextView[] averages = new TextView[]{ (TextView) st.findViewById(R.id.puzzle_panel_avg_5),
				(TextView) st.findViewById(R.id.puzzle_panel_avg_12), (TextView) st.findViewById(R.id.puzzle_panel_avg_50),
				(TextView) st.findViewById(R.id.puzzle_panel_avg_100), (TextView) st.findViewById(R.id.puzzle_panel_avg_all_time)};
		int[] average = new int[]{5, 12, 50, 100, Integer.MAX_VALUE};
		Class<?>[] operations = new Class<?>[]{BestAverage.class, BestAverage.class, BestAverage.class, BestAverage.class, BestAverage.class};
		
		TextStatisticsTask.runAll(operations, averages, average, mPuzzle, getActivity());
		
	}
	@Override
	public String getName() {
		return name;
	}
}
