package ca.germuth.puzzled.openGL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import ca.germuth.puzzled.puzzle.Puzzle;
import ca.germuth.puzzled.puzzle.PuzzleTurn;
/**
 * Specialized view for drawing openGL ES graphics
 * @author Germuth
 *
 */
public class MyGLSurfaceView extends GLSurfaceView {

	private MyRenderer mRenderer;
	private Puzzle mPuzzle;

	private int pointers;
	private float distance;
	
	public MyGLSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);

		// Create an OpenGL ES 2.0 context.

		//say we want to use openGL 2
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);

	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				pointers = 1;
//				return true;
//			case MotionEvent.ACTION_POINTER_2_DOWN:
//				pointers = 2;
//				distance = fingerDist(event);
//				return true;
//			case MotionEvent.ACTION_MOVE:
//				if (pointers == 2) {
//					float newDist = fingerDist(event);
//					float d = distance / newDist;
//					mRenderer.zoom(d);
//					distance = newDist;
//				}
//				return true;
//			default:
//				return false;
//		}
//
//	}
//
//	protected final float fingerDist(MotionEvent event) {
//		float x = event.getX(0) - event.getX(1);
//		float y = event.getY(0) - event.getY(1);
//		return FloatMath.sqrt(x * x + y * y);
//	}


	/**
	 * Must be called before view is created, pairs an openGL 
	 * Rendered to the view
	 * @param p
	 */
	public void initializeRenderer(Puzzle p) {
		this.mPuzzle = p;
		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new MyRenderer(this.mPuzzle);
		setRenderer(mRenderer);

		// Render the view only when there is a change in the drawing data
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		// Render as fast as GPU can
		setRenderMode( GLSurfaceView.RENDERMODE_CONTINUOUSLY);

	}

	public void addPuzzleTurn(PuzzleTurn turn){
		mRenderer.pendingMoves.add(turn);
	}

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

}