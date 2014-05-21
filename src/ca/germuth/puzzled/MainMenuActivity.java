package ca.germuth.puzzled;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import ca.germuth.puzzled.util.FontManager;

public class MainMenuActivity extends PuzzledActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		this.setUpButtons();
	}

	@Override
	public void onClick(View v) {
		Intent myIntent = null;
		switch (v.getId()) {
			case R.id.activity_main_menu_play_button:
				myIntent = new Intent(MainMenuActivity.this,
						PuzzleSelectActivity.class);
				MainMenuActivity.this.startActivity(myIntent);
				break;
			case R.id.activity_main_menu_statistic_button:
				myIntent = new Intent(MainMenuActivity.this,
						StatisticActivity.class);
				MainMenuActivity.this.startActivity(myIntent);
				break;
			case R.id.activity_main_menu_achievement_button:

				break;
			case R.id.activity_main_menu_leaderboard_button:
				break;
			case R.id.activity_main_menu_setting_button:
				break;
			case R.id.activity_main_menu_quit_button:
				MainMenuActivity.this.finish();
				System.exit(0);
				break;
		}

	}

	private void setUpButtons() {
		Typeface agentOrange = FontManager.getTypeface(this,
				FontManager.AGENT_ORANGE_FONT);
		LinearLayout ll = (LinearLayout) this
				.findViewById(R.id.activity_main_menu_container);
		for (int i = 0; i < ll.getChildCount(); i++) {
			View child = ll.getChildAt(i);
			if(child instanceof LinearLayout){
				LinearLayout l2 = (LinearLayout) child;
				for(int j = 0; j < l2.getChildCount(); j++){
					View child2 = l2.getChildAt(j);
					if (child2 instanceof Button) {
						Button btn = (Button) child2;
						btn.setOnClickListener(this);
						btn.setTypeface(agentOrange);
					}
				}
			}
		}
	}
}
