package ca.germuth.puzzled.gui;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import ca.germuth.puzzled.puzzle.PuzzleTurn;

public class MovePanel extends LinearLayout {
	//size of each button, fixed at dp across device
	private static final int button_X = 30;
	private static final int button_Y = 50;
	
	public MovePanel(Context context, ArrayList<PuzzleTurn> moves) {
		super(context);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		//get absolute width and height
		int width = display.getWidth();
		int height = display.getHeight();  // deprecated
		//get width and height in dp
		float density = getResources().getDisplayMetrics().density;
		float dpHeight = outMetrics.heightPixels / density;
		float dpWidth = outMetrics.widthPixels / density;
		
		//TODO calculate number of buttons we can have according to dpHeight and dpWidth
		//maybe not dp is best, cube visual should be physically larger on greater screen
		final int columns = 2;
		final int rows = 5; // plus one buttom row for rotations
		
		
		this.setMinimumHeight(height);
	}

}
