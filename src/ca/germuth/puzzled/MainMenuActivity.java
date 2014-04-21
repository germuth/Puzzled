package ca.germuth.puzzled;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import ca.germuth.puzzled.puzzle.Cube;
import ca.germuth.puzzled.puzzle.PuzzleTurn;

public class MainMenuActivity extends PuzzledActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		Cube c2 = new Cube(2, 2, 2);
		Cube c3 = new Cube(3, 3, 3);
		Cube c4 = new Cube(4, 4, 4);
		Cube c5 = new Cube(5, 5, 5);
		Cube rect = new Cube(3, 3, 5);
		ArrayList<PuzzleTurn> aoeu = c2.getAllMoves();
		ArrayList<PuzzleTurn> aoeu2 = c3.getAllMoves();
		ArrayList<PuzzleTurn> aoeu3 = c4.getAllMoves();
		ArrayList<PuzzleTurn> aoeu4 = c5.getAllMoves();
		ArrayList<PuzzleTurn> aoeu5 = rect.getAllMoves();
		
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
			MainMenuActivity.this.finish();
			System.exit(0);
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
