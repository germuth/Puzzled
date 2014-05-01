package ca.germuth.puzzled;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.puzzle.Tile;
import ca.germuth.puzzled.puzzle.cube.Cube;

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
			PuzzleTurn match = null;
			//search through all turns to find the matching turn
			for(int i = 0; i < mPuzzleTurns.size(); i++){
				PuzzleTurn current = mPuzzleTurns.get(i);
				if( current.getmName().equals( name )){
					//found the matching turn
					match = current;
					//get all of the methods and their arguments
					Method[] turns = current.getMethods();
					//TODO compiler and variable arguments screwing me over
					Object[] args = current.getmArguments();
					//execute each method with it's arguments
					for(int j = 0; j < turns.length; j++){
						Method m = turns[j];
						Object[] argsj = (Object[]) args[j];
						if(argsj.length == 1){
							m.invoke(mPuzzle, argsj[0]);
						}
						if(argsj.length == 2){
							m.invoke(mPuzzle, argsj[0], argsj[1]);
						}
					}
					//turn found, no need to keep searching
					break;
				}
			}
			match.setmChangedTiles(mPuzzle.getChangedTiles());
			//TODO debug why probs hash map is wrong
//			for(int i = 0; i < match.getmChangedTiles().size(); i++){
//				Tile t = match.getmChangedTiles().get(i);
//				Log.w("DEBUG", t.toString());
//				
//				Tile[][] cf = ((Cube)mPuzzle).getTop().getmFace();
//				Tile[][] dow = ((Cube)mPuzzle).getDown().getmFace();
//				Tile[][] lef = ((Cube)mPuzzle).getLeft().getmFace();
//				Tile[][] rig = ((Cube)mPuzzle).getRight().getmFace();
//				Tile[][] fro = ((Cube)mPuzzle).getFront().getmFace();
//				Tile[][] bac = ((Cube)mPuzzle).getBack().getmFace();
//				
//				for(int j = 0; j < cf.length; j++){
//					for(int k = 0; k < cf[j].length; k++){
//						if( cf[j][k] == t ){
//							Log.w("DEBUG", "TOP (" + j + "," + k + ")");
//						}
//						else if( dow[j][k] == t ){
//							Log.w("DEBUG", "DOWN (" + j + "," + k + ")");
//						}
//						else if( lef[j][k] == t ){
//							Log.w("DEBUG", "LEF (" + j + "," + k + ")");
//						}
//						else if( rig[j][k] == t ){
//							Log.w("DEBUG", "RIG (" + j + "," + k + ")");
//						}
//						else if( fro[j][k] == t ){
//							Log.w("DEBUG", "FRO (" + j + "," + k + ")");
//						}
//						else if( bac[j][k] == t ){
//							Log.w("DEBUG", "BAC (" + j + "," + k + ")");
//						}
//					}
//				}
//				Log.w("DEBUG", mPuzzle.getShapeFor(match.getmChangedTiles().get(i)).toString());
//			}
			mPuzzle.moveFinished();
			//pass changed tiles from move to renderer
			this.openGLView.addPuzzleTurn( match );
			
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
