package ca.germuth.puzzled.gui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import ca.germuth.puzzled.GameActivity;
import ca.germuth.puzzled.openGL.MyGLSurfaceView;
import ca.germuth.puzzled.util.Utils;

public class GameActivityLayout extends RelativeLayout{
	private GameActivity mActivity;
	private MyGLSurfaceView mGlView;

	
	private boolean touchedWithinGlView;
	
	private int startX;
	private int startY;
	private int currentX;
	private int currentY;
	
	private Paint mPaint = new Paint();
	
	public GameActivityLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mPaint.setStrokeWidth(5);
	}
	
	public GameActivityLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mPaint.setStrokeWidth(5);
	}
	
	public GameActivityLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
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
				Rect rectangle = new Rect();
				mGlView.getHitRect(rectangle);
				int[] relativeCoords = new int[2];
				mGlView.getLocationOnScreen(relativeCoords);
				rectangle.set(relativeCoords[0] + rectangle.left, relativeCoords[1] + rectangle.top, 
						relativeCoords[0] + rectangle.right, relativeCoords[1] + rectangle.bottom);
				if (rectangle.contains(x, y) ) {
					touchedWithinGlView = true;
					// switch buttons to next level
					mActivity.changeButtonLevel(true);
					// touch event has been handled
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
						mActivity.changeButtonLevel(false);
						return true;
					}
				}
				mActivity.changeButtonLevel(false);
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

	public GameActivity getmActivity() {
		return mActivity;
	}

	public void setmActivity(Activity mActivity) {
		if (mActivity instanceof GameActivity){
			this.mActivity = (GameActivity)mActivity;			
		}
	}

	public MyGLSurfaceView getmGlView() {
		return mGlView;
	}

	public void setmGlView(MyGLSurfaceView mGlView) {
		this.mGlView = mGlView;
	}
}
