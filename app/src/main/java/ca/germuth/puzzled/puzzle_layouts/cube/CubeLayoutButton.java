package ca.germuth.puzzled.puzzle_layouts.cube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ca.germuth.puzzled.PuzzleMoveListener;
import ca.germuth.puzzled.PuzzleState;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
import ca.germuth.puzzled.util.Utils;
// TODO move all button logic into here
public class CubeLayoutButton extends PuzzleLayout{
	private boolean touchedWithinGlView;
	private int startX;
	private int startY;
	private int currentX;
	private int currentY;
	private Paint mPaint = new Paint();
	private ArrayList<Button> mButtons;
	
	public CubeLayoutButton(Context context) {
		super(context);
		mPaint.setStrokeWidth(5);
	}
	
	public CubeLayoutButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setStrokeWidth(5);
	}
	
	public CubeLayoutButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint.setStrokeWidth(5);
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
				currentX = x;
				currentY = y;
				// pressing down
				// must determine if x and y within mGLView
				// need to get rectangle of view
				//TODO use MotionEvent.ACTION_OUTSIDE ?
				Rect rectangle = new Rect();
				mGlView.getHitRect(rectangle);
				int[] relativeCoords = new int[2];
				mGlView.getLocationOnScreen(relativeCoords);
				rectangle.set(relativeCoords[0] + rectangle.left, relativeCoords[1] + rectangle.top, 
						relativeCoords[0] + rectangle.right, relativeCoords[1] + rectangle.bottom);
				if (rectangle.contains(x, y) ) {
					touchedWithinGlView = true;
					// switch buttons to next level
					changeButtonLevel(true);
					// touch event has  been handled
					return true;
				}
				// touch event not dealt with, transfer to child view
				break;
			case MotionEvent.ACTION_MOVE:
				currentX = x;
				currentY = y;
				break;
			case MotionEvent.ACTION_UP:
				// releasing finger from screen
				if (touchedWithinGlView) {
					touchedWithinGlView = false;
					View touched = Utils.findViewAtPosition(this, x, y);
					if (touched instanceof Button) {
						Button btn = (Button) touched;
						btn.performClick();
						changeButtonLevel(false);
						return true;
					}
				}
				changeButtonLevel(false);
				break;
			default :
				return false;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//TODO: not working, not sure if i want, maybe make it option?
		if(touchedWithinGlView){
			canvas.drawLine(startX, startY, currentX, currentY, mPaint);
		}
	}

	@Override
	public void onPuzzleStateChange(PuzzleState newState){
		switch(newState){
			case SOLVING:
				disableButtons(false, true, true);
				break;
			case REPLAY:
				disableButtons(true, true, true);
				break;
			case INSPECTION:
				disableButtons(false, false, true);
				break;
			case SCRAMBLING:
				disableButtons(true, true, true);
				break;
		}
	}

	@Override
	public void setPuzzleMoveListener(PuzzleMoveListener mPuzzleMoveListener) {
		super.setPuzzleMoveListener(mPuzzleMoveListener);
		addButtonListeners();
	}

	private void addButtonListeners() {
		OnClickListener custom = new OnClickListener() {
			@Override
			public void onClick(View view) {
				Button btn = (Button) view;
				mPuzzleMoveListener.onInput(btn.getText().toString());
			}
		};
		this.mButtons = new ArrayList<Button>();
		for (int i = 0; i < this.getChildCount(); i++) {
			View v = this.getChildAt(i);
			if (v instanceof LinearLayout) {
				LinearLayout l = (LinearLayout) v;
				for (int j = 0; j < l.getChildCount(); j++) {
					View v2 = l.getChildAt(j);
					if (v2 instanceof Button) {
						final Button btn = (Button) v2;
						btn.setOnClickListener(custom);
						this.mButtons.add(btn);
					}
				}
			}
		}
	}

	public void changeButtonLevel(boolean advance) {
		// TODO should only increment if the cube needs it
		// TODO this might be SiGN specific
		for (Button btn : mButtons) {
			String name = btn.getText().toString();
			if (advance) {
				if (Character.isLowerCase(name.charAt(0))) {
					btn.setText("3" + name);
				} else {
					btn.setText("2" + name);
				}
			} else {
				btn.setText(name.substring(1));
			}
		}
	}

	/**
	 * Disables all non-rotation buttons for the inspection period
	 */
	private void disableButtons(boolean disable, boolean normalMoves, boolean rotations) {
		ArrayList<PuzzleTurn> turns = mPuzzleMoveListener.getmPuzzleTurns();
		// iterate through puzzleTurns
		for (int i = 0; i < turns.size(); i++) {
			PuzzleTurn current = turns.get(i);

			if( !current.isRotation() && !normalMoves){
				continue;
			}

			if (current.isRotation() && !rotations) {
				continue;
			}

			// iterate through buttons
			for (Button btn : mButtons) {
				if (btn.getText().toString().equals(current.getmName())) {
					btn.setEnabled(!disable);
					break;
				}
			}
		}
	}

	//temp bad method
	public void pressButton(String text) {
		for (int i = 0; i < this.getChildCount(); i++) {
			View v = this.getChildAt(i);
			if (v instanceof LinearLayout) {
				LinearLayout l = (LinearLayout) v;
				for (int j = 0; j < l.getChildCount(); j++) {
					View v2 = l.getChildAt(j);
					if (v2 instanceof Button) {
						final Button btn = (Button) v2;
						if(btn.getText().equals(text)){
							btn.performClick();
							break;
						}
					}
				}
			}
		}
	}
}
