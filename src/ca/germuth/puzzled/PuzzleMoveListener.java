package ca.germuth.puzzled;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;

public class PuzzleMoveListener implements OnClickListener{
	private Puzzle mPuzzle;
	private ArrayList<PuzzleTurn> mPuzzleTurns; 
	private MyGLSurfaceView openGLView;
	
	public PuzzleMoveListener(Puzzle p, MyGLSurfaceView view){
		this.mPuzzle = p;
		this.mPuzzleTurns = p.getAllMoves();
		this.openGLView = view;
	}
	
	@Override
	public void onClick(View v) {
		Button btn = (Button) v;
		String name = (String) btn.getText();
		try {
			//search through all turns to find the matching turn
			for(int i = 0; i < mPuzzleTurns.size(); i++){
				PuzzleTurn current = mPuzzleTurns.get(i);
				if( current.getmName().equals( name + "Turn")){
					//found the matching turn
					//get all of the methods and their arguments
					Method[] turns = current.getmMethod();
					Object[] args = current.getmArguments();
					//execute each method with it's arguments
					for(int j = 0; j < turns.length; j++){
						Method m = turns[j];
						m.invoke(mPuzzle, args[j]);
					}
					//turn found, no need to keep searching
					break;
				}
			}
			//pass changed tiles from move to renderer
			this.openGLView.passChangedTiles( mPuzzle.getChangedTiles() );
			//move is finished
			this.mPuzzle.moveFinished();
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
