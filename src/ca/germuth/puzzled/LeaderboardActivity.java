package ca.germuth.puzzled;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import ca.germuth.puzzled.R;


public class LeaderboardActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
		
		LinearLayout list = (LinearLayout) this.findViewById(R.id.leaderboard_list);
		Button btn = (Button) this.findViewById(R.id.leaderboard_button);
		btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
			}
		});

	}
}
