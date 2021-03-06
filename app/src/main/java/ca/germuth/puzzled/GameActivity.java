package ca.germuth.puzzled;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import android.net.Uri;

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
import ca.germuth.puzzled.puzzle_layouts.cube.CubeLayoutButton;
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
        if (inputType.equals("Buttons")) {
            container = (PuzzleLayout) this.findViewById(R.id.cube_layout_button);
        } else if (inputType.equals("Swipe")) {
            container = (PuzzleLayout) this.findViewById(R.id.cube_layout_swipe);
        }

        container.setActivity(this);
        container.setGlView(mGlView);
        container.setPuzzleMoveListener(mPuzzleMoveListener);
        this.mPuzzleStateChangeListener = container;

        setState(PuzzleState.PLAYING);
        setOnPuzzleSolved();

        if (mActivityType == GameActivityType.PLAY && mSolve == null) {
            // add listener to scramble cube and start inspection time
            addShakeListener();
        } else if(mActivityType == GameActivityType.PLAY){
            setUpResumeSolve();
        } else if (mActivityType == GameActivityType.REPLAY) {
            setUpReplay();
        }
    }

    private void setOnPuzzleSolved() {
        if (mActivityType == GameActivityType.PLAY) {
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
                                SolveDB ss = new SolveDB((int) mTimer.getTimeElapsed(),
                                        mPuzzleMoveListener.getReplay(), mScramble,
                                        db.convert(new Cube(3)), d.getTime(), true);
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
        } else if (mActivityType == GameActivityType.REPLAY) {
        }
    }

    public int getCurrentTime() {
        return (int) this.mTimer.getTimeElapsed();
    }

    //special window just for aaron thalman
    @Override
    public void onBackPressed() {
        final long timer = mTimer.getTimeElapsed();
        mTimer.stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to exit your solve?");
//		builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Exit Solve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        if(mState == PuzzleState.SOLVING){
            builder.setNeutralButton("Save Solve", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PuzzledDatabase db = new PuzzledDatabase(GameActivity.this);
                    //insert unfinished solve
                    SolveDB ss = new SolveDB((int) timer, mPuzzleMoveListener.getReplay(), mScramble, db.convert(new Cube(3)), new Date().getTime(), false);
                    db.insertSolve(ss);
                    finish();
                }
            });
        }
        builder.setNegativeButton("Keep Going", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mTimer.startCountingUp(timer);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
//		super.onBackPressed();
    }

    /**
     * Invokes SCRAMBLE_LENGTH random non-rotation moves to the cube. Is
     * performed on another thread so it doesnt't freeze the gui
     * <p/>
     * TODO: Also starts the timer (maybe should be removed from this method
     */
    private void scramblePuzzle() {
        if (this.mState != PuzzleState.PLAYING) {
            return;
        }
        Log.wtf("STARTING TO SCRAMBLE BECAUSE IN PLAYING STATE", this.mState.toString());
        setState(PuzzleState.SCRAMBLING);

        //TODO maybe this should be on different thread
//		new Thread(new Runnable() {
//			public void run() {
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


            mPuzzleMoveListener.execute(selected);

            mScramble += selected.getmName() + " ";
        }
        // inspection length is in seconds, mTimer accepts
        // miliseconds
        //TODO this approach is bad, it locks the GUI until scrambling is done.
        //TODO this will lead to bad things when turn speed is really slow or scramble length is really long
        while (!this.mGlView.getmRenderer().pendingMoves.isEmpty()) {
            //loop until all moves have been rendered (at least started being rendered)
        }
        mTimer.startCountingDown(INSPECTION_LENGTH * 1000);
        setState(PuzzleState.INSPECTION);
    }

    private void executeScramble(ArrayList<PuzzleTurn> turns) {
        try {
            //wait 1.5 seconds before starting
            Thread.sleep(1500);
            StringTokenizer s = new StringTokenizer(mSolve.getScramble());
            while (s.hasMoreTokens()) {
                String move = s.nextToken();
                //search puzzleTurns for move with same name
                // TODO create HASHMAP once then use that
                for (int k = 0; k < turns.size(); k++) {
                    PuzzleTurn match = turns.get(k);
                    if (match.getmName().equals(move)) {
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setUpResumeSolve(){
        //start executing moves
        setState(PuzzleState.SCRAMBLING);
        //TODO make slide moves work
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mScramble = mSolve.getScramble();
                    mPuzzleMoveListener.setReplay(mSolve.getReplay());
                    ArrayList<PuzzleTurn> turns = mPuzzleMoveListener.getmPuzzleTurns();
                    executeScramble(turns);

                    //scrambling done
                    //now to inspection and solve
                    ReplayParser rp = new ReplayParser(mSolve.getReplay());
                    //execute all moves they have done so far
                    Iterator<ReplayMove> i = rp.iterator();
                    while (i.hasNext()) {
                        ReplayMove curr = i.next();

                        //search puzzleTurns for move with same name
                        for (int k = 0; k < turns.size(); k++) {
                            final PuzzleTurn match = turns.get(k);
                            if (match.getmName().equals(curr.getMove())) {
                                //found match add to array
                                GameActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mPuzzleMoveListener.execute(match);
                                    }
                                });
                                Thread.sleep(300);
                            }
                        }
                    }

                    Thread.sleep(3000);

                    //GO! replay is now prepared for execution
                    GameActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            setState(PuzzleState.SOLVING);
                            mTimer.startCountingUp(mSolve.getDuration());
                        }
                    });
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setUpReplay() {
        setState(PuzzleState.SCRAMBLING);
        //start executing moves
        //TODO make slide moves work
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<PuzzleTurn> turns = mPuzzleMoveListener.getmPuzzleTurns();
                executeScramble(turns);

                //scrambling done
                //now to inspection and solve
                ReplayParser rp = new ReplayParser(mSolve.getReplay());

                //create arraylist of the puzzleturn to execute, and how long to wait inbetween moves
                ArrayList<PuzzleTurn> moves = new ArrayList<PuzzleTurn>();
                ArrayList<Integer> waitTimes = new ArrayList<Integer>();

                Iterator<ReplayMove> i = rp.iterator();
                //time starts at -15 seconds for inspection
                //TODO make inspection time not hardcoded
                int previousTime = -15000;

                while (i.hasNext()) {
                    ReplayMove curr = i.next();

                    //TODO same hashmap as above
                    //search puzzleTurns for move with same name
                    for (int k = 0; k < turns.size(); k++) {
                        PuzzleTurn match = turns.get(k);
                        if (match.getmName().equals(curr.getMove())) {
                            //found match add to array
                            moves.add(match);
                            int difference = previousTime - curr.getTime();
                            //if negative then reverse
                            if (difference < 0) {
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
                for (int a = 0; a < moves.size(); a++) {
                    final PuzzleTurn curr = moves.get(a);
                    //execute move
                    GameActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            mPuzzleMoveListener.execute(curr);
                        }
                    });
                    //sleep time inbetween moves
                    try {
                        Thread.sleep(waitTimes.get(a));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                GameActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mTimer.stop();
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
        Log.wtf("CHANGING STATE", state.toString());
        this.mState = state;
        this.mPuzzleStateChangeListener.onPuzzleStateChange(state);
    }
}
