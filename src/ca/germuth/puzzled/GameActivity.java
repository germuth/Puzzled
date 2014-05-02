package ca.germuth.puzzled;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import ca.germuth.puzzled.ShakeListener.OnShakeListener;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.cube.Cube;
import ca.germuth.puzzled.util.Chronometer;
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
	
	private static final int SCRAMBLE_LENGTH = 20;
	
	private Puzzle mPuzzle;
	private PuzzleMoveListener mPuzzleMoveListener;
	private Chronometer mTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPuzzle = new Cube(3, 3, 3);
		this.setContentView(Cube.getLayout());
		
		mTimer = (Chronometer) this.findViewById(R.id.activity_game_timer);
		mTimer.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		
		MyGLSurfaceView glView = (MyGLSurfaceView) this.findViewById(R.id.activity_game_gl_surface_view);
		this.mPuzzleMoveListener = new PuzzleMoveListener(this.mPuzzle, glView);
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
						btn.setOnClickListener(mPuzzleMoveListener);
					}
				}
			}
		}
		
		addShakeListener();
	}
	
	private void addShakeListener(){
		ShakeListener mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new OnShakeListener(){
			@Override
			public void onShake() {
				ArrayList<PuzzleTurn> turns = mPuzzleMoveListener.getmPuzzleTurns();
				Random r = new Random();
				//TODO grab from preferences here
				for(int i = 0; i < SCRAMBLE_LENGTH; i++){
					PuzzleTurn selected = turns.get(r.nextInt(turns.size()));
					while(selected.isRotation()){
						selected = turns.get(r.nextInt(turns.size()));
					}
					mPuzzleMoveListener.execute(selected);
					//TODO only necessary because of concurrecy bug
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mTimer.start();
			}
		});
	}
	
}
