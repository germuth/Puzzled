package ca.germuth.puzzled;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.cube.Cube;
import ca.germuth.puzzled.util.FontManager;
/**
 * TODO Ask for fonts
 * You stole a font:
 * AGENT_ORANGE
 * Use this font for non-commercial use only! If you plan to use it for commercial purposes, contact me before doing so!

Have fun and enjoy!

Jakob Fischer
jakob@pizzadude.dk
www.pizzadude.dk
 */

public class GameActivity extends PuzzledActivity {
	
	private Puzzle mPuzzle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPuzzle = new Cube(3, 3, 3);
		this.setContentView(Cube.getLayout());
		
		TextView timer = (TextView) this.findViewById(R.id.activity_game_timer);
		timer.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		
		MyGLSurfaceView glView = (MyGLSurfaceView) this.findViewById(R.id.activity_game_gl_surface_view);
		OnClickListener listener = new PuzzleMoveListener(this.mPuzzle, glView);
		glView.initializeRenderer(mPuzzle);
		
		ViewGroup container = (ViewGroup) this.findViewById(R.id.activity_game_container);
		for(int i = 0; i < container.getChildCount(); i++){
			View v = container.getChildAt(i);
			if( v instanceof LinearLayout){
				LinearLayout l = (LinearLayout) v;
				for(int j = 0; j < l.getChildCount(); j++){
					View v2 = l.getChildAt(j);
					if( v2 instanceof Button){
						final Button btn = (Button) v2;
						btn.setOnClickListener(listener);
					}
				}
			}
		}
	}
	
}
