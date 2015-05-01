package ca.germuth.puzzled.puzzle_layouts.cube;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import ca.germuth.puzzled.PuzzleState;

public class CubeLayoutSwipe extends PuzzleLayout{
	private Context mContext;
	private int startX;
	private int startY;
	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;

	public CubeLayoutSwipe(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public CubeLayoutSwipe(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public CubeLayoutSwipe(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	private void init(){
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		SCREEN_WIDTH = p.x;
		SCREEN_HEIGHT = p.y;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mActivity == null){
			return true;
		}
		
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = x;
				startY = y;
				return true;
			case MotionEvent.ACTION_UP:
				// releasing finger from screen
				GridLocation startGrid = findGridIndex(startX, startY);
				GridLocation endGrid = findGridIndex(x, y);

				String move = "";
				switch(startGrid){
					case TOP_LEFT:
						switch(endGrid){
							case TOP: move = "U'"; break;
							case TOP_RIGHT: move = "U2"; break;
							case LEFT: move = "L"; break;
							case MIDDLE: move = "u'"; break;
							case RIGHT: move = "u2"; break;
							case BOTTOM_LEFT: move = "L2"; break;
							case BOTTOM: move = "M2"; break;
							case BOTTOM_RIGHT: move = "z"; break;
						}
						break;
					case TOP:
						switch(endGrid){
							case TOP_LEFT: move = "U"; break;
							case TOP_RIGHT: move = "U'"; break;
							case LEFT: move = "B"; break;
							case MIDDLE: move = "x'"; break;
							case RIGHT: move = "B'"; break;
							case BOTTOM_LEFT: move = "B2"; break;
							case BOTTOM: move = "x2'"; break;
							case BOTTOM_RIGHT: move = "B2"; break;
							}
						break;
					case TOP_RIGHT:
						switch(endGrid){
							case TOP_LEFT: move = "U2"; break;
							case TOP: move = "U"; break;
							case LEFT: move = "u2"; break;
							case MIDDLE: move = "u"; break;
							case RIGHT: move = "R'"; break;
							case BOTTOM_LEFT: move = "z'"; break;
							case BOTTOM: move = "M2"; break;
							case BOTTOM_RIGHT: move = "R2'"; break;
						}
						break;
					case LEFT:
						switch(endGrid){
							case TOP_LEFT: move = "L'"; break;
							case TOP: move = "B'"; break;
							case TOP_RIGHT: move = "u2'"; break;
							case MIDDLE: move = "y'"; break;
							case RIGHT: move = "y2'"; break;
							case BOTTOM_LEFT: move = "F'"; break;
							case BOTTOM: move = "M"; break;
							case BOTTOM_RIGHT: move = "F2'"; break;
						}
						break;
					case MIDDLE:
						switch(endGrid){
							case TOP_LEFT: move = "u"; break;
							case TOP: move = "x"; break;
							case TOP_RIGHT: move = "u'"; break;
							case LEFT: move = "y"; break;
							case RIGHT: move = "y'"; break;
							case BOTTOM_LEFT: move = "l"; break;
							case BOTTOM: move = "x'"; break;
							case BOTTOM_RIGHT: move = "r'"; break;
						}
						break;
					case RIGHT:
						switch(endGrid){
							case TOP_LEFT: move = "u2"; break;
							case TOP: move = "B"; break;
							case TOP_RIGHT: move = "R"; break;
							case LEFT: move = "y2"; break;
							case MIDDLE: move = "y"; break;
							case BOTTOM_LEFT: move = "F2"; break;
							case BOTTOM: move = "M"; break;
							case BOTTOM_RIGHT: move = "F"; break;
						}
						break;
					case BOTTOM_LEFT:
						switch(endGrid){
							case TOP_LEFT: move = "L2'"; break;
							case TOP: move = "B2'"; break;
							case TOP_RIGHT: move = "z"; break;
							case LEFT: move = "F"; break;
							case MIDDLE: move = "l'"; break;
							case RIGHT: move = "F2"; break;
							case BOTTOM: move = "D"; break;
							case BOTTOM_RIGHT: move = "D2"; break;
						}
						break;
					case BOTTOM:
						switch(endGrid){
							case TOP_LEFT: move = "M2'"; break;
							case TOP: move = "x2"; break;
							case TOP_RIGHT: move = "M2'"; break;
							case LEFT: move = "M'"; break;
							case MIDDLE: move = "x"; break;
							case RIGHT: move = "M'"; break;
							case BOTTOM_LEFT: move = "D'"; break;
							case BOTTOM_RIGHT: move = "D"; break;
						}
						break;
					case BOTTOM_RIGHT:
						switch(endGrid){
							case TOP_LEFT: move = "z'"; break;
							case TOP: move = "B2"; break;
							case TOP_RIGHT: move = "R2"; break;
							case LEFT: move = "F2'"; break;
							case MIDDLE: move = "r"; break;
							case RIGHT: move = "F'"; break;
							case BOTTOM_LEFT: move = "D2'"; break;
							case BOTTOM: move = "D'"; break;
						}
						break;
				}

				if(!move.isEmpty()){
					mPuzzleMoveListener.onInput(move);
				}
//				if (touchedWithinGlView) {
//					touchedWithinGlView = false;
//					View touched = Utils.findViewAtPosition(this, x, y);
//					if (touched instanceof Button) {
//						Button btn = (Button) touched;
//						btn.performClick();
//						mActivity.changeButtonLevel(false);
//						return true;
//					}
//				}
//				mActivity.changeButtonLevel(false);
				break;
			default :
				return false;
		}
		return false;
	}

	@Override
	public void onPuzzleStateChange(PuzzleState newState){

	}

	//Grid across the screen is 9 elements
	//     ---------------------------------------
	//     |    top    |     top     |   top     |
	//     |    left   |             |   right   |
	//     |           |             |           |
	//     ---------------------------------------
	//     |           |             |           |
	//     |  left     |   middle    |  right    |
	//     |           |             |           |
	//     ---------------------------------------
	//     | bottom    |  bottom     | bottom    |
	//     | left      |             | right     |
	//     |           |             |           |
	//     ---------------------------------------
	private static GridLocation findGridIndex(int xPos, int yPos) {
		GridLocation gl;
		if(xPos < SCREEN_WIDTH / 3){
			//left
			if(yPos < SCREEN_HEIGHT / 3){
				gl = GridLocation.TOP_LEFT;
			}else if(yPos < SCREEN_HEIGHT / 3 * 2){
				gl = GridLocation.LEFT;
			}else{
				gl = GridLocation.BOTTOM_LEFT;
			}
		}else if(xPos < SCREEN_WIDTH / 3 * 2){
			//middle
			if(yPos < SCREEN_HEIGHT / 3){
				gl = GridLocation.TOP;
			}else if(yPos < SCREEN_HEIGHT / 3 * 2){
				gl = GridLocation.MIDDLE;
			}else{
				gl = GridLocation.BOTTOM;
			}
		}else {
			//right
			if(yPos < SCREEN_HEIGHT / 3){
				gl = GridLocation.TOP_RIGHT;
			}else if(yPos < SCREEN_HEIGHT / 3 * 2){
				gl = GridLocation.RIGHT;
			}else{
				gl = GridLocation.BOTTOM_RIGHT;
			}
		}
		return gl;
	}

	private enum GridLocation {
		TOP_LEFT, TOP, TOP_RIGHT, LEFT, MIDDLE, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT
	}
}
