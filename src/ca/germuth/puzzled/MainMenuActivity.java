package ca.germuth.puzzled;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MainMenuActivity extends PuzzledActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		/**
		 * TODO NEED TO ADD SPLASH SCREEN 
		 * http://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen-in-android
		 * WITH THE SVG IMAGE (but not actually in svg form)
		 */
		this.setUpButtons();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_main_menu_play_button:
			Intent myIntent = new Intent(MainMenuActivity.this, CubeSelectionActivity.class);
			MainMenuActivity.this.startActivity(myIntent);
			break;
		case R.id.activity_main_menu_statistic_button:
			break;
		case R.id.activity_main_menu_achievement_button:
			break;
		case R.id.activity_main_menu_leaderboard_button:
			break;
		case R.id.activity_main_menu_setting_button:
			break;
		case R.id.activity_main_menu_quit_button:
			break;
		}

	}

	private void setUpButtons() {
		LinearLayout ll = (LinearLayout) this
				.findViewById(R.id.activity_main_menu_container);
		for (int i = 0; i < ll.getChildCount(); i++) {
			View child = ll.getChildAt(i);
			child.setOnClickListener(this);
		}
	}
}
