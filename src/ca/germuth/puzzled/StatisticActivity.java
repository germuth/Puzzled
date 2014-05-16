package ca.germuth.puzzled;

import net.peterkuterna.android.apps.swipeytabs.SwipeyTabFragment;
import net.peterkuterna.android.apps.swipeytabs.SwipeyTabs;
import net.peterkuterna.android.apps.swipeytabs.SwipeyTabsAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.fragments.PuzzleFragment;
import ca.germuth.puzzled.fragments.SolveFragment;
import ca.germuth.puzzled.fragments.UserFragment;
import ca.germuth.puzzled.util.FontManager;

public class StatisticActivity extends PuzzledFragmentActivity {

	private SwipeyTabs mTabs;
	private ViewPager mViewPager;
	
	private SolveFragment mSolveFragment;
	private PuzzleFragment mPuzzleFragment;
	private UserFragment mUserFragment;
	
	private SwipeyTabFragment[] mFragments;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_statistic);

		PuzzledDatabase db = new PuzzledDatabase(StatisticActivity.this);
		//TODO bad
		SolveDB theSolve = db.getLastSolve();

		mSolveFragment = SolveFragment.newInstance(theSolve);
		mPuzzleFragment = PuzzleFragment.newInstance(theSolve.getmPuzzle());
		mUserFragment = new UserFragment();

		mFragments = new SwipeyTabFragment[3];
		mFragments[0] = mSolveFragment;
		mFragments[1] = mPuzzleFragment;
		mFragments[2] = mUserFragment;

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mTabs = (SwipeyTabs) findViewById(R.id.swipeytabs);
		mTabs.setTypeface(FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));

		SwipeyTabsPagerAdapter adapter = new SwipeyTabsPagerAdapter(this,
				getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		mTabs.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(mTabs);
		mViewPager.setCurrentItem(0);
	}

	private class SwipeyTabsPagerAdapter extends FragmentPagerAdapter implements
			SwipeyTabsAdapter {

		private final Context mContext;

		public SwipeyTabsPagerAdapter(Context context, FragmentManager fm) {
			super(fm);

			this.mContext = context;
		}

		@Override
		public Fragment getItem(int position) {
			//return SwipeyTabFragment.newInstance(FRAGMENTS[position]);
			return mFragments[position];
		}

		@Override
		public int getCount() {
			return mFragments.length;
		}

		public TextView getTab(final int position, SwipeyTabs root) {
			TextView view = (TextView) LayoutInflater.from(mContext).inflate(
					R.layout.swipey_tab_indicator, root, false);
			view.setText(mFragments[position].getName());
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mViewPager.setCurrentItem(position);
				}
			});

			return view;
		}
	}
}
