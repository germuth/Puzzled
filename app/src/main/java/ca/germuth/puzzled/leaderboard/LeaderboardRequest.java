package ca.germuth.puzzled.leaderboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class LeaderboardRequest extends AsyncTask<Void, Void, Void> {
	public static final String USERNAME = "Germoose";

	protected Activity mActivity;
	protected String mMessage;
	private ProgressDialog mPD;

	public LeaderboardRequest(Activity act, String message){
		this.mActivity = act;
		this.mMessage = message;
		this.mPD = new ProgressDialog(this.mActivity);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.mPD.setMessage(mMessage);
		this.mPD.show();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		this.mPD.dismiss();
	}
}
