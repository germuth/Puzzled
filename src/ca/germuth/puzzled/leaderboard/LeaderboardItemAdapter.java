package ca.germuth.puzzled.leaderboard;

import java.util.ArrayList;

import ca.germuth.puzzled.R;
import ca.germuth.puzzled.util.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LeaderboardItemAdapter extends BaseAdapter{
	private ArrayList<Solve> mSolves;
	private Context mContext;
	private LayoutInflater inflater;
	
	public LeaderboardItemAdapter(Context c, ArrayList<Solve> solves){
		this.mContext = c;
		this.mSolves = solves;
		this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mSolves.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mSolves.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if(row == null){
			row= inflater.inflate(R.layout.activity_leaderboard_list_item, null);
			
			Solve item = this.mSolves.get(position);
			
			TextView num = (TextView) row.findViewById(R.id.leaderboard_list_item_number);
			num.setText(position + 1 + ".");
			
			TextView user = (TextView) row.findViewById(R.id.leaderboard_list_item_user);
			user.setText(item.getUsername());
			
			TextView duration = (TextView) row.findViewById(R.id.leaderboard_list_item_duration);
			duration.setText(Utils.solveDurationToString(item.getDuration()));
			
			TextView dateSolved = (TextView) row.findViewById(R.id.leaderboard_list_item_date_solved);
			dateSolved.setText(Utils.solveDateToString(item.getDateSolved()));
			
			Button btn = (Button) row.findViewById(R.id.leaderbaord_list_item_replay_btn);
			//btn.setOnClickListener(l);
			
			return row;
		}
		// TODO Auto-generated method stub
		return row;
	}

}
