package ca.germuth.puzzled;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import ca.germuth.puzzled.ReplayParser.ReplayMove;
import ca.germuth.puzzled.ShakeListener.OnShakeListener;
import ca.germuth.puzzled.database.PuzzledDatabase;
import ca.germuth.puzzled.database.SolveDB;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.Puzzle.OnPuzzleSolvedListener;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.cube.Cube;
import ca.germuth.puzzled.puzzle_layouts.cube.PuzzleLayout;
import ca.germuth.puzzled.util.Chronometer;
import ca.germuth.puzzled.util.Chronometer.OnChronometerFinishListener;
import ca.germuth.puzzled.util.FontManager;

/**
 * TODO Ask for fonts You stole a font: AGENT_ORANGE Use this font for
 * non-commercial use only! If you plan to use it for commercial purposes,
 * contact me before doing so!
 * 
 * Have fun and enjoy!
 * 
 * Jakob Fischer jakob@pizzadude.dk www.pizzadude.dk
 */
/*
add message when solve deleted, and move to previous solve?
add 3 more graphs
make stats linkable
actually do user panel
change leaderboard look
make background of play screen black
make buttons look like buttons?
*/
public class GameActivity extends PuzzledActivity {
	private static final int SCRAMBLE_LENGTH = 25;
	private static final int INSPECTION_LENGTH = 15;

	private Puzzle mPuzzle;
	private SolveDB mSolve;
	private PuzzleMoveListener mPuzzleMoveListener;
	private MyGLSurfaceView mGlView;
	private Chronometer mTimer;
	private String mScramble;
	private PuzzleState mState;
	//when puzzle state changes (scrambling -> inspection) some things need to be notified
	//such as the puzzle layout, as it must disable movements being allowed in inspection stage
	private PuzzleStateChangeListener mPuzzleStateChangeListener;

	//this activity can be used both to play the game, or to watch a replay
	private GameActivityType mActivityType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String inputType = sp.getString("input_type", "Buttons");

		this.setContentView(Cube.getLayout(inputType));

		mScramble = "";

		//grab arguments
		mPuzzle = ((PuzzledApplication) this.getApplication()).getPuzzle();
		if (mPuzzle == null) {
			Intent myIntent = new Intent(this, PuzzleSelectActivity.class);
			this.startActivity(myIntent);
		}
		//will be null if mActivityState = PLAY
		//otherwise, is the solve we are replaying
		mSolve = this.getIntent().getParcelableExtra("solve");
		mActivityType = this.getIntent().getParcelableExtra("activity_type");


		// grab and set up timer
		mTimer = (Chronometer) this.findViewById(R.id.activity_game_timer);
		mTimer.setTypeface(FontManager.getTypeface(this,
				FontManager.AGENT_ORANGE_FONT));
		// fires when inspection time runs out
		mTimer.setOnChronometerFinishListener(new OnChronometerFinishListener() {
			@Override
			public void onChronometerFinish(Chronometer chronometer) {
				mTimer.startCountingUp();
				setState(PuzzleState.SOLVING);
			}
		});

		// grab openGL view
		mGlView = (MyGLSurfaceView) this
				.findViewById(R.id.activity_game_gl_surface_view);
		mGlView.initializeRenderer(mPuzzle);
		mPuzzleMoveListener = new PuzzleMoveListener(this, mPuzzle, mGlView);

		PuzzleLayout container = null;
		if(inputType.equals("Buttons")){
			container = (PuzzleLayout) this.findViewById(R.id.cube_layout_button);
		}else if(inputType.equals("Swipe")){
			container = (PuzzleLayout) this.findViewById(R.id.cube_layout_swipe);
		}


		container.setActivity(this);
		container.setGlView(mGlView);
		container.setPuzzleMoveListener(mPuzzleMoveListener);
		this.mPuzzleStateChangeListener = container;

		setState(PuzzleState.PLAYING);
		setOnPuzzleSolved();

		if( mActivityType == GameActivityType.PLAY){
			// add listener to scramble cube and start inspection time
			addShakeListener();			
		}else if(mActivityType == GameActivityType.REPLAY){
			setState(PuzzleState.REPLAY);
			//start executing moves
			//TODO make slide moves work
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						//wait 1.5 seconds before starting
						Thread.sleep(1500);
						
						ArrayList<PuzzleTurn> turns = mPuzzleMoveListener
								.getmPuzzleTurns();
						
						StringTokenizer s = new StringTokenizer(mSolve.getScramble());
						while(s.hasMoreTokens()){
							String move = s.nextToken();	
							//search puzzleTurns for move with same name
							// TODO create HASHMAP once then use that
							for(int k = 0; k < turns.size(); k++){
								PuzzleTurn match = turns.get(k);
								if( match.getmName().equals(move)){
									//found match execute turn 
									final PuzzleTurn selected = match;
									GameActivity.this.runOnUiThread(new Runnable() {
										public void run() {
											mPuzzleMoveListener.execute(selected);
										}
									});
									//so scramble isn't ridiculously fast
									Thread.sleep(300);
									break;
								}
							}
						}
						
						//scrambling done
						//now to inspection and solve
						ReplayParser rp = new ReplayParser(mSolve.getReplay());
						
						//create arraylist of the puzzleturn to execute, and how long to wait inbetween moves
						ArrayList<PuzzleTurn> moves = new ArrayList<PuzzleTurn>();
						ArrayList<Integer> waitTimes = new ArrayList<Integer>();
						
						Iterator<ReplayMove>i = rp.iterator();
						//time starts at -15 seconds for inspection
						//TODO make inspection time not hardcoded
						int previousTime = -15000;
						
						while(i.hasNext()){
							ReplayMove curr = i.next();

							//TODO same hashmap as above
							//search puzzleTurns for move with same name
							for(int k = 0; k < turns.size(); k++){
								PuzzleTurn match = turns.get(k);
								if( match.getmName().equals(curr.getMove())){
									//found match add to array
									moves.add(match);
									int difference = previousTime - curr.getTime();
									//if negative then reverse
									if(difference < 0){
										difference *= -1;
									}
									waitTimes.add(difference);
									previousTime = curr.getTime();
									break;
								}
							}
						}
						//replay is now prepared for execution
						GameActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								mTimer.startCountingDown(INSPECTION_LENGTH * 1000);
							}
						});
						for(int a = 0; a < moves.size(); a++){
							final PuzzleTurn curr = moves.get(a);
							//execute move
							GameActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									mPuzzleMoveListener.execute(curr);
								}
							});
							//sleep time inbetween moves
							Thread.sleep(waitTimes.get(a));
						}
						
						GameActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								mTimer.stop();
							}
						});
						
					}catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	private void setOnPuzzleSolved(){
		if( mActivityType == GameActivityType.PLAY){
			// TODO receive puzzle somehow
			mPuzzle.setOnPuzzleSolvedListener(new OnPuzzleSolvedListener() {
				@Override
				public void onPuzzleSolved() {
					if (mState == PuzzleState.SOLVING) {
						setState(PuzzleState.PLAYING);
						mTimer.stop();

						// async task to insert solve in database
						// when done switch windows
						new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... params) {
								PuzzledDatabase db = new PuzzledDatabase(
										GameActivity.this);
								// int duration, String replay, PuzzleDB puz, long
								// dateTime){
								Date d = new Date();
								SolveDB ss = new SolveDB((int) mTimer.getTimeElapsed(), mPuzzleMoveListener.getReplay(), mScramble, db.convert(new Cube(3)), d.getTime());
								db.insertSolve(ss);
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								super.onPostExecute(result);
								Intent mainIntent = new Intent(GameActivity.this,
										StatisticActivity.class);
								GameActivity.this.startActivity(mainIntent);
								GameActivity.this.finish();
							}
						}.execute((Void[]) null);
					}
				}
			});
		}else if( mActivityType == GameActivityType.REPLAY){
		}
	}
	
	public int getCurrentTime(){
		return (int)this.mTimer.getTimeElapsed();
	}

	/**
	 * Invokes SCRAMBLE_LENGTH random non-rotation moves to the cube. Is
	 * performed on another thread so it doesnt't freeze the gui
	 * 
	 * TODO: Also starts the timer (maybe should be removed from this method
	 */
	private void scramblePuzzle() {
		setState(PuzzleState.SCRAMBLING);
		
		new Thread(new Runnable() {
			public void run() {				
				mScramble = "";
				ArrayList<PuzzleTurn> turns = mPuzzleMoveListener
						.getmPuzzleTurns();
				Random r = new Random();
				// TODO grab from preferences here
				for (int i = 0; i < SCRAMBLE_LENGTH; i++) {
					PuzzleTurn current = turns.get(r.nextInt(turns.size()));
					while (current.isRotation()) {
						current = turns.get(r.nextInt(turns.size()));
					}
					final PuzzleTurn selected = current;
					GameActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							mPuzzleMoveListener.execute(selected);
						}
					});
				
					mScramble += selected.getmName() + " ";
					// TODO only necessary because of concurrecy bug
					//maybe i wanna keep it because it looks better
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				GameActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// inspection length is in seconds, mTimer accepts
						// miliseconds
						mTimer.startCountingDown(INSPECTION_LENGTH * 1000);
						setState(PuzzleState.INSPECTION);
					}
				});

			}
		}).start();
	}

	private void addShakeListener() {
		ShakeListener mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
				scramblePuzzle();
			}
		});
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

	public void setState(PuzzleState state) {
		this.mState = state;
		this.mPuzzleStateChangeListener.onPuzzleStateChange(state);
	}
}
