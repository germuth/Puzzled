package ca.germuth.puzzled;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;

//TODO Not really listening for puzzle moves
//it is listening for input from the touch screen
public class PuzzleMoveListener implements OnClickListener{
	private Puzzle mPuzzle;
	private ArrayList<PuzzleTurn> mPuzzleTurns; 
	private MyGLSurfaceView mOpenGLView;
	private String mReplay;
	private GameActivity mActivity;
	public PuzzleMoveListener(GameActivity activity, Puzzle p, MyGLSurfaceView view){
		this.mActivity = activity;
		this.mPuzzle = p;
		this.mPuzzleTurns = p.getAllMoves();
		this.mOpenGLView = view;
		this.mReplay = "";
	}

	public void onInput(String name){
		// search through all turns to find the matching turn
		for (int i = 0; i < mPuzzleTurns.size(); i++) {
			PuzzleTurn current = mPuzzleTurns.get(i);
			if (current.getmName().equals(name)) {
				// found the matching turn
				// execute this puzzleturn on the specied puzzle
				this.execute(current);
				// save move and time in replay
				int currentTime = mActivity.getCurrentTime();
				if( mActivity.getState() == PuzzleState.INSPECTION){
					this.mReplay += name + " " + -1 * currentTime + " ";
				}

				if( mActivity.getState() == PuzzleState.SOLVING){
					this.mReplay += name + " " + currentTime + " ";
				}

				if(!current.isRotation()){
					this.mPuzzle.checkSolved();
				}

				// turn found, no need to keep searching
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		Button btn = (Button) v;
		String name = (String) btn.getText();
		onInput(name);
	}
	
	public void execute(PuzzleTurn current){

		executePuzzleTurn( mPuzzle, current);
		PuzzleTurn now = new PuzzleTurn(current.getmPuzzle(),
				current.getmName(), current.getMethods(),
				current.getmArguments(), current.getmAngle(), current.getAxis());
		now.setmChangedTiles(mPuzzle.getChangedTiles());

		mPuzzle.moveFinished();
		// pass puzzleTurn to openGL
		// puzzleTurn must be duplicated becuase if same button is pressed again
		// before openGL has had its way with it, this puzzleturns changed tiles
		// will be changed
		mOpenGLView.addPuzzleTurn(now);

	}
	
	public String getReplay(){
		return mReplay.toString();
	}
	
	public static void executePuzzleTurn(Puzzle puzzle, PuzzleTurn puzzleTurn) {
		try {
			Method[] turns = puzzleTurn.getMethods();
			// TODO perhaps generic way of doing this rather than if length ==
			// 1, 2 etc
			Object[] args = puzzleTurn.getmArguments();
			// execute each method with it's arguments
			for (int j = 0; j < turns.length; j++) {
				Method m = turns[j];
				Object[] argsj = (Object[]) args[j];
				if (argsj == null) {
					m.invoke(puzzle, (Object[]) null);
				} else if (argsj.length == 1) {
					m.invoke(puzzle, argsj[0]);
				} else if (argsj.length == 2) {
					m.invoke(puzzle, argsj[0], argsj[1]);
				}
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<PuzzleTurn> getmPuzzleTurns() {
		return mPuzzleTurns;
	}

	public void setmPuzzleTurns(ArrayList<PuzzleTurn> mPuzzleTurns) {
		this.mPuzzleTurns = mPuzzleTurns;
	}
	
}