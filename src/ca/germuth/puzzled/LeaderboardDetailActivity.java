package ca.germuth.puzzled;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.fragments.SolveFragment;
import ca.germuth.puzzled.fragments.SolveType;

public class LeaderboardDetailActivity extends FragmentActivity{
	private SolveDB mSolve;
	private SolveFragment mSolveFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		mSolve = i.getParcelableExtra("solve");
		mSolveFragment = SolveFragment.newInstance(mSolve, SolveType.GLOBAL);
		
		setContentView(R.layout.activity_leaderboard_detail);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.activity_leaderboard_detail_container, mSolveFragment);
		ft.commit();
	}
}
