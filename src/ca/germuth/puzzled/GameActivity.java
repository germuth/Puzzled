package ca.germuth.puzzled;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
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
import ca.germuth.puzzled.util.Chronometer.OnChronometerFinishListener;
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
	private static final int INSPECTION_LENGTH = 15;
	
	private Puzzle mPuzzle;
	private PuzzleMoveListener mPuzzleMoveListener;
	private MyGLSurfaceView mGlView;
	private Chronometer mTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO recieve puzzle somehow
		mPuzzle = new Cube(3, 3, 3);
		this.setContentView(Cube.getLayout());
		
		//grab and set up timer
		mTimer = (Chronometer) this.findViewById(R.id.activity_game_timer);
		mTimer.setTypeface( FontManager.getTypeface(this, FontManager.AGENT_ORANGE_FONT));
		//fires when inspection time runs out
		mTimer.setOnChronometerFinishListener(new OnChronometerFinishListener(){
			@Override
			public void onChronometerFinish(Chronometer chronometer) {
				disableButtons(false);
				mTimer.start();
			}
		});
		
		//grab openGL view
		mGlView = (MyGLSurfaceView) this.findViewById(R.id.activity_game_gl_surface_view);
		mPuzzleMoveListener = new PuzzleMoveListener(this.mPuzzle, mGlView);
		mGlView.initializeRenderer(mPuzzle);
		
		// add PuzzeMoveListener to each button 
		addButtonListeners();
		
		// add listener to scramble cube and start inspection time
		addShakeListener();
	}
	
	private void addButtonListeners(){
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
	}
	
	/**
	 * Invokes SCRAMBLE_LENGTH random non-rotation moves to the cube. Is performed
	 * on another thread to it doesnt't freeze the gui
	 */
	private void scramblePuzzle(){
		new Thread(new Runnable(){
			public void run(){
				ArrayList<PuzzleTurn> turns = mPuzzleMoveListener.getmPuzzleTurns();
				Random r = new Random();
				//TODO grab from preferences here
				for(int i = 0; i < SCRAMBLE_LENGTH; i++){
					PuzzleTurn current = turns.get(r.nextInt(turns.size()));
					while(current.isRotation()){
						current = turns.get(r.nextInt(turns.size()));
					}
					final PuzzleTurn selected = current;
					GameActivity.this.runOnUiThread(new Runnable(){
						public void run(){
							mPuzzleMoveListener.execute(selected);
						}
					});
					//TODO only necessary because of concurrecy bug
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * Disables all non-rotation buttons for the inspection period
	 */
	private void disableButtons(boolean disable){
		ArrayList<PuzzleTurn> turns = mPuzzleMoveListener.getmPuzzleTurns();
		//iterate through puzzleTurns
		for (int i = 0; i < turns.size(); i++) {
			PuzzleTurn current = turns.get(i);
			
			if(current.isRotation()){
				continue;
			}
			
			ViewGroup container = (ViewGroup) this.findViewById(R.id.activity_game_container);
			//iterate through buttons
			for(int j = 0; j < container.getChildCount(); j++){
				View v = container.getChildAt(j);
				if( v instanceof LinearLayout){
					LinearLayout l = (LinearLayout) v;
					for(int k = 0; k < l.getChildCount(); k++){
						View v2 = l.getChildAt(k);
						if( v2 instanceof Button){
							final Button btn = (Button) v2;
							
							if(btn.getText().toString().equals(current.getmName())){
								btn.setEnabled(!disable);
								break;
							}
						}
					}
				}
			}
			
		}
	}
	
	private void addShakeListener(){
		ShakeListener mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new OnShakeListener(){
			@Override
			public void onShake() {
				
				disableButtons(true);
				scramblePuzzle();
				// inspection length is in seconds, mTimer accepts miliseconds
				mTimer.startCountingDown(INSPECTION_LENGTH * 1000);
			}
		});
	}
	
}
