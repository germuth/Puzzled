package ca.germuth.puzzled.openGL;

import ca.germuth.puzzled.puzzle.Puzzle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class MyGLSurfaceView extends GLSurfaceView {

	private MyRenderer mRenderer;
	private Puzzle mPuzzle;
	private int inspection;
	private Handler solvedHandler;
	private boolean solving;
	private TextView inspectionTime;

	public MyGLSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);

		// Create an OpenGL ES 2.0 context.
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);

	}

	public void initializeRenderer(Puzzle p) {
		this.mPuzzle = p;
		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyRenderer(this.mPuzzle);
		setRenderer(mRenderer);

		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

	}

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	
	/**
	 * @return the mRenderer
	 */
	public MyRenderer getmRenderer() {
		return mRenderer;
	}

	/**
	 * @param mRenderer the mRenderer to set
	 */
	public void setmRenderer(MyRenderer mRenderer) {
		this.mRenderer = mRenderer;
	}
	
	/**
	 * @return the tOUCH_SCALE_FACTOR
	 */
	public float getTOUCH_SCALE_FACTOR() {
		return TOUCH_SCALE_FACTOR;
	}

}
