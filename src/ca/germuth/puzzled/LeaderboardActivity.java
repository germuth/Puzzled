package ca.germuth.puzzled;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.fragments.LeaderboardListFragment;
import ca.germuth.puzzled.fragments.LeaderboardListFragment.OnSolveSelectedListener;
import ca.germuth.puzzled.fragments.SolveFragment;
import ca.germuth.puzzled.fragments.SolveType;
import ca.germuth.puzzled.leaderboard.RequestType;
import ca.germuth.puzzled.leaderboard.RetrieveRequest;
import ca.germuth.puzzled.leaderboard.SubmitRequest;

public class LeaderboardActivity extends FragmentActivity implements OnSolveSelectedListener, OnClickListener{
	// TODO move to settings
	private LeaderboardListFragment mListFrag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
	
		Button btn = (Button) this.findViewById(R.id.activity_leaderboard_Today_btn);
		btn.setOnClickListener(this);
		btn = (Button) this.findViewById(R.id.activity_leaderboard_This_Week_btn);
		btn.setOnClickListener(this);
		btn = (Button) this.findViewById(R.id.activity_leaderboard_All_Time_btn);
		btn.setOnClickListener(this);
		
		this.mListFrag = (LeaderboardListFragment) getSupportFragmentManager().findFragmentById(R.id.leaderboard_list_fragment);
		
		promptToSubmit();
	}
	
	private void promptToSubmit(){
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Add the buttons
		builder.setPositiveButton("Submit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
						submitSolves();
						retrieveSolves();
					}
				});
		builder.setNegativeButton("No Thanks",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						retrieveSolves();
					}
				});

		// 2. Chain together various setter methods to set the dialog
		// characteristics
		builder.setMessage("Submit Solves to Leaderboard?");

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void submitSolves() {
		SubmitRequest sr = new SubmitRequest(this);
		sr.execute((Void[])null);
	}
	
	private void retrieveSolves(){
		RetrieveRequest rr = new RetrieveRequest(this, this.mListFrag, RequestType.TODAY);
		rr.execute((Void[])null);
	}
	
	/**
	 * This is a callback that the list fragment (Fragment A) calls when a list
	 * item is selected
	 */
	public void onItemSelected(SolveDB solve) {
		SolveFragment listFrag = (SolveFragment) getSupportFragmentManager()
				.findFragmentById(R.id.leaderboard_detail_fragment);
		if (listFrag == null) {
			// DisplayFragment (Fragment B) is not in the layout (handset
			// layout),
			// so start DisplayActivity (Activity B)
			// and pass it the info about the selected item
			Intent intent = new Intent(this, LeaderboardDetailActivity.class);
			intent.putExtra("solve", solve);
			startActivity(intent);
		} else {
			// DisplayFragment (Fragment B) is in the layout (tablet layout),
			// so tell the fragment to update
			SolveFragment frag = SolveFragment.newInstance(solve, SolveType.GLOBAL);
			getSupportFragmentManager().beginTransaction().replace(R.id.leaderboard_detail_fragment, frag);
		}
	}

	@Override
	public void onSolveSelected(SolveDB solve) {
		onItemSelected(solve);
	}

	@Override
	public void onClick(View v) {
		RetrieveRequest rr = new RetrieveRequest(this, mListFrag, RequestType.TODAY);
		switch(v.getId()){
			case R.id.activity_leaderboard_Today_btn:
				rr.setType(RequestType.TODAY);
				break;
			case R.id.activity_leaderboard_This_Week_btn:
				rr.setType(RequestType.THIS_WEEK);
				break;
			case R.id.activity_leaderboard_All_Time_btn:
				rr.setType(RequestType.ALL_TIME);
				break;
		}
		rr.execute((Void[])null);
	}
}
