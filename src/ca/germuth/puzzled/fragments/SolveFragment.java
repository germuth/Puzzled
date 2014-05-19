package ca.germuth.puzzled.fragments;

import net.peterkuterna.android.apps.swipeytabs.SwipeyTabFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import ca.germuth.puzzled.R;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.StatisticsPanel;
import ca.germuth.puzzled.statistics.graph.GraphStatisticsTask;
import ca.germuth.puzzled.statistics.graph.MoveDistribution;
import ca.germuth.puzzled.statistics.graph.TimeDistribution;
import ca.germuth.puzzled.statistics.text.MoveCount;
import ca.germuth.puzzled.statistics.text.Percentile;
import ca.germuth.puzzled.statistics.text.SolveNumber;
import ca.germuth.puzzled.statistics.text.TextStatisticsTask;
import ca.germuth.puzzled.util.Utils;
//add guage for turns per second
//graph for turns per second overtime
public class SolveFragment extends SwipeyTabFragment{
	private static final String name = "SOLVE";
	
	private SolveDB mSolve;
	
	public static final SolveFragment newInstance(SolveDB solve)
	{
	    SolveFragment fragment = new SolveFragment();
	    Bundle bundle = new Bundle(1);
	    bundle.putParcelable("solve", solve);
	    fragment.setArguments(bundle);
	    return fragment ;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.statistic_panel, null);
		ViewGroup list = (ViewGroup) root.getChildAt(0);
		
		mSolve = this.getArguments().getParcelable("solve");
		
		//create header panel which left half is last solve, and right half is averages
		StatisticsPanel st = new StatisticsPanel(this.getActivity());
		View.inflate(this.getActivity(), R.layout.statistic_solve_header, st);
		
		list.addView(st, 0);
		((Button)list.findViewById(R.id.stats_puzzle_delete_button)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				PuzzledDatabase db = new PuzzledDatabase(getActivity());
				db.deleteSolve(mSolve);
			}
		});
		
		setHeader(root);
		
		new GraphStatisticsTask(this.getActivity(), MoveDistribution.class, (ScrollView)root, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
		new GraphStatisticsTask(this.getActivity(), TimeDistribution.class, (ScrollView)root, list, new StatisticsPanel(this.getActivity())).execute((Void[]) null);
		
		
		return root;
	}
	
	private void setHeader(ViewGroup root){
		TextView time = (TextView) root.findViewById(R.id.stats_solve_duration);
		time.setText( Utils.solveDurationToString( mSolve.getmDuration() ) );
		TextView puzName = (TextView) root.findViewById(R.id.stats_solve_puzzle);
		puzName.setText( mSolve.getmPuzzle().getmName() );
		TextView solDate = (TextView) root.findViewById(R.id.stats_solve_date);
		solDate.setText( Utils.solveDateToString( mSolve.getmDateTime() ) );
		
		TextView moveCount = (TextView) root.findViewById(R.id.stats_solve_move_count);
		TextView solveNum = (TextView) root.findViewById(R.id.stats_solve_number);
		TextView percentile = (TextView) root.findViewById(R.id.stats_solve_percentile);
		
		TextStatisticsTask.runAll(new Class[]{MoveCount.class, SolveNumber.class, Percentile.class},
				new TextView[]{moveCount, solveNum, percentile}, new int[3], mSolve, getActivity());
	}
	
	@Override
	public String getName() {
		return name;
	}
}
