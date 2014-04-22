package ca.germuth.puzzled;

import android.os.Bundle;
import android.widget.TextView;
import ca.germuth.puzzled.puzzle.Cube;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.util.FontManager;

public class GameActivity extends PuzzledActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Puzzle c3 = new Cube(3, 3, 3);
		this.setContentView(c3.getLayout());
		
		TextView timer = (TextView) this.findViewById(R.id.timer);
		timer.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
	}
	
}
