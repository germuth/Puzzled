package ca.germuth.puzzled;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ca.germuth.puzzled.puzzle.Puzzle;

public class PuzzleMoveListener implements OnClickListener{
	private Puzzle mPuzzle;
	
	public PuzzleMoveListener(Puzzle p){
		this.mPuzzle = p;
	}
	
	@Override
	public void onClick(View v) {
		Button btn = (Button) v;
		String name = (String) btn.getText();
		Class<? extends Puzzle> c = mPuzzle.getClass();
		try {
			Method m = c.getMethod(name + "Turn", (Class<?>[])null);
			
			m.invoke(mPuzzle, (Object[])null);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
