package ca.germuth.puzzled;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import ca.germuth.puzzled.ReplayParser.ReplayMove;
import ca.germuth.puzzled.ShakeListener.OnShakeListener;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.gui.GameActivityLayout;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.Puzzle.OnPuzzleSolvedListener;
import ca.germuth.puzzled.puzzle.cube.Cube;
import ca.germuth.puzzled.util.Chronometer;
import ca.germuth.puzzled.util.FontManager;
import ca.germuth.puzzled.util.Chronometer.OnChronometerFinishListener;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ReplayActivity extends Activity{
	private Puzzle mPuzzle;
	private SolveDB mSolve;
	private MyGLSurfaceView mGlView;
	private PuzzleMoveListener mPuzzleMoveListener;
	private Chronometer mTimer;
	private String mScramble;
	private PuzzleState mState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(Cube.getLayout());

		mState = PuzzleState.Playing;
		mPuzzle = ((PuzzledApplication) this.getApplication()).getPuzzle();
		mSolve = this.getIntent().getParcelableExtra("solve");
		
		// grab and set up timer
		mTimer = (Chronometer) this.findViewById(R.id.activity_game_timer);
		mTimer.setTypeface(FontManager.getTypeface(this,
				FontManager.AGENT_ORANGE_FONT));

		// grab openGL view
		mGlView = (MyGLSurfaceView) this
				.findViewById(R.id.activity_game_gl_surface_view);
		mGlView.initializeRenderer(mPuzzle);
//		mPuzzleMoveListener = new PuzzleMoveListener(this, mPuzzle, mGlView);

		GameActivityLayout container = (GameActivityLayout) this
				.findViewById(R.id.activity_game_container);
		container.setmActivity(this);
		container.setmGlView(mGlView);
		
		new Thread(new Runnable(){
			public void run(){
				ReplayParser rp = new ReplayParser(mSolve.getScramble());
				Iterator<ReplayMove> i = rp.iterator();
				while(i.hasNext()){
					ReplayMove current = i.next();
					
				}
				rp = new ReplayParser(mSolve.getReplay());
				rp.iterator();
				Random r = new Random();
				// TODO grab from preferences here
//				for (int i = 0; i < SCRAMBLE_LENGTH; i++) {
//					PuzzleTurn current = turns.get(r.nextInt(turns.size()));
//					while (current.isRotation()) {
//						current = turns.get(r.nextInt(turns.size()));
//					}
//					final PuzzleTurn selected = current;
//					GameActivity.this.runOnUiThread(new Runnable() {
//						public void run() {
//							mPuzzleMoveListener.execute(selected);
//						}
//					});
//				
//					mScramble += selected.getmName() + " ";
//					// TODO only necessary because of concurrecy bug
//					//maybe i wanna keep it because it looks better
//					try {
//						Thread.sleep(300);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			}
		}).start();
	}
	
	public int getCurrentTime(){
		return (int)this.mTimer.getTimeElapsed();
	}

	public Puzzle getPuzzle() {
		return mPuzzle;
	}

	public String getScramble() {
		return mScramble;
	}

	public PuzzleState getState() {
		return mState;
	}
}
